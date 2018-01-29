package ua.artjoker.mvvmsamples.ui.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import ru.terrakok.cicerone.commands.Replace
import ua.artjoker.mvvmsamples.R
import ua.artjoker.mvvmsamples.Screens
import ua.artjoker.mvvmsamples.ui.activities.bases.BaseUiActivity
import ua.artjoker.mvvmsamples.ui.fragments.AuthFragment
import ua.artjoker.mvvmsamples.ui.fragments.InitialFragment
import ua.artjoker.mvvmsamples.ui.fragments.ProfilesFragment

class MainActivity : BaseUiActivity() {

    override val navigator: BaseNavigator = NavigatorImpl()

    override val layoutId get() = R.layout.fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            navigator.applyCommand(Replace(Screens.INITIAL, null))
        }
    }

    private inner class NavigatorImpl() : BaseNavigator() {

        override fun createFragment(screenKey: String, data: Any?): Fragment? =
                when (screenKey) {
                    Screens.INITIAL -> InitialFragment()
                    Screens.AUTH -> AuthFragment()
                    Screens.PROFILES_LIST -> ProfilesFragment()
                    else -> super.createFragment(screenKey, data)
                }
    }
}
