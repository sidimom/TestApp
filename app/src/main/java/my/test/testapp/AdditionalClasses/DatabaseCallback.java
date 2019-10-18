package my.test.testapp.AdditionalClasses;

import java.util.List;

import my.test.testapp.Room.ProductRoom;

public interface DatabaseCallback {

    void onProductLoaded(List<ProductRoom> products);

    void onProductDeleted(ProductRoom product);

    void onProductAdded(ProductRoom product);

    void onDataNotAvailable(String errorText);

    void onProductUpdated(ProductRoom product);

    void onProductsUpdated();
}
