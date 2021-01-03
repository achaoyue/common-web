package com.mwy.socket.cmd;

import com.mwy.socket.Player;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author mouwenyao 2021/1/2 5:07 下午
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JoinCmd extends AbstractCmd {
    final byte type = 1;
    byte dirX;
    byte dirY;
    int targetX;
    int targetY;
    String pic;

    public JoinCmd() {
    }
    public void fillByPlayer(Player player){
        this.setId(player.getId());
        this.setPic(player.getPic());
        this.setDirX(player.getDirX());
        this.setDirY(player.getDirY());
        this.setTargetX(player.getX());
        this.setTargetY(player.getY());
    }
}
