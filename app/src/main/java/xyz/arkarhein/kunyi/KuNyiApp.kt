package xyz.arkarhein.kunyi

import android.app.Application
import xyz.arkarhein.kunyi.data.models.JobListModel

class KuNyiApp : Application() {

    override fun onCreate() {
        super.onCreate()
        JobListModel.initNewsAppModel(applicationContext)
    }
}