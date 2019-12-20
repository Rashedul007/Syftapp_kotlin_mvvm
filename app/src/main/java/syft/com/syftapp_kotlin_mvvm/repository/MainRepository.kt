package syft.com.syftapp_kotlin_mvvm.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import syft.com.syftapp_kotlin_mvvm.models.GitResult
import syft.com.syftapp_kotlin_mvvm.network.MainApiClient
import syft.com.syftapp_kotlin_mvvm.utils.GenericApiResponse
import javax.inject.Inject

class MainRepository @Inject constructor(val mainApiClient: MainApiClient) {

    private var mfilter_search: String?=null
    private var mfilter_topics: String?=null
    private var mfilter_language: String?=null
    private var mPageNumber = 0


     fun fetchApiresultFromClient(filter_search: String, filter_topics: String?, filter_language: String?, page_number:Int)
             : LiveData<GenericApiResponse<GitResult>> {

         setPreviousApiCallData(filter_search, filter_topics, filter_language ,  page_number)

//region ...filterSearch.=================================

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


 //endregion....=================================


        var returnedData =    mainApiClient.getApiResultFromServer(fullQueryString.toString(), page_number  )


            return returnedData
         }





//region ... for nextPage search

     fun setPreviousApiCallData(filter_search: String, filter_topics: String?, filter_language: String? , page_number:Int) {

         mfilter_search = filter_search
         mfilter_topics = filter_topics
         mfilter_language = filter_language
          mPageNumber = page_number

     }

     fun searchNextPage(): LiveData<GenericApiResponse<GitResult>> {
         var  returnedNextData : LiveData<GenericApiResponse<GitResult>> =  fetchApiresultFromClient(mfilter_search!!, mfilter_topics, mfilter_language , mPageNumber +1)

         return returnedNextData

     }

//endregion



}