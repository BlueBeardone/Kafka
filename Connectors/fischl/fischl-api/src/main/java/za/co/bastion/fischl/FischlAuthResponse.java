package za.co.bastion.fischl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Accessors(chain = true)
public class FischlAuthResponse {
    private FischlUser user;
    private String token;
}