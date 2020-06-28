package core.entity;

import com.github.fracpete.processoutput4j.output.CollectingProcessOutput;
import com.github.fracpete.rsync4j.RSync;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ThreadFactory;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-28 17:05
 * @description : 需要同步文件的文件系统根目录，监听该目录
 */
public class DuneDirectory {

    private final Path path;

    private final WatchService watchService;

    private final ThreadFactory factory;

    private DuneDirectory(Path path, ThreadFactory factory) throws IOException {
        this.path = path;
        this.watchService = this.path.getFileSystem().newWatchService();
        this.factory = factory;
        registerDirectories(this.path, this.watchService);
    }

    public static DuneDirectory newInstance(Path path) throws IOException {
        ThreadFactory factory = new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("Dunelr-Sync-Daemon-")
                .build();
        return new DuneDirectory(path, factory);
    }

    public static DuneDirectory newInstance(Path path, ThreadFactory factory) throws IOException {
        return new DuneDirectory(path, factory);
    }

    private static void registerDirectories(Path path, WatchService watchService) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                dir.register(
                        watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY
                );
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void keepWatching() {
        try {
            for(; ;) {
                WatchKey watchKey = watchService.take();
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
                        //事件可能lost or discarded
                        continue;
                    }
                    // TODO:事件处理，目前规划是将事件提交到环形队列中，待定，需要确定一次版本的边界在哪
                    Path file = (Path) event.context();
                    System.out.println("事件类型：" + event.kind());
                    System.out.println("文件：" + file);
                }
                // 重设WatchKey
                if (!watchKey.reset()) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(){
        factory.newThread(this::keepWatching).start();
        System.out.println("线程启动");
    }

    public static void main(String[] args) throws IOException {
        DuneDirectory directory = DuneDirectory.newInstance(Paths.get("/Users/gaoxiaodong/Desktop/test"));
        //directory.start();
        new Thread(directory::keepWatching).start();
    }
}
