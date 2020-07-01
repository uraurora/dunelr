package core.entity;

import com.google.common.collect.Lists;
import core.entity.file.Dunelr;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DunelrTest {

    String str1 = "/Users/gaoxiaodong/Desktop/test.txt";
    String str2 = "/Users/gaoxiaodong/Desktop/test的副本.txt";
    String str3 = "/Users/gaoxiaodong/Desktop/test的副本4.txt";
    Path source = Paths.get(str1), target = Paths.get(str2), target2 = Paths.get(str3);

    @Test
    public void execute() {
        Dunelr dunelr = Dunelr.builder().setSource(source).setDestination(target).build();
        dunelr.execute();
    }

    @Test
    public void copyTest() throws IOException {
        LocalTime time = LocalTime.now();
        Files.copy(source, target2);
        System.out.println("byte length : " + Files.size(target2));
        System.out.println(ChronoUnit.MILLIS.between(time, LocalTime.now()));
        LocalTime time2 = LocalTime.now();
        Dunelr dunelr = Dunelr.builder().setSource(source).setDestination(target).build();
        dunelr.execute();
        System.out.println(ChronoUnit.MILLIS.between(time2, LocalTime.now()));
    }

    @Test
    public void test(){

        System.out.println(testPath("/users/test", "/test/myself"));
    }

    private List<Path> testPath(String one, String... o){
        final ArrayList<Path> res = Lists.newArrayList(Paths.get(one));
        for (String t : o){
            res.add(Paths.get(t));
        }
        return res;
    }

}