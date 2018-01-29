package ua.artjoker.mvvmsamples.ui

import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import ua.artjoker.mvvmsamples.BR
import ua.artjoker.mvvmsamples.ui.activities.bases.BaseActivity
import ua.artjoker.mvvmsamples.viewmodels.bases.FieldViewModel


fun ViewDataBinding.bindViewModel(viewModel: Any): View =
        let {
            setVariable(BR.viewModel, viewModel)
            root
        }

val Intent?.extrasOrEmpty: Bundle
    get() =
        if (this == null || this.extras == null) {
            Bundle.EMPTY
        } else {
            extras
        }


inline fun <reified F : DialogFragment> F.show(fragmentManager: FragmentManager) =
        apply { show(fragmentManager, F::class.java.canonicalName) }


fun Collection<FieldViewModel>.clear() = forEach(FieldViewModel::clear)
