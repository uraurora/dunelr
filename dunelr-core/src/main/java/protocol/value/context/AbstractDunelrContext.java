package protocol.value.context;

import enums.ProtocolType;

import java.util.Map;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-07-22 16:05
 * @description : 抽象的dunelr网络上下文
 */
public class AbstractDunelrContext implements IDunelrContext {
    @Override
    public ProtocolType protocolType() {
        return null;
    }

    @Override
    public Map<String, String> extraMessage() {
        return null;
    }
}
