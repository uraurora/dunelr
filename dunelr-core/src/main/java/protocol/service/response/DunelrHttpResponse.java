package protocol.service.response;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-07-22 19:43
 * @description :
 */
public class DunelrHttpResponse<T> extends CommonResponse<T> {

    protected DunelrHttpResponse(int code, boolean success, String msg, T result) {
        super(code, success, msg, result);
    }

    public static <T> DunelrHttpResponse<T> createSuccessResponse(T result){
        return createResponse(HttpResponseStatus.OK, result, true);
    }

    public static <T> DunelrHttpResponse<T> createFailResponse(T result){
        return createResponse(HttpResponseStatus.BAD_REQUEST, result, false);
    }

    public static <T> DunelrHttpResponse<T> createNotFoundResponse(T result){
        return createResponse(HttpResponseStatus.NOT_FOUND, result, false);
    }

    public static <T> DunelrHttpResponse<T> createResponse(HttpResponseStatus status, T result, boolean isSuccess){
        return new DunelrHttpResponse<>(status.code(), isSuccess, status.reasonPhrase(), result);
    }

}
