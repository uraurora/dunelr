package core.value;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-22 11:03
 * @description : 增量文件
 */
public class DeltaFile implements IDelataFile{

    private long versionId;


    @Override
    public IDelataFile plus(IDelataFile other) {
        return null;
    }

    @Override
    public IDelataFile subtract(IDelataFile other) {
        return null;
    }
}
