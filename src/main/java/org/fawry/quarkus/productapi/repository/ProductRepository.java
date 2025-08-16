package org.fawry.quarkus.productapi.repository;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import org.fawry.quarkus.productapi.model.Product;

public interface ProductRepository {
    Observable<Product> getAllProducts();
    Maybe<Product> getProduct(int id);
    Single<Product> addProduct(Product product);
    Single<Product> updateProduct(Product product);
    Completable removeProduct(int id);
}
