import lattice.Labeled;
import lattice.LevelLabel;
import ops.IFCOps;

public class Main {
    public static void main(String[] args) {
        // 1. Create our inputs
        Labeled<Integer> publicData = new Labeled<>(10, LevelLabel.LOW);
        Labeled<Integer> secretData = new Labeled<>(5, LevelLabel.HIGH);

        // 2. Perform an operation: publicData + secretData
        Labeled<Integer> result1 = IFCOps.compute(publicData, secretData, (Integer a, Integer b) -> a + b);
        
        // Output should be: Labeled{value=15, label=HIGH}
        System.out.println("Result 1: " + result1); 

        // 3. Perform an operation with a constant: secretData * 2
        Labeled<Integer> result2 = IFCOps.compute(secretData, 2, (a, b) -> a * b);
        
        // Output should be: Labeled{value=10, label=HIGH}
        System.out.println("Result 2: " + result2);
    }
}