package com.mwy.save.util;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author mouwenyao
 * @since 2019-05-05
 */
public class EnumUtil {
    /**
     * 通过code获取枚举
     *
     * @param c
     * @param code
     * @param <T>
     * @return
     */
    public static <T extends CodeEnum> T getEnumByCode(Class<T> c, Integer code, boolean... f) {
        if (code == null) {
            return null;
        }
        T[] enums = c.getEnumConstants();
        for (T codeEnum :
                enums) {
            if (Objects.equals(code, codeEnum.getCode())) {
                return codeEnum;
            }
        }
        return forceCheck(f,code,c);
    }

    /**
     * 字符串映射到枚举
     *
     * @param c
     * @param desc
     * @param <T>
     * @return
     */
    public static <T extends CodeEnum> T getEnumByDesc(Class<T> c, String desc, boolean... f) {
        if (desc == null) {
            return null;
        }
        T[] enums = c.getEnumConstants();
        for (T codeEnum :
                enums) {
            if (Objects.equals(desc, codeEnum.getDesc())) {
                return codeEnum;
            }
        }
        return forceCheck(f,desc,c);
    }

    /**
     * 任意值映射到枚举
     *
     * @param c
     * @param value
     * @param fun
     * @param <T>
     * @return
     */
    public static <T extends Enum> T getEnum(Object value, Function<T, Object> fun,Class<T> c, boolean... f) {
        if (fun == null) {
            throw new RuntimeException("fun 不能为null");
        }
        T[] enums = c.getEnumConstants();
        for (T codeEnum :
                enums) {
            if (Objects.equals(value, fun.apply(codeEnum))) {
                return codeEnum;
            }
        }
        return forceCheck(f,value,c);
    }

    /**
     * 返回默认值
     * @param f
     * @param value
     * @param c
     * @param <T>
     * @return
     */
    private static <T> T forceCheck(boolean [] f,Object value,Class c){
        if (f != null && f.length > 0 && !f[0]) {
            return null;
        }
        throw new RuntimeException("未知的code：" + value + ",枚举：" + c.getName());
    }
}
