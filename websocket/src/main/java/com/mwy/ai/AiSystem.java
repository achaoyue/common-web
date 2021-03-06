package com.mwy.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mouwenyao 2021/1/19 12:26 上午
 */
@Slf4j
public class AiSystem implements Runnable {
    private final static AiSystem INSTANCE = new AiSystem();
    private List<AbstractAi> aiList = new ArrayList<>();

    private AiSystem(){
    }
    public static AiSystem getInstance(){
        return INSTANCE;
    }

    public void start(){
        Thread thread = new Thread(this,"AiSystem");
        thread.setDaemon(true);
        thread.start();
    }


    @Override
    public void run() {
        log.info("start scanning....");
        long start = System.currentTimeMillis();
        while (true){
            try {
                Iterator<AbstractAi> iterator = aiList.iterator();
                while (iterator.hasNext()){
                    AbstractAi next = iterator.next();
                    next.onFrame();
                }
                long end = System.currentTimeMillis();
                if(end - start > 40){
                    start = end;
                }else{
                    Thread.sleep(40+start-end);
                    start = start+40;
                }
            } catch (Exception e) {
                log.error("scanning 失败:",e);
            }
        }
    }

    public void add(AbstractAi abstractAi){
        aiList.add(abstractAi);
    }

    public void remove(AbstractAi abstractAi){
        aiList.remove(abstractAi);
    }
}
