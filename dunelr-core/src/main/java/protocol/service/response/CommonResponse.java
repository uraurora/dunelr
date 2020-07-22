package protocol.service.response;

/**
 * @author : gaoxiaodong
 * @program : dunelr
 * @date : 2020-07-22 19:26
 * @description : 通用的消息返回类
 */
public class CommonResponse<T> implements IDunelrResponse {

    protected T result;

    protected int code;

    protected String msg;

    protected boolean success;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    protected CommonResponse(int code, boolean success, String msg, T result) {
        this.code = code;
        this.success = success;
        this.result = result;
        this.msg = msg;
    }

    public static <T> CommonResponse<T> createSuccessResponse(int code, T result){
        return new CommonResponse<>(code, true,"success", result);
    }

    public static <T> CommonResponse<T> createFailResponse(int code, T result, String msg){
        return new CommonResponse<>(code, false, msg,result);
    }
}
