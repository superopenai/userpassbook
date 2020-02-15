package me.superning.userpassbook;

import com.alibaba.fastjson.JSON;
import me.superning.userpassbook.domain.Merchant;
import me.superning.userpassbook.service.MerchantService;
import me.superning.userpassbook.service.UserService;
import me.superning.userpassbook.service.passTemplateService;
import me.superning.userpassbook.utils.HBaseConn;
import me.superning.userpassbook.utils.Hbaseutil;
import me.superning.userpassbook.vo.PassTemplate;
import me.superning.userpassbook.vo.User;
import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

@SpringBootTest
class UserpassbookApplicationTests {

    @Autowired
    passTemplateService passTemplateService;
    @Autowired
    UserService userService;
    @Autowired
    MerchantService merchantService;
    @Test
    void getConn() throws IOException {

        Connection hbaseConn = HBaseConn.getHbaseConn();
//        System.out.println(hbaseConn.isClosed());
        HBaseConn.closeConn();
        System.out.println(hbaseConn.isClosed());
    }
    @Test
    void getTable() {
        try {
            Table pass = HBaseConn.getTable("feedback");
            System.out.println(pass.getTableDescriptor().toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void createTable() {
        Hbaseutil.createTable("FileTable",new String[]{"fileInfo","saveInfo"});


    }
//    @Test
//    void addfileDetails(){
//        Hbaseutil.createTable("FileTable",new String[]{"fileInfo","saveInfo"});
//
//        Hbaseutil.putRowData("FileTable","fileInfo","row1","name","file1.txt");
//        Hbaseutil.putRowData("FileTable","fileInfo","row1","type","txt");
//        Hbaseutil.putRowData("FileTable","fileInfo","row1","size","1024");
//        Hbaseutil.putRowData("FileTable","saveInfo","row1","author","superning");
//
//        Hbaseutil.putRowData("FileTable","fileInfo","row2","name","file2.png");
//        Hbaseutil.putRowData("FileTable","fileInfo","row2","type","png");
//        Hbaseutil.putRowData("FileTable","fileInfo","row2","size","2048");
//        Hbaseutil.putRowData("FileTable","saveInfo","row2","author","superning");
//
//    }
    @Test
    void getfileDetails(){
        Result result = Hbaseutil.getRowData("FileTable", "row1", "fileInfo", "size");
        if (result!=null) {
            byte[] value = result.value();
            System.out.println(Bytes.toString(value));
        }

    }

    @Test
    void hbasePassService(){
        PassTemplate passTemplate = new PassTemplate();
        passTemplate.setMerchantId(1L);
        passTemplate.setBackground(1);
        passTemplate.setDesc("测试二号desc");
        passTemplate.setHasToken(false);
        passTemplate.setLimit(10000L);
        passTemplate.setTitle("测试二号title");
        passTemplate.setSummary("测试二号summary");
        passTemplate.setStart(new Date());
        passTemplate.setEnd(DateUtils.addDays(new Date(),7));
        passTemplateService.dropPassTemplateToHBase(passTemplate);



    }
    @Test
    void testCreateUser() {
        User user = new User();
        user.setPersonal(new User.Personal("superning",21,"male","17667429358","china"));
        System.out.println(JSON.toJSONString(userService.createUser(user)));


    }
    @Test
    void readDB(){

        Merchant merchant = merchantService.findById(1L);
        System.out.println(merchant);

        Merchant merchant2 = merchantService.findByName("测试一号");
        System.out.println(merchant2);


    }
    @Test
    void writeDB(){
        merchantService.updateNameInMerchantById(1L,"新一号");
    }

}
