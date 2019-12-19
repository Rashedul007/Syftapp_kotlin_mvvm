package syft.com.syftapp_kotlin_mvvm.ui.main

import android.util.Log
import androidx.lifecycle.*
import io.reactivex.Flowable
import syft.com.syftapp_kotlin_mvvm.models.ItemList
import syft.com.syftapp_kotlin_mvvm.repository.MainRepository
import syft.com.syftapp_kotlin_mvvm.models.GitResult
import javax.inject.Inject

class MainViewModel @Inject constructor() : ViewModel() {

    var liveGitResult = MediatorLiveData<GitResult>()
    val liveItemList =  MediatorLiveData<MutableList<ItemList>>()



    @set:Inject
    lateinit var mainRepository: MainRepository

    var lst = mutableListOf<ItemList>()

    fun getReposFromServer(filter_search: String, filter_topics: String?, filter_language: String?, page_number:Int =0)
    {
        // val source: LiveData<GitResult> = LiveDataReactiveStreams.fromPublisher(  mainRepository.fetchToDosFromServer(filterDate, filterStatus, filterName)       )

          val resultFromApiCall_flowable : Flowable<GitResult> =  mainRepository.fetchToDosFromServer(filter_search , filter_topics, filter_language , page_number)


        lateinit var source: LiveData<GitResult>




        resultFromApiCall_flowable.let { source = LiveDataReactiveStreams.fromPublisher(it)

              liveGitResult.addSource(source) { todos ->
                   liveGitResult.setValue(todos)
                   liveGitResult.removeSource(source)
            }

        }



        var itemList_observable = resultFromApiCall_flowable.map {//it = gitResult
                gitResult ->
//            Log.d("searchNextPage" , "inside vieewModel for listITem: " + gitResult.items[0].name)



                    gitResult.items.forEach {
                                                lst.add(it)   }

             lst
        }



        itemList_observable?.let{
            var liveItemList  = LiveDataReactiveStreams.fromPublisher(itemList_observable)

            this.liveItemList.addSource(liveItemList){ itemList ->
                this.liveItemList.setValue(itemList)
                this.liveItemList.removeSource(liveItemList)

            }
        }



    }



    fun observeReposFromServer(): LiveData<GitResult> {
        return liveGitResult
    }

    fun observeItemList(): LiveData<MutableList<ItemList>> {

        return liveItemList
    }

    fun clearRetrofitCall()
    {
        liveGitResult.value = null
        liveItemList.value = null
        mainRepository.clearDisposables()
    }


    fun searchNextPage() {
        val resultFromApiCall_flowable : Flowable<GitResult> =  mainRepository.searchNextPage()

        var itemList_observable = resultFromApiCall_flowable.map {//it = gitResult
                gitResult ->
            Log.d("searchNextPage" , "2. inside vieewModel for listITem: " + gitResult.items[0].name)


            gitResult.items.forEach {
                lst.add(it)   }

            lst
        }



        itemList_observable?.let{
            var liveItemList  = LiveDataReactiveStreams.fromPublisher(itemList_observable)

            this.liveItemList.addSource(liveItemList){ itemList ->
                this.liveItemList.setValue(itemList)
                this.liveItemList.removeSource(liveItemList)

            }
        }
    }



}