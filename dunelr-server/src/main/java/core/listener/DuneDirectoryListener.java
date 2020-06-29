package core.listener;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import core.entity.DuneDirectory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-28 17:05
 * @description : 需要同步文件的文件系统根目录，监听该目录
 */
public class DuneDirectoryListener {

    private final WatchService watchService;
    /**
     * 线程工厂
     */
    private final ThreadFactory factory;
    /**
     * 保存watchKey对根目录下所有监听目录的映射，用来快速组合文件的目录
     */
    private final Map<WatchKey, Path> keys;

    private DuneDirectoryListener(DuneDirectory directory, ThreadFactory factory) throws IOException {
        Path path = directory.getPath();
        this.watchService = path.getFileSystem().newWatchService();
        this.factory = factory;
        keys = Maps.newHashMap();
        registerDirectories(path, this.watchService);
    }

    public static DuneDirectoryListener newInstance(DuneDirectory directory) throws IOException {
        ThreadFactory factory = new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat("Dunelr-Sync-Daemon-%d")
                .build();
        return new DuneDirectoryListener(directory, factory);
    }

    public static DuneDirectoryListener newInstance(DuneDirectory directory, ThreadFactory factory) throws IOException {
        return new DuneDirectoryListener(directory, factory);
    }

    private void registerDirectories(Path path, WatchService watchService) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                WatchKey watchKey = dir.register(
                        watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY
                );
                keys.put(watchKey, dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void keepWatching() {
        for(; ;) {
            WatchKey watchKey;
            try {
                watchKey = watchService.take();
                Path dir = keys.get(watchKey);
                for (WatchEvent<?> ev : watchKey.pollEvents()) {
                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> event = (WatchEvent<Path>) ev;
                    if (ev.kind() == StandardWatchEventKinds.OVERFLOW) {
                        //事件可能lost or discarded
                        continue;
                    }
                    // 如果创建新目录，则注册所有子目录
                    Path context = dir.resolve(event.context());
                    if (Files.isDirectory(context) && ev.kind() == StandardWatchEventKinds.ENTRY_CREATE){
                        registerDirectories(context, watchService);
                    }

                    // TODO:事件处理，目前规划是将事件提交到环形队列中，待定，需要确定一次版本的边界在哪
                    Path file = event.context();
                    System.out.println("事件类型：" + event.kind());
                    System.out.println("路径" + keys.get(watchKey).toString() + " / 文件：" + file);
                    System.out.println("====================");
                }

                // 如果复位无效就删除该key
                if (!watchKey.reset()) {
                    keys.remove(watchKey);
                    // 当次所有目录都无法访问的时候，就退出
                    if (keys.isEmpty()) {
                        break;
                    }
                }
            } catch (InterruptedException | IOException e) {
                // TODO:logger
                return;
            }
        }
    }

    public void start(){
        this.factory.newThread(this::keepWatching).start();
    }
}
