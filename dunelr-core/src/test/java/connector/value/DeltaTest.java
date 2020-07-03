package connector.value;

import entity.file.DuneFile;
import entity.file.IDuneFile;
import value.context.DuneFileSummary;
import value.context.IDelta;
import value.entry.DeltaEntry;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

public class DeltaTest {

    String str1 = "/Users/gaoxiaodong/Desktop/test.txt";
    String str2 = "/Users/gaoxiaodong/Desktop/test的副本.txt";
    Path source = Paths.get(str1), target = Paths.get(str2);

    @Test
    public void testPlus() throws IOException {

        System.out.println("target size : " + Files.size(target)/(1024*1024));
        System.out.println("source size : " + Files.size(source)/(1024*1024));
        IDuneFile file = DuneFile.newInstance(target);
        DuneFileSummary summary = file.toSummary();
        System.out.println("block size : " + summary.toBlocks().size());
        IDelta deltaFile = DuneFile.newInstance(source).delta(summary);
        for (DeltaEntry buf : deltaFile){
            System.out.println(buf);
        }
    }


    @Test
    public void getIsMatch() throws IOException {
        Random random = new Random();
        try (BufferedWriter writer = Files.newBufferedWriter(target, StandardOpenOption.CREATE)) {
            for (int i = 0; i < 512 * 1024; i++) {
                writer.write(String.valueOf(random.nextInt()));
            }
            writer.flush();
        }
    }
}