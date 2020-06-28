package core.entity;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class DuneDirectoryTest {

    @Test
    public void start() throws IOException {
        DuneDirectory directory = DuneDirectory.newInstance(Paths.get("/Users/gaoxiaodong/Desktop/"));
        directory.start();
    }
}