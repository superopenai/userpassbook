package me.superning.userpassbook.service.Impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.superning.userpassbook.constant.Constants;
import me.superning.userpassbook.service.FeedbackService;
import me.superning.userpassbook.utils.HBaseConn;
import me.superning.userpassbook.utils.Hbaseutil;
import me.superning.userpassbook.utils.RowGen;
import me.superning.userpassbook.vo.FeedBack;
import me.superning.userpassbook.vo.Response;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Classname FeedbackServiceImpl
 * @Description feedback 服务实现
 * @Date 2020/2/10 16:20
 * @Created by superning
 */
@Slf4j
@Service
public class FeedbackServiceImpl implements FeedbackService {


    @Override
    public Response createFeedback(FeedBack feedBack) {
        if (!feedBack.vaild()) {
            log.error("Feedback Error---->[{}]", JSON.toJSONString(feedBack));
            return Response.failue("Feedback Error");
        }
        String feedBackRowKey = RowGen.genFeedBack(feedBack);
        Hbaseutil.putRowStringData(Constants.Feedback.TABLE_NAME, Constants.Feedback.FAMILY_I, feedBackRowKey, Constants.Feedback.COMMENT, feedBack.getComment());
        Hbaseutil.putRowStringData(Constants.Feedback.TABLE_NAME, Constants.Feedback.FAMILY_I, feedBackRowKey, Constants.Feedback.TYPE, feedBack.getType());
        Hbaseutil.putRowStringData(Constants.Feedback.TABLE_NAME, Constants.Feedback.FAMILY_I, feedBackRowKey, Constants.Feedback.USER_ID, feedBack.getUserId());
        Hbaseutil.putRowStringData(Constants.Feedback.TABLE_NAME, Constants.Feedback.FAMILY_I, feedBackRowKey, Constants.Feedback.TEMPLATE_ID, feedBack.getTempalteId());

        return Response.success();
    }

    @Override
    public Response getFeedback(String userId) {
        byte[] reverse = new StringBuilder(String.valueOf(userId)).reverse().toString().getBytes();
        PrefixFilter prefixFilter = new PrefixFilter(reverse);
        FilterList filterList = new FilterList();
        filterList.addFilter(prefixFilter);
        ResultScanner results = null;
        try {
            results = Hbaseutil.getscanner(Constants.Feedback.TABLE_NAME, filterList);
        } catch (IOException e) {
            log.error("Hbaseutil.getscanner----->[{}]", e.getMessage());
        }
        if (results != null) {
            ArrayList<FeedBack> feedBacks = new ArrayList<>();
            results.forEach(result -> {
                FeedBack feedBack = new FeedBack();
                feedBack.setUserId(Arrays.toString(result.getValue(Bytes.toBytes(Constants.Feedback.FAMILY_I), Bytes.toBytes(Constants.Feedback.USER_ID))));

                feedBack.setComment(Arrays.toString(result.getValue(Bytes.toBytes(Constants.Feedback.FAMILY_I), Bytes.toBytes(Constants.Feedback.COMMENT))));

                feedBack.setTempalteId(Arrays.toString(result.getValue(Bytes.toBytes(Constants.Feedback.FAMILY_I), Bytes.toBytes(Constants.Feedback.TEMPLATE_ID))));

                feedBack.setType(Arrays.toString(result.getValue(Bytes.toBytes(Constants.Feedback.FAMILY_I), Bytes.toBytes(Constants.Feedback.TYPE))));

                feedBacks.add(feedBack);
            });
            return new Response(feedBacks);
        }
        return Response.failue("没有任何反馈");
    }


}
