package com.mwy.ai;

/**
 * @author mouwenyao 2021/1/18 11:00 下午
 */
public interface AbstractAi {

    /**
     * 当进入运行,注意效率,耗时不要超过1ms
     */
    void onFrame();
}
