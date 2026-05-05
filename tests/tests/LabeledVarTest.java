package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import core.IFCContext;
import core.LabeledVar;
import exceptions.ImplicitFlowException;
import exceptions.ExplicitFlowException;
import lattice.Labeled;
import lattice.SetLabel;

public class LabeledVarTest {
    @Test
    // This test ensures that the get method correctly returns the value and label of the LabeledVar for both 
    // a variable with a specific owner and a global variable with no owners.
    public void getBehavior() {
        LabeledVar<Integer> aliceVar = new LabeledVar<>(99, new SetLabel("Alice"));
        LabeledVar<Integer> globalVar = new LabeledVar<>(99, new SetLabel());

        Labeled<Integer> aliceData = aliceVar.get();
        Labeled<Integer> globalData = globalVar.get();

        assertEquals(aliceData.getValue(), aliceVar.get().getValue());
        assertEquals(aliceData.getLabel(), aliceVar.get().getLabel());

        assertEquals(globalData.getValue(), globalVar.get().getValue());
        assertEquals(globalData.getLabel(), globalVar.get().getLabel());
    }
    
    @Test
    // This test ensures that the set method correctly updates the value of the LabeledVar when provided with 
    // data that has a label that flows to the variable's clearance and when the current PC label 
    // flows to the variable's clearance. It tests this behavior for both a variable with a specific owner and 
    // a global variable with no owners.
    public void setBehavior() {
        LabeledVar<Integer> aliceVar = new LabeledVar<>(0, new SetLabel("Alice"));
        Labeled<Integer> aliceData = new Labeled<>(1, new SetLabel("Alice"));

        LabeledVar<Integer> bobVar = new LabeledVar<>(0, new SetLabel("Bob"));
        Labeled<Integer> bobData = new Labeled<>(1, new SetLabel("Bob"));

        LabeledVar<Integer> globalVar = new LabeledVar<>(0, new SetLabel());
        Labeled<Integer> globalData = new Labeled<>(1, new SetLabel());

        Labeled<Boolean> aliceCond = new Labeled<>(true, new SetLabel("Alice"));
        Labeled<Boolean> globalCond = new Labeled<>(true, new SetLabel());

        IFCContext.runIf(aliceCond, () -> 
            aliceVar.set(aliceData)
        );
        IFCContext.runIf(globalCond, () -> {
            globalVar.set(globalData);
            bobVar.set(bobData);
        }
        );

        assertEquals(aliceData.getValue(), aliceVar.get().getValue());
        assertEquals(aliceData.getLabel(), aliceVar.get().getLabel());

        assertEquals(bobData.getValue(), bobVar.get().getValue());
        assertEquals(bobData.getLabel(), bobVar.get().getLabel());

        assertEquals(globalData.getValue(), globalVar.get().getValue());
        assertEquals(globalData.getLabel(), globalVar.get().getLabel());
    }

    @Test
    // Ensures that attempting to set a LabeledVar with data that has a label that does not flow to the 
    // variable's clearance results in an ExplicitFlowException being thrown for both a variable with a 
    // specific owner and a global variable with no owners.
    public void setIllegalExplicitFlowThrowsException() {
        LabeledVar<Integer> aliceVar = new LabeledVar<>(0, new SetLabel("Alice"));
        LabeledVar<Integer> globalVar = new LabeledVar<>(0, new SetLabel());
        Labeled<Integer> bobData = new Labeled<>(1, new SetLabel("Bob"));

        assertThrows(ExplicitFlowException.class, () -> aliceVar.set(bobData));
        assertThrows(ExplicitFlowException.class, () -> globalVar.set(bobData));
    }

    @Test
    // Ensures that attempting to set a LabeledVar while the current IFC context's PC label does not flow to the 
    // variable's clearance results in an ImplicitFlowException being thrown.
    public void setIllegalImplicitFlowThrowsException() {
        LabeledVar<Integer> aliceVar = new LabeledVar<>(0, new SetLabel("Alice"));
        Labeled<Integer> aliceData = new Labeled<>(1, new SetLabel("Alice"));
        Labeled<Boolean> cond = new Labeled<>(true, new SetLabel("Bob"));

        assertThrows(ImplicitFlowException.class, () ->
            IFCContext.runIf(cond, () -> aliceVar.set(aliceData))
        );
    }
}
