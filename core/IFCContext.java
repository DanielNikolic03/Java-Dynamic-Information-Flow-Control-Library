package core;

import java.util.function.Supplier;

import exceptions.IFCBreakException;
import exceptions.IFCContinueException;
import lattice.Label;
import lattice.Labeled;
import lattice.SetLabel;

public class IFCContext {
    private static final ThreadLocal<Label> pcLabel = ThreadLocal.withInitial(() -> new SetLabel());

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

    public static void doBreak() {
        throw IFCBreakException.INSTANCE;
    }

    public static void doContinue() {
        throw IFCContinueException.INSTANCE;
    }

    public static void runWhile(Supplier<Labeled<Boolean>> conditionSupplier, Runnable block) {
        Label previousPC = pcLabel.get();

        try {
            while (true) {
                Labeled<Boolean> condition = conditionSupplier.get();
                Label newPC = previousPC.join(condition.getLabel());
                pcLabel.set(newPC);

                if(!condition.getValue()) {
                    break;
                }

                try {
                    block.run();
                } catch (IFCContinueException e) {
                    continue;
                } catch (IFCBreakException e) {
                    break;
                }
            }
        } finally {
            pcLabel.set(previousPC);
        }
    }

}
