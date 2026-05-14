package core;

import java.util.concurrent.ConcurrentHashMap;

public class ProcessRegistry {
    private static final ConcurrentHashMap<String, IFCProcess> processes = new ConcurrentHashMap<>();

    public static void register(IFCProcess process) {
        processes.put(process.getProcessId(), process);
    }

    public static void unregister(String processId) {
        processes.remove(processId);
    }

    public static IFCProcess getProcess(String processId) {
        return processes.get(processId);
    }
}