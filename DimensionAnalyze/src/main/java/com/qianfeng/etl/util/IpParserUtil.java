package com.qianfeng.etl.util;

import com.alibaba.fastjson.JSONObject;
import com.qianfeng.etl.util.ip.IPSeeker;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * @Auther: lyd
 * @Date: 2018/7/25 14:52
 * @Description:ip的解析工具类
 */
public class IpParserUtil extends IPSeeker{
    private static final Logger logger = Logger.getLogger(IpParserUtil.class);
    RegionInfo info = new RegionInfo();
    /**
     * @param ip
     * @return 返回地域信息
     */
    public RegionInfo parserIp(String ip){
        //判断ip是否为空
        if(StringUtils.isEmpty(ip) || StringUtils.isEmpty(ip.trim())){
            return info;
        }
        try {
            //ip不为空，正常解析
            String country = super.getCountry(ip);
            if("局域网".equals(country)){
                info.setCountry("中国");
                info.setProvince("北京");
                info.setCity("昌平区");
            } else if(country != null || StringUtils.isNotEmpty(country.trim())){
                //查找省的位置
                info.setCountry("中国");
                int index = country.indexOf("省");
                if(index > 0){
                    //设置省份
                    info.setProvince(country.substring(0,index+1));
                    //判断是否有市
                    int index2 = country.indexOf("市");
                    if(index2 > 0){
                        //设置市
                        info.setCity(country.substring(index+1,index2+1));
                    }
                } else {
                    //代码走到这儿，就代表没有省份.就是直辖市、自治区、特别行政区
                    String flag = country.substring(0,2);
                    switch (flag){
                        case "内蒙":
                            info.setProvince("内蒙古");
                            country = country.substring(3);
                            index = country.indexOf("市");
                            if(index > 0){
                                //设置市
                                info.setCity(country.substring(0,index+1));
                            }
                            break;
                        case "广西":
                        case "西藏":
                        case "新疆":
                        case "宁夏":
                            info.setProvince(flag);
                            country = country.substring(2);
                            index = country.indexOf("市");
                            if(index > 0){
                                //设置市
                                info.setCity(country.substring(0,index+1));
                            }
                            break;
                        case "北京":
                        case "上海":
                        case "重庆":
                        case "天津":
                            info.setProvince(flag+"市");
                            country = country.substring(3);
                            index = country.indexOf("区");
                            if(index > 0){
                                char ch = country.charAt(index - 1);
                                if(ch != '小' && ch != '校' && ch != '军'){
                                    info.setCity(country.substring(0,index+1));
                                }
                            }
                            //在直辖市中如果有县
                            index = country.indexOf("县");
                            if(index > 0){
                                info.setCity(country.substring(0, index + 1));
                            }
                            break;
                        case "香港":
                        case "澳门":
                        case "台湾":
                            info.setProvince(flag+"特别行政区");
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e){
            logger.warn("解析ip工具方法异常",e);
        }
        return info;
    }

    /**
     *使用淘宝的ip解析库解析ip
     * @param url
     * @param charset
     * @return
     * @throws Exception
     */
   public RegionInfo parserIp1(String url, String charset) throws Exception {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);

        if (null == url || !url.startsWith("http")) {
            throw new Exception("请求地址格式不对");
        }
        // 设置请求的编码方式
        if (null != charset) {
            method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + charset);
        } else {
            method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + "utf-8");
        }
        int statusCode = client.executeMethod(method);

        if (statusCode != HttpStatus.SC_OK) {// 打印服务器返回的状态
            System.out.println("Method failed: " + method.getStatusLine());
        }
        // 返回响应消息
        byte[] responseBody = method.getResponseBodyAsString().getBytes(method.getResponseCharSet());
        // 在返回响应消息使用编码(utf-8或gb2312)
        String response = new String(responseBody, "utf-8");

       //将reponse的字符串转换成json对象
       JSONObject jo = JSONObject.parseObject(response);
       JSONObject jo1 = JSONObject.parseObject(jo.getString("data"));

       //赋值
       info.setCountry(jo1.getString("country"));
       info.setProvince(jo1.getString("region"));
       info.setCity(jo1.getString("city"));
       return info;
    }


    /**
     * 使用该类来进行封装地域信息。ip解析出来的国家、省、市
     */
    public static class RegionInfo{
        private final String DEFAULT_VALUE = "unknown";
        private String country = DEFAULT_VALUE;
        private String province = DEFAULT_VALUE;
        private String city = DEFAULT_VALUE;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        @Override
        public String toString() {
            return "RegionInfo{" +
                    "country='" + country + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    '}';
        }
    }
}
