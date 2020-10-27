package com.nada.pool.service;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import com.nada.pool.controller.IndexController;
import com.nada.pool.controller.TulingController;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @ClassName nada
 * @Author lihai
 * @Date 2020/10/27 10:01 上午
 * @Version 1.0
 */
@Service
public class TulingTask {

    private final static int timeout = 20*1000;//超时，毫秒(20秒)

    @Scheduled(cron = "0 */1 * * * ?")
    public static boolean checkLogin(){
        try {
            String cookie = "PHPSESSID=eho80en682doi0s6r04cr6joq4; visitor_type=new; 53revisit=1603780599528; 53kf_72211022_from_host=www.tulingxueyuan.cn; 53kf_72211022_land_page=https%253A%252F%252Fwww.tulingxueyuan.cn%252F; kf_72211022_land_page_ok=1; 53uvid=1; onliner_zdfq72211022=0; Hm_lvt_8bcb19db78384ee654732a25509a98d0=1603780599,1603780670; 53kf_72211022_keyword=https%3A%2F%2Fopen.weixin.qq.com%2F; Hm_lpvt_8bcb19db78384ee654732a25509a98d0=1603780673; 53gid2=11213322782006; 53gid0=11213322782006; 53gid1=11213322782006";
            //String cookie = TulingController.initTulingCookie;
            if(StringUtils.isEmpty(cookie)){
                StaticLog.error("请求Cookid为空");
                return false;
            }
            int port = 1081;
            String host = "localhost";
            String url = "https://www.tulingxueyuan.cn";
            HttpResponse httpResponse = HttpRequest.get(url)
                    .header(Header.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36")
                    // .setHttpProxy(host,port)
                    .cookie(cookie)
                    .timeout(timeout)
                    .execute();
            String result = httpResponse.body();
            String resultCookie = httpResponse.getCookieStr();
            if(StringUtils.isEmpty(resultCookie)){
                resultCookie = cookie;
            }
            Map<String, List<String>> headers = httpResponse.headers();
            JSON json = JSONUtil.parse(headers);
            if(StringUtils.isEmpty(result)){
                StaticLog.error("请求响应为空");
                return false;
            }
            if(result.contains("face") || result.contains("YANG")){
                IndexController.tulingCookie = resultCookie;
                IndexController.tulingHeader = json.toString();
                StaticLog.info("重新登陆成功:{},{}",resultCookie,json);
                return true;
            }else{
                StaticLog.error("重新登陆失败");
                return false;
            }
        } catch (Exception e) {
            StaticLog.error("请求异常.{}",e);
            return false;
        }
    }
}
