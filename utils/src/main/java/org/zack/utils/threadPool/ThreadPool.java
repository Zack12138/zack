package org.zack.utils.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {

    private static int maxThread = 10;


    public static int getMaxThread() {
        return maxThread;
    }

    private static class InstanceFactory {
        private static ExecutorService executorService = Executors.newFixedThreadPool(getMaxThread());
    }

    public static ExecutorService getInstance() {
        return InstanceFactory.executorService;
    }

}
