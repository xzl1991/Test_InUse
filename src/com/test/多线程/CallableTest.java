package com.test.多线程;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// 类似实现Runnable，这里实现自己的线程任务，假设返回的结果为String
class TaskWithResult implements Callable<String> {
    private int id;
 
    public TaskWithResult(int id) {
        this.id = id;
    }
 
    public String call() throws Exception {
        return "result of TaskWithResult " + id+"当前线程:"+Thread.currentThread().getName();
    }
}
 
public class CallableTest {
    public static void main(String[] args) throws InterruptedException,
            ExecutionException {
        // 这个线程池可以根据需要实例化不同的线程池
        ExecutorService exec = Executors.newCachedThreadPool();//适合小型的
         
        List<Future<String>> results = new ArrayList<Future<String>>();    //Future 相当于是用来存放Executor执行的结果的一种容器
        for (int i = 0; i < 10; i++) {
            // 这里之所以先将返回的Future放到List中，是避免串行化执行线程；先将任务放入线程池，再一个个调取结果
            results.add(exec.submit(new TaskWithResult(i)));
        }
        for (Future<String> fs : results) {
        // 这个if判断可以去掉，就会变成阻塞式等等线程结果；不去掉的话，需要更复杂的实现来做多次遍历，以避免漏掉某个线程结果
            if (fs.isDone()) {
            // 这里的get方法，会将String的结果拿到。
                System.out.println(fs.get());
            } else {
                System.out.println("Future result is not yet complete");
            }
        }
        exec.shutdown();
    }
}