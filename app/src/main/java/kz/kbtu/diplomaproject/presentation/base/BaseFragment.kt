package kz.kbtu.diplomaproject.presentation.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kz.kbtu.diplomaproject.presentation.loading.LoadingDialog
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer

abstract class BaseFragment : Fragment() {
  abstract val viewModel: BaseViewModel
  private val compositeDisposable = CompositeDisposable()
  private var loadingDialog: LoadingDialog? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
      object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
          val result = findNavController().popBackStack()
          if (!result) {
            activity?.finish()
          }
        }
      })

    context?.let { loadingDialog = LoadingDialog(it) }
    viewModel.observeLoader().subscribe { isLoading ->
      if (isLoading) {
        loadingDialog?.showLoading()
      } else {
        loadingDialog?.hideLoading()
      }
    }

//    viewModel.observeErrors().subscribe({
//      showError(getString(it.errorDescriptionId))
//    }, {})
  }

  protected fun disposer() = Consumer<Disposable> {
    compositeDisposable.add(it)
  }

  override fun onDestroyView() {
    compositeDisposable.clear()
    loadingDialog?.hideLoading()
    viewModel.clearOnDestroyView()
    super.onDestroyView()
  }

  fun showLoading() {
    loadingDialog?.showLoading()
  }

  fun hideLoading() {
    loadingDialog?.hideLoading()
  }

  fun showError(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
  }

  fun disableBackPress() = true

//  fun openMainContainer() {
//    (activity as? MainActivity)?.openMainContainer()
//  }
//
//  fun openAuthContainer() {
//    (activity as? MainActivity)?.openAuthorizationContainer()
//  }
}