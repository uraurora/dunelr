package file.value.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.ExtensionMethod;

import java.util.Arrays;


/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2021-02-23 11:38
 * @description :
 */
@Data
@AllArgsConstructor
public class DeltaEntry {

    private byte[] buf;

    private boolean match = false;

    public boolean isMatch() {
        return match;
    }

}
