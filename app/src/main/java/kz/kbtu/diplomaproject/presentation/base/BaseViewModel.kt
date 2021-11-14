package kz.kbtu.diplomaproject.presentation.base

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.delay

abstract class BaseViewModel : ViewModel() {

  private val screen = PublishSubject.create<Screen>()
  private val isLoading = PublishSubject.create<Boolean>()
//  private val error = PublishSubject.create<ErrorType>()
//  private val animation = PublishSubject.create<AnimationEvent>()

  private val compositeDisposable = CompositeDisposable()

  protected suspend fun defaultDelay() {
    delay(timeMillis = 200)
  }

  protected fun disposer() = Consumer<Disposable> {
    compositeDisposable.add(it)
  }

  override fun onCleared() {
    compositeDisposable.clear()
    super.onCleared()
  }

//  fun onError(event: ErrorType) {
//    error.onNext(event)
////    sendEvent(MetricaEvent.getErrorEventInstance(event))
//  }

  fun onScreen(event: Screen) {
    screen.onNext(event)
  }

  fun observeScreens(): Observable<Screen> {
    return screen.observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe(disposer())
  }

  fun observeLoader(): Observable<Boolean> {
    return isLoading.observeOn(AndroidSchedulers.mainThread())
      .doOnSubscribe(disposer()) as Observable<Boolean>
  }

//  fun observeErrors(): Observable<ErrorType> {
//    return error.observeOn(AndroidSchedulers.mainThread())
//      .doOnSubscribe(disposer())
//  }

  open fun clearOnDestroyView() {
    compositeDisposable.clear()
  }

  open fun showLoader() {
    isLoading.onNext(true)
  }

  open fun hideLoader() {
    isLoading.onNext(false)
  }

//  protected open fun errorHandler(): Consumer<Throwable> {
//    return Consumer { errorHandler(it) }
//  }

//  protected open fun errorHandler(thr: Throwable) {
////    if (connectivity?.isOffline() == true) {
////      onError(ErrorType.NETWORK)
////    } else {
//    val error = NetworkError.getInstance(thr)
//    onError(error.getErrorType())
////    }
//  }
//
//  protected fun printError(thr: Throwable) {
//    Timber.d(thr)
//  }
//
//  protected fun printError(): Consumer<Throwable> {
//    return Consumer { printError(it) }
//  }
//
//  fun sendEvent(event: MetricaEvent) {
//    eventSender?.send(event)
//  }
//
//  fun sendFacebookEvent(event: String) {
//    thirdPartyEventSender.send(event)
//  }

//  fun handleError(): Consumer<Throwable> {
//    return Consumer {
//      val error = NetworkError.getInstance(it)
//      onError(error.getErrorType())
//    }
//  }
}