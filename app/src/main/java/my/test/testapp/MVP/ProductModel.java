package my.test.testapp.MVP;

import java.util.List;

import my.test.testapp.Room.ProductDao;
import my.test.testapp.Room.ProductRoom;

public class ProductModel {

    private ProductDao productDao;

    public ProductModel(ProductDao _productDao){
        productDao = _productDao;
    }

    public void insertProduct(ProductRoom product){
        productDao.insertProduct(product);
    }

    public void updateProduct(ProductRoom product){
        productDao.updateProduct(product);
    }

    public void deleteProduct(ProductRoom product){
        productDao.deleteProduct(product);
    }

    public List<ProductRoom> getAll(){
        return productDao.getAll();
    }

    public int getMaxID() {
        ProductRoom lastProduct = productDao.getMaxId();
        if (lastProduct == null){
            return 1;
        }else{
            return productDao.getMaxId().id + 1;
        }
    }

    public ProductRoom getProductById(int productID) {
        return productDao.getProductById(productID);
    }
}
