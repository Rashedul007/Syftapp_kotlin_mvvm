package syft.com.syftapp_kotlin_mvvm.di.main

import androidx.lifecycle.ViewModel
import syft.com.syftapp_kotlin_mvvm.di.ViewModelKey
import syft.com.syftapp_kotlin_mvvm.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}
