package service;

import enums.DirectionEnum;
import enums.DunelrFileEnum;
import value.context.DunelrContext;

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
    public static DunelrContext toDunelrContext(WatchEvent<?> ev, Path root){
        WatchEvent<Path> event = (WatchEvent<Path>) ev;
        final boolean isModify = ev.kind() == StandardWatchEventKinds.ENTRY_MODIFY,
                isCreate = ev.kind() == StandardWatchEventKinds.ENTRY_CREATE;
        DunelrFileEnum eventType =  isModify ? DunelrFileEnum.DELTA : (isCreate ? DunelrFileEnum.FULL : DunelrFileEnum.DELETE);
        Path path = event.context();

        return DunelrContext.builder()
                .setDirection(DirectionEnum.SOURCE)
                .setFileEnum(eventType)
                //.setPath()
                // .setFile()
                .build();
    }

    public static void main(String[] args) {
        Path p1 = Paths.get("/a/b/c");
        Path p2 = Paths.get("/d/a");

        System.out.println();
    }
}
