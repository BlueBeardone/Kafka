package za.co.bastion.maurabastion.adapter;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.InvalidRequestException;

@Provider
public class MauraBastionExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        if (e instanceof InvalidRequestException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.failure(e.getMessage(), null))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.failure(e.getMessage(), null))
                .build();
    }
}
