package my.test.testapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import my.test.testapp.Room.ProductDao;
import my.test.testapp.Room.ProductRoom;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder>{

    private List<ProductRoom> products = new ArrayList<>();
    OnProductClickListener onProductClickListener;

    public ProductAdapter(OnProductClickListener _onProductClickListener){
        onProductClickListener = _onProductClickListener;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_item, viewGroup, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder productHolder, int i) {
        productHolder.bind(products.get(i));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void refreshRecyclerView(List<ProductRoom> productList) {
        products = productList;
        notifyDataSetChanged();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_description)
        TextView tv_description;

        @BindView(R.id.tv_price)
        TextView tv_price;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ProductRoom product){
            tv_name.setText(product.name);
            tv_description.setText(product.description);
            tv_price.setText(Long.toString(product.price));
        }

        @OnClick
        void onClick(){
            onProductClickListener.onProductClick(products.get(getLayoutPosition()));
        }

    }

    public interface OnProductClickListener{
        void onProductClick(ProductRoom product);
    }
}
