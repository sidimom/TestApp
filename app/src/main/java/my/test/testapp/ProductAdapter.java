package my.test.testapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import my.test.testapp.Room.ProductRoom;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder>{

    private Context context;
    private List<ProductRoom> products = new ArrayList<>();
    private OnProductClickListener onProductClickListener;
    private OnSwitchClickListener onSwitchClickListener;

    public ProductAdapter(Context _context, OnProductClickListener _onProductClickListener, OnSwitchClickListener _onSwitchClickListener){
        context = _context;
        onProductClickListener = _onProductClickListener;
        onSwitchClickListener = _onSwitchClickListener;
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

        @BindView(R.id.switch_isTicked)
        Switch switch_isTicked;

        @BindView(R.id.iv_image)
        ImageView iv_image;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(ProductRoom product){
            tv_name.setText(product.getName());
            tv_description.setText(product.getDescription());
            tv_price.setText("" + product.getPrice());
            switch_isTicked.setChecked(product.isBought());
            Glide.with(context)
                    .load(product.getUrlImage())
                    .asBitmap()
                    .error(R.drawable.ic_cloud_off_red)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(iv_image);
        }

        @OnClick
        void onClick(){
            onProductClickListener.onProductClick(products.get(getLayoutPosition()));
        }

        @OnCheckedChanged(R.id.switch_isTicked)
        void onSwitch(CompoundButton button, boolean checked) {
            onSwitchClickListener.onSwitchClick(products.get(getLayoutPosition()), checked);
        }

    }

    public interface OnSwitchClickListener{
        void onSwitchClick(ProductRoom product, boolean checked);
    }

    public interface OnProductClickListener{
        void onProductClick(ProductRoom product);
    }
}
