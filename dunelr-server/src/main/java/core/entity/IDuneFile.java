package core.entity;

import core.value.IDelataFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author gaoxiaodong
 */
public interface IDuneFile {
    /**
     * 获取该文档的路径Path
     * @return
     */
    Path getPath();

    /**
     * 获取文件块的数量
     * @return 块的个数
     */
    long getBlockNum();

    /**
     * 生成deltaFile，比对另一个duneFile生成增量文件，约定本身为版本较新的版本文件
     * @param duneFile 另一个duneFile
     * @return 抽象增量文件
     */
    IDelataFile delta(IDuneFile duneFile);

    /**
     * 根据delta文件生成新的抽象文件，更新文件块
     * @param deltaFile delta文件
     * @return dune文件
     */
    IDuneFile plus(IDelataFile deltaFile);

    /**
     * 获取文件容量大小，单位为byte
     * @return 文件容量
     * @throws IOException io异常
     */
    default long size() throws IOException {
        return Files.size(getPath());
    }

}
