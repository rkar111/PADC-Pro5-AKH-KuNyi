package xyz.arkarhein.kunyi.adapters

import android.content.Context
import android.view.ViewGroup
import xyz.arkarhein.kunyi.R
import xyz.arkarhein.kunyi.delegates.JobListItemDelegate
import xyz.arkarhein.kunyi.viewholders.JobsItemViewHolder
import xyz.waiphyoag.kuunyiapp.data.vos.JobPostVO

class JobsItemAdapter(context: Context, private val mDelegate: JobListItemDelegate) : BaseRecyclerAdapter<JobsItemViewHolder, JobPostVO>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsItemViewHolder {
        val jobsItemView = mLayoutInflator.inflate(R.layout.item_jobs, parent, false)
        return JobsItemViewHolder(jobsItemView, mDelegate)
    }
}