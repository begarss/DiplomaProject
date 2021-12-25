package kz.kbtu.diplomaproject.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.kbtu.diplomaproject.databinding.FragmentPostDetailBinding
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostDetailFragment : BaseFragment() {
  override val viewModel: DetailVIewModel by viewModel()
  private lateinit var binding: FragmentPostDetailBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentPostDetailBinding.inflate(inflater, container, false)
    return binding.root
  }
}