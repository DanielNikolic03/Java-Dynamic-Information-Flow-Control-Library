package core;

import java.io.Console;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import lattice.Authority;
import lattice.Label;
import lattice.Labeled;

public abstract class IFCProcess implements Runnable {
    private final String processId;
    private final Authority processAuth;

    private final BlockingQueue<Message<?>> mailbox = new LinkedBlockingQueue<>();

    public IFCProcess(String processId, Authority initalAuth) {
        this.processId = processId;
        this.processAuth = initalAuth;
        ProcessRegistry.register(this);
    }

    public String getProcessId() {
        return processId;
    }

    protected Authority getAuthority() {
        return processAuth;
    }

    public void enqueueMessage(Message<?> message) {
        mailbox.add(message);
    }

    protected <T> void send(String targetProcessId, Labeled<T> data) {
        IFCProcess target = ProcessRegistry.getProcess(targetProcessId);
        if (target != null) {
            Label presenceLabel = IFCContext.getBlockingLabel();
            Message<T> msg = new Message<>(data, presenceLabel, this.processId);
            target.enqueueMessage(msg);
        } else {
            System.err.println("Process " + targetProcessId + "not found");
        }
    }

    protected Message<?> receieve() {
        try {
            Message<?> msg = mailbox.take();

            IFCContext.taintBlockingLabel(msg.getPresenceLabel());

            return msg;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Process interrupted while receiving");
        }
    }

    @Override
    public final void run() {
        try {
            execute();
        } finally {
            ProcessRegistry.unregister(processId);
        }
    }

    protected abstract void execute();
}
