package za.co.bastion.fischl.newspapers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import za.co.bastion.fischl.CustomHttpClientOptions;
import za.co.bastion.fischl.FischlArticle;
import za.co.bastion.fischl.FischlNewspaperException;

import java.util.List;

@RegisterRestClient(configKey = "fischl-newspapers-api")
@RegisterProvider(FischlNewspapersExceptionMapper.class)
@RegisterProvider(CustomHttpClientOptions.class)
public interface FischlNewspapersConnector {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    FischlArticle create(FischlArticle article) throws FischlNewspaperException;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    List<FischlArticle> readAll() throws FischlNewspaperException;
}