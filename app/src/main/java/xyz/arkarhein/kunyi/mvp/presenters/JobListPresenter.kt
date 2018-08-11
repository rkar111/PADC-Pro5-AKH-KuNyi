package xyz.arkarhein.kunyi.mvp.presenters

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import xyz.arkarhein.kunyi.data.models.JobListModel
import xyz.arkarhein.kunyi.delegates.JobListItemDelegate
import xyz.arkarhein.kunyi.mvp.views.JobListView
import xyz.waiphyoag.kuunyiapp.data.vos.JobPostVO

class JobListPresenter : BasePresenter<JobListView>(), JobListItemDelegate {
    override fun onTapJobListItem(jobPostVO: JobPostVO) {
        mView.lauchJobDetailScreen(jobPostVO.jobPostId!!)
    }

    override fun onTapApply() {
        mView.onTapApplyJobButton()
    }

    private lateinit var jobListLd: MutableLiveData<List<JobPostVO>>

    override fun initPresenter(mView: JobListView) {
        super.initPresenter(mView)
        jobListLd = MutableLiveData()
        JobListModel.getInstance().loadJobList(jobListLd, mErrorLD)
    }

    fun getJobListLD(): LiveData<List<JobPostVO>> {
        return jobListLd
    }
}