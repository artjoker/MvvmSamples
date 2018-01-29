package ua.artjoker.mvvmsamples.viewmodels.bases

import android.databinding.ObservableBoolean
import android.support.annotation.CallSuper
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.observers.DisposableSingleObserver
import ua.artjoker.mvvmsamples.ui.clear


abstract class FormViewModel<T> : FragmentViewModel() {

    val loading = ObservableBoolean()

    protected abstract val fields: List<FieldViewModel>

    private var sending: Disposable = Disposables.disposed()

    protected abstract fun onSend(): Single<T>
    protected abstract fun onFormSent()
    protected abstract fun onSendingFailed(e: Throwable)

    fun attemptToSend() {
        if (validate()) {
            sending = FormSendingObserver().apply(onSend()::subscribe)
        }
    }

    @CallSuper
    open fun clear() {
        fields.clear()
    }

    override fun onBackPressed(): Boolean =
            when {

                sending.isDisposed -> {
                    clear()
                    false
                }

                else -> {
                    sending.dispose()
                    loading.set(false)
                    true
                }
            }

    private fun validate() =
            fields.reversed()
                    .asSequence()
                    .lastOrNull { !it.validate() }
                    ?.also { it.focused.set(true) } == null

    private inner class FormSendingObserver : DisposableSingleObserver<T>() {


        override fun onStart() {
            loading.set(true)
        }

        override fun onSuccess(t: T) {
            loading.set(false)
            clear()
            onFormSent()
        }

        override fun onError(e: Throwable) {
            loading.set(false)
            onSendingFailed(e)
        }
    }
}
