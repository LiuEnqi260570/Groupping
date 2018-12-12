package com.qianfeng.analystic.mr;

import com.qianfeng.analystic.model.dim.base.BaseDimension;
import com.qianfeng.analystic.model.dim.value.OutputValueBaseWritable;
import com.qianfeng.analystic.mr.service.IDimensionConvert;
import org.apache.hadoop.conf.Configuration;


import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Auther: lyd
 * @Date: 2018/7/30 09:51
 * @Description:将reduce阶段的统计结果直接输出到mysql的库中。
 */
public interface IOutputWritter {

    /**
     * 操作最终结果表的接口
     * @param conf
     * @param key
     * @param ps
     * @param convert
     * @throws IOException
     * @throws SQLException
     */
    void outputWrite(Configuration conf, BaseDimension key, OutputValueBaseWritable value,
                     PreparedStatement ps, IDimensionConvert convert) throws IOException,SQLException;
}
