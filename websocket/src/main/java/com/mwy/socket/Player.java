package com.mwy.socket;

import com.alibaba.fastjson.JSON;
import com.mwy.socket.cmd.JoinCmd;
import com.mwy.socket.cmd.LeaveCmd;
import com.mwy.socket.cmd.MoveCmd;
import com.mwy.socket.cmd.TextCmd;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author mouwenyao 2021/1/2 12:36 下午
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class Player {
    int id;
    int x;
    int y;
    byte dirX;
    byte dirY;
    String pic;

    public void join(JoinCmd joinCmd){
        //加入
        MwyMap.save(1,1,this);
        this.x = joinCmd.getTargetX();
        this.y = joinCmd.getTargetY();
        this.dirX = joinCmd.getDirX();
        this.dirY = joinCmd.getDirY();
        this.pic = joinCmd.getPic();
        this.id = joinCmd.getId();

        //通知其他人加入
        String joinMsg = JSON.toJSONString(joinCmd);
        JoinCmd newJoinCmd = new JoinCmd();
        for (int i = -2; i < 3; i++) {
            for(int j = -3 ;j< 4;j++){
                List<Player> players = MwyMap.get(i * MwyMap.width + x, j * MwyMap.width + y);
                if(players != null){
                    players.forEach(e->{
                        e.sendMsg(joinMsg);
                        newJoinCmd.fillByPlayer(e);
                        sendMsg(JSON.toJSONString(newJoinCmd));
                    });

                }
            }
        }
    }

    public void leave(LeaveCmd leaveCmd){
        MwyMap.remove(this);
        String leaveMsg = JSON.toJSONString(leaveCmd);
        for (int i = -2; i < 3; i++) {
            for(int j = -3 ;j< 4;j++){
                List<Player> players = MwyMap.get(i * MwyMap.width + x, j * MwyMap.width + y);
                if(players != null){
                    players.forEach(e->{
                        e.sendMsg(leaveMsg);
                    });

                }
            }
        }
    }

    public void move(MoveCmd moveCmd){
        MwyMap.save(moveCmd.getTargetX(),moveCmd.getTargetY(),this);

        this.x = moveCmd.getTargetX();
        this.y = moveCmd.getTargetY();
        this.dirY = moveCmd.getDirY();
        this.dirX = moveCmd.getDirX();

        String moveMsg = JSON.toJSONString(moveCmd);
        for (int i = -2; i < 3; i++) {
            for(int j = -3 ;j< 4;j++){
                List<Player> players = MwyMap.get(i * MwyMap.width + x, j * MwyMap.width + y);
                if(players != null){
                    players.forEach(e->{
                        e.sendMsg(moveMsg);
                    });

                }
            }
        }
    }

    public void text(TextCmd textCmd){
        String textMsg = JSON.toJSONString(textCmd);
        for (int i = -2; i < 3; i++) {
            for(int j = -3 ;j< 4;j++){
                List<Player> players = MwyMap.get(i * MwyMap.width + x, j * MwyMap.width + y);
                if(players != null){
                    players.forEach(e->{
                        e.sendMsg(textMsg);
                    });

                }
            }
        }
    }

    public abstract void sendMsg(String msg);
}
