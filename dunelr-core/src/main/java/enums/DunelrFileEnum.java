package enums;

/**
 * @author gaoxiaodong
 */

public enum DunelrFileEnum {
    /**
     * 增量文件同步，发送方必定为为source方
     */
    DELTA,

    /**
     * 文件删除
     */
    DELETE,
    /**
     * 全量文件，表示新增
     */
    FULL,

    /**
     * checksum摘要文件
     */
    SUMMARY,
    ;

}
