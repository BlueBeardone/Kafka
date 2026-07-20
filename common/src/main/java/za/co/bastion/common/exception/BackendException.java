package za.co.bastion.common.exception;

import lombok.Getter;

@Getter
public class BackendException extends RuntimeException {
    private final int backendStatus;
    private final String backendMessage;

    public BackendException(int backendStatus, String backendMessage) {
        super(backendMessage);
        this.backendStatus = backendStatus;
        this.backendMessage = backendMessage;
    }
}