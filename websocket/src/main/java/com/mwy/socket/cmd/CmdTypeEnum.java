package com.mwy.socket.cmd;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mouwenyao 2021/1/18 11:37 下午
 */
@Getter
@AllArgsConstructor
public enum  CmdTypeEnum {
    JOIN((byte)1,"加入"),
    MOVE((byte)2,"移动"),
    TEXT((byte)3,"消息"),
    LEAVE((byte)4,"退出"),
    FIRE((byte)5,"开火"),
    ;
    byte code;
    String desc;
}
