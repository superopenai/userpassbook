package me.superning.userpassbook.utils;

import com.yammer.metrics.core.Clock;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.apache.kafka.common.protocol.types.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author superning
 * @Classname Hbaseutil
 * @Description TODO
 * @Date 2020/2/7 14:33
 * @Created by superning
 */
public class Hbaseutil {
    /**
     * 创建表
     *
     * @param tableName 表名
     * @param cfs       列族的数组
     * @return 是否成功
     */
    public static boolean createTable(String tableName, String[] cfs) {
        try {
            HBaseAdmin admin = (HBaseAdmin) HBaseConn.getHbaseConn().getAdmin();
            if (admin.tableExists(tableName)) {
                return false;
            } else {
                HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
                Arrays.stream(cfs).forEach(cf -> {
                    HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cf);
                    hColumnDescriptor.setMaxVersions(1);
                    hTableDescriptor.addFamily(hColumnDescriptor);
                });
                admin.createTable(hTableDescriptor);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 删除表
     *
     * @param tableName 表名
     * @return
     */
    public static boolean deleteTable(String tableName) {

        try (HBaseAdmin admin = (HBaseAdmin) HBaseConn.getHbaseConn().getAdmin();
        ) {
            admin.disableTable(TableName.valueOf(tableName));
            admin.deleteTable(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * @param tableName 表名
     * @param cfName    列族名
     * @param rowKey    唯一标识rowKey
     * @param qualifier 列族的列名
     * @param data      数据
     * @return
     */
    public static boolean putRowData(String tableName, String cfName, String rowKey, String qualifier, String data) {
        try (Table table = HBaseConn.getTable(tableName)) {
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(cfName), Bytes.toBytes(qualifier), Bytes.toBytes(data));
            table.put(put);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * @param tableName 表名
     * @param putList   Put对象数组
     * @return
     */
    public static boolean putSomeRowData(String tableName, List<Put> putList) {
        try (Table table = HBaseConn.getTable(tableName)) {
            table.put(putList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * @param tableName
     * @param rowKey
     * @param cfName
     * @param qualifier
     * @return
     */
    public static Result getRowData(String tableName, String rowKey, String cfName, String qualifier) {
        try (Table table = HBaseConn.getTable(tableName)) {
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addColumn(Bytes.toBytes(cfName), Bytes.toBytes(qualifier));
            return table.get(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param tableName
     * @param rowKey
     * @param cfName
     * @return
     */
    public static Result getRowData(String tableName, String rowKey, String cfName) {
        try (Table table = HBaseConn.getTable(tableName)) {
            Get get = new Get(Bytes.toBytes(rowKey));
            get.addFamily(Bytes.toBytes(cfName));
            return table.get(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param tableName
     * @param rowKey
     * @return
     */
    public static Result getRowData(String tableName, String rowKey) {
        try (Table table = HBaseConn.getTable(tableName)) {
            Get get = new Get(Bytes.toBytes(rowKey));
            return table.get(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param tableName
     * @param rowKey
     * @param filterList
     * @return
     */
    public static Result getRowData(String tableName, String rowKey, FilterList filterList) {
        try (Table table = HBaseConn.getTable(tableName)) {
            Get get = new Get(Bytes.toBytes(rowKey));
            get.setFilter(filterList);
            return table.get(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param tableName
     * @return
     */
    public static ResultScanner getscanner(String tableName) {
        try (Table table = HBaseConn.getTable(tableName)) {
            Scan scan = new Scan();
            scan.setCaching(1000);
            return table.getScanner(scan);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param tableName
     * @param startRowKey
     * @param endRowkey
     * @return
     */
    public static ResultScanner getscanner(String tableName,String startRowKey, String endRowkey) {
        try (Table table = HBaseConn.getTable(tableName)) {
            Scan scan = new Scan();
            scan.setCaching(1000);
            scan.setBatch(100);
            scan.withStartRow(Bytes.toBytes(startRowKey));
            scan.withStopRow(Bytes.toBytes(endRowkey));
            return table.getScanner(scan);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param tableName
     * @param startRowKey
     * @param endRowkey
     * @param filterList
     * @return
     */
    public static ResultScanner getscanner(String tableName,String startRowKey, String endRowkey,FilterList filterList) {
        try (Table table = HBaseConn.getTable(tableName)) {
            Scan scan = new Scan();
            scan.setCaching(1000);
            scan.setBatch(100);
            scan.setFilter(filterList);
            scan.withStartRow(Bytes.toBytes(startRowKey));
            scan.withStopRow(Bytes.toBytes(endRowkey));
            return table.getScanner(scan);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param tableName
     * @param rowKey
     * @return
     */
    public static boolean deleteRow(String tableName,String rowKey)
    {
        try (Table table = HBaseConn.getTable(tableName)) {
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     *
     * @param tableName
     * @param cfName
     * @return
     */
    public static boolean deleteColumnFamily(String tableName,String cfName) {
        try {
            HBaseAdmin admin = (HBaseAdmin) HBaseConn.getHbaseConn().getAdmin();
            if (admin.tableExists(tableName)) {
                return false;
            } else {
                 admin.deleteColumn(tableName,cfName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    public static boolean deleteQualifier(String tableName, String rowKey, String cfName, String qualifier) {
        try (Table table = HBaseConn.getTable(tableName))  {
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            delete.addColumn(Bytes.toBytes(cfName), Bytes.toBytes(qualifier));
            table.delete(delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
