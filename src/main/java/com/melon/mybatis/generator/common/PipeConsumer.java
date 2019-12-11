package com.melon.mybatis.generator.common;

public abstract class PipeConsumer<T> implements Runnable{

    private PipeLine<T> pipeLine;
    private String id;

    public PipeConsumer(String id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            for(;;){
                T t = pipeLine.poll();
                if(t!=null){
                    consume(t);
                }else {
                    if(pipeLine.producerCount()==0)
                        break;
                    System.out.println(String.format("PipeConsumer[%s] hungry",id));
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            System.err.println("PipeConsumer["+id+"] got an ERROR!"+e.getMessage());
        }finally {
            pipeLine.unregistConsumer(this);
        }
    }

    public void setPipeLine(PipeLine<T> pipeLine) {
        this.pipeLine = pipeLine;
    }

    public String getId() {
        return id;
    }

    abstract protected void consume(T t) throws Exception;

}
