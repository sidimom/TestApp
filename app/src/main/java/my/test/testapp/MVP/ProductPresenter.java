package my.test.testapp.MVP;

import android.util.Log;

import java.util.List;

import my.test.testapp.Dagger.AppModule;
import my.test.testapp.Dagger.DaggerAppComponent;
import my.test.testapp.Dagger.RoomModule;
import my.test.testapp.DatabaseCallback;
import my.test.testapp.MainActivity;
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
        product.setName(view.getStringExtra("NameProduct"));
        product.setDescription(view.getStringExtra("DescriptionProduct"));
        product.setPrice(view.getFloatExtra("PriceProduct"));
    }

    private int getProductID(int productID){
        return ((productID == 0) ? model.getMaxID() : productID);
    }

    public void viewIsReady() {
        Log.d("My_TAG", "Presenter: viewIsReady");
        model.getAll(this);
    }

    public void onActivityResult(int requestCode) {
        if (view.isProductActivity(requestCode)){
            if (view.isIntentResultNull()){
                view.showToast("Data of product is not received!");
                return;
            }

            int productID = view.getIntExtra("ProductID");
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
        model.checkAllProducts(this, checked);
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
        view.showToast("Product " + product.getName() + " is deleted");
    }

    @Override
    public void onProductAdded(ProductRoom product) {
        view.showToast("Product " + product.getName() + " is added");
    }

    @Override
    public void onDataNotAvailable(String errorText) {
        view.showToast("Error: " + errorText);
    }

    @Override
    public void onProductUpdated(ProductRoom product) {
        view.showToast("Product " + product.getName() + " is updated");
    }
}
