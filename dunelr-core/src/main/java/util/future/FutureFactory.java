package util.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-07-27 21:01
 * @description :
 */
public class FutureFactory {
    private static final ThreadLocal<Future<?>> threadFuture = new ThreadLocal<>();

    public static Future<?> getFuture() {
        Future<?> future = threadFuture.get();
        threadFuture.remove();
        return future;
    }

    public static <T> Future<T> getFuture(Class<T> type) {
        @SuppressWarnings("unchecked")
        Future<T> future = (Future<T>) threadFuture.get();
        threadFuture.remove();
        return future;
    }

    public static void setFuture(Future<?> future) {
        threadFuture.set(future);
    }

    public static void remove() {
        threadFuture.remove();
    }

    /**
     * 直接返回调用结果，用于异步调用配置情况下的同步调用
     *
     * @param <T>
     *            返回值类型
     * @param type
     *            返回值类
     * @return 调用结果
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static <T> T getResult(Class<T> type) throws InterruptedException, ExecutionException {
        return type.cast(getFuture().get());
    }

    /**
     * 直接返回调用结果，用于异步调用配置情况下的同步调用
     *
     * @return 调用结果
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static Object getResult() throws InterruptedException, ExecutionException {
        return getFuture().get();
    }

}
