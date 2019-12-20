package syft.com.syftapp_kotlin_mvvm.ui.main

import android.util.Log
import androidx.arch.core.util.Function
import androidx.lifecycle.*
import syft.com.syftapp_kotlin_mvvm.repository.MainRepository
import syft.com.syftapp_kotlin_mvvm.models.GitResult
import syft.com.syftapp_kotlin_mvvm.models.SearchQuery
import syft.com.syftapp_kotlin_mvvm.utils.GenericApiResponse
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    var searchQuery:MutableLiveData<SearchQuery> = MutableLiveData()
    var liveResult:MediatorLiveData<GenericApiResponse<GitResult>> = MediatorLiveData()

    @set:Inject
    lateinit var mainRepository: MainRepository



    var apiData:LiveData<GenericApiResponse<GitResult>> = Transformations
        .switchMap(searchQuery){query ->
            query?.let {
                val source: LiveData<GenericApiResponse<GitResult>> = mainRepository.fetchApiresultFromClient(it.filter_search , it.filter_topics, it.filter_language , it.page_number)

                liveResult.addSource(source){ item->
                    liveResult.value = item
                    liveResult.removeSource(source)
                }

                source

            }
        }




    fun searchNextPage() {
        val nextSource : LiveData<GenericApiResponse<GitResult>> =  mainRepository.searchNextPage()


        liveResult.addSource(nextSource){ item->
            liveResult.value = item
            liveResult.removeSource(nextSource)
        }

    }



}