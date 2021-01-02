package com.mwy.socket;

import java.nio.ByteBuffer;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

/**
 * @author mouwenyao 2021/1/1 11:12 下午
 */
@Component
@ServerEndpoint( value = "/chat/{userid}" )
public class EndPoint {
    @OnOpen
    public void onOpen(Session session){ System.out.println("加入连接");  }

    @OnClose
    public void onClose(){ System.out.println("关闭连接");  }

    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 收到客户端消息后调用的方法
     * @param messages 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(byte[] messages, Session session) {
        try {
            System.out.println("接收到消息:"+new String(messages,"utf-8"));
            //返回信息
            String resultStr="{name:\"张三\",age:18,addr:\"上海浦东\"}";
            //发送字符串信息的 byte数组
            ByteBuffer bf= ByteBuffer.wrap(resultStr.getBytes("utf-8"));
            session.getBasicRemote().sendBinary(bf);
            //发送字符串
            //session.getBasicRemote().sendText("测试");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
