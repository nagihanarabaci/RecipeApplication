package com.example.recipeapplication.ui.removerecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.recipeapplication.common.navigateWithDelay
import com.example.recipeapplication.common.showMotionToast
import com.example.recipeapplication.databinding.FragmentRemoveRecipeBinding
import com.example.recipeapplication.ui.BaseFragment
import www.sanju.motiontoast.MotionToastStyle


class RemoveRecipeFragment : BaseFragment<FragmentRemoveRecipeBinding> () {

    private var recipeId :Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewBinding = FragmentRemoveRecipeBinding.inflate(inflater,container,false)
        setBinding(viewBinding)
        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            recipeId = it.getInt("recipe_id",0)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            gifRemoveRecipe.setOnClickListener {
                recipeId?.let { removeRecipe(it) }
            }
        }
    }


    private fun removeRecipe(recipeId:Int) {
        recipeViewModel.deleteById(recipeId)
        recipeViewModel.recipeStatus.observe(viewLifecycleOwner) { success ->
            if (success) {
                requireContext().showMotionToast("Ürün Silindi", "Ürün Silme İşleminiz başarılı", style = MotionToastStyle.DELETE)
                navigateWithDelay()
            } else {
                requireContext().showMotionToast("Hata", "Silme işlemi sırasında bir hata oluştu", style = MotionToastStyle.ERROR)
            }
        }
    }

}