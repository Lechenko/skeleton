package com.arch.presentation.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.window.layout.WindowMetrics
import androidx.window.layout.WindowMetricsCalculator
import com.arch.comm.BuildConfig
import dagger.android.support.DaggerFragment
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import kotlin.math.min


abstract class BaseFragment<Binding : ViewDataBinding, ViewModelType : ViewModel> :
    DaggerFragment() {
    protected var disposable: CompositeDisposable? = CompositeDisposable()
    protected var binding: Binding? = null
    var subjetTestVMStatus: PublishSubject<Boolean>? = null


    init {
        if (BuildConfig.DEBUG) subjetTestVMStatus = PublishSubject.create()
    }

    fun testStatusVM(): Single<Boolean>? {
        return subjetTestVMStatus?.lastOrError()
    }

    @Inject
    lateinit var viewModel: ViewModelType
    override fun onAttach(context: Context) {
        super.onAttach(context)
        attachFragment()
    }


    override fun onStop() {
        stopFragment()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        startFragment()
    }

    override fun onPause() {
        pauseFragment()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        resume()
    }

    protected abstract fun listenerViewModel()

    @FunctionalInterface
    protected interface ActionState<V> {
        fun <T : V> action(model: T)
    }

    @FunctionalInterface
    protected interface ActionError {
        fun error(msg: String)
    }

//    @Suppress("UNCHECKED_CAST")
//    protected open fun <T> byViewModel(
//        viewModel: IState,
//        actionState: ActionState<T>,
//        actionError: ActionError
//    ) {
//        disposable?.add(viewModel.state()
//            .doOnNext {  Timber.tag(News::class.java.name.toString())
//                .i("observationState doOnNext : ".plus(it.status)) }
//            .subscribe({
//            actionState.action(model = it as T)
//            if (BuildConfig.DEBUG) subjetTestVMStatus?.onNext(true)
//        }, {
//            actionError.error(it.message.toString())
//            Timber.tag(News::class.java.name.toString())
//                .i("error observationState : ".plus(it.message.toString()))
//            showMessage("error ".plus(it.message))
//        }))
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        disposable?.clear()
        initFragmentView()
        return binding?.root
    }

    private fun toastShort(message: String) {
        Toast.makeText(activity?.applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun toastLong(message: String) {
        Toast.makeText(activity?.applicationContext, message, Toast.LENGTH_LONG).show()
    }

    fun showMessage(message: String) {
        toastShort(message)
    }

    @get:LayoutRes
    protected abstract val layoutRes: Int
    protected abstract fun initFragmentView()
    protected abstract fun attachFragment()
    protected abstract fun startFragment()
    protected abstract fun stopFragment()
    protected abstract fun destroyFragment()
    protected abstract fun pauseFragment()
    protected abstract fun resume()
    override fun onDestroy() {
        binding?.unbind()
        destroyFragment()
        disposable?.dispose()
        disposable = null
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
    }

    fun shareLink(url: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, url)
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }

    fun shareFile(path: String) {
        val f = File(path)
        val intentShareFile = Intent(Intent.ACTION_SEND)
        if (f.exists()) {
            val imageUri = FileProvider.getUriForFile(
                requireActivity(),
                requireActivity().packageName + ".provider",
                f
            )
            intentShareFile.type = "file/*"
            intentShareFile.putExtra(Intent.EXTRA_STREAM, imageUri)
            intentShareFile.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val chooser = Intent.createChooser(intentShareFile, "Share File")
            //
            @SuppressLint("QueryPermissionsNeeded") val resInfoList =
                requireActivity().packageManager.queryIntentActivities(
                    chooser,
                    PackageManager.MATCH_DEFAULT_ONLY
                )
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                requireActivity().grantUriPermission(
                    packageName,
                    imageUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            super.startActivity(chooser)
        }
    }


    fun getDimension(activity: Activity): Int {
        val windowMetrics: WindowMetrics =
            WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(activity)
        val height: Int = windowMetrics.bounds.height()
        val width: Int = windowMetrics.bounds.width()
        return min(height, width)
    }

    fun getDimensionA(): Int {
        var size = 0
        activity?.let {
            val windowMetrics =
                WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(it)
            val height: Int = windowMetrics.bounds.height()
            val width: Int = windowMetrics.bounds.width()
            size = min(height, width)
        }
        return size
    }

}