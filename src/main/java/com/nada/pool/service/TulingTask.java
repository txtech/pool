package com.nada.pool.service;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import com.nada.pool.controller.IndexController;
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

    @Scheduled(cron = "0/1 * * * * *")
    public boolean checkLogin(){
        try {
            String cookie = "Hm_lvt_8bcb19db78384ee654732a25509a98d0=1603711196,1603722315,1603763470,1603763754;PHPSESSID=eho80en682doi0s6r04cr6joq4;";
            StaticLog.info("This is static {} log.", "INFO");
            String url = "https://www.tulingxueyuan.cn";
            HttpResponse httpResponse = HttpRequest.get(url)
                    .header(Header.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36")
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
            if(result.contains("face") || result.contains("yang")){
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
