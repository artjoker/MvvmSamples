package ua.artjoker.mvvmsamples.viewmodels.bases

import android.databinding.BaseObservable
import ru.terrakok.cicerone.Router
import ua.artjoker.mvvmsamples.AppFacade
import javax.inject.Inject

abstract class FragmentViewModel : BaseObservable() {

    @Inject lateinit var router: Router
    @Inject lateinit var appFacade: AppFacade

    @Inject
    fun init() {
        onInit()
    }

    open fun onBackPressed() = false

    open fun onFragmentCreated() {}

    open fun onResume() {}

    protected open fun onInit() {}
}
