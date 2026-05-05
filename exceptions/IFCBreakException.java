package exceptions;

public class IFCBreakException extends RuntimeException {
    public static final IFCBreakException INSTANCE = new IFCBreakException();
    
    private IFCBreakException() {}

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
