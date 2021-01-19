import com.alibaba.fastjson.JSON;
import com.mwy.ai.AiPlayer;
import com.mwy.ai.AiSystem;
import com.mwy.ai.TestAI;
import com.mwy.socket.Player;
import com.mwy.socket.cmd.AbstractCmd;
import com.mwy.socket.cmd.JoinCmd;
import com.mwy.socket.cmd.MoveCmd;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mouwenyao 2021/1/19 3:49 下午
 */
@Slf4j
public class TestAiSystem {
    List<JoinCmd> list = new ArrayList<>();

    @PostConstruct
    public void init(){
        for (int i = 0; i < 20; i++) {

            JoinCmd joinCmd = new JoinCmd();
            joinCmd.setId(Double.valueOf(Math.random()*10000000).intValue());
            joinCmd.setTargetY(Double.valueOf(Math.random()*2000-1000).intValue());
            joinCmd.setTargetX(Double.valueOf(Math.random()*3000-1500).intValue());
            joinCmd.setDirX((byte) 13);
            joinCmd.setDirY((byte) 13);
            joinCmd.setPic("turtle_png");
            list.add(joinCmd);
            AiPlayer aiPlayer = new AiPlayer();
            aiPlayer.join(joinCmd);
            AiSystem.getInstance().add(aiPlayer);
        }

    }

    public static void main(String[] args) throws InterruptedException {
        Player player = new Player() {
            @Override
            public void sendMsg(AbstractCmd msg) {
                log.info("aaaa:{}", JSON.toJSONString(msg));
            }
        };

        JoinCmd joinCmd1 = new JoinCmd();
        joinCmd1.setId(Double.valueOf(Math.random()*10000000).intValue());
        joinCmd1.setTargetY(Double.valueOf(Math.random()*2000-1000).intValue());
        joinCmd1.setTargetX(Double.valueOf(Math.random()*3000-1500).intValue());
        joinCmd1.setDirX((byte) 13);
        joinCmd1.setDirY((byte) 13);
        joinCmd1.setPic("turtle_png");
        player.join(joinCmd1);

        TestAiSystem testAI = new TestAiSystem();
        testAI.init();

        JoinCmd joinCmd = testAI.list.get(2);
        MoveCmd moveCmd = new MoveCmd();
        moveCmd.setTargetX(joinCmd.getTargetX()+3);
        moveCmd.setTargetY(joinCmd.getTargetY()+3);
        moveCmd.setDirY((byte)2);
        moveCmd.setDirX((byte)2);
        moveCmd.setId(player.getId());
//        player.move(moveCmd);


        log.info("system start");
        Thread.sleep(100000000);
    }
}
