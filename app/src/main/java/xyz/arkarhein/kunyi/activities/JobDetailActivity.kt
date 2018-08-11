package xyz.arkarhein.kunyi.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_job_details.*
import xyz.arkarhein.kunyi.R
import xyz.arkarhein.kunyi.mvp.presenters.JobDetailPresenter
import xyz.arkarhein.kunyi.mvp.views.JobDetailView
import xyz.waiphyoag.kuunyiapp.data.vos.JobPostVO

class JobDetailActivity : BaseActivity(), JobDetailView {

    private lateinit var mPresenter: JobDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_details)

        mPresenter = ViewModelProviders.of(this).get(JobDetailPresenter::class.java)
        mPresenter.initPresenter(this)


        val jobPostId = intent.getIntExtra("jobPostId", -1)
        mPresenter.showDetailScreen(jobPostId).observe(this, Observer<JobPostVO> { t -> displayJobDetail(t!!) })

    }

    fun displayJobDetail(jobPostVO: JobPostVO) {
        tvJobDetailTitle.text = jobPostVO.shortDesc
        tvPhoneNo.text = jobPostVO.phoneNo
        tvFullDesc.text = jobPostVO.fullDesc
        tvLocation.text = jobPostVO.location
    }
}