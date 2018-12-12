import com.qianfeng.etl.util.LogUtil;

import java.util.Map;

/**
 * @Auther: lyd
 * @Date: 2018/7/26 10:27
 * @Description:
 */
public class LogUtilTest {
    public static void main(String[] args) {
        Map<String,String> info = new LogUtil().parserLog("192.168.216.1^A1532481763.298^A192.168.216.111^A/index.html?en=e_pv&p_url=http%3A%2F%2Flocalhost%3A8080%2FGP1706LogData%2Fdemo2.jsp&p_ref=http%3A%2F%2Flocalhost%3A8080%2FGP1706LogData%2Fdemo.jsp&tt=%E6%B5%8B%E8%AF%95%E9%A1%B5%E9%9D%A22&ver=1&pl=website&sdk=js&u_ud=95136D3D-90DF-445D-A87F-5A183A022715&u_mid=liyadong&u_sd=6A469C9A-82A1-45BA-92B0-A6FF98F51FB6&c_time=1532395397264&l=zh-CN&b_iev=Mozilla%2F5.0%20(Windows%20NT%206.1%3B%20WOW64)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Chrome%2F58.0.3029.110%20Safari%2F537.36%20SE%202.X%20MetaSr%201.0&b_rst=1600*900");
        for (Map.Entry<String,String> m:info.entrySet()) {
            System.out.println(m.getKey()+":"+m.getValue());
        }
    }
}
