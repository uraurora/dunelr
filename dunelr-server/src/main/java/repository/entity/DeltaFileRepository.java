package repository.entity;

import com.google.common.collect.Lists;
import core.value.IDeltaFile;

import java.util.LinkedList;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-22 11:32
 * @description : 增量文件的版本仓库
 */
public class DeltaFileRepository {
    /**
     * 存储版本与增量文件集合的链表
     */
    private final LinkedList<VersionNode> versions;

    private final VersionRepository versionRepository;

    private DeltaFileRepository(){
         versions = Lists.newLinkedList();
         versionRepository = VersionRepository.getInstance();
    }

    private static class DeltaFileRepositoryHodler{
        private static final DeltaFileRepository INSTANCE = new DeltaFileRepository();
    }

    public static DeltaFileRepository getInstance(){
        return DeltaFileRepositoryHodler.INSTANCE;
    }

    public IDeltaFile get(long id){
        return versions.stream()
                .filter(e->e.version == id)
                .findFirst()
                .map(VersionNode::getDelataFile)
                .get();
    }

    public boolean add(IDeltaFile deltaFile){
        versions.addLast(new VersionNode(versionRepository.incrementAndGet(), deltaFile));
        return true;
    }

//    public boolean merge(){
//
//    }

    /**
     * 删除指定版本的增量文件
     * @param id
     * @return
     */
    public boolean remove(long id){
        versions.removeIf(versionNode -> versionNode.version == id);
        return true;
    }

    private static class VersionNode{
        private final long version;

        private final IDeltaFile delataFile;

        private VersionNode(long version, IDeltaFile delataFile) {
            this.version = version;
            this.delataFile = delataFile;
        }


        public long getVersion() {
            return version;
        }

        public IDeltaFile getDelataFile() {
            return delataFile;
        }
    }
}
