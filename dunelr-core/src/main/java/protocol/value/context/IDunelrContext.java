package protocol.value.context;

import enums.ProtocolType;

import java.util.Map;

/**
 * dunelr的上下文接口，包含协议的的主要信息，为了扩展兼容http和websocket
 * @author gaoxiaodong
 */
public interface IDunelrContext {

    /**
     * 获取协议类型
     * @return 协议类型
     */
    ProtocolType protocolType();
    /**
     * 获取额外参数
     * @return 额外参数
     */
    Map<String, String> extraMessage();

}
