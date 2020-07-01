package core.value.context;

import core.enums.DunelrFileEnum;

import java.nio.file.Path;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-07-01 11:02
 * @description : server与client交互的上下文信息
 */
public class DunelrContext {

    private final Object file;

    /**
     * 文件消息，是全量还是增量
     */
    private final DunelrFileEnum fileEnum;

    /**
     * 文件路径
     */
    private final Path path;

    //<editor-fold desc="builder pattern">
    public DunelrContext(DunelrContextBuilder builder) {
        this.file = builder.file;
        this.fileEnum = builder.fileEnum;
        this.path = builder.path;
    }

    public static DunelrContextBuilder builder() {
        return new DunelrContextBuilder();
    }

    public static final class DunelrContextBuilder {
        private Object file;
        private DunelrFileEnum fileEnum;
        private Path path;

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
}
