package com.toast.threadpool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

/**
 * @author 土司先生
 * @time 2023/3/5
 * @describe 自定义线程池
 */
public class ThreadPool {
    /**
     * 队列默认大小
     */
    private static final int DEFAULT_WORK_QUEUE_SIZE = 5;
    /**
     * 模拟阻塞队列
     */
    private BlockingQueue<Runnable> workQueue;
    /**
     * 模拟线程池
     */
    private List<WorkThread> workThreads = new ArrayList<>();

    public ThreadPool(int poolSize) {
        this(poolSize, new LinkedBlockingQueue<>(DEFAULT_WORK_QUEUE_SIZE));
    }

    public ThreadPool(int poolSize, BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        // 创建指定的线程数量并开启任务
        IntStream.range(0, poolSize).forEach((i) -> {
            WorkThread workThread = new WorkThread();
            workThread.start();
            workThreads.add(workThread);
        });
    }

    /**
     * 执行提交的任务线程
     * @param runnable
     */
    public void execute(Runnable runnable) {
        try {
            workQueue.put(runnable);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭线程
     */
    public void shutdown() {
        if (workThreads != null && workThreads.size() > 0) {
            workThreads.stream().forEach(workThread -> {
                workThread.interrupt();
            });
        }
    }
    /**
     * 工作线程
     */
    class WorkThread extends Thread {
        @Override
        public void run() {
            Thread current = Thread.currentThread();
            while (true) {
                if (current.isInterrupted()) {
                    break;
                }
                try {
                    Runnable take = workQueue.take();
                    take.run();
                } catch (InterruptedException e) {
                    // 当发生中断异常时需要重新设置中断标志位
                    current.interrupt();
                }
            }
        }
    }
}