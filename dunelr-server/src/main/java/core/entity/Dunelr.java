package core.entity;

import com.google.common.collect.Lists;
import core.entity.IDuneFile;
import core.service.DuneFiles;
import core.value.DuneBlock;
import core.value.IDelta;

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

    private final Path source;

    private final List<Path> destination;


    private Dunelr (DunelrBuilder builder){
        source = builder.source;
        destination = builder.destination;
    }

    public static class DunelrBuilder {
        private Path source;

        private List<Path> destination;

        public DunelrBuilder setSource(Path source) {
            this.source = source;
            return this;
        }

        public DunelrBuilder setDestination(List<Path> destination) {
            this.destination = destination;
            return this;
        }

        public DunelrBuilder setDestination(Path destination, Path... other) {
            this.destination = Lists.newArrayList(other);
            this.destination.add(destination);
            return this;
        }

        public DunelrBuilder setDestination(Path destination) {
            this.destination = Lists.newArrayList(destination);
            return this;
        }



        public Dunelr build(){
            return new Dunelr(this);
        }
    }

    public static DunelrBuilder builder(){
        return new DunelrBuilder();
    }

    public List<IDuneFile> execute(){
        List<IDuneFile> res = Lists.newArrayList();
        try {
            final IDuneFile src = DuneFiles.getIfPresent(source);
            for (Path path : destination){
                final IDuneFile tar = DuneFiles.getIfPresent(path);
                final IDelta delta = DuneFiles.delta(src, tar);
                res.add(DuneFiles.plus(tar, delta));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }



}
