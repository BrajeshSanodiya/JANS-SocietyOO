package com.jans.societyoo.ui.societyservice

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.jans.imageload.DefaultImageLoader
import com.jans.societyoo.R
import com.jans.societyoo.data.remote.NetworkInstance
import com.jans.societyoo.model.ApiDataFile
import com.jans.societyoo.ui.customviews.ImageWithCrossView
import com.jans.societyoo.utils.Constants
import com.jans.societyoo.utils.PrintMsg
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_provider_post.*
import kotlinx.android.synthetic.main.layout_image_container_with_add.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.collections.HashMap


class ProviderPostActivity : AppCompatActivity() {
    val GALLERY_IMAGE_REQUEST_CODE: Int = 1
    val GALLERY_LOGO_REQUEST_CODE: Int = 2
    val CROP_IMAGE_REQUEST_CODE = 1001
    val CROP_LOGO_REQUEST_CODE = 2001
    var progressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider_post)

        supportActionBar!!.setTitle("Post Service")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);

        progressBar = progress_bar

        addLogo_providerPost.setOnClickListener {
            callImagePicker(true)
        }
        addImage_ImageContainer.setOnClickListener {
            callImagePicker(false)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun progressBarVisibility(visible: Boolean) {
        if (progressBar != null) {
            if (visible) {
                progressBar!!.visibility = View.VISIBLE
                this.window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            } else {
                progressBar!!.visibility = View.GONE
                this.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
    }

    private fun callImagePicker(isLogo: Boolean) {
        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val rationale = "Please provide permission to access files"
        val options: Permissions.Options = Permissions.Options()
            .setRationaleDialogTitle("Info")
            .setSettingsDialogTitle("Warning")

        Permissions.check(this, permissions, rationale, options, object : PermissionHandler() {
            override fun onGranted() {
                // do your task.
                var galleryIntent = Intent()
                galleryIntent.type = "image/*"
                galleryIntent.action = Intent.ACTION_GET_CONTENT
                var requestCode = GALLERY_IMAGE_REQUEST_CODE
                if (isLogo)
                    requestCode = GALLERY_LOGO_REQUEST_CODE
                startActivityForResult(
                    Intent.createChooser(galleryIntent, "SELECT_IMAGE"),
                    requestCode
                )
            }

            override fun onDenied(context: Context?, deniedPermissions: ArrayList<String?>?) {
                // permission denied, block the feature.
            }
        })
    }

    var orignalSize = ""
    var croppedSize = ""
    var compressedSize = ""

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ((requestCode == GALLERY_IMAGE_REQUEST_CODE || requestCode == GALLERY_LOGO_REQUEST_CODE) && resultCode == Activity.RESULT_OK) {
            val image: Uri = data!!.data!!
            // start cropping activity for pre-acquired image saved on the device
            orignalSize = Constants.getImageSize(this, image)
            /*CropImage.activity(image)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(720, 1280)
                .start(this);  // for default startActivity CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  */

            var cropimageRequestCode = CROP_IMAGE_REQUEST_CODE
            var ratioX = 720
            var ratioY = 1280

            if (requestCode == GALLERY_LOGO_REQUEST_CODE) {
                cropimageRequestCode = CROP_LOGO_REQUEST_CODE
                ratioX = 1
                ratioY = 1
            }

            val intent = CropImage.activity(image)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(ratioX, ratioY)
                .getIntent(this);

            startActivityForResult(intent, cropimageRequestCode);
        }

        if (requestCode == CROP_IMAGE_REQUEST_CODE || requestCode == CROP_LOGO_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                croppedSize = Constants.getImageSize(this, resultUri)
                //image_providerPost.setImageURI(resultUri)

                val compressedByteArray: ByteArray =
                    Constants.getCompressedImage(this, resultUri.path!!)
                compressedSize = Constants.getImageSize(this, compressedByteArray)

                PrintMsg.toast(
                    this,
                    " Orignal Size : $orignalSize \n Cropped Size : $croppedSize \n Commpressed Size : $compressedSize"
                )
                var isLogo = false
                if (requestCode == CROP_LOGO_REQUEST_CODE)
                    isLogo = true
                uploadFile(isLogo, compressedByteArray, resultUri.path!!)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                var error = result.error
            }
        }
    }

    // Uploading Image/Video
    private fun uploadFile(isLogo: Boolean, byteArray: ByteArray, path: String) {
        if (byteArray == null || byteArray.size == 0) {
            PrintMsg.toast(this, "please select an image ")
            return
        } else {
            progressBarVisibility(true)
            val file = File(path)
            // Parsing any Media type file
            val requestBody = RequestBody.create("image/.jpg".toMediaTypeOrNull(), file)
            val body: MultipartBody.Part = createFormData("image", file.getName(), requestBody)

            // create a map of data to pass along
            val type: RequestBody =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "1")
            val name: RequestBody =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "name.png")

            val map: HashMap<String, RequestBody> = HashMap()
            map["type"] = type
            map["name"] = name

            val call: Call<ApiDataFile> = NetworkInstance.jsonServices.uploadFile(body, map)
            call.enqueue(object : Callback<ApiDataFile?> {
                override fun onResponse(
                    call: Call<ApiDataFile?>,
                    response: Response<ApiDataFile?>
                ) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            progressBarVisibility(false)
                            var apiDataFile: ApiDataFile = response.body() as ApiDataFile
                            var imageObject = apiDataFile.data_details
                            var link = imageObject.link
                            PrintMsg.toast(applicationContext, link)

                            if (isLogo) {
                                DefaultImageLoader.load(logo_providerPost, link, null)
                            } else {
                                var imageView = ImageWithCrossView(getBaseContext());
                                imageView.btnClose!!.setOnClickListener {
                                    layoutImages_ImageContainer.removeView(imageView)
                                }
                                layoutImages_ImageContainer.addView(imageView)

                                DefaultImageLoader.load(imageView.imgPhoto!!, link, null)
                            }
                        }
                    } else {
                        progressBarVisibility(false)
                        PrintMsg.toast(applicationContext, "problem uploading image")
                    }
                }

                override fun onFailure(
                    call: Call<ApiDataFile?>,
                    t: Throwable
                ) {
                    progressBarVisibility(false)
                    Log.v("Response gotten is", t.message)
                }
            })
        }
    }


}
