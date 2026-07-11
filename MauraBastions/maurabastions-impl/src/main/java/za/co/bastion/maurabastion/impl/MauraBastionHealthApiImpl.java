package za.co.bastion.maurabastion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.maurabastion.api.MauraBastionHealthApi;

@ApplicationScoped
public class MauraBastionHealthApiImpl implements MauraBastionHealthApi {
    @Override
    public ApiResponse health() {
        return ApiResponse.success("Kafka is running", null);
    }
}
