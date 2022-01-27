package kz.kbtu.diplomaproject.presentation.support

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.databinding.FragmentWebViewBinding
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.explore.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WebViewFragment : BaseFragment() {
  private lateinit var binding: FragmentWebViewBinding
  override val viewModel: SharedViewModel by viewModel()

  private val url by lazy {
    requireArguments().getString(WEB_URL)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_web_view, container, false)
    return binding.root
  }


  @SuppressLint("SetJavaScriptEnabled")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    bindViews()
    initWebView()
  }

  private fun bindViews() {
    binding.toolbar.toolbar.setNavigationOnClickListener {
      requireActivity().onBackPressed()
    }
  }

  @SuppressLint("SetJavaScriptEnabled")
  private fun initWebView() {
    val webView = binding.webView
    webView.settings.javaScriptEnabled = true
    webView.webViewClient = CommonWebViewClient()
    url?.let { webView.loadUrl(it) }
  }

  inner class CommonWebViewClient : WebViewClient() {

    override fun onPageFinished(view: WebView?, url: String?) {
      binding.progressBar.visibility = View.GONE
      super.onPageFinished(view, url)
    }
  }

  companion object {
    const val WEB_URL = "web_url"
  }
}