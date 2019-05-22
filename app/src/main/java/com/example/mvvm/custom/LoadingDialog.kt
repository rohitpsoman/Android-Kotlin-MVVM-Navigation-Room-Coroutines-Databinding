package com.example.mvvm.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.mvvm.R
import kotlinx.android.synthetic.main.loading_dialog.*

class LoadingDialog(context: Context) : Dialog(context),
    LifecycleObserver {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.loading_dialog)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes = lp
        progressBar.indeterminateDrawable.setColorFilter(
            ContextCompat.getColor(context, R.color.black),
            android.graphics.PorterDuff.Mode.MULTIPLY
        )
    }

    fun toggle(status: Boolean = true) {
        if (status) {
            if (!isShowing) {
                show()
            }
        } else {
            cancel()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun release() {
        cancel()
    }

}
