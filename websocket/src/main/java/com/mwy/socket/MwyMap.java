package com.mwy.socket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.util.CollectionUtils;

/**
 * @author mouwenyao 2021/1/2 12:34 下午
 */
public class MwyMap {
    public static final int width = 320;
    private static Map<Long,List<Player>> map = new HashMap<>();

    public static List<Player> get(int x,int y){
        long key = buildKey(x,y);
        List<Player> players = map.get(key);
        if(CollectionUtils.isEmpty(players)){
            return Collections.emptyList();
        }
        System.out.println(x+"_"+y+"_"+Long.toHexString(key));
        return new ArrayList<>(players);
    }

    public static void remove(Player player){
        long newKey = buildKey(player.getX(),player.getY());
        List<Player> players = map.get(newKey);
        if(!CollectionUtils.isEmpty(players)){
            synchronized (players){
                players.remove(player);
            }
        }
    }

    public static void save(int x,int y,Player player){
        long newKey = buildKey(x,y);
        long oldKey = buildKey(player.getX(),player.getY());
        //未移出块则不移动了。
        if(newKey == oldKey){
            return;
        }

        //添加到新块
        synchronized (map){
            List<Player> endPoints = map.get(newKey);
            if(CollectionUtils.isEmpty(endPoints)){
                endPoints = new LinkedList<>();
                map.put(newKey,endPoints);
            }
            endPoints.add(player);
        }

        //从老块移除
        List<Player> oldPlayers = map.get(oldKey);
        if(!CollectionUtils.isEmpty(oldPlayers)){
            synchronized (oldPlayers){
                oldPlayers.remove(player);
                if(oldPlayers.size() == 0){
                    map.remove(oldKey);
                }
            }
        }
    }

    public static long format(int x){
        return x / width * width;
    }

    private static long buildKey(int x,int y){
        return (format(y) & 0xFFFFFFFFL) | ((format(x) << 32) & 0xFFFFFFFF00000000L);
    }
}
