package syft.com.syftapp_kotlin_mvvm.di

import syft.com.syftapp_kotlin_mvvm.di.main.MainViewModelModule
import syft.com.syftapp_kotlin_mvvm.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {


    @ContributesAndroidInjector(modules = [ MainViewModelModule::class] )
        abstract fun contributeMainActivity(): MainActivity



   /* @ContributesAndroidInjector()
            abstract fun contributeDetailActivity(): DetailActivity*/


}