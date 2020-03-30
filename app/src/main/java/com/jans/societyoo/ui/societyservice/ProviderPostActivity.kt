package com.jans.societyoo.ui.societyservice

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.jans.societyoo.R
import com.jans.societyoo.utils.Constants
import com.jans.societyoo.utils.PrintMsg
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
import kotlinx.android.synthetic.main.activity_provider_post.*

class ProviderPostActivity : AppCompatActivity() {
    var GALLERY_ID: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider_post)

        uploadBtn_providerPost.setOnClickListener {
            callImagePicker()
        }
    }

    private fun callImagePicker() {
        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val rationale = "Please provide permission to access files"
        val options: Permissions.Options = Permissions.Options()
            .setRationaleDialogTitle("Info")
            .setSettingsDialogTitle("Warning")

        Permissions.check(this , permissions, rationale, options, object : PermissionHandler() {
            override fun onGranted() {
                // do your task.
                var galleryIntent = Intent()
                galleryIntent.type = "image/*"
                galleryIntent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT_IMAGE"), GALLERY_ID)
            }
            override fun onDenied(context: Context?, deniedPermissions: ArrayList<String?>?) {
                // permission denied, block the feature.
            }
        })


    }

    var orignalSize=""
    var croppedSize=""
    var compressedSize=""
    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GALLERY_ID && resultCode == Activity.RESULT_OK) {
            var image: Uri = data!!.data!!
            // start cropping activity for pre-acquired image saved on the device
            orignalSize=Constants.getImageSize(this,image)
            CropImage.activity(image)
                .setAspectRatio(720, 1280)
                .start(this);
        }

        if (requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                var resultUri = result.uri
                croppedSize=Constants.getImageSize(this,resultUri)
                image_providerPost.setImageURI(resultUri)
                compressedSize=Constants.getImageSize(this,Constants.getCompressedImage(this,resultUri.path!!))

                PrintMsg.toast(this, " Orignal Size : $orignalSize \n Cropped Size : $croppedSize \n Commpressed Size : $compressedSize")

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                var error = result.error
            }
        }
    }


}
