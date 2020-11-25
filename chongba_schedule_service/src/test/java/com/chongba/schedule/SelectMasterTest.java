package com.chongba.schedule;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-25 14:44
 */
public class SelectMasterTest {

    private String selectMasterZookeeper = "127.0.0.1:2181";

    Map<String,Boolean> masterMap = new HashMap<>();

    public void selectMaster(String leaderPath){
        CuratorFramework client = CuratorFrameworkFactory.builder().
                connectString(selectMasterZookeeper)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        client.start();
        LeaderSelector selector = new LeaderSelector(client,leaderPath,new LeaderSelectorListenerAdapter() {
            @Override
            public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                masterMap.put(leaderPath,true);
                while (true){
                    TimeUnit.SECONDS.sleep(3);
                }
            }
        });

        masterMap.put(leaderPath,false);
        selector.autoRequeue();
        selector.start();
    }

    public boolean checkMaster(String leaderPath){
        Boolean isMaster = masterMap.get(leaderPath);
        return isMaster == null ? false : isMaster;
    }

    public static void main(String[] args) throws Exception {

        String leaderPath = "/master";

        SelectMasterTest selectMasterTest = new SelectMasterTest();

        selectMasterTest.selectMaster(leaderPath);

        TimeUnit.SECONDS.sleep(1);

        while (true){
            if (selectMasterTest.checkMaster(leaderPath)){
                System.out.println("节点5 主节点");
            }else {
                System.out.println("节点5 从节点");
            }

            TimeUnit.SECONDS.sleep(6);
        }

    }
}
