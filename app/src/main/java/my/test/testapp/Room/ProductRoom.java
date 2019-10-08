package my.test.testapp.Room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ProductRoom {

    @PrimaryKey
    public int id;

    public String name;
    public String description;
    public float price;
    public boolean isBought;
}
