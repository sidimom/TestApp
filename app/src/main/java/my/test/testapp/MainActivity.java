package my.test.testapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import my.test.testapp.Dagger.AppModule;
import my.test.testapp.Dagger.DaggerAppComponent;
import my.test.testapp.Dagger.RoomModule;
import my.test.testapp.Room.ProductDao;
import my.test.testapp.Room.ProductRoom;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_products)
    RecyclerView rv_products;

    ProductDao productDao;
    ProductAdapter adapter;

    final int ID_ACTIVITY_NOTE_DETAILS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_without_binding);

        ButterKnife.bind(this);
        productDao = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .build()
                .getProductDao();

        rv_products.setLayoutManager(new LinearLayoutManager(this));
        ProductAdapter.OnProductClickListener onProductClickListener = new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(ProductRoom product) {
                openProductActivity(product);
            }
        };
        adapter = new ProductAdapter(onProductClickListener);
        rv_products.setAdapter(adapter);
        adapter.refreshRecyclerView(productDao);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ID_ACTIVITY_NOTE_DETAILS){
            if (data == null){
                Toast.makeText(this, "Data of note is not received!", Toast.LENGTH_SHORT).show();
                return;
            }

            ProductRoom product;
            int productID = data.getIntExtra("ProductID", 0);
            if (productID == 0){
                product = new ProductRoom();
                ProductRoom lastProduct = productDao.getMaxId();
                if (lastProduct == null){
                    product.id = 1;
                }else{
                    product.id = productDao.getMaxId().id + 1;
                }
                product.name = data.getStringExtra("NameProduct");
                product.description = data.getStringExtra("DescriptionProduct");
                product.price = data.getIntExtra("PriceProduct", 0);
                productDao.insertProduct(product);
            }else{
                product = productDao.getProductById(productID);
                if (product == null){
                    product = new ProductRoom();
                    product.id = productID;
                    product.name = data.getStringExtra("NameProduct");
                    product.description = data.getStringExtra("DescriptionProduct");
                    product.price = data.getIntExtra("PriceProduct", 0);
                    productDao.insertProduct(product);
                }else{
                    productDao.updateProduct(product);
                }
            }

            Toast.makeText(this, "Product " + product.name + " is created!", Toast.LENGTH_SHORT).show();
            adapter.refreshRecyclerView(productDao);
        }
    }

    @OnClick(R.id.fab_add)
    void addProduct(){
        openProductActivity(new ProductRoom());
    }

    private void openProductActivity(ProductRoom product) {
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra("ProductID", product.id);
        intent.putExtra("ProductName", product.name);
        intent.putExtra("ProductDescription", product.description);
        intent.putExtra("ProductPrice", product.price);
        startActivityForResult(intent, ID_ACTIVITY_NOTE_DETAILS);
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

