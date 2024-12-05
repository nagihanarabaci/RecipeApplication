package com.example.recipeapplication.common

import android.os.Bundle
import androidx.fragment.app.Fragment

object FragmentArgumentHelper {
    fun <T:Fragment> createFragmentWithArgument(fragmentClass: Class<T>,key: String, value: Int) : T {
        val fragment = fragmentClass.getDeclaredConstructor().newInstance()
        val args = Bundle()
        args.putInt(key, value)
        fragment.arguments = args
        return fragment
    }
}