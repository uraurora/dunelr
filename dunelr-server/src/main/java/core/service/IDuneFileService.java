package core.service;

import core.entity.IDuneFile;
import core.value.IDeltaFile;

import java.io.IOException;
import java.nio.file.Path;

/**
 * 完成针对IDuneFile的操作，增删改查，缓存
 * @author gaoxiaodong
 */
public interface IDuneFileService {

    /**
     * 获取存在的文件，如果不存在则抛出文件不存在异常
     * @param path
     * @return
     */
    IDuneFile getIfPresent(Path path) throws IOException;

    /**
     * 获取duneFile，如果不存在则新建，返回新建的版本
     * @param path
     * @return
     */
    IDuneFile get(Path path);

    boolean remove(Path path);

    IDuneFile update(IDuneFile duneFile, IDeltaFile deltaFile);

    IDeltaFile delta(IDuneFile source, IDuneFile target);

}
