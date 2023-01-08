package com.mwy.base.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class DingDingUtil {
    public static final String defaultToken = "a88fc186fead6d32e60134f43696ce5a29d936269176734d977a6e2f835b789a";
    public static final String NoticeUrl = "https://oapi.dingtalk.com/robot/send?access_token=";
    public static void sendMsg(String token, String msg){
        String url = NoticeUrl + (StringUtils.isEmpty(token) ? defaultToken : token);
        String content = "{\"msgtype\": \"text\",\"text\": {\"content\":\"通知:"+msg+"\"}}";
        String s = HttpsUtils.doPost(url, content);
        log.info("通知发送OK。 {}", s);
    }
}
