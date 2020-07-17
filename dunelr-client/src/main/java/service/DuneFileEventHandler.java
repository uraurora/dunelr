package service;

import enums.DirectionEnum;
import enums.DunelrFileEnum;
import file.value.context.DunelrContext;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-07-02 21:15
 * @description : 处理文件变化的handler，主要是生成上下文
 */
public class DuneFileEventHandler {

    /**
     * 根据文件路径和事件构建网络传输的上下文
     * @param ev 事件
     * @param root 文件根目录路径
     * @return 上下文信息
     */
    public static DunelrContext toDunelrContext(WatchEvent<?> ev, String root){
        @SuppressWarnings("unchecked")
        WatchEvent<Path> event = (WatchEvent<Path>) ev;
        final boolean isModify = ev.kind() == StandardWatchEventKinds.ENTRY_MODIFY,
                isCreate = ev.kind() == StandardWatchEventKinds.ENTRY_CREATE;
        DunelrFileEnum eventType =  isModify ? DunelrFileEnum.DELTA : (isCreate ? DunelrFileEnum.FULL : DunelrFileEnum.DELETE);

        // 这个path是client或server端文件的绝对路径，与root的文件根目录比较生成相对路径
        String path = event.context().toString();
        // 构建上下文信息
        return DunelrContext.builder()
                .setDirection(DirectionEnum.SOURCE)
                .setFileEnum(eventType)
                // 这个路径为文件的相对于根目录的路径
                .setPath(toRelativePath(path, root))
                // TODO:文件如何设置
                // .setFile()
                .build();
    }

    private static String toRelativePath(String path1, String path2){
        String file = path1.length() >= path2.length() ? path1 : path2,
                root = path1.length() < path2.length() ? path1 : path2;
        return file.substring(root.length()-1);
    }

    public static void main(String[] args) {
        Path p1 = Paths.get("asasas/f/e/a/b/c");
        Path p2 = Paths.get("asasas/f/e/a");
        long time = System.nanoTime();
        System.out.println(p2.relativize(p1));
        System.out.println(System.nanoTime() - time);
        time = System.nanoTime();
        System.out.println(toRelativePath(p1.toString(), p2.toString()));
        System.out.println(System.nanoTime() - time);


    }
}
