package me.superning.userpassbook.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author superning
 */
@Slf4j
public class HBaseConn {
    //    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final HBaseConn INSTANCE = new HBaseConn();
    private static Configuration configuration;
    private static Connection connection;

    private HBaseConn() {
        try {
            if (configuration == null) {
                configuration = HBaseConfiguration.create();
                configuration.set("hbase.zookeeper.quorum", "192.168.124.134,192.168.124.135,192.168.124.129");
                configuration.set("hbase.zookeeper.property.clientPort", "2181");
                configuration.set("hbase.master", "192.168.124.133:16010");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws IOException {
        if (connection == null) {
            System.setProperty("hadoop.home.dir", "C:\\winutils");
            connection = ConnectionFactory.createConnection(configuration);
            if (connection == null) {
                System.out.println("秋梨膏");
            }

            log.info("HBase已经连接-----Connection是------> [{}]", connection);
            System.out.println("HBase已经连接");

        }
        return connection;
    }

    public static Connection getHbaseConn() throws IOException {
        return INSTANCE.getConnection();
    }

    public static Table getTable(String tableName) throws IOException {
        return INSTANCE.getConnection().getTable(TableName.valueOf(tableName));
    }

    public static void closeConn() throws IOException {
        if (!connection.isClosed()) {
            log.info("HBase关闭连接-----Connection是------> [{}]", connection);
            connection.close();
            System.out.println("关闭HBase连接");
        }

    }
}
