package org.fawry.quarkus.productapi.resource;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.converters.multi.MultiRx3Converters;
import io.smallrye.mutiny.converters.uni.UniRx3Converters;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.fawry.quarkus.productapi.model.Product;
import org.fawry.quarkus.productapi.repository.ProductRepository;
import org.jboss.resteasy.reactive.ResponseStatus;

import java.net.URI;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {
    @Inject
    ProductRepository productRepository;

    @GET
    public Multi<Product> getAllProducts() {
        return Multi.createFrom().converter(MultiRx3Converters.fromObservable(), productRepository.getAllProducts());
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getProduct(@PathParam("id") int id) {
        return Uni.createFrom()
                .converter(UniRx3Converters.fromMaybe(), productRepository.getProduct(id))
                .map(product -> Response.ok(product).build())
                .onItem().ifNull()
                .continueWith(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Uni<Response> createProduct(@Valid Product product) {
        return Uni.createFrom()
                .converter(UniRx3Converters.fromSingle(), productRepository.addProduct(product))
                .map(createdProduct ->
                        Response.created(URI.create("/products/" + createdProduct.getId()))
                                .entity(createdProduct)
                                .build()
                )
                .onFailure()
                .recoverWithItem(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
    }
}
