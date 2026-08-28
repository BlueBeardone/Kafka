package za.co.bastion.fischl;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class FischlHirelingException extends Exception {
    private int status;
    private String code;
    private String title;
    private String message;
    private String details;
}