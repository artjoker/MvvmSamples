package ua.artjoker.mvvmsamples.internal.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ua.artjoker.mvvmsamples.Application
import javax.inject.Singleton


@Singleton
@Component(
        modules = [
            AppModule::class,
            ImplementationsModule::class,
            RouterModule::class,
            AndroidInjectionModule::class,
            AndroidSupportInjectionModule::class
        ]
)
interface AppComponent : AndroidInjector<Application> {


    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<Application>()
}
