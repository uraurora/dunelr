package core.value.context;

import core.enums.DunelrRequestEnum;
import core.enums.DunelrSyncEnum;

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
    private DunelrRequestEnum requestEnum;

    /**
     * 文件同步方向，发送此上下文的对象是target还是source
     */
    private DunelrSyncEnum syncEnum;

}
