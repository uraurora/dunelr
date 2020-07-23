package protocol.value.response;

import enums.ProtocolType;

/**
 * @author gaoxiaodong
 */
public interface IDunelrResponse {

    Object getResult();

    ProtocolType getProtocol();

    String getMsg();

}
