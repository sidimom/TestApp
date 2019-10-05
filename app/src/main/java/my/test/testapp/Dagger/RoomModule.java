package my.test.testapp.Dagger;

import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import my.test.testapp.Room.AppDataBase;
import my.test.testapp.Room.ProductDao;

@Module
public class RoomModule {

    private AppDataBase dataBase;

    public RoomModule(Application mApplication){
        dataBase = Room.databaseBuilder(mApplication, AppDataBase.class, "database")
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    AppDataBase providesRoomDataBased(){
        return dataBase;
    }

    @Provides
    @Singleton
    ProductDao providesProductDao(){
        return dataBase.productDao();
    }
}
