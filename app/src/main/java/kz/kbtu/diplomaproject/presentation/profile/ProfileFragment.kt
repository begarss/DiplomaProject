package kz.kbtu.diplomaproject.presentation.profile

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView.CropShape.OVAL
import com.canhub.cropper.options
import kotlinx.coroutines.flow.collect
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.databinding.FragmentProfileBinding
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class ProfileFragment : BaseFragment() {
  override val viewModel: ProfileViewModel by viewModel()
  private lateinit var binding: FragmentProfileBinding

  val permission = registerForActivityResult(RequestPermission()) { granted ->
    when {
      granted -> {
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
        Log.v("Bitmap", result.bitmap.toString())
        Log.v("File Path", context?.let { result.getUriFilePath(it) }.toString())
        result.uriContent?.let { setImage(it) }
      }
      result is CropImage.CancelledResult -> {
      }
      else -> {
      }
    }
  }

  private fun setImage(imageUri: Uri) {
    Glide.with(this).load(imageUri).into(binding.ivAvatar)
//    userViewModel.saveUserAva(imageUri.toString())
    val file = File(imageUri.path)

    val filePart = MultipartBody.Part.createFormData(
      "profile_pic",
      file.name,
      file.asRequestBody(
        "image/*".toMediaType()
      )
    )
    Log.d("UPP", "setImageContextResolver: ${"image/*".toMediaTypeOrNull()}")

//    userViewModel.setUserAva(userId, filePart)

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

    binding.btnSetAva.setOnClickListener {
      if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
        // explain to the user why the permission is needed
      } else {
        permission.launch(Manifest.permission.CAMERA)
      }
    }
  }

  private fun observeUser() {
    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
      viewModel.profileState.collect {
        binding.tvName.text = it?.email
      }
    }
  }
}