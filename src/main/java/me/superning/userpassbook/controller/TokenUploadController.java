package me.superning.userpassbook.controller;

import lombok.extern.slf4j.Slf4j;
import me.superning.userpassbook.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author superning
 * @Classname TokenUploadController
 * @Description Passtemplate token upload
 * @Date 2020/2/11 16:13
 * @Created by superning
 */
@Slf4j
@Controller
public class TokenUploadController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 将token 写入redis
     *
     * @param path {@link Path}
     * @param key  redis 的key
     * @return true/false
     */
    private boolean writeTokenToRedis(Path path, String key) {
        Set<String> tokens;
        try (Stream<String> stream = Files.lines(path)) {

            tokens = stream.collect(Collectors.toSet());
        } catch (IOException ex) {
            log.info("IOEXCEPTION IS ------>[{}]", ex.getMessage());
            return false;
        }

        if (!CollectionUtils.isEmpty(tokens)) {
            stringRedisTemplate.executePipelined(
                    (RedisCallback<Object>) redisConnection -> {
                        for (String token : tokens) {
                            redisConnection.sAdd(key.getBytes(), token.getBytes());
                        }
                        return null;
                    }
            );
            return true;
        }

            return false;
    }

    @GetMapping(value = "/upload")
    public String upload (){
            return "upload";
    }
    @GetMapping(value = "/uploadStatus")
    public String uploadStatus(){
        return "uploadStatus";
    }

    @PostMapping("/token")
    public String tokenFileUpload(@RequestParam("MerchantsId") String MerchantsId,
                                  @RequestParam("PasstemplateId") String PasstemplateId,
                                  @RequestParam("file") MultipartFile file,
                                  RedirectAttributes redirectAttributes) {
        if (PasstemplateId==null||file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "PasstemplateId is NULL OR" +
                    "File is Empty");
            return "redirect:/uploadStatus";
        }
        try {
            File curFile = new File(Constants.TOKEN_DIR+MerchantsId);
            if (!curFile.exists()) {
                log.info("Create file---->[{}]",curFile.mkdir());
            }
            Path path = Paths.get(Constants.TOKEN_DIR,MerchantsId,PasstemplateId);
            Files.write(path,file.getBytes());
            if (!writeTokenToRedis(path,PasstemplateId)){
                redirectAttributes.addFlashAttribute("message","write token error");

            } else {
                redirectAttributes.addFlashAttribute("message","you success upload---->"+file.getOriginalFilename());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/uploadStatus";
    }
}
