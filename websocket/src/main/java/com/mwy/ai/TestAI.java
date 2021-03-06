package com.mwy.ai;

import com.mwy.socket.cmd.JoinCmd;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author mouwenyao 2021/1/19 8:25 上午
 */
@Slf4j
@Component
public class TestAI {

    @PostConstruct
    public void init(){
        //投放200个机器人
        for (int i = 0; i < 200; i++) {
            JoinCmd joinCmd = new JoinCmd();
            joinCmd.setId(Double.valueOf(Math.random()*10000000).intValue());
            joinCmd.setTargetY(Double.valueOf(Math.random()*4000-2000).intValue()-960);
            joinCmd.setTargetX(Double.valueOf(Math.random()*6000-3000).intValue()-1704);
            joinCmd.setDirX((byte) 13);
            joinCmd.setDirY((byte) 13);
            joinCmd.setPic("mouse_png");
            AiPlayer aiPlayer = new AiPlayer();
            aiPlayer.setSpeed((byte) 15);
            aiPlayer.join(joinCmd);
            AiSystem.getInstance().add(aiPlayer);
        }
        //投放200个机器人
        for (int i = 0; i < 100; i++) {
            JoinCmd joinCmd = new JoinCmd();
            joinCmd.setId(Double.valueOf(Math.random()*10000000).intValue());
            joinCmd.setTargetY(Double.valueOf(Math.random()*4000-2000).intValue()-960);
            joinCmd.setTargetX(Double.valueOf(Math.random()*6000-3000).intValue()-1704);
            joinCmd.setDirX((byte) 13);
            joinCmd.setDirY((byte) 13);
            joinCmd.setPic("jiake_png");
            AiPlayer aiPlayer = new AiPlayer();
            aiPlayer.setSpeed((byte) 8);
            aiPlayer.join(joinCmd);
            AiSystem.getInstance().add(aiPlayer);
        }
        AiSystem.getInstance().start();

    }
}
