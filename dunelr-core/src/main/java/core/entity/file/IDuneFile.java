package core.entity.file;

import core.value.context.DuneFileSummary;
import core.value.context.IDelta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author gaoxiaodong
 */
public interface IDuneFile {
    /**
     * 获取该文档的路径Path
     * @return 文档路径
     */
    Path getPath();

    /**
     * 获取文件块的数量
     * @return 块的个数
     */
    int getBlockNum();


    /**
     * 获取文件的块集合
     * @return 文件块列表
     */
    DuneFileSummary toSummary() throws IOException;

    /**
     * 生成deltaFile，比对另一个duneFile摘要生成增量文件，约定本身source文件
     * @param other 另一个duneFile摘要
     * @return 抽象增量文件
     */
    IDelta delta(DuneFileSummary other) throws IOException;

    /**
     * 根据delta文件生成新的抽象文件，更新文件块
     * @param deltaFile delta文件
     * @return dune文件
     */
    IDuneFile plus(IDelta deltaFile) throws IOException;

    /**
     * 获取文件容量大小，单位为byte
     * @return 文件容量
     * @throws IOException io异常
     */
    default long size() throws IOException {
        return Files.size(getPath());
    }

}
