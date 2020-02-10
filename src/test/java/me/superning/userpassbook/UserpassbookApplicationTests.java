package me.superning.userpassbook;

import me.superning.userpassbook.service.passTemplateService;
import me.superning.userpassbook.utils.HBaseConn;
import me.superning.userpassbook.utils.Hbaseutil;
import me.superning.userpassbook.vo.PassTemplate;
import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
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
    void scanfile(){
        ResultScanner getscanner = Hbaseutil.getscanner("FileTable", "row1", "row3");

        assert getscanner != null;
        getscanner.forEach(result -> {
                System.out.println(Bytes.toString(result.getValue(Bytes.toBytes("fileInfo"),Bytes.toBytes("type"))));
            });

    }
    @Test
    void deleteTable(){
        Hbaseutil.deleteTable("demo");

    }
    @Test
    public void fowFilter() {
        Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("row1")));
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, Collections.singletonList(filter));
        Hbaseutil.getscanner("FileTable","row1","row3",filterList);


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

}
