package za.co.bastion.maurabastion.adapter.newspapers;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.maurabastion.api.newspapers.MauraBastionNewspapersApi;

@Path("/maura/v1/newspapers")
public class MauraBastionNewspapersRoute {

    @Inject
    private MauraBastionNewspapersApi newspapersApi;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse createNewspaperArticle(MauraBastionNewspapersApi.NewspaperArticle article) {
        return newspapersApi.createNewspaperArticle(article);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getAllNewspaperArticles() {
        return newspapersApi.getAllNewspaperArticles();
    }
}