package com.jans.societyoo.ui.navigation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jans.imageload.DefaultImageLoader
import com.jans.societyoo.R
import com.jans.societyoo.data.remote.NetworkInstance
import com.jans.societyoo.databinding.FragmentCreatePostBinding
import com.jans.societyoo.model.ApiDataFile
import com.jans.societyoo.ui.customviews.ImageWithCrossView
import com.jans.societyoo.utils.Constants
import com.jans.societyoo.utils.PrintMsg
import com.jans.societyoo.viewmodel.PostViewModel
import com.jans.societyoo.viewmodel.PostViewModelFactory
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
import com.theartofdev.edmodo.cropper.CropImageView
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


class CreatePostFragment : Fragment() {
    val GALLERY_IMAGE_REQUEST_CODE: Int = 1
    val CROP_IMAGE_REQUEST_CODE = 1001
    private lateinit var binding: FragmentCreatePostBinding
    private lateinit var postViewModel: PostViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_create_post, container, false)
        binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        postViewModel =
            ViewModelProvider(viewModelStore, PostViewModelFactory(requireContext())).get(
                PostViewModel::class.java
            )
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.postViewModel = postViewModel
        binding.addImageImageContainer.setOnClickListener {
            callImagePicker()
        }
    }

    fun progressBarVisibility(visible: Boolean) {
        if (visible) {
            binding.progressBar.visibility = View.VISIBLE
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            binding.progressBar.visibility = View.GONE
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


    private fun callImagePicker() {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == GALLERY_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val image: Uri = data!!.data!!
            // start cropping activity for pre-acquired image saved on the device
            orignalSize = Constants.getImageSize(requireContext(), image)
            CropImage.activity(image)
                .setGuidelines(CropImageView.Guidelines.ON)
                //.setAspectRatio(720, 1280)
                .start(
                    requireContext(),
                    this
                );  // for default startActivity CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE

            /*   var cropimageRequestCode = CROP_IMAGE_REQUEST_CODE
                 var ratioX = 720
                 var ratioY = 1280
                 val intent = CropImage.activity(image)
                     .setGuidelines(CropImageView.Guidelines.ON)
                     //.setAspectRatio(ratioX, ratioY)
                     .getIntent(requireContext());
                 startActivityForResult(intent, cropimageRequestCode);*/
        }

        if (requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
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
                uploadFile(compressedByteArray, resultUri.path!!)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                var error = result.error
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    // Uploading Image/Video
    private fun uploadFile(byteArray: ByteArray, path: String) {
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

                            var imageView = ImageWithCrossView(requireContext());
                            imageView.btnClose!!.setOnClickListener {
                                binding.layoutImagesImageContainer.removeView(imageView)
                            }
                            binding.layoutImagesImageContainer.addView(imageView)
                            imageView.root!!.layoutParams = FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT
                            )
                            val params: ViewGroup.LayoutParams =
                                imageView!!.imgPhoto!!.getLayoutParams()
                            params.width = ViewGroup.LayoutParams.MATCH_PARENT
                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                            imageView!!.imgPhoto!!.requestLayout()

                            imageView.imgPhoto!!.tag = link
                            DefaultImageLoader.load(imageView.imgPhoto!!, link, null)

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
