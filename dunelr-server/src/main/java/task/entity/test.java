package task.entity;

import com.github.fracpete.processoutput4j.output.CollectingProcessOutput;
import com.github.fracpete.rsync4j.RSync;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-23 11:08
 * @description :
 */
public class test {
    public static void main(String[] args) throws Exception {
        RSync rsync = new RSync()
                .source("/Users/gaoxiaodong/Downloads/文化寻访模板.pptx")
                .destination("/Users/gaoxiaodong/Desktop/")
                .update(true)
                .verbose(true)
                .recursive(true);

        CollectingProcessOutput output = rsync.execute();
        System.out.println(output.getStdOut());
        System.out.println("Exit code: " + output.getExitCode());
        if (output.getExitCode() > 0) {
            System.err.println(output.getStdErr());
        }
    }
}
