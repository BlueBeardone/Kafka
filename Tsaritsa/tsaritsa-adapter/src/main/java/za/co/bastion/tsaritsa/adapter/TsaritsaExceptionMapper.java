package za.co.bastion.tsaritsa.adapter;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.BackendException;
import za.co.bastion.common.exception.InvalidRequestException;

@Provider
public class TsaritsaExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(TsaritsaExceptionMapper.class);

    @Override
    public Response toResponse(Exception e) {
        if (e instanceof InvalidRequestException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.failure(e.getMessage(), null))
                    .build();
        }
        if (e instanceof BackendException be) {
            if (be.getBackendStatus() >= 500) {
                LOG.error("Backend failure", e);
                return Response.status(be.getBackendStatus())
                        .entity(ApiResponse.failure("Internal server error", null))
                        .build();
            }
            return Response.status(be.getBackendStatus())
                    .entity(ApiResponse.failure(be.getBackendMessage(), null))
                    .build();
        }
        LOG.error("Unhandled error", e);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(ApiResponse.failure("Internal server error", null))
                .build();
    }
}