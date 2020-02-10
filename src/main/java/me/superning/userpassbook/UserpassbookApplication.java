package me.superning.userpassbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "me.superning.userpassbook.mapper")
public class UserpassbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserpassbookApplication.class, args);
    }

}
