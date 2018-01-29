package ua.artjoker.mvvmsamples.ui.fragments.bases

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import ru.terrakok.cicerone.Router
import ua.artjoker.mvvmsamples.R
import ua.artjoker.mvvmsamples.ui.bindViewModel
import ua.artjoker.mvvmsamples.viewmodels.bases.FragmentViewModel
import javax.inject.Inject

sealed class BaseFragment<VDB : ViewDataBinding> : Fragment() {

    @get:LayoutRes
    abstract val layoutId: Int
    abstract val viewModel: Any

    protected lateinit var binding: VDB

    open val title = 0

    protected open val invalidateOnResume get() = false

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()

        if (invalidateOnResume) {
            binding.invalidateAll()
        }

        binding.executePendingBindings()
        (title.takeIf { it != 0 } ?: R.string.app_name).let(activity!!::setTitle)
    }

    final override fun onCreateView(inflater: LayoutInflater,
                                    container: ViewGroup?,
                                    savedInstanceState: Bundle?): View =
            DataBindingUtil.inflate<VDB>(inflater, layoutId, container, false)
                    .also { binding = it }
                    .bindViewModel(viewModel)

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.executePendingBindings()
        onViewBound(binding, savedInstanceState)
    }

    override fun onDestroyView() {
        binding.unbind()
        onViewUnbound()
        super.onDestroyView()
    }

    protected open fun onViewBound(binding: VDB, savedInstanceState: Bundle?) {}

    protected open fun onViewUnbound() {}

    open fun onBackPressed() = false

    abstract class SimpleFragment<VDB : ViewDataBinding> : BaseFragment<VDB>() {

        final override val viewModel get() = this

        @Inject
        lateinit var router: Router
    }

    abstract class ViewModelFragment<VM : FragmentViewModel, VDB : ViewDataBinding>()
        : BaseFragment<VDB>() {

        @Inject
        final override lateinit var viewModel: VM

        final override fun onBackPressed() = viewModel.onBackPressed()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            viewModel.onFragmentCreated()
        }

        override fun onResume() {
            super.onResume()
            viewModel.onResume()
        }
    }
}
