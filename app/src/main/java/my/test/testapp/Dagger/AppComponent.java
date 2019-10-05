package my.test.testapp.Dagger;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import my.test.testapp.Room.AppDataBase;
import my.test.testapp.Room.ProductDao;

@Singleton
@Component(modules = {AppModule.class, RoomModule.class})
public interface AppComponent {
    Application getApplication();
    AppDataBase getDataBase();
    ProductDao getProductDao();
}
