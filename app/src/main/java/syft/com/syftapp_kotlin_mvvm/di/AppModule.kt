package syft.com.syftapp_kotlin_mvvm.di



import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import syft.com.syftapp_kotlin_mvvm.network.MainApi
import syft.com.syftapp_kotlin_mvvm.repository.MainRepository
import syft.com.syftapp_kotlin_mvvm.utils.Constants
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class AppModule {

    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)
            .addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    var request: Request = chain.request()
                    val url: HttpUrl = request.url.newBuilder()
                        .addQueryParameter("sort", "stars")
                        .addQueryParameter("order", "desc")
                        .build()
                    request = request.newBuilder().url(url).build()
                    return chain.proceed(request)
                }
            })
            .build()



        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }


    @Singleton
    @Provides
     fun provideMainApi(retrofit: Retrofit): MainApi {
        return retrofit.create<MainApi>(MainApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMainRepository(mainApi: MainApi): MainRepository {
        return MainRepository(mainApi)
    }



//-------------- for Details MVP
/*
    @Provides
    fun provideDetailPresenter(mainModel: ToDoMvp.Model): DetailMvp.Presenter {
        return DetailPresenter(mainModel)
    }
*/



}