package com.mwy.task;

import com.mwy.socket.MwyMap;
import com.mwy.socket.Player;
import com.mwy.socket.cmd.MoveCmd;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author mouwenyao 2021/1/24 9:41 上午
 */
@Slf4j
@Component
public class FlushTask implements Runnable {
    volatile boolean init = false;

    @PostConstruct
    public void start(){
        if(init){
            return;
        }
        init = true;
        Thread thread = new Thread(this,"FlushTask");
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {
        log.info("FlushTask start");
        while (true){
            try {
                Set<Long> allKey = MwyMap.getAllKey();
                MoveCmd moveCmd = new MoveCmd();
                for (Long key:allKey){
                    List<Player> byKey = MwyMap.getByKey(key);
                    if(CollectionUtils.isEmpty(allKey)){
                        continue;
                    }
                    for (Player player:byKey){
                        if(System.currentTimeMillis()-player.getLastSendTime() > 1000*5){
                            moveCmd.setId(player.getId());
                            moveCmd.setDirX(player.getDirX());
                            moveCmd.setDirY(player.getDirY());
                            moveCmd.setTargetX(player.getX());
                            moveCmd.setTargetY(player.getY());
                            moveCmd.setPic(player.getPic());
                            player.move(moveCmd);
                            player.setLastSendTime(System.currentTimeMillis());
                        }
                    }
                }
                Thread.sleep(5000);
            } catch (Exception e) {
                log.error("刷新任务出错",e);
            }
        }
    }
}
