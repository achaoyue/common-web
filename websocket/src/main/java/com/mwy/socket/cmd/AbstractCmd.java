package com.mwy.socket.cmd;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author mouwenyao 2021/1/2 5:10 下午
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractCmd {
    int id;
    public abstract byte getType();
}
