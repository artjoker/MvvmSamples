package ua.artjoker.mvvmsamples.internal

import android.util.Log
import ua.artjoker.mvvmsamples.BuildConfig

object Logger {

    fun e(any: Any, message: String) {
        e(any, message, null)
    }

    fun e(any: Any, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) {
            var tag = makeTag(any)

            if (tag.length > 23) {
                tag = tag.substring(tag.length - 23, tag.length)
            }

            throwable?.let { Log.e(tag, message) } ?: Log.e(tag, message, throwable)
        }
    }

    private fun makeTag(any: Any) =
            any as? String ?: getNamePrototypeClass(any).simpleName

    private fun getNamePrototypeClass(any: Any) =
            (any as? Class<*> ?: any.javaClass).let(this::getDeclaredClass)

    private fun getDeclaredClass(clazz: Class<*>) =
            with(clazz) {
                takeUnless { it.isAnonymousClass } ?: superclass
            }
}
