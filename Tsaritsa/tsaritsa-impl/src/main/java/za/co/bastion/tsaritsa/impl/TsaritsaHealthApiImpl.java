package za.co.bastion.tsaritsa.impl;

import jakarta.enterprise.context.ApplicationScoped;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.tsaritsa.api.TsaritsaHealthApi;

@ApplicationScoped
public class TsaritsaHealthApiImpl implements TsaritsaHealthApi {
    @Override
    public ApiResponse health() {
        return ApiResponse.success("Tsaritsa is running", null);
    }
}