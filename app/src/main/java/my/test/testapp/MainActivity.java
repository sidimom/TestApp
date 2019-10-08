package my.test.testapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import my.test.testapp.MVP.ProductPresenter;
import my.test.testapp.Room.ProductRoom;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_products)
    RecyclerView rv_products;

    ProductPresenter presenter;
    ProductAdapter adapter;
    Intent intentResult;

    final int ID_ACTIVITY_PRODUCT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        intentResult = data;
        presenter.onActivityResult(requestCode);
        presenter.viewIsReady();
    }

    @OnClick(R.id.fab_add)
    void addProduct(){
        presenter.startProductActivity(new ProductRoom());
    }

    private void init() {
        ButterKnife.bind(this);

        rv_products.setLayoutManager(new LinearLayoutManager(this));
        ProductAdapter.OnProductClickListener onProductClickListener = new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(ProductRoom product) {
                presenter.startProductActivity(product);
            }
        };
        adapter = new ProductAdapter(onProductClickListener);
        rv_products.setAdapter(adapter);

        presenter = new ProductPresenter();
        presenter.attachView(this);
        presenter.viewIsReady();
    }

  public void openProductActivity(ProductRoom product) {
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra("ProductID", product.id);
        intent.putExtra("ProductName", product.name);
        intent.putExtra("ProductDescription", product.description);
        intent.putExtra("ProductPrice", product.price);
        startActivityForResult(intent, ID_ACTIVITY_PRODUCT);
    }


    public void showProducts() {
        adapter.refreshRecyclerView(presenter.getProducts());
    }

    public boolean isProductActivity(int requestCode) {
        return requestCode == ID_ACTIVITY_PRODUCT;
    }

    public boolean isIntentResultNull() {
        return intentResult == null;
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public int getIntExtra(String key) {
        return intentResult.getIntExtra(key, 0);
    }

    public String getStringExtra(String key) {
        return intentResult.getStringExtra(key);
    }

    //@OnItemClick

//    @OnClick(R.id.btn_delete_product)
//    void deleteProduct(){
//        String et_id_value = et_id.getText().toString();
//        if (et_id_value.isEmpty()){
//            Toast.makeText(this, "Value Id is empty!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        int id_value = Integer.parseInt(et_id_value);
//        ProductRoom product = productDao.getProductById(id_value);
//
//        if (product == null){
//            Toast.makeText(this, "Product with id " + id_value + " is not founded!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        productDao.deleteProduct(product);
//        Toast.makeText(this, "Product with id " + id_value + " is deleted!", Toast.LENGTH_SHORT).show();
//    }

}

