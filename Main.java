import core.IFCContext;
import core.LabeledVar;
import lattice.Label;
import lattice.Labeled;
import lattice.SetLabel;
import ops.IFCOps;
import io.IFCOutputChannel;

public class Main {

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("Testing Dynamic IFC While Loops");
        System.out.println("=========================================\n");

        testBasicWhileLoop();
        testBreakLoop();
        testContinueLoop();

        System.out.println("=========================================");
        System.out.println("All Loop Tests Completed!");
        System.out.println("=========================================");
    }

    private static void testBasicWhileLoop() {
        System.out.println("--- TEST 1: Basic Countdown Loop ---");
        
        Label publicLvl = new SetLabel(); // {}
        LabeledVar<Integer> counter = new LabeledVar<>(3, publicLvl);
        Labeled<Integer> zero = new Labeled<>(0, publicLvl);
        Labeled<Integer> one = new Labeled<>(1, publicLvl);

        System.out.println("Starting countdown...");

        // while (counter > 0)
        IFCContext.runWhile(
            () -> IFCOps.isGreaterThan(counter.get(), zero),
            () -> {
                System.out.println("Counter is at: " + counter.get().getValue());
                
                // counter = counter - 1
                Labeled<Integer> decremented = IFCOps.subtract(counter.get(), one);
                counter.set(decremented);
            }
        );
        
        System.out.println("Countdown finished!\n");
    }

    private static void testBreakLoop() {
        System.out.println("--- TEST 2: Loop with early BREAK ---");
        
        Label publicLvl = new SetLabel();
        LabeledVar<Integer> i = new LabeledVar<>(0, publicLvl);
        Labeled<Integer> max = new Labeled<>(10, publicLvl);
        Labeled<Integer> one = new Labeled<>(1, publicLvl);
        Labeled<Integer> target = new Labeled<>(4, publicLvl); // The number we are searching for

        // while (i < 10)
        IFCContext.runWhile(
            () -> IFCOps.isLessThan(i.get(), max),
            () -> {
                System.out.println("Checking i = " + i.get().getValue());

                // if (i == 4)
                Labeled<Boolean> isMatch = IFCOps.equals(i.get(), target);
                IFCContext.runIf(isMatch, () -> {
                    System.out.println("Target found at i = 4! Breaking out of loop.");
                    IFCContext.doBreak(); // Exits the loop immediately
                });

                // i = i + 1
                i.set(IFCOps.add(i.get(), one));
            }
        );
        
        System.out.println("Search loop terminated.\n");
    }

    private static void testContinueLoop() {
        System.out.println("--- TEST 3: Loop with CONTINUE ---");
        
        Label publicLvl = new SetLabel();
        LabeledVar<Integer> i = new LabeledVar<>(0, publicLvl);
        Labeled<Integer> max = new Labeled<>(4, publicLvl);
        Labeled<Integer> one = new Labeled<>(1, publicLvl);
        Labeled<Integer> skipTarget = new Labeled<>(2, publicLvl); // We want to skip processing the number 2

        // while (i < 4)
        IFCContext.runWhile(
            () -> IFCOps.isLessThan(i.get(), max),
            () -> {
                // IMPORTANT: When using continue, increment first, or you will get stuck in an infinite loop!
                // i = i + 1
                i.set(IFCOps.add(i.get(), one));
                
                Integer currentValue = i.get().getValue();

                // if (i == 2)
                Labeled<Boolean> shouldSkip = IFCOps.equals(i.get(), skipTarget);
                IFCContext.runIf(shouldSkip, () -> {
                    System.out.println("Encountered " + currentValue + ". Skipping rest of iteration!");
                    IFCContext.doContinue(); // Skips the code below and goes to the next loop iteration
                });

                // This code will NOT run when i == 2
                System.out.println("Processing data for iteration: " + currentValue);
            }
        );
        
        System.out.println("Continue loop finished.\n");

        Labeled<Integer> test = new Labeled<>(10, new SetLabel("Alice"));
        Labeled<Integer> testBob = new Labeled<>(5, new SetLabel("Bob"));
        Labeled<Integer> testPed = new Labeled<>(7, new SetLabel("Pedro"));
        Labeled<Integer> testPublic = new Labeled<>(20, new SetLabel());
        IFCContext.runIf(IFCOps.isGreaterThan(test, testBob), () -> {
            IFCOutputChannel channel = new IFCOutputChannel(new SetLabel("Bob", "Alice", "Guh"));
            channel.println(IFCOps.add(testPublic, testPublic));
        });
    }


}