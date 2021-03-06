package com.mwy.ai;

import com.mwy.socket.MwyMap;
import com.mwy.socket.Player;
import com.mwy.socket.cmd.AbstractCmd;
import com.mwy.socket.cmd.FireCmd;
import com.mwy.socket.cmd.LeaveCmd;
import com.mwy.socket.cmd.MoveCmd;
import com.mwy.util.DistanceUtil;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.CollectionUtils;

/**
 * @author mouwenyao 2021/1/18 10:58 下午
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AiPlayer extends Player implements AbstractAi {
    private int maxDistance = 200;
    private byte speed = 15;
    private volatile LatestPlayer latestPlayer;
    MoveCmd moveCmd = new MoveCmd();
    double moveDir = Math.PI/4;

    @Override
    public void sendMsg(AbstractCmd msg) {
        if(msg.getId() == this.getId()){
            return;
        }
        if(msg instanceof FireCmd){
            handFireMsg((FireCmd)msg);
        }else if(msg instanceof MoveCmd){
            handleMoveMsg(msg);
        }else if(msg instanceof LeaveCmd){
            if(latestPlayer != null && latestPlayer.getId() == msg.getId()){
                this.latestPlayer = null;
            }
        }
    }

    private void handFireMsg(FireCmd fireCmd) {
        if(Math.sqrt(Math.pow(this.getX()-fireCmd.getX(),2)+Math.pow(this.getY()-fireCmd.getY(),2)) < 100){
            MoveCmd moveCmd = new MoveCmd();
            moveCmd.setId(this.getId());
            moveCmd.setDirY((byte)-32);
            if(moveDir<0){
                moveCmd.setDirY((byte)32);
            }
            moveCmd.setDirX((byte) (Math.tan(-Math.PI/2-moveDir)*moveCmd.getDirY()));
            moveCmd.setTargetY(Double.valueOf(Math.random()*4000-2000).intValue()-960);
            moveCmd.setTargetX(Double.valueOf(Math.random()*6000-3000).intValue()-1704);
            moveCmd.setPic(getPic());
            this.move(moveCmd);
        }
    }

    private void handleMoveMsg(AbstractCmd msg) {
        MoveCmd moveCmd = (MoveCmd) msg;
        int newDistance = DistanceUtil
            .getDistance(this.getX(), this.getY(), moveCmd.getTargetX(), moveCmd.getTargetY());
        //距离较远直接不处理
        if(newDistance > maxDistance){
            return;
        }
        if(this.latestPlayer == null || latestPlayer.getId() == moveCmd.getId()){
            //第一次，或相同玩家进入，直接修改距离
            this.latestPlayer
                = new LatestPlayer(msg.getId(),moveCmd.getTargetX(),moveCmd.getTargetY());
        }else if(moveCmd.getId() != latestPlayer.getId()){
            //计算最近的一个玩家
            int oldDistance = DistanceUtil
                .getDistance(latestPlayer.getTargetX(), latestPlayer.getTargetY(), this.getX(), this.getY());
            if(oldDistance > newDistance){
                this.latestPlayer
                    = new LatestPlayer(msg.getId(),moveCmd.getTargetX(),moveCmd.getTargetY());
            }
        }
        //计算最近一个玩家的方向,顺时针偏+-5'
        moveDir = Math.atan2(this.getY() - latestPlayer.getTargetY(), this.getX() - latestPlayer.getTargetX())+Math.PI/180*(Math.random()*10-5);
    }

    @Override
    public void onFrame() {
        LatestPlayer temp = latestPlayer;

        synchronized (this){
            if(this.latestPlayer == null){
                //this.flush();
                return;
            }else{
                temp = latestPlayer;
            }
        }

        int distance = DistanceUtil
            .getDistance(this.getX(), this.getY(), temp.getTargetX(), temp.getTargetY());
        if(distance > maxDistance*3){
            this.latestPlayer = null;
            //this.flush();
            return;
        }
        //向更远距离移动
        moveCmd.setId(this.getId());
        moveCmd.setDirY((byte)-32);

        if(moveDir<0){
            moveCmd.setDirY((byte)32);
        }
        moveCmd.setDirX((byte) (Math.tan(-Math.PI/2-moveDir)*moveCmd.getDirY()));
        moveCmd.setTargetX((int) (Math.cos(moveDir)*speed +this.getX()));
        moveCmd.setTargetY((int) (Math.sin(moveDir)*speed +this.getY()));
        moveCmd.setPic(getPic());
        this.move(moveCmd);
        this.setLastSendTime(System.currentTimeMillis());
    }

    public void flush(){
        if(System.currentTimeMillis()-this.getLastSendTime() > 1000*5){
            moveCmd.setId(this.getId());
            moveCmd.setDirX((byte)128);
            moveCmd.setDirY((byte) (Math.tan(moveDir)*128));
            moveCmd.setTargetX(this.getX());
            moveCmd.setTargetY(this.getY());
            this.move(moveCmd);
            this.setLastSendTime(System.currentTimeMillis());
        }
    }

    @Data
    @AllArgsConstructor
    private class LatestPlayer{

        /**
         * 最近玩家id
         */
        int id;
        int targetX;
        int targetY;
    }
}
