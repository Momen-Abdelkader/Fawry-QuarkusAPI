package org.fawry.quarkus.productapi.repository;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.fawry.quarkus.productapi.model.Product;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class SimpleProductRepository implements ProductRepository {
    private final ConcurrentHashMap<Integer, Product> products = new ConcurrentHashMap<>();
    private final AtomicInteger newProductId = new AtomicInteger(11);

    @PostConstruct
    public void init() {
        products.put(1, new Product(1, "Product 1", 10.0));
        products.put(2, new Product(2, "Product 2", 20.0));
        products.put(3, new Product(3, "Product 3", 30.0));
        products.put(4, new Product(4, "Product 4", 40.0));
        products.put(5, new Product(5, "Product 5", 50.0));
        products.put(6, new Product(6, "Product 6", 60.0));
        products.put(7, new Product(7, "Product 7", 70.0));
        products.put(8, new Product(8, "Product 8", 80.0));
        products.put(9, new Product(9, "Product 9", 90.0));
        products.put(10, new Product(10, "Product 10", 100.0));
    }

    @Override
    public Observable<Product> getAllProducts() {
        return Observable.fromIterable(products.values()).subscribeOn(Schedulers.io());
    }

    @Override
    public Maybe<Product> getProduct(int id) {
        return Maybe.fromCallable(() -> products.get(id)).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Product> addProduct(Product product) {
        return Single.fromCallable(() -> {
            int id = newProductId.getAndIncrement();
            product.setId(id);
            products.put(id, product);
            return product;
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<Product> updateProduct(Product product) {
        return Single.fromCallable(() -> {
            products.put(product.getId(), product);
            return product;
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable removeProduct(int id) {
        return Completable.fromAction(() -> products.remove(id)).subscribeOn(Schedulers.io());
    }
}
