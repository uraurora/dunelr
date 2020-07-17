package file.entity;

import com.google.common.collect.Lists;
import file.service.DuneFiles;
import file.value.context.IDelta;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-23 11:49
 * @description : dunelr文件同步工具，完成抽象文件的同步工作
 */
public class Dunelr {

    private final IDuneFile source;

    private final List<IDuneFile> destination;

    private final boolean isAsync;

    //<editor-fold desc="builder pattern">
    private Dunelr (DunelrBuilder builder){
        source = builder.source;
        if (builder.destination.isEmpty()) {
            throw new IllegalArgumentException("至少加入一个同步目标文件地址");
        }
        destination = builder.destination;
        this.isAsync = builder.isAsync;
    }

    public static class DunelrBuilder {
        private IDuneFile source;

        private List<IDuneFile> destination;

        private boolean isAsync = false;

        public DunelrBuilder setSource(Path source) {
            try {
                this.source = DuneFiles.getIfPresent(source);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return this;
        }

        public DunelrBuilder setDestination(List<IDuneFile> destination) {
            this.destination = destination;
            return this;
        }

        public DunelrBuilder async() {
            isAsync = true;
            return this;
        }

        public DunelrBuilder setDestination(Path... destination) {
            this.destination = Lists.newArrayList();
            for (Path path : destination) {
                // FIXME: 如果duneFile是新建的，那么就不应该采用增量同步
                this.destination.add(DuneFiles.get(path));
            }
            return this;
        }

        public Dunelr build(){
            return new Dunelr(this);
        }
    }

    public static DunelrBuilder builder(){
        return new DunelrBuilder();
    }
    //</editor-fold>

    public List<IDuneFile> execute(){
        List<IDuneFile> res = Lists.newArrayList();
        if (isAsync){

        }
        else{
            for (IDuneFile tar : destination){
                final IDelta delta = DuneFiles.delta(source, tar);

                final int sum = delta.getEntries().stream().filter(e -> !e.isBool()).mapToInt(e -> e.getBuf().length).sum();
                System.out.println("byte length : " + sum);

                res.add(DuneFiles.plus(tar, delta));
            }
        }

        return res;
    }



}
