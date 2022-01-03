package kz.kbtu.diplomaproject.presentation.profile

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView.CropShape.OVAL
import com.canhub.cropper.options
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.airba.infrastructure.helpers.load
import kz.airba.infrastructure.helpers.navigateSafely
import kz.airba.infrastructure.helpers.setOnClickListenerWithDebounce
import kz.kbtu.diplomaproject.MainActivity
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.data.backend.profile.UserInfo
import kz.kbtu.diplomaproject.databinding.FragmentProfileBinding
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.profile.changePassword.ChangePasswordFragment
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileFragment : BaseFragment() {
  override val viewModel: ProfileViewModel by viewModel()
  private lateinit var binding: FragmentProfileBinding
  private var outputUri: Uri? = null
  private lateinit var userInfo: UserInfo

  val permission = registerForActivityResult(RequestPermission()) { granted ->
    when {
      granted -> {
        setupOutputUri()
        cropImage.launch(options {
          setCropShape(OVAL)
        }) // access to the camera is allowed, open the camera
      }
      !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
        // access to the camera is denied, the user has checked the Don't ask again.
      }
      else -> {
        // access to the camera is prohibited
      }
    }
  }

  private val cropImage = registerForActivityResult(CropImageContract()) { result ->
    when {
      result.isSuccessful -> {
        Log.d("Bitmap", "${result.getUriFilePath(requireContext(), true)}")
        Log.d("TAGA", context?.let { result.getUriFilePath(it) }.toString())

//        result.uriContent?.let { setImage(it) }
        result.getUriFilePath(requireContext())?.let {
          result.uriContent?.let { it1 ->
            setImage(
              it,
              it1
            )
          }
        }
      }
      result is CropImage.CancelledResult -> {
      }
      else -> {
      }
    }
  }

  private fun setImage(imagePath: String, imageContent: Uri) {
    Glide.with(this).load(imageContent).into(binding.ivAvatar)
//    userViewModel.saveUserAva(imageUri.toString())
    val file = File(imagePath)

    val filePart = MultipartBody.Part.createFormData(
      "prof_image",
      file.name,
      file.asRequestBody(
        "image/*".toMediaType()
      )
    )
    Log.d("UPP", "setImageContextResolver: ${"image/*".toMediaTypeOrNull()}")

    viewModel.setUserImage(filePart)

  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.getUserInfo()
    observeUser()
    observeLogout()
    bindViews()
  }

  private fun bindViews() {
    with(binding) {
      toolbar.toolbar.navigationIcon = null

      cardChangePassword.setOnClickListenerWithDebounce {
        val bundle = bundleOf(ChangePasswordFragment.EMAIL to userInfo.email)
        navigateSafely(R.id.action_profileFragment_to_changePasswordFragment, bundle)
      }
      cardProfile.setOnClickListener {
        Log.d("TAGQ", "onViewCreated: $userInfo")
        navigateSafely(ProfileFragmentDirections.actionProfileFragmentToEditUserFragment(userInfo))
      }

      btnSetAva.setOnClickListener {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
          // explain to the user why the permission is needed
        } else {
          permission.launch(Manifest.permission.CAMERA)
        }
      }

      btnLogout.setOnClickListener {
        MaterialAlertDialogBuilder(requireContext())
          .setTitle(resources.getString(R.string.dialog_logout_title))
          .setMessage(resources.getString(R.string.dialog_logout_message))
          .setPositiveButton(resources.getString(R.string.dialog_yes)) { _, _ ->
            // Respond to positive button press
            viewModel.logout()
          }
          .setNegativeButton(resources.getString(R.string.dialog_no)) { dialog, which ->
            dialog.dismiss()
          }
          .show()
      }
    }
  }

  private fun observeUser() {
    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
      viewModel.profileState.collect {
        if (it != null) {
          userInfo = it
        }
        it?.profImage = "http://ithuntt.pythonanywhere.com/${it?.profImage}"

        binding.tvName.text = it?.email
        binding.ivAvatar.load(it?.profImage, placeholder = R.drawable.ava)
      }
    }
  }

  private fun setupOutputUri() {
    if (outputUri == null) context?.let { ctx ->
      val authorities = "${ctx.applicationContext?.packageName}$AUTHORITY_SUFFIX"
      outputUri = FileProvider.getUriForFile(ctx, authorities, createImageFile())
    }
  }

  private fun createImageFile(): File {
    val timeStamp = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
    val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
      "$FILE_NAMING_PREFIX${timeStamp}$FILE_NAMING_SUFFIX",
      FILE_FORMAT,
      storageDir
    )
  }

  private fun observeLogout() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.logoutState.collect {
        when (it) {
          true -> {
            openAuthContainer()
          }
          false -> {
          }
          null -> {
          }
        }
      }
    }
  }

  private fun openAuthContainer() {
    (activity as? MainActivity)?.openAuthorizationContainer()
  }

  companion object {

    const val DATE_FORMAT = "yyyyMMdd_HHmmss"
    const val FILE_NAMING_PREFIX = "JPEG_"
    const val FILE_NAMING_SUFFIX = "_"
    const val FILE_FORMAT = ".jpg"
    const val AUTHORITY_SUFFIX = ".cropper.fileprovider"
  }
}