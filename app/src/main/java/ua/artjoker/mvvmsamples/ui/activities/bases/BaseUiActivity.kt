package ua.artjoker.mvvmsamples.ui.activities.bases

import android.os.Bundle
import android.support.annotation.LayoutRes
import ua.artjoker.mvvmsamples.ui.activities.bases.BaseActivity

abstract class BaseUiActivity : BaseActivity() {

    @get:LayoutRes
    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
    }
}
