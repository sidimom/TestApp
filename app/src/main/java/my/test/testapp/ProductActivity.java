package my.test.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductActivity extends AppCompatActivity {

    @BindView(R.id.et_name_product)
    EditText et_name_product;

    @BindView(R.id.et_description_product)
    EditText et_description_product;

    @BindView(R.id.et_price_product)
    EditText et_price_product;

    int productID;
    final int RESULT_OK = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        productID = intent.getIntExtra("ProductID", 0);
        if (productID != 0){
            et_name_product.setText(intent.getStringExtra("ProductName"));
            et_description_product.setText(intent.getStringExtra("ProductDescription"));
            et_price_product.setText(Long.toString(intent.getLongExtra("ProductPrice", 0)));
        }
    }

    @OnClick(R.id.fab_apply)
    void apply(){
        if (!fieldsAreEmpty()) {
            setIntentResult();
            finish();
        }
    }

    private void setIntentResult() {

        Intent intent = new Intent();
        intent.putExtra("ProductID", productID);
        intent.putExtra("NameProduct", et_name_product.getText().toString());
        intent.putExtra("DescriptionProduct", et_description_product.getText().toString());
        intent.putExtra("PriceProduct", Integer.parseInt(et_price_product.getText().toString()));
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
}
