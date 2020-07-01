package core.entity;

import core.entity.file.DuneDirectory;
import core.entity.listener.DuneDirectoryListener;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.*;

public class DuneDirectoryTest {

    @Test
    public void start() throws IOException, InterruptedException {
        DuneDirectoryListener directory = DuneDirectory.newInstance(Paths.get("/Users/gaoxiaodong/Desktop/test"))
                .toListener();
        directory.start();
        Thread.sleep(500000);

    }
    @Test
    public void delayTest() throws InterruptedException {
        BlockingQueue<Task> delayqueue = new DelayQueue<>();
        long now = System.currentTimeMillis();
        delayqueue.put(new Task(now+3000, now));
        delayqueue.put(new Task(now+4000, now));
        delayqueue.put(new Task(now+6000, now));
        delayqueue.put(new Task(now+1000, now));
        delayqueue.put(new Task(now+10000, now));

        System.out.println(delayqueue);
        long n = System.currentTimeMillis();
        while(!delayqueue.isEmpty()) {
            System.out.println(delayqueue.take());
            System.out.println("过去了："  + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - n) + "秒");
        }

    }

    static class Task implements Delayed{
        long time;
        long now;
        public Task(long time, long now) {
            this.time = time;
            this.now = now;
        }
        @Override
        public int compareTo(Delayed o) {
            return Long.compare(this.getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(time - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
        }
        @Override
        public String toString() {
            return String.valueOf(time - now);
        }
    }
}