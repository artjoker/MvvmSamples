package ua.artjoker.mvvmsamples

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import org.jetbrains.anko.toast
import ua.artjoker.mvvmsamples.internal.di.DaggerAppComponent


class Application : DaggerApplication(), AppFacade {

    override fun applicationInjector(): AndroidInjector<Application> =
            DaggerAppComponent.builder().create(this)

    override fun showToast(message: Int) {
        toast(message)
    }

    override fun showToast(message: String) {
        toast(message)
    }
}
