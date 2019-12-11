package com.melon.mybatis.generator.common;


import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者和消费管理的产线
 *
 * @param <T>
 */
public class PipeLine<T> {

    /**
     * 存储消费者的线程
     */
    private Map<PipeConsumer, Thread> consumerMap;
    /**
     * 存储生产者的线程
     */
    private Map<PipeProducer, Thread> producerMap;

    /**
     * 标志产线是否正在运行了
     */
    private volatile boolean started;

    /**
     * 计数器
     */
    private CountDownLatch latch;
    /**
     * 重入锁
     */
    private Lock lock;
    /**
     * 阻塞队列最大个数
     */
    private int maxSize;

    /**
     * 用于生产节点
     */
    private AtomicInteger produceIdx;

    /**
     * 阻塞队列
     */
    private BlockingQueue<T> queue;

    /**
     * 初始化产线
     *
     * @param maxSize 阻塞队列容量
     */
    public PipeLine(int maxSize) {
        consumerMap = new ConcurrentHashMap<>();
        producerMap = new ConcurrentHashMap<>();
        lock = new ReentrantLock();
        queue = new ArrayBlockingQueue<>(maxSize);
        this.maxSize = maxSize;
        produceIdx = new AtomicInteger(0);
    }

    /**
     * 注册生产者
     *
     * @param pipeProducer 生产者
     * @return 产线本身
     */
    public PipeLine<T> registProducer(PipeProducer<T> pipeProducer) {
        lock.lock();
        try {
            pipeProducer.setPipeLine(this);
            pipeProducer.setIdx(produceIdx);
            producerMap.put(pipeProducer, new Thread(pipeProducer));
            System.out.println(String.format("Regist pipeProducer[%s], count %s", pipeProducer.getId(), producerMap.size()));
        } finally {
            lock.unlock();
        }
        return this;
    }

    /**
     * 注册消费者
     *
     * @param pipeConsumer 消费者
     * @return 产线本身
     */
    public PipeLine<T> registConsumer(PipeConsumer<T> pipeConsumer) {
        lock.lock();
        try {
            pipeConsumer.setPipeLine(this);
            consumerMap.put(pipeConsumer, new Thread(pipeConsumer));
            System.out.println(String.format("Regist pipeConsumer[%s], count %s", pipeConsumer.getId(), consumerMap.size()));
        } finally {
            lock.unlock();
        }
        return this;
    }

    /**
     * 启动产线
     *
     * @return 产线
     */
    public PipeLine<T> start() {
        lock.lock();
        try {
            if (started) {
                System.out.println("PipeLine has been started!");
                return this;
            }
            //计数器总共的个数是生产者和消费者的综合
            latch = new CountDownLatch(producerMap.size() + consumerMap.size());
            //循环启动生产者
            for(Thread producer:producerMap.values()){
                producer.start();
            }
            //循环启动消费者
            for(Thread consumer:consumerMap.values()){
                consumer.start();
            }
            //切换标识
            started = true;
            return this;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 生产者向产线添加产品
     *
     * @param t 产品
     * @throws InterruptedException 异常
     */
    void put(T t) throws InterruptedException {
        if (queue.size() == maxSize)
            System.out.println("【PipeLine full】");
        queue.put(t);
    }

    /**
     * 消费者从队列中拿出一个产品
     *
     * @return 产品
     * @throws InterruptedException 异常
     */
    T poll() throws InterruptedException {
        return queue.poll(1, TimeUnit.SECONDS);
    }

    /**
     * 获取生产者个数
     *
     * @return int
     */
    int producerCount() {
        lock.lock();
        try {
            if (!queue.isEmpty())
                return 1;
            return producerMap.size();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取队列容量
     *
     * @return int
     */
    int size() {
        return queue.size();
    }

    /**
     * 移除生产者
     *
     * @param pipeProducer 生产者
     */
    void unregistProducer(PipeProducer pipeProducer) {
        lock.lock();
        try {
            producerMap.remove(pipeProducer);
            System.out.println(String.format("Unregist pipeProducer[%s], count %s", pipeProducer.getId(), producerMap.size()));
        } finally {
            latch.countDown();
            lock.unlock();
        }
    }

    /**
     * 移除消费者
     *
     * @param pipeConsumer 消费者
     */
    void unregistConsumer(PipeConsumer pipeConsumer) {
        lock.lock();
        try {
            consumerMap.remove(pipeConsumer);
            //如果没有消费者，就停止产线
            if (consumerMap.isEmpty()) {
                started = false;
            }
            System.out.println(String.format("Unregist pipeConsumer[%s], count %s", pipeConsumer.getId(), consumerMap.size()));
        } finally {
            latch.countDown();
            lock.unlock();
        }
    }

    public void await() throws InterruptedException {
        latch.await();
    }

}
