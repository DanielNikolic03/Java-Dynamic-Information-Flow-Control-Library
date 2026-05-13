package core;

import java.util.function.Supplier;

import exceptions.IFCBreakException;
import exceptions.IFCContinueException;
import exceptions.IFCViolationException;
import lattice.Authority;
import lattice.Label;
import lattice.Labeled;
import lattice.SetLabel;

public class IFCContext {
    private static final ThreadLocal<Label> pcLabel = ThreadLocal.withInitial(() -> new SetLabel());
    private static final ThreadLocal<Label> blockingLabel = ThreadLocal.withInitial(() -> new SetLabel());

    public static Label getPcLabel() {
        return pcLabel.get();
    }

    public static Label getBlockingLabel() {
        return blockingLabel.get();
    }

    public static void runIf(Labeled<Boolean> condition, Runnable block) {
        
        Label previousPc = pcLabel.get();

        try {
            Label conditionLabel = condition.getLabel();
            pcLabel.set(previousPc.join(conditionLabel));

            blockingLabel.set(blockingLabel.get().join(conditionLabel));

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
                Label conditionLabel = condition.getLabel();
                pcLabel.set(previousPC.join(conditionLabel));

                blockingLabel.set(blockingLabel.get().join(conditionLabel));

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

    public static void runWithPini(Authority auth, Runnable block) {
        Label previousBlocking = blockingLabel.get();
        
        try {
            block.run();
        } finally {
            Label currentTaintedBlocking = blockingLabel.get();
            Label combinedTarget = previousBlocking.join(auth.getEfficacy());

            if(!currentTaintedBlocking.flowsTo(combinedTarget)) {
                throw new IFCViolationException("Pini Declassification failed. Authority {" + auth.getEfficacy() + "} is insufficient to declassify blocking label from {"+currentTaintedBlocking+"} back to {"+previousBlocking+"}");
            }
            blockingLabel.set(previousBlocking);
        }
    }

}
