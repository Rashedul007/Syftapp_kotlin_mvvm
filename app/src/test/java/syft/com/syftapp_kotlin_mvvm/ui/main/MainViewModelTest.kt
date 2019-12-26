package syft.com.syftapp_kotlin_mvvm.ui.main


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.VerificationModeFactory.times
import syft.com.syftapp_kotlin_mvvm.models.GitResult
import syft.com.syftapp_kotlin_mvvm.models.SearchQuery
import syft.com.syftapp_kotlin_mvvm.network.MainApiClient
import syft.com.syftapp_kotlin_mvvm.repository.MainRepository
import syft.com.syftapp_kotlin_mvvm.util.InstantExecutorExtension
import syft.com.syftapp_kotlin_mvvm.utils.ApiSuccessResponse
import syft.com.syftapp_kotlin_mvvm.utils.GenericApiResponse
import syft.com.syftapp_kotlin_mvvm.utils.LiveDataTestUtil
import syft.com.syftapp_kotlin_mvvm.utils.TestUtil
import java.util.*

@ExtendWith(InstantExecutorExtension::class)
class MainViewModelTest {

    lateinit var  mainViewModel: MainViewModel

    @Mock
     lateinit var  mainRepository: MainRepository



    @BeforeEach
    fun init()
    {
        MockitoAnnotations.initMocks(this)
        mainViewModel = MainViewModel(mainRepository)
    }


//check if - searchQuery is empty then the LiveData apiData doenst return anything
    @Test
    @Throws(Exception::class)
    fun observeEmptyResponseWhenQueryNotSet() {
        //arrange
           var liveDataTestUtil :LiveDataTestUtil<GenericApiResponse<GitResult>> = LiveDataTestUtil()

        //act
            var genericEmptyResponse  = liveDataTestUtil.getValue(mainViewModel.apiData)

        //assert
            assertNull(genericEmptyResponse)

    } //ok


//check if - we get success result from repository then LiveData liveResult can be observed
    @Test
    @Throws(Exception::class)
    fun observeResponseWhenQuerySet() {
        //arrange
            var genericResponse = ApiSuccessResponse(TestUtil.obj_gitresult)
            var liveDataTestUtil :LiveDataTestUtil<GenericApiResponse<GitResult>> = LiveDataTestUtil()

        //act
             mainViewModel.liveResult.value = genericResponse

             var responseFromObserver  = liveDataTestUtil.getValue(mainViewModel.liveResult)

        //assert
            assertEquals(genericResponse, responseFromObserver)

    } //ok




//check if - searchQuery is set then Repository.fetchApiresultFromClient is called
    @Test
    @Throws(Exception::class)
    fun setSearchQuery_assert_callsRepositoryMethod() {

        //arrange
            var obj_query_search01: SearchQuery = TestUtil.obj_query_search1
            mainViewModel.searchQuery.value = obj_query_search01

        //act
            var liveDataTestUtil :LiveDataTestUtil<GenericApiResponse<GitResult>> = LiveDataTestUtil()
            liveDataTestUtil.getValue(mainViewModel.apiData)

        //assert
            verify(mainRepository).fetchApiresultFromClient( obj_query_search01.filter_search,obj_query_search01.filter_topics,obj_query_search01.filter_language,obj_query_search01.page_number)

    } //ok



//check if - apiData is not set then repository isnt called
    @Test
    @Throws(Exception::class)
    fun doesntCallRepositoryWithOutApiDataSet() {

        //arrange

        //act
        var liveDataTestUtil :LiveDataTestUtil<GenericApiResponse<GitResult>> = LiveDataTestUtil()
        liveDataTestUtil.getValue(mainViewModel.apiData)

        //assert
        verify(mainRepository, never()).fetchApiresultFromClient( ArgumentMatchers.anyString(),ArgumentMatchers.anyString(),ArgumentMatchers.anyString(),ArgumentMatchers.anyInt())

    } //ok


}