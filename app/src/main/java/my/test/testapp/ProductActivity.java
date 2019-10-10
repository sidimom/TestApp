package my.test.testapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import my.test.testapp.AdditionalClasses.DecimalDigitsInputFilter;

public class ProductActivity extends AppCompatActivity {

    @BindView(R.id.et_name_product)
    EditText et_name_product;

    @BindView(R.id.et_description_product)
    EditText et_description_product;

    @BindView(R.id.et_price_product)
    EditText et_price_product;

    @BindView(R.id.iv_image_product)
    ImageView iv_image_product;

    int productID;
    String urlImage;
    final int RESULT_OK = 1;
    final int ID_ACTIVITY_CHOOSE_IMAGE = 1;
    public static final String EXTRA_PRODUCT_NAME = "ProductName";
    public static final String EXTRA_PRODUCT_DESCRIPTION = "ProductDescription";
    public static final String EXTRA_PRODUCT_PRICE = "ProductPrice";
    public static final String EXTRA_PRODUCT_ID = "ProductID";
    public static final String EXTRA_PRODUCT_URL_IMAGE = "ProductUrlImage";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);

        ButterKnife.bind(this);
        et_price_product.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(12,2)});

        Intent intent = getIntent();
        urlImage = intent.getStringExtra(EXTRA_PRODUCT_URL_IMAGE);
        refreshImage();

        productID = intent.getIntExtra(EXTRA_PRODUCT_ID, 0);
        if (productID != 0){
            et_name_product.setText(intent.getStringExtra(EXTRA_PRODUCT_NAME));
            et_description_product.setText(intent.getStringExtra(EXTRA_PRODUCT_DESCRIPTION));
            et_price_product.setText("" + intent.getFloatExtra(EXTRA_PRODUCT_PRICE, (float) 0));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ID_ACTIVITY_CHOOSE_IMAGE){
            if (data != null){
                urlImage = data.getData().toString();
                refreshImage();
            }
        }
    }

    @OnClick(R.id.fab_apply)
    void apply(){
        if (!fieldsAreEmpty()) {
            setIntentResult();
            finish();
        }
    }

    @OnClick(R.id.iv_image_product)
    void onClickImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose image"), ID_ACTIVITY_CHOOSE_IMAGE);
    }

    private void setIntentResult() {

        Intent intent = new Intent();
        intent.putExtra(EXTRA_PRODUCT_ID, productID);
        intent.putExtra(EXTRA_PRODUCT_NAME, et_name_product.getText().toString());
        intent.putExtra(EXTRA_PRODUCT_DESCRIPTION, et_description_product.getText().toString());
        intent.putExtra(EXTRA_PRODUCT_PRICE, Float.valueOf((et_price_product.getText().toString())));
        intent.putExtra(EXTRA_PRODUCT_URL_IMAGE, urlImage);
        setResult(RESULT_OK, intent);
    }

    private boolean fieldsAreEmpty() {
        if (TextUtils.isEmpty(et_name_product.getText())
            || TextUtils.isEmpty(et_description_product.getText())
            || TextUtils.isEmpty(et_price_product.getText())){

            Toast.makeText(this,"Values are empty!",Toast.LENGTH_SHORT).show();
            return true;
        }else{
            return false;
        }
    }

    private void refreshImage() {
        Glide.with(this)
                .load(urlImage)
                .asBitmap()
                .error(R.drawable.ic_cloud_off_red)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(iv_image_product);
    }
}
