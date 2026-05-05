package core;

import lattice.Label;
import lattice.Labeled;
import lattice.LevelLabel;

public class IFCContext {
    private static final ThreadLocal<Label> pcLabel = ThreadLocal.withInitial(() -> LevelLabel.LOW);

    public static Label getPcLabel() {
        return pcLabel.get();
    }

    public static void runIf(Labeled<Boolean> condition, Runnable block) {
        
        Label previousPc = pcLabel.get();

        try {
            Label newPc = previousPc.join(condition.getLabel());
            pcLabel.set(newPc);

            if (condition.getValue()) {
                block.run();
            }
        } finally {
            pcLabel.set(previousPc);
        }
    }
}
