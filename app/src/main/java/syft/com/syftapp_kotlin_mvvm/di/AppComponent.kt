package syft.com.syftapp_kotlin_mvvm.di

import android.app.Application
import syft.com.syftapp_kotlin_mvvm.BaseApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
            AndroidSupportInjectionModule::class,
            AppModule::class,
            ActivityBuildersModule::class,
            ViewModelFactoryModule::class
            ])
interface AppComponent : AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}