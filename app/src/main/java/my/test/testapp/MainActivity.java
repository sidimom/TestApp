package my.test.testapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding3.widget.RxTextView;
import com.jakewharton.rxbinding3.widget.TextViewAfterTextChangeEvent;
import com.jakewharton.rxbinding3.widget.TextViewTextChangeEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import my.test.testapp.MVP.ProductPresenter;
import my.test.testapp.Room.ProductRoom;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_products)
    RecyclerView rv_products;

    @BindView(R.id.et_search)
    EditText et_search;

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
    }

    @OnClick(R.id.fab_add)
    void addProduct(){
        presenter.startProductActivity(new ProductRoom());
    }

    @OnCheckedChanged(R.id.swt_all)
    void onSwitchAll(CompoundButton button, boolean checked){
        presenter.checkAllProducts(checked);
    }

    @SuppressLint("CheckResult")
    private void init() {
        ButterKnife.bind(this);

        rv_products.setLayoutManager(new LinearLayoutManager(this));
        ProductAdapter.OnProductClickListener onProductClickListener = product -> presenter.startProductActivity(product);
        ProductAdapter.OnSwitchClickListener onSwitchClickListener = (product, checked) -> {
            if (product.isBought() != checked) {
                presenter.checkProduct(product, checked);
            }
        };
        adapter = new ProductAdapter(this, onProductClickListener, onSwitchClickListener);
        rv_products.setAdapter(adapter);

        presenter = new ProductPresenter();
        presenter.attachView(this);
        //убрал, т.к. добавил отображение списка с поиском
        //presenter.viewIsReady();
        presenter.setSearch();

        RxTextView.textChangeEvents(et_search)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> presenter.setSearch());

    }

    public void openProductActivity(ProductRoom product) {
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra(ProductActivity.EXTRA_PRODUCT_ID, product.getId());
        intent.putExtra(ProductActivity.EXTRA_PRODUCT_NAME, product.getName());
        intent.putExtra(ProductActivity.EXTRA_PRODUCT_DESCRIPTION, product.getDescription());
        intent.putExtra(ProductActivity.EXTRA_PRODUCT_PRICE, product.getPrice());
        intent.putExtra(ProductActivity.EXTRA_PRODUCT_URL_IMAGE, product.getUrlImage());
        startActivityForResult(intent, ID_ACTIVITY_PRODUCT);
    }

    public void setAdapter(List<ProductRoom> products) {
        adapter.refreshRecyclerView(products);
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

    public float getFloatExtra(String key) {
        return intentResult.getFloatExtra(key, (float) 0);
    }

    public String getSearch() {
        return "%" + et_search.getText().toString() + "%";
    }
}

