package com.mwy.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mwy.socket.cmd.JoinCmd;
import com.mwy.socket.cmd.LeaveCmd;
import com.mwy.socket.cmd.MoveCmd;
import com.mwy.socket.cmd.TextCmd;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author mouwenyao 2021/1/1 11:12 下午
 */
@Slf4j
@Component
@ServerEndpoint( value = "/websocket" )
public class EndPoint extends Player{
    Session session;
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        JoinCmd joinCmd = new JoinCmd();
        joinCmd.setId(Double.valueOf(Math.random()*10000000).intValue());
        joinCmd.setTargetX(20);
        joinCmd.setTargetY(20);
        joinCmd.setDirX((byte) 13);
        joinCmd.setDirY((byte) 13);
        joinCmd.setPic("turtle2_png");
        this.join(joinCmd);
        System.out.println("加入连接");
    }

    @OnClose
    public void onClose(){
        //通知其他人自己离开
        LeaveCmd leaveCmd = new LeaveCmd();
        leaveCmd.setId(this.getId());
        this.leave(leaveCmd);
        System.out.println("关闭连接");
    }

    @OnError
    public void onError(Session session, Throwable error){
        //通知其他人自己离开
        log.error("some error occur",error);
    }

    /**
     * 收到客户端消息后调用的方法
     * @param messages 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String messages, Session session) {
        //移动自己
        JSONObject jsonObject = JSON.parseObject(messages);
        Integer type = jsonObject.getInteger("type");
        //通知其他人移动
        if(type == 2){
            MoveCmd moveCmd = jsonObject.toJavaObject(MoveCmd.class);
            moveCmd.setId(this.getId());
            this.move(moveCmd);
        }
        //通知其他人消息
        else if(type == 3){
            TextCmd textCmd = jsonObject.toJavaObject(TextCmd.class);
            textCmd.setId(this.getId());
            this.text(textCmd);
        }
    }

    @Override
    public void sendMsg(String msg) {
        try {
            if(this.session.isOpen()){
                synchronized (this.session){
                    this.session.getBasicRemote().sendText(msg);
                }
            }else {
                MwyMap.remove(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
