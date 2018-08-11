package xyz.arkarhein.kunyi.mvp.presenters

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import xyz.arkarhein.kunyi.mvp.views.BaseView

abstract class BasePresenter<T : BaseView> : ViewModel() {

    lateinit var mView: T
    lateinit var mErrorLD: MutableLiveData<String>

    open fun initPresenter(mView: T) {
        this.mView = mView
        mErrorLD = MutableLiveData()
    }

}