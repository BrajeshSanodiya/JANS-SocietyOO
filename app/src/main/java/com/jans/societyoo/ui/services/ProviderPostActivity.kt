package com.jans.societyoo.ui.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jans.imageload.DefaultImageLoader
import com.jans.societyoo.R
import com.jans.societyoo.data.local.prefs.UserPreferences
import com.jans.societyoo.data.remote.NetworkInstance
import com.jans.societyoo.model.ApiDataFile
import com.jans.societyoo.model.ApiDataObject
import com.jans.societyoo.model.ApiDataWithOutObject
import com.jans.societyoo.model.services.*
import com.jans.societyoo.ui.customviews.ImageWithCrossView
import com.jans.societyoo.utils.Constants
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.PrintMsg
import com.jans.societyoo.viewmodel.ProviderPostViewModel
import com.jans.societyoo.viewmodel.ProviderPostViewModelFactory
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
    private lateinit var postProviderViewModel: ProviderPostViewModel
    lateinit var preferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider_post)

        supportActionBar!!.setTitle("Post Service")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);

        preferences = UserPreferences(this)

        progressBar = progress_bar

        postProviderViewModel =
            ViewModelProvider(viewModelStore, ProviderPostViewModelFactory(this)).get(
                ProviderPostViewModel::class.java
            )

        addObserver()

        addLogo_providerPost.setOnClickListener {
            errorTextLogo_providerPost.visibility = View.GONE
            callImagePicker(true)
        }
        addImage_ImageContainer.setOnClickListener {
            errorTextImages_providerPost.visibility = View.GONE
            callImagePicker(false)
        }

        uploadBtn_providerPost.setOnClickListener {
            callUploadPostData()
        }
    }

    private fun addObserver() {

        postProviderViewModel.postProviderViewState.observe(this, androidx.lifecycle.Observer {
            var postProviderViewState = it
            checkValidation(postProviderViewState)
        })
        postProviderViewModel.getAllServices().observe(this, androidx.lifecycle.Observer {
            var result = it
            if (result is MyResult.Success) {
                val data: ApiDataObject<Services> = result.data
                if (data.dis_msg == 1 && !TextUtils.isEmpty(data.msg))
                    PrintMsg.toast(this, data.msg);
                if (data.success_stat == 1 && data.data_details != null && data.data_details.services != null && data.data_details.services.size > 0) {
                    val serviceList = data.data_details.services
                    val microServiceList = data.data_details.microServices
                    val spinnerServiceAdapter = ServiceDropDownAdapter(this, serviceList)
                    categorySpinner_providerPost?.adapter = spinnerServiceAdapter
                    categorySpinner_providerPost?.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val service: Service =
                                    categorySpinner_providerPost.selectedItem as Service

                                postProviderViewModel.getFilterMicroServices(
                                    service.id,
                                    microServiceList
                                ).observe(this@ProviderPostActivity, androidx.lifecycle.Observer {
                                    val filteredMicroService = it
                                    val spinnerMicroServiceAdapter = MicroServiceDropDownAdapter(
                                        this@ProviderPostActivity,
                                        filteredMicroService
                                    )
                                    subcategorySpinner_providerPost?.adapter =
                                        spinnerMicroServiceAdapter
                                })

                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                            }
                        }


                    /*viewModel.setDashboardServicesDB(result.data.data_details)
                    view.service_dashboard.adapter=
                        ServiceAdapter(
                            context = requireContext(),
                            dataSource = data.data_details.services
                        )
                    view.service_dashboard.adapter!!.notifyDataSetChanged()*/
                }
            } else if (result is MyResult.Error) {
                PrintMsg.toastDebug(this, result.message)
            }

            /* var spinnerServiceAdapter: ServiceDropDownAdapter =ServiceDropDownAdapter(this, result)
             categorySpinner_providerPost?.adapter = spinnerServiceAdapter
             categorySpinner_providerPost?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                 override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                     var service:Service= categorySpinner_providerPost.selectedItem as Service

                     postProviderViewModel.getFilterMicroServices(service.id,ser).observe(this@ProviderPostActivity, androidx.lifecycle.Observer {
                         var result=it
                         var spinnerMicroServiceAdapter: MicroServiceDropDownAdapter =MicroServiceDropDownAdapter(this@ProviderPostActivity, result)
                         subcategorySpinner_providerPost?.adapter = spinnerMicroServiceAdapter
                     })

                 }
                 override fun onNothingSelected(parent: AdapterView<*>) {
                 }
             }*/
        })

        /*   postProviderViewModel.getMicroServicesDB(2).observe(this, androidx.lifecycle.Observer {
               var result=it
               var spinnerAdapter: ServiceDropDownAdapter =ServiceDropDownAdapter(this, result)
               categorySpinner_providerPost?.adapter = spinnerAdapter
           })*/

    }

    private fun checkValidation(postProviderViewState: PostProviderViewState) {
        if (!postProviderViewState.validLogo) {
            errorTextLogo_providerPost.text = getString(R.string.invalid_provider_logo)
            errorTextLogo_providerPost.visibility = View.VISIBLE
            errorTextPage_providerPost.visibility = View.VISIBLE
        }
        if (!postProviderViewState.validServiceTitle) {
            title_providerPost.error = getString(R.string.invalid_provider_title)
            errorTextPage_providerPost.visibility = View.VISIBLE
        }
        if (!postProviderViewState.validImages) {
            errorTextImages_providerPost.text = getString(R.string.invalid_provider_images)
            errorTextImages_providerPost.visibility = View.VISIBLE
            errorTextPage_providerPost.visibility = View.VISIBLE
        }
        if (!postProviderViewState.validWebsite) {
            website_providerPost.error = getString(R.string.invalid_provider_website)
            errorTextPage_providerPost.visibility = View.VISIBLE
        }
        if (!postProviderViewState.validTime) {
            time_providerPost.error = getString(R.string.invalid_provider_time)
            errorTextPage_providerPost.visibility = View.VISIBLE
        }
        if (!postProviderViewState.validAbout) {
            about_providerPost.error = getString(R.string.invalid_provider_about)
            errorTextPage_providerPost.visibility = View.VISIBLE
        }
        if (!postProviderViewState.validCategory) {
            errorCategory_providerPost.visibility = View.VISIBLE
            errorTextPage_providerPost.visibility = View.VISIBLE
        }
        if (!postProviderViewState.validSubCategory) {
            errorSubCategory_providerPost.visibility = View.VISIBLE
            errorTextPage_providerPost.visibility = View.VISIBLE
        }
        if (!postProviderViewState.validPerson) {
            contact_providerPost.error = getString(R.string.invalid_provider_person)
            errorTextPage_providerPost.visibility = View.VISIBLE
        }
        if (!postProviderViewState.validEmail) {
            email_providerPost.error = getString(R.string.invalid_provider_email)
            errorTextPage_providerPost.visibility = View.VISIBLE
        }
        if (!postProviderViewState.validMobile) {
            mobile_providerPost.error = getString(R.string.invalid_provider_phone)
            errorTextPage_providerPost.visibility = View.VISIBLE
        }
        if (!postProviderViewState.validWhatsapp) {
            whatsapp_providerPost.error = getString(R.string.invalid_provider_whatsapp)
            errorTextPage_providerPost.visibility = View.VISIBLE
        }
    }

    private fun callUploadPostData() {

        hideErrorWarning()

        var defaultUserId: Int = 0
        if (preferences.defaultUserId != null) {
            defaultUserId = preferences.defaultUserId!!
        }

        var defaultFlatId: Int = 0
        if (preferences.defaultFlatId != null) {
            defaultFlatId = preferences.defaultFlatId!!
        }

        var service = Service()
        if (categorySpinner_providerPost.selectedItem != null && categorySpinner_providerPost.selectedItem is Service)
            service = categorySpinner_providerPost.selectedItem as Service

        var microService = MicroService()
        if (subcategorySpinner_providerPost.selectedItem != null && subcategorySpinner_providerPost.selectedItem is MicroService)
            microService = subcategorySpinner_providerPost.selectedItem as MicroService


        var uploadedImage = ArrayList<String>()
        var imageUploadedCount: Int = layoutImages_ImageContainer.childCount
        for (index in 0 until imageUploadedCount) {
            var imageView: ImageWithCrossView =
                layoutImages_ImageContainer.getChildAt(index) as ImageWithCrossView
            if (imageView.imgPhoto!!.tag != null) {
                uploadedImage.add(imageView.imgPhoto!!.tag as String)
            }
        }

        var contactInformation = ContactInformation(
            address = "",
            contactPerson = contact_providerPost.text.toString().trim(),
            email = email_providerPost.text.toString().trim(),
            phone = mobile_providerPost.text.toString().trim(),
            website = website_providerPost.text.toString().trim(),
            whatsApp = whatsapp_providerPost.text.toString().trim(),
            workingHours = time_providerPost.text.toString().trim()
        )

        var logo: String = ""
        if (logo_providerPost.tag != null) {
            logo = logo_providerPost.tag as String
        }

        var providerDetail = ProviderDetail(
            about = about_providerPost.text.toString().trim(),
            contactInformation = contactInformation,
            images = uploadedImage,
            location = "",
            logo = logo,
            serviceTitle = title_providerPost.text.toString().trim()
        )

        var providerPost = ProviderPost(
            default_user_id = defaultUserId,
            default_flat_id = defaultFlatId,
            service_id = service.id,
            micro_service_id = microService.id,
            providerDetail = providerDetail
        )


        var postValid = postProviderViewModel.validPostData(providerPost)
        if (postValid) {
            //TODO submit post
            PrintMsg.toastDebug(this, "Data Valid")
            postProviderViewModel.postProviderDetail(providerPost)
                .observe(this, androidx.lifecycle.Observer {
                    var result = it
                    if (result is MyResult.Success) {
                        var data: ApiDataWithOutObject = result.data
                        if (data.dis_msg == 1 && !TextUtils.isEmpty(data.msg))
                            PrintMsg.toast(this, data.msg);
                        if (data.success_stat == 1) {
                            //bindUIData(data.data_details)
                            PrintMsg.toastDebug(this, "Data Posted !!")
                            finish()
                        }
                    } else if (result is MyResult.Error) {
                        PrintMsg.toastDebug(this, result.message)
                    }
                })


        }
    }

    private fun hideErrorWarning() {
        errorTextLogo_providerPost.visibility = View.GONE
        errorTextImages_providerPost.visibility = View.GONE
        errorCategory_providerPost.visibility = View.GONE
        errorSubCategory_providerPost.visibility = View.GONE
        errorTextPage_providerPost.visibility = View.GONE
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

                PrintMsg.toastDebug(
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
                            PrintMsg.toastDebug(applicationContext, link)

                            if (isLogo) {
                                logo_providerPost.tag = link
                                DefaultImageLoader.load(logo_providerPost, link, null)
                            } else {
                                var imageView = ImageWithCrossView(getBaseContext());
                                var attributeSet:AttributeSet
                                imageView.btnClose!!.setOnClickListener {
                                    layoutImages_ImageContainer.removeView(imageView)
                                }
                                layoutImages_ImageContainer.addView(imageView)
                                imageView.imgPhoto!!.tag = link
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
