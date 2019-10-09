package my.test.testapp.MVP;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import my.test.testapp.DatabaseCallback;
import my.test.testapp.Room.ProductDao;
import my.test.testapp.Room.ProductRoom;

public class ProductModel {

    private ProductDao productDao;

    public ProductModel(ProductDao _productDao){
        productDao = _productDao;
    }

    public void insertProduct(final DatabaseCallback databaseCallback, ProductRoom product){
        Log.d("My_TAG", "Model: insertProduct");
        Completable.fromAction(() -> productDao.insertProduct(product))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        databaseCallback.onProductAdded(product);
                    }

                    @Override
                    public void onError(Throwable e) {
                        databaseCallback.onDataNotAvailable(e.toString());
                    }});

    }

    public void updateProduct(final DatabaseCallback databaseCallback, ProductRoom product){
        Completable.fromAction(() -> productDao.updateProduct(product))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        databaseCallback.onProductUpdated(product);
                    }

                    @Override
                    public void onError(Throwable e) {
                        databaseCallback.onDataNotAvailable(e.toString());
                    }});
    }

    public void deleteProduct(final DatabaseCallback databaseCallback, ProductRoom product){
        Completable.fromAction(() -> productDao.deleteProduct(product))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        databaseCallback.onProductDeleted(product);
                    }

                    @Override
                    public void onError(Throwable e) {
                        databaseCallback.onDataNotAvailable(e.toString());
                    }});
    }

    @SuppressLint("CheckResult")
    public void getAll(final DatabaseCallback databaseCallback){
        Log.d("My_TAG", "Model: getAll");
        productDao.getAllRX()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(products -> databaseCallback.onProductLoaded(products));
    }

    public int getMaxID() {
        ProductRoom lastProduct = productDao.getMaxId();
        if (lastProduct == null){
            return 1;
        }else{
            return productDao.getMaxId().getId() + 1;
        }
    }

    public ProductRoom getProductById(int productID) {
        return productDao.getProductById(productID);
    }

    @SuppressLint("CheckResult")
    public void checkAllProducts(final DatabaseCallback databaseCallback, boolean checked) {
        Log.d("My_TAG", "Model: checkAllProducts");
        Completable.fromAction(() -> {
            List<ProductRoom> products = productDao.getAll();
            for (ProductRoom product : products) {
                product.setBought(checked);
            }
            productDao.updateProducts(products);
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        databaseCallback.onDataNotAvailable(e.toString());
                    }});
    }
}
