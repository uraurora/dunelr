package core.service;

import core.entity.IDuneFile;
import core.value.IDelta;
import io.netty.buffer.ByteBufUtil;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

public class DuneFilesTest {


    String str1 = "/Users/gaoxiaodong/Desktop/test.txt";
    String str2 = "/Users/gaoxiaodong/Desktop/test的副本.txt";
    Path source = Paths.get(str1), target = Paths.get(str2);

    @Test
    public void update() {
        IDuneFile tar = DuneFiles.get(target), src = DuneFiles.get(source);
        final IDelta delta = DuneFiles.delta(src, tar);
        // 统计不匹配的字节总数
        final int sum = delta.getEntries().stream().filter(e -> !e.isBool()).mapToInt(e -> ByteBufUtil.getBytes(e.getBuf()).length).sum();
        System.out.println("byte length : " + sum);
        DuneFiles.plus(tar, delta);
    }

    @Test
    public void delta() {
    }

    @Test
    public void createFile() throws IOException {
        Random random = new Random();
        try (BufferedWriter writer = Files.newBufferedWriter(source, StandardOpenOption.CREATE)) {
            for (int i = 0; i < 512 * 1024; i++) {
                writer.write(String.valueOf(random.nextInt()));
            }
            writer.flush();
        }
    }
}