package ua.artjoker.mvvmsamples.ui.fragments

import ua.artjoker.mvvmsamples.R
import ua.artjoker.mvvmsamples.databinding.FragmentAuthBinding
import ua.artjoker.mvvmsamples.databinding.ListContentBinding
import ua.artjoker.mvvmsamples.ui.fragments.bases.BaseFragment
import ua.artjoker.mvvmsamples.viewmodels.AuthViewModel
import ua.artjoker.mvvmsamples.viewmodels.ProfilesListViewModel

class AuthFragment : BaseFragment.ViewModelFragment<AuthViewModel, FragmentAuthBinding>() {
    override val layoutId get() = R.layout.fragment_auth
}

class ProfilesFragment : BaseFragment.ViewModelFragment<ProfilesListViewModel, ListContentBinding>() {
    override val layoutId get() = R.layout.list_content
}
