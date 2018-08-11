package xyz.arkarhein.kunyi.data.models

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import xyz.arkarhein.kunyi.KuNyiApp
import xyz.waiphyoag.kuunyiapp.data.vos.JobPostVO

class JobListModel private constructor(context: Context) {

    val JOB_LIST: String = "jobs"

    var mDatabasereference: DatabaseReference? = null
    lateinit var mJobListDR: DatabaseReference
    var jobsList = ArrayList<JobPostVO>()

    var mFirebaseAuth: FirebaseAuth? = null
    var mFirebaseUser: FirebaseUser? = null

    init {
        mDatabasereference = FirebaseDatabase.getInstance().reference
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mFirebaseAuth!!.currentUser
    }

    companion object {
        private var INSTANCE: JobListModel? = null
        fun getInstance(): JobListModel {
            if (INSTANCE == null) {
                throw RuntimeException("NewsModel is being invoked before initializing.")
            }

            /*val i = INSTANCE*/
            return INSTANCE!!
        }

        fun initNewsAppModel(context: Context) {
            INSTANCE = JobListModel(context)
        }
    }

    fun loadJobList(mJobsListLd: MutableLiveData<List<JobPostVO>>, errorLd: MutableLiveData<String>) {
        mJobListDR = mDatabasereference!!.child(JOB_LIST)
        mJobListDR.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot != null) {
                    dataSnapshot?.children?.forEach { snapShot: DataSnapshot ->
                        var jobs: JobPostVO = snapShot.getValue(JobPostVO::class.java)!!
                        jobsList.add(jobs)
                    }
                    Log.d("KuNyiApp", "Data" + jobsList.size)
                    mJobsListLd.value = jobsList
                }

            }
        })
    }

    fun getJobDetailById(jobPostId: Int): JobPostVO? {

        if (jobsList.isNotEmpty()) {
            for (jobPostVO: JobPostVO in jobsList) {
                if (jobPostVO.jobPostId!!.equals(jobPostId))
                    return jobPostVO
            }

        }
        return null
    }

    fun isUserSignIn(): Boolean {
        return mFirebaseUser != null
    }

    fun authenticateUserWithGoogleAccount(signInAccount: GoogleSignInAccount, delegate: SignInWithGoogleAccountDelegate) {

        val credential = GoogleAuthProvider.getCredential(signInAccount.idToken, null)
        mFirebaseAuth!!.signInWithCredential(credential)
                .addOnCompleteListener { task ->

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful) {
                        delegate.onFailureSignIn(task.exception!!.message!!)
                    } else {
                        delegate.onSuccessSignIn(signInAccount)
                        mFirebaseAuth = FirebaseAuth.getInstance()
                        mFirebaseUser = mFirebaseAuth!!.currentUser

                    }
                }
                .addOnFailureListener { e ->
                    delegate.onFailureSignIn(e.message!!)
                }
    }

    interface SignInWithGoogleAccountDelegate {
        fun onSuccessSignIn(signInAccount: GoogleSignInAccount)

        fun onFailureSignIn(msg: String)
    }
}