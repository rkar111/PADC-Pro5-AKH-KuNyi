package xyz.arkarhein.kunyi.mvp.views

interface JobListView : BaseView {
    fun lauchJobDetailScreen(jobPostId: Int)
    fun onTapApplyJobButton()
}