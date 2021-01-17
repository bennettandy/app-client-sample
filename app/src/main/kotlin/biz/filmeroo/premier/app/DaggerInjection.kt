package biz.filmeroo.premier.app

import android.content.Context
import android.content.res.Resources
import biz.filmeroo.premier.R
import biz.filmeroo.premier.api.FilmService
import biz.filmeroo.premier.detail.DetailActivity
import biz.filmeroo.premier.main.MainActivity
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    Binder::class])
interface AppComponent : AndroidInjector<PremierApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<PremierApp>()
}

@Module
internal class AppModule {

    @Provides
    fun provideAppContext(application: PremierApp) = application.applicationContext

    @Provides
    fun provideResource(context: Context) = context.resources

    @Provides
    @Singleton
    fun provideFilmService(client: OkHttpClient): FilmService {
        return Retrofit.Builder()
                .baseUrl(FilmService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(FilmService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(resources: Resources): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(createAuthInterceptor(resources))
                .build()
    }

    private fun createAuthInterceptor(resources: Resources): Interceptor {
        return Interceptor { chain ->
            val updatedUrl = chain.request().url().newBuilder()
                    .addQueryParameter(FilmService.API_KEY_PARAM, resources.getString(R.string.api_key))
                    .build()
            chain.proceed(chain.request().newBuilder()
                    .url(updatedUrl)
                    .build())
        }
    }

    @Provides
    @Singleton
    fun providePicasso():Picasso = Picasso.get()
}

@Module
internal abstract class Binder {

    @ContributesAndroidInjector
    internal abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun detailActivity(): DetailActivity
}