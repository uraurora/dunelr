package core.entity;

import core.listener.DuneDirectoryListener;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class DuneDirectoryTest {

    @Test
    public void start() throws IOException, InterruptedException {
        DuneDirectoryListener directory = DuneDirectoryListener.newInstance(Paths.get("/Users/gaoxiaodong/Desktop/test"));
        directory.start();
        Thread.sleep(500000);
    }
}