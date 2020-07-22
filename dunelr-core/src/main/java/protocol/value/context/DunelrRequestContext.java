package protocol.value.context;

import enums.DunelrRequestEnum;

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
    private final DunelrRequestEnum requestEnum;

    //<editor-fold desc="builder pattern">

    public DunelrRequestContext(DunelrRequestContextBuilder builder) {
        this.requestEnum = builder.requestEnum;
    }

    public static DunelrRequestContextBuilder builder() {
        return new DunelrRequestContextBuilder();
    }

    /**
     * builder模式
     */
    public static final class DunelrRequestContextBuilder {

        private DunelrRequestEnum requestEnum;

        private DunelrRequestContextBuilder() {
        }

        public DunelrRequestContextBuilder setRequestEnum(DunelrRequestEnum requestEnum) {
            this.requestEnum = requestEnum;
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
}
