package protocol.service.response;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-07-22 19:46
 * @description :
 */
public class DunelrWebsocketResponse<T> extends CommonResponse<T> {
    protected DunelrWebsocketResponse(int code, boolean success, String msg, T result) {
        super(code, success, msg, result);
    }
}
