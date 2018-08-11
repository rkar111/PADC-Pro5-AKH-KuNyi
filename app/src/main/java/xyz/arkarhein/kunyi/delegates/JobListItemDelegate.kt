package xyz.arkarhein.kunyi.delegates

import xyz.waiphyoag.kuunyiapp.data.vos.JobPostVO

interface JobListItemDelegate {
    fun onTapJobListItem(jobPostVO: JobPostVO)
    fun onTapApply()
}