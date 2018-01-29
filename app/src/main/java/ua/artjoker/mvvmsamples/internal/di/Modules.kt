package ua.artjoker.mvvmsamples.internal.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ua.artjoker.mvvmsamples.AppFacade
import ua.artjoker.mvvmsamples.Application
import ua.artjoker.mvvmsamples.ui.activities.MainActivity
import ua.artjoker.mvvmsamples.ui.fragments.AuthFragment
import ua.artjoker.mvvmsamples.ui.fragments.InitialFragment
import ua.artjoker.mvvmsamples.ui.fragments.ProfilesFragment
import javax.inject.Singleton

@Module
abstract class ImplementationsModule {

    @Binds
    abstract fun bindAppContext(application: Application): Context

    @Binds
    abstract fun bindAppFacade(application: Application): AppFacade
}

@Module
abstract class AppModule {

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity


    @ContributesAndroidInjector
    abstract fun authFragment(): AuthFragment

    @ContributesAndroidInjector
    abstract fun initialFragment(): InitialFragment

    @ContributesAndroidInjector
    abstract fun profilesFragment(): ProfilesFragment
}

@Module
class RouterModule {

    private val cicerone by lazy { Cicerone.create() }

    @Provides
    @Singleton
    fun provideRouter(): Router = cicerone.router

    @Provides
    @Singleton
    fun provideNavigatorHolder(): NavigatorHolder = cicerone.navigatorHolder
}
