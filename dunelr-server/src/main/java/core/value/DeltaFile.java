package core.value;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-22 11:03
 * @description : 增量文件
 */
public class DeltaFile implements IDeltaFile {

    private long versionId;


    @Override
    public IDeltaFile plus(IDeltaFile other) {
        return null;
    }

    @Override
    public IDeltaFile subtract(IDeltaFile other) {
        return null;
    }
}
