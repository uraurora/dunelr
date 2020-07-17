package listener;

import file.value.context.IDelta;

/**
 * @author : gaoxiaodong04
 * @program : dunelr
 * @date : 2020-06-30 17:10
 * @description :
 */
public class DeltaListener {
    private final IDelta delta;

    public DeltaListener(IDelta delta) {
        this.delta = delta;
    }

    public long sumBytes(){
        return delta.getEntries().stream()
                .filter(e -> !e.isBool())
                .mapToInt(e -> e.getBuf().length)
                .sum();
    }
}
