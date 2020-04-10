package com.jans.societyoo.ui.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jans.imageload.DefaultImageLoader
import com.jans.societyoo.R
import com.jans.societyoo.data.remote.NetworkInstance
import com.jans.societyoo.model.ApiDataFile
import com.jans.societyoo.model.ApiDataObject
import com.jans.societyoo.model.ApiDataWithOutObject
import com.jans.societyoo.model.login.UserDetail
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
import kotlinx.android.synthetic.main.fragment_provider_post.*
import kotlinx.android.synthetic.main.layout_image_container_with_add.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set


class ProviderPostFragment : Fragment() {
    val GALLERY_IMAGE_REQUEST_CODE: Int = 1
    val GALLERY_LOGO_REQUEST_CODE: Int = 2
    val CROP_IMAGE_REQUEST_CODE = 1001
    val CROP_LOGO_REQUEST_CODE = 2001
    var progressBar: ProgressBar? = null
    private lateinit var postProviderViewModel: ProviderPostViewModel
   // lateinit var preferences: UserPreferences
    lateinit var userDetail: UserDetail

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_provider_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //preferences = UserPreferences(requireContext())

        progressBar = progress_bar

        postProviderViewModel =
            ViewModelProvider(viewModelStore, ProviderPostViewModelFactory(requireContext())).get(
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
        postProviderViewModel.postProviderViewState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                var postProviderViewState = it
                checkValidation(postProviderViewState)
            })
        postProviderViewModel.getAllServices()
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                var result = it
                if (result is MyResult.Success) {
                    val data: ApiDataObject<Services> = result.data
                    if (data.dis_msg == 1 && !TextUtils.isEmpty(data.msg))
                        PrintMsg.toast(requireContext(), data.msg);
                    if (data.success_stat == 1 && data.data_details != null && data.data_details.services != null && data.data_details.services.size > 0) {
                        val serviceList = data.data_details.services
                        val microServiceList = data.data_details.microServices
                        initSpinner(serviceList,microServiceList)
                    }
                } else if (result is MyResult.Error) {
                    PrintMsg.toastDebug(requireContext(), result.message)
                }
            })
        postProviderViewModel.userDetailLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                userDetail=it
            }
        })

        postProviderViewModel.getUserDetailDB()
    }

    fun initSpinner(
        serviceList: List<Service>,
        microServiceList: List<MicroService>
    ) {
        val spinnerServiceAdapter =ServiceDropDownAdapter(requireContext(), serviceList)
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
                    ).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                        val filteredMicroService = it
                        val spinnerMicroServiceAdapter =
                            MicroServiceDropDownAdapter(
                                requireContext(),
                                filteredMicroService
                            )
                        subcategorySpinner_providerPost?.adapter =
                            spinnerMicroServiceAdapter
                    })

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
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
        var defaultFlatId: Int = 0
        userDetail?.let {
            defaultUserId=it.defaultUserId
            defaultFlatId=it.defaultFlatId
        }
        var service = Service()
        if (categorySpinner_providerPost.selectedItem != null && categorySpinner_providerPost.selectedItem is Service)
            service = categorySpinner_providerPost.selectedItem as Service

        var microService = MicroService()
        if (subcategorySpinner_providerPost.selectedItem != null && subcategorySpinner_providerPost.selectedItem is MicroService)
            microService = subcategorySpinner_providerPost.selectedItem as MicroService


        val uploadedImage = ArrayList<String>()
        val imageUploadedCount: Int = layoutImages_ImageContainer.childCount
        for (index in 0 until imageUploadedCount) {
            val imageView: ImageWithCrossView =
                layoutImages_ImageContainer.getChildAt(index) as ImageWithCrossView
            if (imageView.imgPhoto!!.tag != null) {
                uploadedImage.add(imageView.imgPhoto!!.tag as String)
            }
        }

        val contactInformation = ContactInformation(
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

        val providerDetail = ProviderDetail(
            about = about_providerPost.text.toString().trim(),
            contactInformation = contactInformation,
            images = uploadedImage,
            location = "",
            logo = logo,
            serviceTitle = title_providerPost.text.toString().trim()
        )

        val providerPost = ProviderPost(
            default_user_id = defaultUserId,
            default_flat_id = defaultFlatId,
            service_id = service.id,
            micro_service_id = microService.id,
            providerDetail = providerDetail
        )


        val postValid = postProviderViewModel.validPostData(providerPost)
        if (postValid) {
            progressBarVisibility(true)
            postProviderViewModel.postProviderDetail(providerPost)
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    val result = it
                    if (result is MyResult.Success) {
                        progressBarVisibility(false)
                        val data: ApiDataWithOutObject = result.data
                        if (data.dis_msg == 1 && !TextUtils.isEmpty(data.msg))
                            PrintMsg.toast(requireContext(), data.msg);
                        if (data.success_stat == 1) {
                            //bindUIData(data.data_details)
                            PrintMsg.toast(requireContext(), "Congratulation! Your service has been posted successfully.")
                            //requireActivity().finish()
                            onClear()
                        }
                    } else if (result is MyResult.Error) {
                        progressBarVisibility(false)
                        PrintMsg.toastDebug(requireContext(), result.message)
                    }
                })
        }
    }

    private fun onClear() {
        logo_providerPost.setImageResource(R.drawable.db_splash_logo)
        logo_providerPost.tag=""
        title_providerPost.setText("")
        layoutImages_ImageContainer.removeAllViews()
        website_providerPost.setText("")
        time_providerPost.setText("")
        about_providerPost.setText("")
        categorySpinner_providerPost.setSelection(1)
        contact_providerPost.setText("")
        email_providerPost.setText("")
        mobile_providerPost.setText("")
        whatsapp_providerPost.setText("")
    }


    private fun hideErrorWarning() {
        errorTextLogo_providerPost.visibility = View.GONE
        errorTextImages_providerPost.visibility = View.GONE
        errorCategory_providerPost.visibility = View.GONE
        errorSubCategory_providerPost.visibility = View.GONE
        errorTextPage_providerPost.visibility = View.GONE
    }

    fun progressBarVisibility(visible: Boolean) {
        if (progressBar != null) {
            if (visible) {
                progressBar!!.visibility = View.VISIBLE
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            } else {
                progressBar!!.visibility = View.GONE
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
    }

    private fun callImagePicker(isLogo: Boolean) {
        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val rationale = "Please provide permission to access files"
        val options: Permissions.Options = Permissions.Options()
            .setRationaleDialogTitle("Info")
            .setSettingsDialogTitle("Warning")

        Permissions.check(
            requireContext(),
            permissions,
            rationale,
            options,
            object : PermissionHandler() {
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
            orignalSize = Constants.getImageSize(requireContext(), image)
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
                .getIntent(requireContext());

            startActivityForResult(intent, cropimageRequestCode);
        }

        if (requestCode == CROP_IMAGE_REQUEST_CODE || requestCode == CROP_LOGO_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                croppedSize = Constants.getImageSize(requireContext(), resultUri)
                //image_providerPost.setImageURI(resultUri)

                val compressedByteArray: ByteArray =
                    Constants.getCompressedImage(requireContext(), resultUri.path!!)
                compressedSize = Constants.getImageSize(requireContext(), compressedByteArray)

                PrintMsg.toastDebug(
                    requireContext(),
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
            PrintMsg.toast(requireContext(), "please select an image ")
            return
        } else {
            progressBarVisibility(true)
            val file = File(path)
            // Parsing any Media type file
            val requestBody = RequestBody.create("image/.jpg".toMediaTypeOrNull(), file)
            val body: MultipartBody.Part =
                MultipartBody.Part.createFormData("image", file.getName(), requestBody)

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
                            PrintMsg.toastDebug(requireContext(), link)

                            if (isLogo) {
                                logo_providerPost.tag = link
                                DefaultImageLoader.load(logo_providerPost, link, null)
                            } else {
                                var imageView = ImageWithCrossView(requireContext());
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
                        PrintMsg.toast(requireContext(), "problem uploading image")
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
