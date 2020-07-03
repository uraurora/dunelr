package repository.entity;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-22 11:19
 * @description : 全局的增量文件版本控制器
 */
class VersionRepository {
    // FIXME: 需要修改逻辑
    private final LongAdder longAdder;

    //<editor-fold desc="singleton">
    private VersionRepository(){
        longAdder = new LongAdder();
    }

    private static class VersionRepositoryHolder{
        private static final VersionRepository INSTANCE = new VersionRepository();
    }

    public static VersionRepository getInstance(){
        return VersionRepositoryHolder.INSTANCE;
    }
    //</editor-fold>

    public boolean increment(){
        try {
            longAdder.increment();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean decrement(){
        try {
            longAdder.decrement();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long get(){
        return longAdder.longValue();
    }

    public boolean reset(){
        try {
            longAdder.reset();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long incrementAndGet(){
        increment();
        return get();
    }
}
