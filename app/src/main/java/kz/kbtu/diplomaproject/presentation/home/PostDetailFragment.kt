package kz.kbtu.diplomaproject.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType.CENTER
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.airba.infrastructure.helpers.load
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.data.backend.main.opportunity.PostDetail
import kz.kbtu.diplomaproject.databinding.FragmentPostDetailBinding
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostDetailFragment : BaseFragment() {
  override val viewModel: DetailVIewModel by viewModel()
  private lateinit var binding: FragmentPostDetailBinding
  private val args: PostDetailFragmentArgs by navArgs()
  private var postDetail: PostDetail? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentPostDetailBinding.inflate(inflater, container, false)
    viewModel.getDetails(args.postId)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    observePost()
    bindViews()
    observeFavState()
  }

  private fun bindViews() {
    with(binding) {
      ivBack.scaleType = CENTER
      ivShare.scaleType = CENTER
      ivSave.setOnClickListener {
        viewModel.addToFavorite(postDetail)
      }
      ivBack.setOnClickListener {
        requireActivity().onBackPressed()
      }
    }
  }

  private fun observePost() {
    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
      viewModel.detailState.collect {
        with(binding) {
          postDetail = it
          ivCompany.load(it?.company?.picture)
          if (it?.isFavourate == true) {
            ivSave.setBackgroundResource(R.drawable.ic_bookmark_big_fill)
          } else {
            ivSave.setBackgroundResource(R.drawable.ic_bookmark_big)
          }
          tvPostTitle.text = it?.title
          tvCompanyName.text = it?.company?.name
          tvJobType.text = it?.jobType
          tvJobCategory.text = it?.jobCategory?.title
          tvDeadline.text = it?.deadline
          tvDescription.text = it?.description
          tvRequirements.text = it?.requirements
        }
      }
    }
  }

  private fun observeFavState() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.favState.collect {
        if (it == true) {
          viewModel.getDetails(args.postId)
          viewModel.clearFavState()
        }
      }
    }
  }
}