package my.test.testapp.MVP;

import io.reactivex.android.schedulers.AndroidSchedulers;
import my.test.testapp.Dagger.AppModule;
import my.test.testapp.Dagger.DaggerAppComponent;
import my.test.testapp.Dagger.RoomModule;
import my.test.testapp.MainActivity;
import my.test.testapp.Room.ProductDao;
import my.test.testapp.Room.ProductRoom;

public class ProductPresenter {

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

    /*public List<ProductRoom> getProducts(){
        return model.getAll();
    }*/

    private ProductRoom addProduct(int productID){
        ProductRoom product = new ProductRoom();
        product.id = getProductID(productID);
        product.name = view.getStringExtra("NameProduct");
        product.description = view.getStringExtra("DescriptionProduct");
        product.price = view.getIntExtra("PriceProduct");
        return product;
    }

    private int getProductID(int productID){
        return ((productID == 0) ? model.getMaxID() : productID);
    }

    public void viewIsReady() {
        //view.showProducts();
        productDao.getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(products -> {
                    view.setAdapter(products);
                });
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
                model.insertProduct(addProduct(productID));
            }else{
                model.updateProduct(product);
            }
        }
    }
}
