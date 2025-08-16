package org.fawry.quarkus.productapi.resource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

@Provider
public class ProductValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<String> errors = exception
                .getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(String.join(", ", errors))
                .build();
    }
}
