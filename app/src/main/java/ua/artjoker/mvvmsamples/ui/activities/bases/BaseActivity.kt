package ua.artjoker.mvvmsamples.ui.activities.bases

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import org.jetbrains.anko.AlertBuilder
import org.jetbrains.anko.inputMethodManager
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Command
import ua.artjoker.mvvmsamples.R
import ua.artjoker.mvvmsamples.ui.fragments.bases.BaseFragment
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    protected open val navigator = BaseNavigator()

    private var alert: DialogInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onBackPressed() {
        getCurrentFragment().let {
            if (it !is BaseFragment<*> || !it.onBackPressed()) {
                navigator.applyCommand(Back())
            }
        }
    }

    fun hideKeyboard() {
        val fragmentRoot = getCurrentFragment()
                ?.view
                ?.rootView

        (fragmentRoot ?: currentFocus)
                ?.let { inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0) }
    }

    protected fun AlertBuilder<DialogInterface>.replace() {
        alert?.dismiss()
        alert = show()
    }

    private fun getCurrentFragment(): Fragment? =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

    protected open inner class BaseNavigator : SupportAppNavigator(
            this,
            supportFragmentManager,
            R.id.fragment_container) {

        override fun applyCommand(command: Command?) {
            hideKeyboard()
            super.applyCommand(command)
        }

        override fun createActivityIntent(screenKey: String, data: Any?): Intent? =
                null

        override fun createFragment(screenKey: String, data: Any?): Fragment? = null
    }
}
