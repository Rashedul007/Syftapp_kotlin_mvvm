package syft.com.syftapp_kotlin_mvvm.network

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codingwithmitch.mviexample.util.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import syft.com.syftapp_kotlin_mvvm.models.GitResult
import syft.com.syftapp_kotlin_mvvm.utils.Constants
import syft.com.syftapp_kotlin_mvvm.utils.GenericApiResponse
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainApiClient  @Inject constructor(val mainApi: MainApi) {


   fun getApiResultFromServer(query :String , pageNumber:Int)
            : LiveData<GenericApiResponse<GitResult>> {

        var apiResult =   mainApi.getAllRepo(query  , pageNumber, "stars" , "desc" )

       return apiResult

    }





}