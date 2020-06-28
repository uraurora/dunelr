package core.service;

import core.entity.DuneFile;
import core.entity.IDuneFile;
import core.value.IDeltaFile;
import repository.entity.DuneFileRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-22 11:05
 * @description : 执行DeltaFile的识别与处理
 */
public class DuneFileService implements IDuneFileService {

    @Override
    public IDuneFile getIfPresent(Path path) throws IOException {
        return DuneFile.newInstance(path);
    }

    @Override
    public IDuneFile get(Path path) {
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

    @Override
    public boolean remove(Path path) {
        boolean res = false;
        try {
            res = Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public IDuneFile update(IDuneFile duneFile, IDeltaFile deltaFile) {
        return null;
    }

    @Override
    public IDeltaFile delta(IDuneFile source, IDuneFile target) {
        IDeltaFile res = null;
        try {
            res = source.delta(target.toSummary());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
