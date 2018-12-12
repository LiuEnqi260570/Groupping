import com.qianfeng.etl.util.UserAgentUtil;

/**
 * @Auther: lyd
 * @Date: 2018/7/25 17:03
 * @Description:
 */
public class UserAgentTest {
    public static void main(String[] args) {
        //console ---> window.navigator.userAgent
        System.out.println(new UserAgentUtil().parserUserAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Mobile Safari/537.36"));
        System.out.println(new UserAgentUtil().parserUserAgent("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Win64; x64; Trident/4.0; .NET CLR 2.0.50727; SLCC2; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)"));
        System.out.println(new UserAgentUtil().parserUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36"));
        System.out.println(new UserAgentUtil().parserUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 SE 2.X MetaSr 1.0"));
        System.out.println(new UserAgentUtil().parserUserAgent("Mozilla/5.0%20(Windows%20NT%206.1%3B%20WOW64)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Chrome%2F58.0.3029.110%20Safari%2F537.36%20SE%202.X%20MetaSr%201.0"));

    }
}
