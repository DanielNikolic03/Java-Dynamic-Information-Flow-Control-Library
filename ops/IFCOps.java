package ops;
import java.util.function.BiFunction;
import java.util.function.Function;

import lattice.Label;
import lattice.Labeled;


public class IFCOps {
    //Binary operation, Combines two labeled objects
    public static <T, U, R> Labeled<R> compute(Labeled<T> op1, Labeled<U> op2, BiFunction<T, U, R> operation) {
        //Apply the raw operation to the underlying values
        R resultValue = operation.apply(op1.getValue(), op2.getValue());

        //Calculate the new label
        Label resultLabel = op1.getLabel().join(op2.getLabel());

        //Return the new tainted data
        return new Labeled<>(resultValue, resultLabel);
    }


    //Binary operation with constant
    public static <T, U, R> Labeled<R> compute(Labeled<T> op1, U constantOp2, BiFunction<T, U, R> operation) {

        R resultValue = operation.apply(op1.getValue(), constantOp2);

        Label resultLabel = op1.getLabel();

        return new Labeled<>(resultValue, resultLabel);
    }

    //Unary operation
    public static <T, R> Labeled<R> compute(Labeled<T> op1, Function<T, R> operation) {
        R resultValue = operation.apply(op1.getValue());
        Label resultLabel = op1.getLabel();

        return new Labeled<>(resultValue, resultLabel);
    }
}
