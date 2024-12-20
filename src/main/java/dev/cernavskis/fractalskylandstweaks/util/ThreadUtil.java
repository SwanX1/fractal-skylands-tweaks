// Copyright (c) 2024 Kārlis Čerņavskis, All Rights Reserved unless otherwise explicitly stated.
package dev.cernavskis.fractalskylandstweaks.util;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ThreadUtil {

    public static void runAsync(Runnable function) {
        new Thread(function).start();
    }

    public static void runAsync(String name, Runnable function) {
        new Thread(function, name).start();
    }

    public static class AnyThreadMonitor {

        private List<Object> runnables = new CopyOnWriteArrayList<>();

        public void attachWait() throws InterruptedException {
            Object lock = new Object();
            runnables.add(lock);
            synchronized (lock) {
                lock.wait();
            }
            runnables.remove(lock);
        }

        public void notifyAllWait() {
            for (Object lock : runnables) {
                synchronized (lock) {
                    lock.notifyAll();
                }
            }
        }
    }
}
