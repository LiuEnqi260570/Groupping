package com.qianfeng.analystic.hive;


import com.qianfeng.analystic.model.dim.base.PaymentTypeDimension;
import com.qianfeng.analystic.mr.service.IDimensionConvert;
import com.qianfeng.analystic.mr.service.impl.IDimensionConvertImpl;
import com.qianfeng.common.GlobalConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * 获取支付方式维度的Id
 * Created by lyd on 2018/4/9.
 */
public class PaymentTypeDimensionUdf extends UDF{

    public IDimensionConvert converter =null;

    public PaymentTypeDimensionUdf(){
        converter = new IDimensionConvertImpl();
    }


    public int evaluate(String name){
        name = name == null || StringUtils.isEmpty(name.trim()) ? GlobalConstants.DEFAULT_VALUE :name.trim() ;
        PaymentTypeDimension pd = new PaymentTypeDimension(name);
        try {
            return converter.getDimensionIdByDimension(pd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("获取支付方式维度的Id异常");
    }

    public static void main(String[] args) {
        System.out.println(new PaymentTypeDimensionUdf().evaluate("alipay"));
    }
}
