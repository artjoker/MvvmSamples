package ua.artjoker.mvvmsamples.viewmodels.bases

import android.databinding.*
import android.support.annotation.CallSuper
import android.text.InputType
import ua.artjoker.mvvmsamples.R


open class FieldViewModel constructor(
        val hint: Int,
        val inputType: Int = InputType.TYPE_CLASS_TEXT,
        private val conditions: Set<ConditionChecker> = emptySet(),
        val maxLines: Int = 1,
        val minLines: Int = 1,
        val optional: Boolean = false,
        open val maxLength: Int = 20) : BaseObservable() {

    var input = ObservableField<String>("")
    val error = ObservableInt()

    val enabled = ObservableBoolean(true)

    val focused = ObservableBoolean()

    protected open val shortLength get() = false

    init {
        input.addOnPropertyChangedCallback(InputChangedCallback())
    }

    open fun value(): String = input.get()

    fun validate(): Boolean {

        error.set(0)

        return when {

            input.get().trim().isEmpty() ->

                when {
                    optional -> true
                    else -> {
                        error.set(R.string.error_field_required)
                        false
                    }
                }

            else -> conditions.find { !it.check(input.get()) }
                    ?.error
                    ?.also(error::set) == null
        }
    }

    @CallSuper
    open fun clear() {
        error.set(0)
        input.set("")
    }

    fun showVerificationError(errorId: Int) {
        error.set(errorId)
        focused.set(true)
    }

    class ConditionChecker(
            val error: Int,
            val check: CharSequence.() -> Boolean)

    private inner class InputChangedCallback : Observable.OnPropertyChangedCallback() {

        override fun onPropertyChanged(sender: Observable, propertyId: Int) {
            error.set(0)
        }
    }
}


class EmailViewModel(optional: Boolean = false, hint: Int = R.string.hint_email)
    : FieldViewModel(
        hint,
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
        setOf(ConditionChecker(R.string.error_wrong_email, Regex(EMAIL_PATTERN)::matches)),
        1,
        1,
        optional) {

    override val maxLength get() = 50

    companion object {
        private val EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"()," +
                ":;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$"
    }
}

class PasswordViewModel(hint: Int = R.string.hint_password) : FieldViewModel(
        hint,
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD,
        setOf(ConditionChecker(R.string.error_invalid_password) { length >= 6 }))
