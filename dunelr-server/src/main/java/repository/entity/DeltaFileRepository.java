package repository.entity;

import com.google.common.collect.Lists;
import file.value.entry.IDelta;

import java.util.LinkedList;
import java.util.List;

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

    private static class DeltaFileRepositoryHolder {
        private static final DeltaFileRepository INSTANCE = new DeltaFileRepository();
    }

    public static DeltaFileRepository getInstance(){
        return DeltaFileRepositoryHolder.INSTANCE;
    }

    public List<IDelta> get(long id){
        return versions.stream()
                .filter(e->e.version == id)
                .findFirst()
                .map(VersionNode::getDeltaFiles)
                .orElse(Lists.newArrayList());
    }

    public boolean add(List<IDelta> deltaFile){
        versions.addLast(new VersionNode(versionRepository.incrementAndGet(), deltaFile));
        return true;
    }

//    public boolean merge(){
//
//    }

    /**
     * 删除指定版本的增量文件
     * @param id 提交版本
     * @return 对应版本的提交文件
     */
    public boolean remove(long id){
        versions.removeIf(versionNode -> versionNode.version == id);
        return true;
    }

    private static class VersionNode{
        private final long version;
        // FIXME: 一个版本应该对应一系列的增量文件才对
        private final List<IDelta> deltaFiles;

        private VersionNode(long version, List<IDelta> deltaFiles) {
            this.version = version;
            this.deltaFiles = deltaFiles;
        }


        public long getVersion() {
            return version;
        }

        public List<IDelta> getDeltaFiles() {
            return deltaFiles;
        }
    }
}
