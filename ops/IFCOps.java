package ops;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

import core.IFCContext;
import lattice.Label;
import lattice.Labeled;


public class IFCOps {
    //Binary operation, Combines two labeled objects
    public static <T, U, R> Labeled<R> compute(Labeled<T> op1, Labeled<U> op2, BiFunction<T, U, R> operation) {
        //Apply the raw operation to the underlying values
        R resultValue = operation.apply(op1.getValue(), op2.getValue());

        //Calculate the new label
        Label resultLabel = op1.getLabel().join(op2.getLabel()).join(IFCContext.getPcLabel());

        //Return the new tainted data
        return new Labeled<>(resultValue, resultLabel);
    }


    //Binary operation with constant
    public static <T, U, R> Labeled<R> compute(Labeled<T> op1, U constantOp2, BiFunction<T, U, R> operation) {

        R resultValue = operation.apply(op1.getValue(), constantOp2);

        Label resultLabel = op1.getLabel().join(IFCContext.getPcLabel());

        return new Labeled<>(resultValue, resultLabel);
    }

    //Unary operation
    public static <T, R> Labeled<R> compute(Labeled<T> op1, Function<T, R> operation) {
        R resultValue = operation.apply(op1.getValue());
        Label resultLabel = op1.getLabel().join(IFCContext.getPcLabel());

        return new Labeled<>(resultValue, resultLabel);
    }



    //Helper binary operations
    // equals
    // notEquals
    // add
    // subtract
    // divide
    // multiply
    // isGreaterThan
    // isLessThan
    // contains

    public static <T> Labeled<Boolean> equals(Labeled<T> op1, Labeled<T> op2) {
        return compute(op1, op2, (T x, T y) -> x.equals(y));
    }

    public static <T> Labeled<Boolean> notEquals(Labeled<T> op1, Labeled<T> op2) {
        return compute(op1, op2, (T x, T y) -> !(x.equals(y)));
    }

    public static Labeled<Integer> add(Labeled<Integer> op1, Labeled<Integer> op2) {
        return compute(op1, op2, (Integer x, Integer y) -> x + y);
    }

    public static Labeled<Integer> subtract(Labeled<Integer> op1, Labeled<Integer> op2) {
        return compute(op1, op2, (Integer x, Integer y) -> x - y);
    }

    public static Labeled<Integer> divide(Labeled<Integer> op1, Labeled<Integer> op2) {
        return compute(op1, op2, (Integer x, Integer y) -> x / y);
    }

    public static Labeled<Integer> multiply(Labeled<Integer> op1, Labeled<Integer> op2) {
        return compute(op1, op2, (Integer x, Integer y) -> x * y);
    }

    public static Labeled<Boolean> isGreaterThan(Labeled<Integer> op1, Labeled<Integer> op2) {
        return compute(op1, op2, (Integer x, Integer y) -> x > y);
    }

    public static Labeled<Boolean> isLessThan(Labeled<Integer> op1, Labeled<Integer> op2) {
        return compute(op1, op2, (Integer x, Integer y) -> x < y);
    }

    public static <E> Labeled<Boolean> contains (Labeled<Collection<E>> op1, Labeled<Collection<E>> op2) {
        return compute(op1, op2, (Collection<E> x, Collection<E> y) -> x.contains(y));
    }


    //Logical helper operators
    
    public static Labeled<Boolean> not(Labeled<Boolean> op1) {
        return compute(op1, (Boolean x) -> !x);
    }

    public static Labeled<Boolean> and(Labeled<Boolean> op1, Labeled<Boolean> op2) {
        return compute(op1, op2, (Boolean x, Boolean y) -> x && y);
    }

    public static Labeled<Boolean> or(Labeled<Boolean> op1, Labeled<Boolean> op2) {
        return compute(op1, op2, (Boolean x, Boolean y) -> x || y);
    }


}
