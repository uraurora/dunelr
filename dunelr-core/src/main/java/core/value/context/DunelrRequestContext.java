package core.value.context;

import core.enums.DunelrRequestEnum;
import core.enums.DunelrSyncEnum;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-07-01 11:41
 * @description : dunelr请求的上下文
 */
public class DunelrRequestContext {

    /**
     * 消息是否成功发送或者接收
     */
    private DunelrRequestEnum requestEnum;

    /**
     * 文件同步方向，发送此上下文的对象是target还是source
     */
    private DunelrSyncEnum syncEnum;

    //<editor-fold desc="builder pattern">

    public DunelrRequestContext(DunelrRequestContextBuilder builder) {
        this.requestEnum = builder.requestEnum;
        this.syncEnum = builder.syncEnum;
    }

    public static DunelrRequestContextBuilder builder() {
        return new DunelrRequestContextBuilder();
    }

    /**
     * builder模式
     */
    public static final class DunelrRequestContextBuilder {
        private DunelrRequestEnum requestEnum;
        private DunelrSyncEnum syncEnum;

        private DunelrRequestContextBuilder() {
        }

        public DunelrRequestContextBuilder setRequestEnum(DunelrRequestEnum requestEnum) {
            this.requestEnum = requestEnum;
            return this;
        }

        public DunelrRequestContextBuilder setSyncEnum(DunelrSyncEnum syncEnum) {
            this.syncEnum = syncEnum;
            return this;
        }

        public DunelrRequestContext build() {
            return new DunelrRequestContext(this);
        }
    }
    //</editor-fold>

    public DunelrRequestEnum getRequestEnum() {
        return requestEnum;
    }

    public DunelrSyncEnum getSyncEnum() {
        return syncEnum;
    }
}
