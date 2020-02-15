package me.superning.userpassbook.bean;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author superning
 * @Classname DataSourceAop
 * @Description TODO
 * @Date 2020/2/15 16:20
 * @Created by superning
 */
@Aspect
@Component
public class DataSourceAop {

    @Pointcut("!@annotation(me.superning.userpassbook.annotation.Master) " +
            "&& (execution(* me.superning.userpassbook.service..*.select*(..)) " +
            "|| execution(* me.superning.userpassbook.service..*.find*(..)))")
    public void readPointcut() {

    }


    @Pointcut("@annotation(me.superning.userpassbook.annotation.Master) " +
            "|| execution(* me.superning.userpassbook.service..*.insert*(..)) " +
            "|| execution(* me.superning.userpassbook.service..*.add*(..)) " +
            "|| execution(* me.superning.userpassbook.service..*.update*(..)) " +
            "|| execution(* me.superning.userpassbook.service..*.edit*(..)) " +
            "|| execution(* me.superning.userpassbook.service..*.delete*(..)) " +
            "|| execution(* me.superning.userpassbook.service..*.remove*(..))")
    public void writePointcut() {

    }

    @Before("writePointcut()")
    public void write(){
        DBcontextHolder.master();
    }
    @Before("readPointcut()")
    public void read(){
        DBcontextHolder.slave();
    }


    }
