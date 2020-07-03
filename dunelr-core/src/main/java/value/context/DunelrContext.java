package value.context;

import enums.DirectionEnum;
import enums.DunelrFileEnum;

import java.nio.file.Path;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-07-01 11:02
 * @description : server与client交互的上下文信息
 */
public class DunelrContext {

    /**
     * 文件文本信息
     */
    private final Object file;

    /**
     * 文件消息，发生了什么事件，是新增、修改还是删除
     */
    private final DunelrFileEnum fileEnum;

    /**
     * 文件路径，是哪个文件发生了变化
     */
    private final Path path;

    /**
     * 发送方的方向，将三种情况统一，如果是source则包含两种情况，为target则只有一种
     */
    private final DirectionEnum direction;

    //<editor-fold desc="builder pattern">
    public DunelrContext(DunelrContextBuilder builder) {
        this.file = builder.file;
        this.fileEnum = builder.fileEnum;
        this.path = builder.path;
        this.direction = builder.direction;
    }

    public static DunelrContextBuilder builder() {
        return new DunelrContextBuilder();
    }

    public static final class DunelrContextBuilder {
        private Object file = null;
        private DunelrFileEnum fileEnum;
        private Path path;
        private DirectionEnum direction;

        private DunelrContextBuilder() {
        }

        public DunelrContextBuilder setFile(Object file) {
            this.file = file;
            return this;
        }

        public DunelrContextBuilder setFileEnum(DunelrFileEnum fileEnum) {
            this.fileEnum = fileEnum;
            return this;
        }

        public DunelrContextBuilder setPath(Path path) {
            this.path = path;
            return this;
        }

        public DunelrContextBuilder setDirection(DirectionEnum direction) {
            this.direction = direction;
            return this;
        }

        public DunelrContext build() {
            return new DunelrContext(this);
        }
    }
    //</editor-fold>


    public Object getFile() {
        return file;
    }

    public DunelrFileEnum getFileEnum() {
        return fileEnum;
    }

    public Path getPath() {
        return path;
    }

    public DirectionEnum getDirection() {
        return direction;
    }
}
