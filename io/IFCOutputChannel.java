package io;

import core.IFCContext;
import exceptions.BlockingLabelFlowException;
import exceptions.ExplicitFlowException;
import exceptions.ImplicitFlowException;
import lattice.Label;
import lattice.Labeled;

public class IFCOutputChannel {
    private final Label channelClearance;

    public IFCOutputChannel(Label channelClearence) {
        this.channelClearance = channelClearence;
    }

    public void print(Labeled<?> data) {
        //Explicit flow check
        if (!data.getLabel().flowsTo(this.channelClearance)) {
            throw new ExplicitFlowException("Explicit Flow Violation: Cannot write " + data.getLabel() + " data to a " + this.channelClearance + " output channel.");
        }

        //Implicit flow check
        Label currentPC = IFCContext.getPcLabel();
        if (!currentPC.flowsTo(this.channelClearance)) {
            throw new ImplicitFlowException("Implicit Flow Violation: Cannot write to a "+this.channelClearance+" output channel from within a "+currentPC + " context.");
        }

        //Blocking label check
        Label currentBlocking = IFCContext.getBlockingLabel();
        if(!currentBlocking.flowsTo(this.channelClearance)) {
            throw new BlockingLabelFlowException("Progess Leak Violation: Cannot write to a "+ this.channelClearance + " output channel because the theard's blocking label is elevated to " + currentBlocking);
        }


        System.out.print(data.getValue()+" {"+this.channelClearance+"}");
    }

    public void println(Labeled<?> data) {
        print(data);
        System.out.println();
    }

}
