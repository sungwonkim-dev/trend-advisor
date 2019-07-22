package exception;

public class NaverSearchExtractorException extends Exception {
    private int exceptionCode;

    public NaverSearchExtractorException(String msg, int exceptionCode) {
        super(msg);
        this.exceptionCode = exceptionCode;
    }

    public NaverSearchExtractorException(Exception ex, int exceptionCode){
        super(ex);
        this.exceptionCode = exceptionCode;
    }
}
