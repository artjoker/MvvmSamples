package ua.artjoker.mvvmsamples.ui

import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.databinding.InverseBindingAdapter
import android.databinding.InverseBindingListener
import android.graphics.drawable.Drawable
import android.support.design.widget.TextInputLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.jetbrains.anko.inputMethodManager
import ua.artjoker.mvvmsamples.R

@BindingConversion
fun booleanToVisibility(visible: Boolean) = View.VISIBLE.takeIf { visible } ?: View.GONE

@InverseBindingAdapter(attribute = "refreshing", event = "refreshingAttrChanged")
fun SwipeRefreshLayout.getRefreshing() = isRefreshing

@BindingAdapter("refreshingAttrChanged")
fun SwipeRefreshLayout.setRefreshingAttrChanged(attrChanged: InverseBindingListener?) {
    setOnRefreshListener { attrChanged?.onChange() }
}

@BindingAdapter("refreshing")
fun SwipeRefreshLayout.doSetRefreshing(refreshing: Boolean) {
    isRefreshing = refreshing
}

@BindingAdapter("hintId", "optional")
fun TextInputLayout.setHint(hintId: Int, optional: Boolean) {
    hint =
            when (hintId) {
                0 -> null
                else -> resources.getString(hintId) +
                        " " +
                        ("*".takeIf { !optional } ?: resources.getString(R.string.hint_optional))
            }
}

@BindingAdapter("error")
fun TextInputLayout.setError(errorId: Int) {
    error = if (errorId == 0) {
        null
    } else {
        resources.getString(errorId)
    }
}


@BindingAdapter("text", "textRes")
fun TextView.setText(text: String?, textRes: Int) {
    if (textRes == 0) {
        setText(text)
    } else {
        setText(textRes)
    }
}

@BindingAdapter("inputType", "passwordToggleEnabled")
fun TextInputLayout.setInputType(inputType: Int, passwordToggleEnabled: Boolean) {
    editText!!.inputType = inputType
    isPasswordVisibilityToggleEnabled = passwordToggleEnabled
}

@BindingAdapter(
        value = ["url", "placeholder"],
        requireAll = false)
fun ImageView.loadImage(url: Any?, placeholder: Drawable) {
    Glide.with(context)
            .load(url)
            .apply(RequestOptions().placeholder(placeholder))
            .into(this)
}

@BindingAdapter(
        value = ["focused", "focusedAttrChanged"],
        requireAll = false)
fun View.setFocusedAlias(
        focused: Boolean,
        attrChange: InverseBindingListener?) {

    if (focused) {
        requestFocus()
    }

    if (attrChange != null) {
        context.inputMethodManager.showSoftInput(this, 0)
        setOnFocusChangeListener { _, _ -> attrChange.onChange() }
    } else {
        onFocusChangeListener = null
    }
}

@InverseBindingAdapter(attribute = "focused", event = "focusedAttrChanged")
fun View.isFocusedAlias() = isFocused

