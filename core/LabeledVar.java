package core;

import exceptions.ExplicitFlowException;
import exceptions.ImplicitFlowException;
import lattice.Label;
import lattice.Labeled;

public class LabeledVar<T> {
    private T value;
    private final Label clearance; //max sensitivity

    public LabeledVar(T initalValue, Label clearence){
        this.value = initalValue;
        this.clearance = clearence;
    }


    public Labeled<T> get() {
        return new Labeled<>(value, clearance);
    }

    public void set(Labeled<T> newData) {
        // We start wth Explicit flow check
        // Can the incoming data label flow into this variables clearence?

        if(!newData.getLabel().flowsTo(this.clearance)) {
            throw new ExplicitFlowException("Explicit Flow Violation: {"+newData.getLabel()+"} -> {"+this.clearance+"}");
        }

        // Now we check implicit flow
        // Does the IFC context allow?
        Label currentPC = IFCContext.getPcLabel();
        if (!currentPC.flowsTo(this.clearance)) {
            throw new ImplicitFlowException("Implicit Flow Violation: Attempted to modify a "+this.clearance + " variable while inside a "+currentPC+ " control flow block");
        }

        this.value = newData.getValue();
    }

}
