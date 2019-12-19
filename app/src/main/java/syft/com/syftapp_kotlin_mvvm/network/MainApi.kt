package syft.com.syftapp_kotlin_mvvm.network

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import syft.com.syftapp_kotlin_mvvm.models.GitResult
import syft.com.syftapp_kotlin_mvvm.models.ItemList


interface MainApi {

    @GET("repositories")
    fun getAllRepo(@Query("q") q: String?,
                   @Query("page") page: String?
    ) : Observable<GitResult>



}