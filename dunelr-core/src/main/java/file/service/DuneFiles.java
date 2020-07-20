package file.service;

import file.entity.DuneFile;
import file.entity.IDuneFile;
import file.value.context.IDelta;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.EnumSet;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-22 11:05
 * @description : 执行DeltaFile的识别与处理
 */
public abstract class DuneFiles {
    private static final Logger logger = LogManager.getLogger(DuneFiles.class);

    /**
     * 获取DuneFile实例，如果不存在则报异常
     * @param path 文件path
     * @return 文件实例
     * @throws IOException
     */
    public static IDuneFile getIfPresent(Path path) throws IOException {
        if (Files.exists(path)) {
            return DuneFile.newInstance(path);
        } else {
            throw new NoSuchFileException(path.toString());
        }
    }

    /**
     * 获取DuneFile实例，如果不存在则创建
     * @param path
     * @return
     */
    public static IDuneFile get(Path path) {
        IDuneFile res = null;
        try {
            if(!Files.exists(path)){
                Files.createFile(path);
            }
            res = DuneFile.newInstance(path);
        } catch (IOException e) {
            logger.error(path.toString() + "文件发生异常：" + e);
        }
        return res;
    }

    /**
     *
     * @param duneFile
     * @param deltaFile
     * @return
     */
    public static IDuneFile plus(IDuneFile duneFile, IDelta deltaFile) {
        IDuneFile res = null;
        try {
            res = duneFile.plus(deltaFile);
        } catch (IOException e) {
            logger.error(duneFile.getPath().toString() + "文件发生异常：" + e);
        }
        return res;
    }

    public static IDelta delta(IDuneFile source, IDuneFile target) {
        IDelta res = null;
        try {
            res = source.delta(target.toSummary());
        } catch (IOException e) {
            logger.error(source.getPath().toString() + "source文件同步" + target + "target文件时发生异常：" + e);
        }
        return res;
    }

    /**
     * 文件切片，并写入目标文件目录
     * @param file dune文件
     * @param chunkSize 文件块大小
     * @throws IOException
     */
    public static void chunkFile(IDuneFile file, long chunkSize, Path targetPath) throws IOException {
        final Path path = file.getPath();
        if(!Files.isDirectory(targetPath)){
            throw new IllegalArgumentException("目标目录不是文件夹:" + targetPath);
        }
        if (Files.notExists(path) || Files.isDirectory(path)) {
            throw new IllegalArgumentException("文件不存在:" + file);
        }
        if (chunkSize < 1) {
            throw new IllegalArgumentException("分片大小不能小于1个字节:" + chunkSize);
        }
        // 原始文件大小
        final long fileSize = file.size();
        // 分片数量
        final long numberOfChunk = fileSize % chunkSize == 0 ? fileSize / chunkSize : (fileSize / chunkSize) + 1;
        // 原始文件名称
        final String fileName = path.getFileName().toString();
        // 读取原始文件
        try(FileChannel fileChannel = FileChannel.open(path, EnumSet.of(StandardOpenOption.READ))){
            for (int i = 0; i < numberOfChunk; i++) {
                long start = i * chunkSize;
                long end = start + chunkSize;
                if (end > fileSize) {
                    end = fileSize;
                }
                // 分片文件名称
                Path chunkFile = Paths.get(fileName + "-" + (i + 1));
                try (FileChannel chunkFileChannel = FileChannel.open(targetPath.resolve(chunkFile),
                        EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE))){
                    // 返回写入的数据长度
                    fileChannel.transferTo(start, end - start, chunkFileChannel);
                }
            }
        }
    }

    public static void chunkFile(IDuneFile file, long chunkSize) throws IOException {
        chunkFile(file, chunkSize, file.getPath().getParent());
    }

    /**
     *
     * @param file
     * @param chunkFiles
     * @throws IOException
     */
    public static void mergeFile (IDuneFile file, IDuneFile... chunkFiles) throws IOException {
        if (chunkFiles == null || chunkFiles.length == 0) {
            throw new IllegalArgumentException("分片文件不能为空");
        }
        try (FileChannel fileChannel = FileChannel.open(file.getPath(), EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE))){
            for (IDuneFile chunkFile : chunkFiles) {
                try(FileChannel chunkChannel = FileChannel.open(chunkFile.getPath(), EnumSet.of(StandardOpenOption.READ))){
                    chunkChannel.transferTo(0, chunkChannel.size(), fileChannel);
                }
            }
        }
    }
}
