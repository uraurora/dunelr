package core.service;

import core.entity.DuneFile;
import core.entity.IDuneFile;
import core.value.IDelta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-22 11:05
 * @description : 执行DeltaFile的识别与处理
 */
public abstract class DuneFiles {
    // TODO:logger

    /**
     * 获取DuneFile实例，如果不存在则报异常
     * @param path 文件path
     * @return 文件实例
     * @throws IOException
     */
    public static IDuneFile getIfPresent(Path path) throws IOException {
        if (Files.exists(path)) {
            return DuneFile.newInstance(path);
        } else {
            throw new NoSuchFileException(path.toString());
        }
    }

    /**
     * 获取DuneFile实例，如果不存在则创建
     * @param path
     * @return
     */
    public static IDuneFile get(Path path) {
        IDuneFile res = null;
        try {
            if(!Files.exists(path)){
                Files.createFile(path);
            }
            res = DuneFile.newInstance(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static boolean remove(Path path) {
        boolean res = false;
        try {
            res = Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static IDuneFile plus(IDuneFile duneFile, IDelta deltaFile) {
        IDuneFile res = null;
        try {
            res = duneFile.plus(deltaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static IDelta delta(IDuneFile source, IDuneFile target) {
        IDelta res = null;
        try {
            res = source.delta(target.toSummary());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
