package xyz.arkarhein.kunyi.viewholders

import android.view.View
import xyz.waiphyoag.kuunyiapp.data.vos.JobPostVO
import kotlinx.android.synthetic.main.item_jobs.view.*
import xyz.arkarhein.kunyi.delegates.JobListItemDelegate

class JobsItemViewHolder(itemView: View, private val mDelegate: JobListItemDelegate) : BaseViewHolder<JobPostVO>(itemView) {
    override fun setData(data: JobPostVO) {
        mData = data
        var jobPrice: String = "" + data.offerAmount!!.amount + " kyats " + data.offerAmount!!.duration

        itemView.tvJobTitle.text = data.shortDesc
        itemView.tvJobPrice.text = jobPrice
        itemView.tvJobLocation.text = data.location
        itemView.tvJobPostedDate.text = data.postedDate

        itemView.btnApply.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mDelegate.onTapApply()
            }
        })
    }

    override fun onClick(v: View?) {
        mDelegate.onTapJobListItem(mData!!)
    }
}