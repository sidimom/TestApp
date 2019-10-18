package my.test.testapp.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ProductDao {

    @Query("select * from productRoom order by id")
    Flowable<List<ProductRoom>> getAllRX();

    @Query("select * from productRoom order by id")
    List<ProductRoom> getAll();

    @Query("select * from productRoom where id = :id")
    ProductRoom getProductById(int id);

    @Query("SELECT * FROM productroom WHERE id = (SELECT MAX(id) from productroom)")
    ProductRoom getMaxId();

    @Query("SELECT * FROM productroom WHERE name LIKE :searchString order by id")
    List<ProductRoom> getAllWithSearch(String searchString);

    @Insert
    void insertProduct(ProductRoom productRoom);

    @Delete
    void deleteProduct(ProductRoom productRoom);

    @Update
    void updateProduct(ProductRoom productRoom);

    @Update
    void updateProducts(List<ProductRoom> products);
}
