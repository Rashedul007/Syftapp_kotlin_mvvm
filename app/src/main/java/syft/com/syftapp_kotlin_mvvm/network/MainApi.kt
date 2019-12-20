package syft.com.syftapp_kotlin_mvvm.network

import androidx.lifecycle.LiveData
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import syft.com.syftapp_kotlin_mvvm.models.GitResult
import syft.com.syftapp_kotlin_mvvm.utils.GenericApiResponse


interface MainApi {

    @GET("repositories")
    fun getAllRepo(@Query("q") q: String?,
                   @Query("page") page: Int=1,
                   @Query("sort") sort: String?,
                    @Query("order") order: String?
        ):LiveData<GenericApiResponse<GitResult>>



}