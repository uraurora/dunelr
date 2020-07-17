package file.entity;

import listener.DuneDirectoryListener;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-29 14:40
 * @description :
 */
public class DuneDirectory {

    private final Path path;

    private DuneDirectory(Path path) throws NoSuchFileException {
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            throw new NoSuchFileException(path.toString());
        }
        this.path = path;
    }

    public static DuneDirectory newInstance(Path path) throws NoSuchFileException {
        return new DuneDirectory(path);
    }

    public Path getPath() {
        return path;
    }

    public void traversal(Consumer<? super Path> consumer) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                consumer.accept(file);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public DuneDirectoryListener toListener(){
        try {
            return DuneDirectoryListener.newInstance(this);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DuneDirectory directory = (DuneDirectory) o;
        return Objects.equals(path, directory.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
