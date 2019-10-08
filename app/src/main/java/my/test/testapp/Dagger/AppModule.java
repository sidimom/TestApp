package my.test.testapp.Dagger;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application application){
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication(){
        return mApplication;
    }
}
