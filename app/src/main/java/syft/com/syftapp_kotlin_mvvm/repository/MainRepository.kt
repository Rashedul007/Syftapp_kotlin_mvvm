package syft.com.syftapp_kotlin_mvvm.repository

import android.util.Log
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import syft.com.syftapp_kotlin_mvvm.models.GitResult
import syft.com.syftapp_kotlin_mvvm.network.MainApi
import javax.inject.Inject

class MainRepository @Inject constructor(mainApi: MainApi) {

    private val mainApi: MainApi
    private val disposables: CompositeDisposable = CompositeDisposable()

    private var mfilter_search: String?=null
    private var mfilter_topics: String?=null
    private var mfilter_language: String?=null
    private var mPageNumber = 0


    init {
        this.mainApi = mainApi
    }


    fun fetchToDosFromServer(filter_search: String, filter_topics: String?, filter_language: String? , page_number:Int =0)
            : Flowable<GitResult> {

        Log.d("searchNextPage" , "inside fetchServer")
        var pgNumber_str = "0"

        var fullQueryString: StringBuilder  = StringBuilder()


         fullQueryString.append(filter_search.trim())


        if(!filter_topics.isNullOrEmpty() || !filter_language.isNullOrEmpty())
        {
            if(!filter_topics.isNullOrEmpty() )
            {
                fullQueryString.append(" topic:")

                fullQueryString.append(filter_topics?.trim())   }

            if(!filter_language.isNullOrEmpty() )
            {
                fullQueryString.append(" language:")

                fullQueryString.append(filter_language?.trim())   }




        }


      //  fullQueryString.append(" page:")

        if (page_number == 0)   pgNumber_str = "1"
        else    pgNumber_str = page_number.toString()


    //  fullQueryString.append(pgNumber_str.toInt())


        //val returnedData =    mainApi.getAllRepo("ruby").subscribeOn( Schedulers.io())
        lateinit var  returnedData : Observable<GitResult>

        if(fullQueryString.length > 0){
            Log.d("searchNextPage" , "calling api: " + "search " + filter_search + " topics: " + filter_topics + "lang: " + filter_language + " page: " + pgNumber_str)
            Log.d("searchNextPage" , "calling api: " + " ::fullString:: " + fullQueryString)

            setPreviousApiCallData(filter_search, filter_topics, filter_language ,  pgNumber_str.toInt())

             returnedData =    mainApi.getAllRepo(fullQueryString.toString(), pgNumber_str).subscribeOn( Schedulers.io())
                                                             .onErrorReturn(Function {throwable ->
                                                                Log.e( LOG_TAG, "Something went wrong" )
                                                                 null
                                                             })



        returnedData.subscribeOn(Schedulers.io()).observeOn( AndroidSchedulers.mainThread())
            .subscribe(object :Observer<GitResult>{
            override fun onSubscribe(d: Disposable) {
                Log.d(LOG_TAG, "onSubscribe called")
                disposables.add(d)
            }

            override fun onComplete() { }

            override fun onNext(t: GitResult) { }

            override fun onError(e: Throwable) { }

        })


        return returnedData.toFlowable(BackpressureStrategy.BUFFER);

        }


         // return null
        return Flowable.empty()
    }



    fun clearDisposables(){
        if(disposables!=null)
        {if(!disposables.isDisposed)
            disposables.clear()
            disposables.dispose()
            Log.d(LOG_TAG, "onDispose called")
        }
    }

    fun setPreviousApiCallData(filter_search: String, filter_topics: String?, filter_language: String? , page_number:Int =0) {
        Log.d("searchNextPage" , "1: inside setPreviousApiCallData" +"search " + filter_search + " topics: " + filter_topics + "lang: " + filter_language + " page: " +page_number)

        mfilter_search = filter_search
        mfilter_topics = filter_topics
        mfilter_language = filter_language
         mPageNumber = page_number

        Log.d("searchNextPage" , "2: setPreviousApiCallData" +" search " + mfilter_search + " topics: " + mfilter_topics + "lang: " + mfilter_language + " page: " +mPageNumber)
    }

    fun searchNextPage(): Flowable<GitResult> {
        var  returnedNextData : Flowable<GitResult> =  fetchToDosFromServer(mfilter_search!!, mfilter_topics, mfilter_language , mPageNumber +1)

        Log.d("searchNextPage" , "3: searchNextPage" +" search " + mfilter_search + " topics: " + mfilter_topics + "lang: " + mfilter_language + " page: " +mPageNumber)

        return returnedNextData

    }



    companion object {
        private const val LOG_TAG = "shaon_mvvm"
    }




}