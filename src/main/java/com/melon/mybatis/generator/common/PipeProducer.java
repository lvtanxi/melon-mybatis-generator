package com.melon.mybatis.generator.common;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生产者
 * @param <T>
 */
public abstract class PipeProducer<T> implements Runnable{
    private PipeLine<T> pipeLine;
    protected String id;
    private AtomicInteger idx;

    public PipeProducer(String id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            for(;;){
                T t = produce(idx.getAndIncrement());
                if(t!=null){
                    pipeLine.put(t);
                }else {
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println(String.format("PipeProducer[%s] got an ERROR,idx:%s:%s",id,idx,e.getMessage()));
        }finally {
            pipeLine.unregistProducer(this);
        }
    }

    public void setPipeLine(PipeLine<T> pipeLine) {
        this.pipeLine = pipeLine;
    }

    public String getId() {
        return id;
    }

    public void setIdx(AtomicInteger idx) {
        this.idx = idx;
    }

    abstract protected T produce(int idx) throws Exception;
}
