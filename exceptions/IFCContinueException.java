package exceptions;

public class IFCContinueException extends RuntimeException{
    public static final IFCContinueException INSTANCE = new IFCContinueException();

    private IFCContinueException() {}

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
