package repository.entity;

import com.google.common.collect.Sets;
import core.entity.DuneDirectory;

import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Set;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-22 14:19
 * @description : 当前版本的文档仓库
 */
public class DuneFileRepository {

    private final Set<DuneDirectory> roots;

    public DuneFileRepository() {
        this.roots = Sets.newConcurrentHashSet();
    }

    public void addRoot(Path directory){
        try {
            roots.add(DuneDirectory.newInstance(directory));
        } catch (NoSuchFileException e) {
            e.printStackTrace();
        }
    }

    public void removeRoot(Path root){
        try {
            roots.remove(DuneDirectory.newInstance(root));
        } catch (NoSuchFileException e) {
            e.printStackTrace();
        }
    }
}
