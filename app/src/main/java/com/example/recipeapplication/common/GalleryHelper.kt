package com.example.recipeapplication.common

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

object GalleryHelper {
    // İzin kontrolü için kullanılan fonksiyon.
    fun checkPermissionAndLaunch(
        context: Fragment,
        view: View,
        permissionLauncher: ActivityResultLauncher<String>,
        activityResultLauncher: ActivityResultLauncher<Intent>
    ) {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(
                context.requireContext(),
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context.requireActivity(),
                    permission
                )
            ) {
                Snackbar.make(
                    view,
                    "Galeriye erişim için izin veriniz.",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("İzin ver") { permissionLauncher.launch(permission) }.show()
            } else {
                permissionLauncher.launch(permission)
            }
        } else {
            val intentToGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }

    }

    // Seçtiğimiz görselleri veritabanına kaydederken küçültmek için kullanılan fonksiyon.
    fun createSmallBitmap(userChooseBitmap: Bitmap, maxSize: Int): Bitmap {
        var width = userChooseBitmap.width
        var height = userChooseBitmap.height
        val bitmapScale: Double = width.toDouble() / height.toDouble()
        if (bitmapScale > 17) {
            // görsel yatay
            width = maxSize
            val smallHeight = width / bitmapScale
            height = smallHeight.toInt()
        } else {
            // görsel dikey
            height = maxSize
            val smallWidth = height * bitmapScale
            width = smallWidth.toInt()
        }
        return Bitmap.createScaledBitmap(userChooseBitmap, width, height, true)
    }
}