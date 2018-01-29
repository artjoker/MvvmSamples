package ua.artjoker.mvvmsamples.ui.fragments

import ua.artjoker.mvvmsamples.R
import ua.artjoker.mvvmsamples.Screens
import ua.artjoker.mvvmsamples.databinding.FragmentInitialBinding
import ua.artjoker.mvvmsamples.ui.fragments.bases.BaseFragment

class InitialFragment : BaseFragment.SimpleFragment<FragmentInitialBinding>() {

    override val layoutId get() = R.layout.fragment_initial

    fun goToAuth() {
        router.navigateTo(Screens.AUTH)
    }

    fun goToRefreshableList() {
        router.navigateTo(Screens.PROFILES_LIST)
    }
}
