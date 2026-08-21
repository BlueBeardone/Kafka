package za.co.bastion.tsaritsa.adapter.newspapers;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.tsaritsa.api.newspapers.TsaritsaNewspapersApi;

@Path("/tsaritsa/v1/newspapers")
public class TsaritsaNewspapersRoute {

    @Inject
    private TsaritsaNewspapersApi newspapersApi;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse createNewspaperArticle(TsaritsaNewspapersApi.NewspaperArticle article) {
        return newspapersApi.createNewspaperArticle(article);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getNewspaperArticle(@PathParam("id") String id) {
        return newspapersApi.getNewspaperArticle(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse getAllNewspaperArticles() {
        return newspapersApi.getAllNewspaperArticles();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse updateNewspaperArticle(@PathParam("id") String id, TsaritsaNewspapersApi.NewspaperArticle article) {
        return newspapersApi.updateNewspaperArticle(id, article);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiResponse deleteNewspaperArticle(@PathParam("id") String id) {
        return newspapersApi.deleteNewspaperArticle(id);
    }
}