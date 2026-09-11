package za.co.bastion.fischl;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class FischlRenewedTokenHolder {
    private String token;

    public String get() {
        return token;
    }

    public void set(String token) {
        this.token = token;
    }

    public void clear() {
        this.token = null;
    }
}
