package my.test.testapp.MVP;

//import android.util.Log;

import java.util.List;

import my.test.testapp.Dagger.AppModule;
import my.test.testapp.Dagger.DaggerAppComponent;
import my.test.testapp.Dagger.RoomModule;
import my.test.testapp.AdditionalClasses.DatabaseCallback;
import my.test.testapp.MainActivity;
import my.test.testapp.ProductActivity;
import my.test.testapp.Room.ProductDao;
import my.test.testapp.Room.ProductRoom;

public class ProductPresenter implements DatabaseCallback {

    private MainActivity view;
    private ProductModel model;
    private ProductDao productDao;

    public void attachView(MainActivity activity){
        view = activity;
        productDao = DaggerAppComponent.builder()
                .appModule(new AppModule(view.getApplication()))
                .roomModule(new RoomModule(view.getApplication()))
                .build()
                .getProductDao();
        model = new ProductModel(productDao);
    }

    public void detachView(){
        view = null;
        productDao = null;
        model = null;
    }

    public void startProductActivity(ProductRoom product){
        view.openProductActivity(product);
    }

    private ProductRoom addProduct(int productID){
        ProductRoom product = new ProductRoom();
        product.setId(getProductID(productID));
        changeFieldsOfProduct(product);
        return product;
    }

    private void changeFieldsOfProduct(ProductRoom product) {
        product.setName(view.getStringExtra(ProductActivity.EXTRA_PRODUCT_NAME));
        product.setDescription(view.getStringExtra(ProductActivity.EXTRA_PRODUCT_DESCRIPTION));
        product.setPrice(view.getFloatExtra(ProductActivity.EXTRA_PRODUCT_PRICE));
        product.setUrlImage(view.getStringExtra(ProductActivity.EXTRA_PRODUCT_URL_IMAGE));
    }

    private int getProductID(int productID){
        return ((productID == 0) ? model.getMaxID() : productID);
    }

    public void viewIsReady() {
        model.getAll(this);
    }

    public void onActivityResult(int requestCode) {
        if (view.isProductActivity(requestCode)){
            if (view.isIntentResultNull()){
                view.showToast("Data of product is not received!");
                return;
            }

            int productID = view.getIntExtra(ProductActivity.EXTRA_PRODUCT_ID);
            ProductRoom product = model.getProductById(productID);
            if (product == null){
                model.insertProduct(this, addProduct(productID));
            }else{
                changeFieldsOfProduct(product);
                model.updateProduct(this, product);
            }
        }
    }

    public void checkAllProducts(boolean checked) {
        String searchString = view.getSearch();
        model.checkAllProducts(this, checked, searchString);
    }

    public void  checkProduct(ProductRoom product, boolean checked){
        product.setBought(checked);
        model.updateProduct(this, product);
    }

    @Override
    public void onProductLoaded(List<ProductRoom> products) {
        view.setAdapter(products);
    }

    @Override
    public void onProductDeleted(ProductRoom product) {
        setSearch();
        view.showToast("Product " + product.getName() + " is deleted");
    }

    @Override
    public void onProductAdded(ProductRoom product) {
        setSearch();
        view.showToast("Product " + product.getName() + " is added");
    }

    @Override
    public void onDataNotAvailable(String errorText) {
        view.showToast("Error: " + errorText);
    }

    @Override
    public void onProductUpdated(ProductRoom product) {
        setSearch();
        view.showToast("Product " + product.getName() + " is updated");
    }

    @Override
    public void onProductsUpdated() {
        setSearch();
    }

    public void setSearch() {
        String searchString = view.getSearch();
        List<ProductRoom> products = model.getAllWithSearch(searchString);
        view.setAdapter(products);
    }
}
