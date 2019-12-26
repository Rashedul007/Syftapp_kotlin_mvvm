package syft.com.syftapp_kotlin_mvvm.ui.repository

import android.provider.ContactsContract
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.function.Executable
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import syft.com.syftapp_kotlin_mvvm.network.MainApiClient
import syft.com.syftapp_kotlin_mvvm.repository.MainRepository
import syft.com.syftapp_kotlin_mvvm.repository.MainRepository.Companion.SEARCH_NULL
import syft.com.syftapp_kotlin_mvvm.util.InstantExecutorExtension

@ExtendWith(InstantExecutorExtension::class)
class MainRepositoryTest {

    lateinit var  mainRepository: MainRepository

    @Mock
    lateinit var  mainApiClient: MainApiClient

    @BeforeEach
    fun init()
    {
        MockitoAnnotations.initMocks(this)
        mainRepository = MainRepository(mainApiClient)
    }

    //check if - searchQuery is empty then exception in thrown
    @Test
    @Throws(Exception::class)
    fun searchStringNull_throwsException() {

        val exception: java.lang.Exception = Assertions.assertThrows(
            java.lang.Exception::class.java   ) {
            mainRepository.fetchApiresultFromClient("", "","",1)

        }

        assertEquals(SEARCH_NULL, exception.message)
    }




}