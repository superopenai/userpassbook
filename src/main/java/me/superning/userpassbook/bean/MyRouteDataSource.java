package me.superning.userpassbook.bean;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

/**
 * @author superning
 * @Classname MyRouteDataSource
 * @Description TODO
 * @Date 2020/2/15 16:14
 * @Created by superning
 */
public class MyRouteDataSource extends AbstractRoutingDataSource {

    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        return DBcontextHolder.get();
    }
}
