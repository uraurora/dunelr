package protocol.value.response;

import enums.ProtocolType;

/**
 * @author : gaoxiaodong
 * @program : dunelr
 * @date : 2020-07-22 19:26
 * @description : 通用的消息返回类
 */
public class CommonResponse implements IDunelrResponse {

    protected final Object result;

    protected final ProtocolType protocol;

    protected final String msg;

    protected CommonResponse(CommonResponseBuilder builder){
        this.result = builder.result;
        this.protocol = builder.protocol;
        this.msg =builder.msg;
    }

    public static CommonResponseBuilder builder() {
        return new CommonResponseBuilder();
    }

    public static final class CommonResponseBuilder {
        protected Object result;
        protected ProtocolType protocol;
        protected String msg;

        private CommonResponseBuilder() {
        }

        public CommonResponseBuilder result(Object result) {
            this.result = result;
            return this;
        }

        public CommonResponseBuilder protocol(ProtocolType protocol) {
            this.protocol = protocol;
            return this;
        }

        public CommonResponseBuilder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public CommonResponse build() {
            return new CommonResponse(this);
        }
    }

    @Override
    public Object getResult() {
        return result;
    }

    @Override
    public ProtocolType getProtocol() {
        return protocol;
    }

    @Override
    public String getMsg() {
        return msg;
    }


}
