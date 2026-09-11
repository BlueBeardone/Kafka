package za.co.bastion.maurabastion.adapter;

import jakarta.ws.rs.container.ContainerResponseContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.fischl.FischlRenewedTokenHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RenewedTokenEnvelopeFilterTest {

    @Mock
    ContainerResponseContext responseContext;

    private final FischlRenewedTokenHolder holder = new FischlRenewedTokenHolder();
    private final RenewedTokenEnvelopeFilter filter = new RenewedTokenEnvelopeFilter();

    @Test
    void addsRenewedTokenToSuccessEnvelope() throws Exception {
        holder.set("fresh-token");
        filter.holder = holder;
        ApiResponse entity = ApiResponse.success("User found", "user-data");
        when(responseContext.getEntity()).thenReturn(entity);

        filter.filter(null, responseContext);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(responseContext).setEntity(captor.capture());
        ApiResponse updated = (ApiResponse) captor.getValue();
        assertEquals("fresh-token", updated.getRenewedToken());
        assertNull(holder.get());
    }

    @Test
    void doesNothingWithoutCapturedToken() throws Exception {
        filter.holder = holder;
        lenient().when(responseContext.getEntity()).thenReturn(ApiResponse.success("User found", "user-data"));

        filter.filter(null, responseContext);

        verify(responseContext, never()).setEntity(any());
    }

    @Test
    void ignoresNonEnvelopeEntities() throws Exception {
        holder.set("fresh-token");
        filter.holder = holder;
        when(responseContext.getEntity()).thenReturn("not an envelope");

        filter.filter(null, responseContext);

        verify(responseContext, never()).setEntity(any());
    }
}