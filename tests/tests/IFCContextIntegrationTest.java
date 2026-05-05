package tests;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import core.IFCContext;
import core.LabeledVar;
import exceptions.ExplicitFlowException;
import exceptions.ImplicitFlowException;
import lattice.SetLabel;
import lattice.Labeled;

public class IFCContextIntegrationTest {

    @Test
    public void legalExplicitFlowDoesntThrow() {
        LabeledVar<Integer> aliceVar = new LabeledVar<>(0, new SetLabel("Alice"));
        Labeled<Integer> aliceData = new Labeled<>(1, new SetLabel("Alice"));

        aliceVar.set(aliceData); // Should not throw, labels match

        assertEquals(aliceData.getValue(), aliceVar.get().getValue());
        assertEquals(aliceData.getLabel(), aliceVar.get().getLabel());
    }

    @Test
    public void illegalExplicitFlowThrows() {
        LabeledVar<Integer> aliceVar = new LabeledVar<>(0, new SetLabel("Alice"));
        Labeled<Integer> bobData = new Labeled<>(1, new SetLabel("Bob"));

        assertThrows(ExplicitFlowException.class, () -> aliceVar.set(bobData));
    }

    @Test
    public void implicitFlowInsideHighPcThrows() {
        LabeledVar<Integer> lowVar = new LabeledVar<>(0, new SetLabel("Alice"));
        Labeled<Boolean> cond = new Labeled<>(true, new SetLabel("Bob"));

        assertThrows(ImplicitFlowException.class, () ->
            IFCContext.runIf(cond, () -> lowVar.set(new Labeled<>(42, new SetLabel("Alice"))))
        );
    }
}
