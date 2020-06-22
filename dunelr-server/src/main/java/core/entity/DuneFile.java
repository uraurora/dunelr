package core.entity;

import core.value.IDelataFile;

import java.nio.file.Path;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-22 11:04
 * @description : dunelr的文件抽象
 */
public class DuneFile implements IDuneFile{
    @Override
    public Path getPath() {
        return null;
    }

    @Override
    public long getBlockNum() {
        return 0;
    }

    @Override
    public IDelataFile delta(IDuneFile duneFile) {
        return null;
    }
}
