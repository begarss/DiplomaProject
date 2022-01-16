package kz.kbtu.diplomaproject.presentation.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.airba.infrastructure.helpers.dp
import kz.airba.infrastructure.helpers.hide
import kz.airba.infrastructure.helpers.initRecyclerView
import kz.airba.infrastructure.helpers.navigateSafely
import kz.airba.infrastructure.helpers.show
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.explore.SharedViewModel
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.databinding.FragmentHomeBinding
import kz.kbtu.diplomaproject.presentation.base.MenuItemType.EXPLORE
import kz.kbtu.diplomaproject.presentation.home.promotion.PromoBottomSheet
import kz.kbtu.diplomaproject.presentation.home.promotion.PromotionAdapter
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {
  override val viewModel: HomeViewModel by viewModel()
  private val sharedViewModel: SharedViewModel by sharedStateViewModel()
  private lateinit var binding: FragmentHomeBinding
  private lateinit var promotionAdapter: PromotionAdapter
  private lateinit var viewPager2: ViewPager2

  private val sliderHandler: Handler = Handler()
  private val sliderRunnable = Runnable { viewPager2.currentItem += 1 }
  private val postAdapter by lazy {
    PostAdapter(arrayListOf(), onFavClick = {
      viewModel.addToFavorite(it)
    }, onItemClick = {
      navigateSafely(HomeFragmentDirections.actionHomeFragmentToPostDetailFragment(it))
    })
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
    viewPager2 = binding.collapsingToolbar.bannerViewPagerHome
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.getSubscribedOpperts()
    viewModel.getBanners()
    viewModel.getFavourites()
    bindViews()
    observeBanners()
    observeOpports()
    observeFavState()
  }

  override fun onPause() {
    super.onPause()
    sliderHandler.removeCallbacks(sliderRunnable)
  }

  override fun onResume() {
    super.onResume()
    sliderHandler.postDelayed(sliderRunnable, 4000)
  }

  private fun bindViews() {
    promotionAdapter = PromotionAdapter(
      arrayListOf(),
      onBannerClick = {
        val bundle =
          bundleOf(PromoBottomSheet.PROMO_DATA to it)
        navigateSafely(R.id.action_homeFragment_to_promoBottomSheet, bundle)

      },
      viewPager2
    )
    setBanner()
    binding.mainRV.initRecyclerView()
    binding.mainRV.adapter = postAdapter
    binding.emptyView.btnOpenSearch.setOnClickListener {
      sharedViewModel.selectMenuItem(EXPLORE)
    }
  }

  private fun setBanner() {
    binding.collapsingToolbar.bannerViewPagerHome.apply {
      adapter = promotionAdapter
      offscreenPageLimit = 2

      val compositePageTransformer = CompositePageTransformer()
      val offsetPx =
        32.dp
      setPadding(16.dp, 0, offsetPx, 0)

      //increase this offset to increase distance between 2 items
      val pageMarginPx =
        16.dp
      val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
        if (currentItem == 0) {
          setPadding(16.dp, 0, offsetPx, 0)
        } else {
          setPadding(offsetPx, 0, offsetPx, 0)
        }
      }
      compositePageTransformer.addTransformer(pageTransformer)
      val marginTransformer = MarginPageTransformer(pageMarginPx)
      compositePageTransformer.addTransformer(marginTransformer)
      setPageTransformer(compositePageTransformer)
      adapter = promotionAdapter


      registerOnPageChangeCallback(object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
          super.onPageSelected(position)
          sliderHandler.removeCallbacks(sliderRunnable)
          sliderHandler.postDelayed(sliderRunnable, 4000); // slide duration 2 seconds
        }
      })

    }
  }

  private fun observeBanners() {
    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
      viewModel.bannerState.collect {
        if (it != null) {
          promotionAdapter.addAll(it)
        }
      }
    }
  }

  private fun observeOpports() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.postState.collect {
        if (it?.isEmpty() == true) {
          binding.emptyView.root.show()
          binding.mainContainerHome.hide()
          binding.collapsingToolbar.bannerViewPagerHome.hide()
        } else {
          it?.let {
            postAdapter.addAll(it)
          }
          binding.collapsingToolbar.bannerViewPagerHome.show()
          binding.emptyView.root.hide()
          binding.mainContainerHome.show()
        }
      }
    }
  }

  private fun observeFavState() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.favState.collect {
        Log.d("TAGA", "hjghjgjhghjghj: $it")
        if (it == true) {
          viewModel.getSubscribedOpperts()
          viewModel.clearFavState()
        }
      }
    }
  }
}