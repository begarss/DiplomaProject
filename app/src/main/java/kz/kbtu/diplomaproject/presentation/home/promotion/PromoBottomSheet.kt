package kz.kbtu.diplomaproject.presentation.home.promotion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.airba.infrastructure.helpers.load
import kz.airba.infrastructure.helpers.makeLinks
import kz.airba.infrastructure.helpers.navigateSafely
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.data.backend.main.BannerDTO
import kz.kbtu.diplomaproject.databinding.BottomSheetBannerBinding
import kz.kbtu.diplomaproject.presentation.auth.LoginFragmentDirections
import kz.kbtu.diplomaproject.presentation.support.WebViewFragment

class PromoBottomSheet : BottomSheetDialogFragment() {
  private lateinit var binding: BottomSheetBannerBinding

  private val data by lazy {
    requireArguments().getParcelable<BannerDTO>(PROMO_DATA)
  }
  private val image by lazy {
    requireArguments().getString(PROMO_IMAGE)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding =
      DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_banner, container, false)
    return binding.root

  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.ivPromo.load(data?.image, placeholder = R.drawable.test_ad)
    binding.tvBannerTitle.text = data?.title
    binding.btnUnderstand.setOnClickListener {
      dismiss()
    }
    binding.tvReadMore.makeLinks(Pair(getString(R.string.readMore), View.OnClickListener {
      navigateSafely(
        R.id.action_promoBottomSheet_to_webViewFragment,
        bundleOf(WebViewFragment.WEB_URL to data?.url)
      )
    }))
  }

  companion object {
    const val PROMO_DATA = "promo"
    const val PROMO_IMAGE = "promo_image"
  }
}