package me.superning.userpassbook.bean;

import lombok.extern.slf4j.Slf4j;
import me.superning.userpassbook.constant.DBType;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author superning
 * @Classname DBcontextHolder
 * @Description TODO
 * @Date 2020/2/15 15:55
 * @Created by superning
 */
@Slf4j
public class DBcontextHolder {
    private static final ThreadLocal<DBType> CONTEXT_HOLDER = new ThreadLocal<>();
    private static final AtomicInteger COUNTER = new AtomicInteger(-1);
    public static void set(DBType type) {
        CONTEXT_HOLDER.set(type);
    }
    public static DBType get(){
        return CONTEXT_HOLDER.get();
    }
    public static void master() {
        set(DBType.MASTER);
        log.info("数据库连接到主库---->[{}]",CONTEXT_HOLDER);
//        System.out.println("切换成主库");
    }
    public static void slave(){
       int index =  COUNTER.getAndIncrement()%2;

        if (COUNTER.get()>9999) {
            COUNTER.set(-1);
        }
        if (index==0){
            set(DBType.SLAVE1);
            log.info("数据库连接到一号从库---->[{}]",CONTEXT_HOLDER);
//            System.out.println("切换成一号从库");
        }else {
            set(DBType.SLAVE2);
            log.info("数据库连接到二号从库---->[{}]",CONTEXT_HOLDER);
//            System.out.println("切换成二号从库");
        }


    }

}
