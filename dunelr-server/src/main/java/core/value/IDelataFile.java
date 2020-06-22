package core.value;

public interface IDelataFile {


    /**
     * 合并deltaFile，约定本身为逻辑上版本较后的文件，other以增量形式添加
     * @param other 另一个增量文件
     * @return 新的合并的增量文件
     */
    IDelataFile plus(IDelataFile other);

    /**
     * deltaFile的减法，约定本身为逻辑上版本较前的文件，other以增量形式添加
     * @param other 另一个增量文件
     * @return 比较差异，相同的文件差异去除，只留下deltaFile不同的文件块
     */
    IDelataFile subtract(IDelataFile other);


}
