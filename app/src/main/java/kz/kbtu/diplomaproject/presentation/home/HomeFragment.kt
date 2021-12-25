package kz.kbtu.diplomaproject.presentation.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.airba.infrastructure.helpers.dp
import kz.airba.infrastructure.helpers.initRecyclerView
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.explore.SharedViewModel
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.data.backend.opportunity.Company
import kz.kbtu.diplomaproject.data.backend.opportunity.JobCategory
import kz.kbtu.diplomaproject.data.backend.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.databinding.FragmentHomeBinding
import kz.kbtu.diplomaproject.presentation.home.promotion.PromotionAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {
  override val viewModel: HomeViewModel by viewModel()
  private val sharedViewModel: SharedViewModel by sharedViewModel()
  private lateinit var binding: FragmentHomeBinding
  private lateinit var promotionAdapter: PromotionAdapter
  private lateinit var viewPager2: ViewPager2

  private val sliderHandler: Handler = Handler()
  private val sliderRunnable = Runnable { viewPager2.currentItem += 1 }
  private val postAdapter by lazy {
    PostAdapter(arrayListOf())
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
    viewModel.getBanners()
    viewModel.getSubscribedOpperts()
    bindViews()
    observeBanners()
    observeOpports()
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

      },
      viewPager2
    )
    setBanner()
    binding.mainRV.initRecyclerView()
    binding.mainRV.adapter = postAdapter
//    postAdapter.addAll(getTestPosts())
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
        if (it != null) {
          postAdapter.addAll(it)
        }
      }
    }
  }

//  private fun getTestPosts(): ArrayList<OpportunityDTO> {
//    val post = OpportunityDTO(
//      Company("DAR", ""),
//      "20.12.2020",
//      0,
//      false,
//      JobCategory(0, "Android"),
//      "internship",
//      "Middle Android developer"
//    )
//    val list = arrayListOf(
//      OpportunityDTO(
//        Company("DAR", ""),
//        "20.12.2020",
//        0,
//        false,
//        JobCategory(0, "Android"),
//        "internship",
//        "Middle Android developer"
//      ), OpportunityDTO(
//        Company("KOLESA", ""),
//        "20.12.2020",
//        0,
//        false,
//        JobCategory(0, "IOS"),
//        "vacancy",
//        "Middle IOS developer"
//      ),
//      OpportunityDTO(
//        Company("OneLAb", ""),
//        "20.12.2020",
//        0,
//        false,
//        JobCategory(0, "Backend"),
//        "vacancy",
//        "Middle Backend developer"
//      ),
//      OpportunityDTO(
//        Company("KOLESA", ""),
//        "20.12.2020",
//        0,
//        false,
//        JobCategory(0, "Android"),
//        "vacancy",
//        "Senior Android developer"
//      ),
//      OpportunityDTO(
//        Company("KOLESA", ""),
//        "20.12.2020",
//        0,
//        false,
//        JobCategory(0, "IOS"),
//        "vacancy",
//        "Middle IOS developer"
//      ),
//      OpportunityDTO(
//        Company("KOLESA", ""),
//        "20.12.2020",
//        0,
//        false,
//        JobCategory(0, "IOS"),
//        "vacancy",
//        "Middle IOS developer"
//      )
//    )
//    return list
//  }
}