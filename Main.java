import core.IFCContext;
import core.LabeledVar;
import io.IFCOutputChannel;
import lattice.Labeled;
import lattice.LevelLabel;
import ops.IFCOps;

public class Main {
    public static void main(String[] args) {
        IFCOutputChannel publicConsole = new IFCOutputChannel(LevelLabel.LOW);
        IFCOutputChannel secureLogger = new IFCOutputChannel(LevelLabel.HIGH);

        Labeled<String> publicGreeting = new Labeled<>("Hello World", LevelLabel.LOW);
        Labeled<String> secretKey = new Labeled<>("SuperSecret123", LevelLabel.HIGH);

        publicConsole.println(publicGreeting);
        secureLogger.println(secretKey);       
        secureLogger.println(publicGreeting);  

        System.out.println("Attempting to leak secret to public console...");
        publicConsole.println(secretKey);   
    }
}