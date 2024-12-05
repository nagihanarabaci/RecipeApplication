package com.example.recipeapplication.common

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

object ToastUtils {
    fun showMotionToast(
        context: Context,
        title: String,
        message: String,
        style: MotionToastStyle,
        gravity: Int = MotionToast.GRAVITY_BOTTOM,
        duration: Long = MotionToast.LONG_DURATION,
        fontResId: Int = www.sanju.motiontoast.R.font.helvetica_regular
    ) {
        val activity = context as? Activity ?: return
        val typeFont: Typeface? = ResourcesCompat.getFont(context, fontResId)
        MotionToast.createColorToast(
            activity,
            title,
            message,
            style,
            gravity,
            duration,
            typeFont
        )
    }
}

fun Context.showMotionToast(
    title: String,
    message: String,
    style: MotionToastStyle,
    gravity: Int = MotionToast.GRAVITY_BOTTOM,
    duration: Long = MotionToast.LONG_DURATION,
    fontResId: Int = www.sanju.motiontoast.R.font.helvetica_regular
) {
    ToastUtils.showMotionToast(this, title, message, style, gravity, duration, fontResId)
}
