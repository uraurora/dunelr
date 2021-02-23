package file.value.entry;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2021-02-23 11:44
 * @description :文件分块后的checkSum汇总，包含文件每个块的强弱校验，也是传输对象，务必考虑性能
 */
@Data
@AllArgsConstructor
public class DuneFileSummary {

    private List<DuneBlock> blocks;
}
