package kz.kbtu.diplomaproject.presentation.company

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.airba.infrastructure.helpers.initRecyclerView
import kz.airba.infrastructure.helpers.load
import kz.airba.infrastructure.helpers.navigateSafely
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.R.string
import kz.kbtu.diplomaproject.data.backend.main.opportunity.Company
import kz.kbtu.diplomaproject.databinding.FragmentCompanyDetailBinding
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.home.PostAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompanyDetailFragment : BaseFragment() {
  override val viewModel: CompanyViewModel by viewModel()
  private lateinit var binding: FragmentCompanyDetailBinding
  private val args: CompanyDetailFragmentArgs by navArgs()
  private var company: Company? = null

  private val postAdapter by lazy {
    PostAdapter(arrayListOf(), onItemClick = {
      navigateSafely(CompanyDetailFragmentDirections.actionCompanyDetailToPostDetailFragment(it))
    }, onFavClick = { viewModel.addToFavorite(it) })
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentCompanyDetailBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.getCompanyDetail(args.companyId)
    viewModel.getOppByCategory(args.companyId)
    bindViews()
    observeCompany()
    observeOpps()
    observeFollowState()
    observeFavState()
  }

  private fun bindViews() {
    with(binding) {
      toolbar.toolbar.title = "Company"
      toolbar.toolbar.setNavigationOnClickListener {
        requireActivity().onBackPressed()
      }

      rvOpports.initRecyclerView()
      rvOpports.adapter = postAdapter

      ivShare.setOnClickListener {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, "Hey Check out this Company: ${company?.readMoreLink}")
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, "Share To:"))
      }

      btnReadMore.setOnClickListener {
        val defaultBrowser =
          Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER)
        defaultBrowser.data = Uri.parse(company?.readMoreLink)
        startActivity(defaultBrowser)
      }

      btnFollow.setOnClickListener {
        viewModel.makeSubscribe(args.companyId)
      }
    }
  }

  private fun observeCompany() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.companyDetailState.collect {
        with(binding) {
          company = it
          ivCompany.load(it?.picture)
          tvCompanyName.text = it?.name
          tvAboutCompany.text = it?.aboutCompany

          if (args.isFollowed) {
            btnFollow.apply {
              text = context.getString(string.btn_unfollow)
              setBackgroundResource(R.drawable.button_background_colored_disabled)
            }
          } else {
            btnFollow.apply {
              text = context.getString(string.btn_follow)
              setBackgroundResource(R.drawable.button_background_default)
            }
          }
        }
      }
    }
  }

  private fun observeOpps() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.companyOppState.collect {
        it?.let {
          Log.d("TAGA", "observeOpps: $it")
          postAdapter.addAll(it)
        }
      }
    }
  }

  private fun observeFollowState() {
    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
      viewModel.followState.collect {
        if (it == true) {
          with(binding) {
            if (!args.isFollowed) {
              btnFollow.apply {
                text = context.getString(string.btn_unfollow)
                setBackgroundResource(R.drawable.button_background_colored_disabled)
              }
            } else {
              btnFollow.apply {
                text = context.getString(string.btn_follow)
                setBackgroundResource(R.drawable.button_background_default)
              }
            }
          }
          viewModel.clearFollowState()
        }
      }
    }
  }

  private fun observeFavState() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.favState.collect {
        Log.d("TAGA", "hjghjgjhghjghj: $it")
        if (it == true) {
          viewModel.getOppByCategory(args.companyId)
          viewModel.clearFavState()
        }
      }
    }
  }

  companion object {
    const val IS_FOLLOWED = "is_follwed"
  }
}