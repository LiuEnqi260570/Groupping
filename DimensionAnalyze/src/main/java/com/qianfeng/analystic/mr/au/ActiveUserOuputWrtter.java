package com.qianfeng.analystic.mr.au;

import com.qianfeng.analystic.model.dim.StatsUserDimension;
import com.qianfeng.analystic.model.dim.base.BaseDimension;
import com.qianfeng.analystic.model.dim.value.OutputValueBaseWritable;
import com.qianfeng.analystic.model.dim.value.reduce.MapWritableValue;
import com.qianfeng.analystic.mr.IOutputWritter;
import com.qianfeng.analystic.mr.service.IDimensionConvert;
import com.qianfeng.common.GlobalConstants;
import com.qianfeng.common.KpiType;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Auther: lyd
 * @Date: 2018/7/30 11:07
 * @Description: 为活跃用户的ps赋值
 */
public class ActiveUserOuputWrtter implements IOutputWritter {
    @Override
    public void outputWrite(Configuration conf, BaseDimension key, OutputValueBaseWritable value, PreparedStatement ps, IDimensionConvert convert) throws IOException, SQLException {
        StatsUserDimension statsUserDimension = (StatsUserDimension) key;
        MapWritableValue v = (MapWritableValue) value;

        //为ps赋值
        int i = 0;
        switch (v.getKpi()){
            case ACTIVE_USER:
            case BROWSER_ACTIVE_USER:
                int newUsers = ((IntWritable) ((MapWritableValue)value).
                        getValue().get(new IntWritable(-1))).get();

                ps.setInt(++i,convert.getDimensionIdByDimension(statsUserDimension.getStatsCommonDimension().getDateDimension()));
                ps.setInt(++i,convert.getDimensionIdByDimension(statsUserDimension.getStatsCommonDimension().getPlatformDimension()));
                if(v.getKpi().equals(KpiType.BROWSER_ACTIVE_USER)){
                    ps.setInt(++i,convert.getDimensionIdByDimension(statsUserDimension.getBrowserDimension()));
                }
                ps.setInt(++i,newUsers);
                ps.setString(++i,conf.get(GlobalConstants.RUNNING_DATE));
                ps.setInt(++i,newUsers);
                break;
            case HOURLY_ACTIVE_USER:
                ps.setInt(++i,convert.getDimensionIdByDimension(statsUserDimension.getStatsCommonDimension().getDateDimension()));
                System.out.println("aaaa"+convert.getDimensionIdByDimension(statsUserDimension.getStatsCommonDimension().getPlatformDimension()));
                ps.setInt(++i,convert.getDimensionIdByDimension(statsUserDimension.getStatsCommonDimension().getPlatformDimension()));
                ps.setInt(++i,convert.getDimensionIdByDimension(statsUserDimension.getStatsCommonDimension().getKpiDimension()));
                for(i++;i<28;i++){
                    ps.setInt(i,((IntWritable)((MapWritable)v.getValue()).get(new IntWritable(i-4))).get());

                    ps.setInt(i+25,((IntWritable)((MapWritable)v.getValue()).get(new IntWritable(i-4))).get());
                }
                ps.setString(i,conf.get(GlobalConstants.RUNNING_DATE));
                System.out.println(ps);
                break;
            default:
                break;
        }

        //添加到批处理中
        ps.addBatch();
    }
}
