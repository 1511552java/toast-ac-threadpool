package com.toast.test;

import com.toast.threadpool.ThreadPool;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author 土司先生
 * @time 2023/3/5
 * @describe
 */
public class TestThreadPool {
    public static void main(String[] args) {
        ThreadPool pool = new ThreadPool(5);
        IntStream.range(0, 10).forEach((i) -> {
            pool.execute(() -> {
                System.out.printf("【任务线程 - %s】并发编程 %n", Thread.currentThread().getName());
            });
        });
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        pool.shutdown();
    }
}
