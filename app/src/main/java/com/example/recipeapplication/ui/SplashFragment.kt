package com.example.recipeapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.recipeapplication.common.navigateWithDelay
import com.example.recipeapplication.databinding.FragmentSplashBinding


class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val viewBinding = FragmentSplashBinding.inflate(inflater, container, false)
        setBinding(viewBinding)
        navigateWithDelay()
        return binding.root
    }

}