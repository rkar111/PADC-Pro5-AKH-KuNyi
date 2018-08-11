package xyz.arkarhein.kunyi.mvp.presenters

import android.arch.lifecycle.MutableLiveData
import xyz.arkarhein.kunyi.data.models.JobListModel
import xyz.arkarhein.kunyi.mvp.views.JobDetailView
import xyz.waiphyoag.kuunyiapp.data.vos.JobPostVO

class JobDetailPresenter : BasePresenter<JobDetailView>() {

    private lateinit var getJobDetailById: MutableLiveData<JobPostVO>

    override fun initPresenter(mView: JobDetailView) {
        super.initPresenter(mView)
        getJobDetailById = MutableLiveData()
    }

    fun showDetailScreen(jobPostId: Int): MutableLiveData<JobPostVO> {
        val jobPostVO = JobListModel.getInstance().getJobDetailById(jobPostId)
        getJobDetailById.value = jobPostVO
        return getJobDetailById
    }
}