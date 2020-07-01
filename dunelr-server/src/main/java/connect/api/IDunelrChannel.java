package connect.api;


import core.value.context.DunelrContext;
import core.value.context.DunelrRequestContext;
import core.value.context.IDelta;

import java.nio.file.Path;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-30 14:01
 * @description : dunelr channel的功能抽象
 */
public interface IDunelrChannel {

    /**
     * 上传文件和文件信息，是全量还是增量，还包含文件的位置，文件名等信息
     * @param context 文件上下文信息
     * @return 是否成功
     */
    boolean upload(DunelrContext context);

    /**
     * 发送请求，申请同步到，还是同步，是否成功
     * @param requestContext 请求上下文
     */
    void sendContext(DunelrRequestContext requestContext);

    /**
     * 接收请求消息并处理
     * @param requestContext 请求消息
     */
     void receiveContext(DunelrRequestContext requestContext);

    /**
     * 根据delta文件更新
     * @param context 文件信息上下文
     */
    void patch(DunelrContext context);

    /**
     * 本地同步文件
     * @param source source同步源文件
     * @param target target同步目标文件
     */
    void localPatch(Path source, Path target);


}
