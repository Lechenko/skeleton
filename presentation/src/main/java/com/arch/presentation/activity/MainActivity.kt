package com.arch.presentation.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.arch.presentation.R
import com.arch.presentation.base.BaseActivity
import com.arch.presentation.databinding.ActivityMainBinding
import com.arch.presentation.router.IRouter
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>(),IRouter.IRouterActivity{
    private var disposable : CompositeDisposable = CompositeDisposable()

    override val layoutRes: Int = R.layout.activity_main

    override fun initOnCreate() {

    }

    override fun createActivity(savedInstanceState: Bundle?) {
        binding?.let {
            it.router = viewModel.funBindingRouter()
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.startFragmentMain()
            }
            repeatOnLifecycle(Lifecycle.State.DESTROYED) {
               viewModel.onDestroyView()
               disposable.clear()
            }
        }
    }

    override fun stopActivity() {

    }

    override fun startActivity() {

    }

    override fun pauseActivity() {

    }

    override fun resumeActivity() {

    }

    override fun destroyActivity() {
        disposable.clear()
        viewModel.onDestroyView()
    }

    override fun onMessage(message: String) {
        super.toastLong(message)
    }

    override fun isProgress(flag: Boolean) {
        binding?.let {
            it.progressBar.visibility = if (flag) View.VISIBLE else View.INVISIBLE
        }
    }

}