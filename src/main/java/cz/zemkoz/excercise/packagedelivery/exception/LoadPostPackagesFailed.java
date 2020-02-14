package cz.zemkoz.excercise.packagedelivery.exception;

public class LoadPostPackagesFailed extends RuntimeException {
    public LoadPostPackagesFailed(String message) {
        super(message);
    }
}
