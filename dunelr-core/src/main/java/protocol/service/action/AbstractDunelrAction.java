package protocol.service.action;

import protocol.service.request.IDunelrRequest;
import protocol.service.response.CommonResponse;
import protocol.service.response.IDunelrResponse;
import protocol.value.context.IDunelrContext;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-07-22 16:13
 * @description :
 */
public abstract class AbstractDunelrAction<REQUEST extends IDunelrRequest> {

    private static final String COMMON_FAILED_MSG = "cannot pass the validate rules/无法通过所有校验规则";

    protected abstract IDunelrResponse validate(REQUEST request,
                                                IDunelrContext context);

    protected abstract IDunelrResponse process(REQUEST request,
                                               IDunelrContext context);

    protected abstract IDunelrResponse process(Callable<IDunelrResponse> callable);

    protected abstract List<BiFunction<REQUEST, IDunelrContext, Boolean>> getValidateRule();

    public IDunelrResponse execute(REQUEST request, IDunelrContext context) throws Exception {
        final List<BiFunction<REQUEST, IDunelrContext, Boolean>> rules = getValidateRule();
        if(rules.stream().anyMatch(rule-> !rule.apply(request, context))) {
            return CommonResponse.createFailResponse(-1, null, COMMON_FAILED_MSG);
        }

        // Class<REQUEST> clazz = getGenericType();
        // REQUEST request = normalize(context, clazz);
        // 预验证
//        IDunerResponse preValidateResp = preValidate(request, clazz);
//        if (preValidateResp != null) {
//            return preValidateResp;
//        }
        IDunelrResponse validateResp = validate(request, context);
        if (validateResp != null) {
            return validateResp;
        }

        return cacheExecute(request, context);
    }

    private IDunelrResponse cacheExecute(REQUEST request, IDunelrContext context) {
        // todo:cache
        return process(request, context);
    }


}
