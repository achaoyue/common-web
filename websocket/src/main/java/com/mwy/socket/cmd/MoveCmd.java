package com.mwy.socket.cmd;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author mouwenyao 2021/1/2 5:07 下午
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MoveCmd extends AbstractCmd {
    final byte type = 2;
    byte dirX;
    byte dirY;
    int targetX;
    int targetY;
    String pic;
}
