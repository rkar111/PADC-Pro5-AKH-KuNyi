package xyz.arkarhein.kunyi.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.common.api.GoogleApiClient
import com.padcmyanmar.mmnews.kotlin.components.SmartScrollListener
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

import kotlinx.android.synthetic.main.activity_job_list.*
import xyz.arkarhein.kunyi.R
import xyz.arkarhein.kunyi.adapters.JobsItemAdapter
import xyz.arkarhein.kunyi.data.models.JobListModel
import xyz.arkarhein.kunyi.mvp.presenters.JobListPresenter
import xyz.arkarhein.kunyi.mvp.views.JobListView
import xyz.waiphyoag.kuunyiapp.data.vos.JobPostVO


class JobListActivity : BaseActivity(), JobListView, GoogleApiClient.OnConnectionFailedListener {

    private var RC_GOOGLE_SIGN_IN: Int = 1234
    private lateinit var mJobsAdapter: JobsItemAdapter
    private lateinit var mPresenter: JobListPresenter
    private var mSmartScrollListener: SmartScrollListener? = null
    private lateinit var mGoogleApiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_job_list)
        setSupportActionBar(toolbar)

        if (supportActionBar != null) {
            supportActionBar!!.setTitle(R.string.app_name)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/

        mPresenter = ViewModelProviders.of(this).get(JobListPresenter::class.java)
        mPresenter.initPresenter(this)


        rvJobs.setEmptyView(vpEmptyNews)
        rvJobs.layoutManager = LinearLayoutManager(applicationContext)

        mSmartScrollListener = SmartScrollListener(object : SmartScrollListener.OnSmartScrollListener {
            override fun onListEndReach() {
                Snackbar.make(rvJobs, "End of Data", Snackbar.LENGTH_SHORT).show()
            }
        })
        rvJobs.addOnScrollListener(mSmartScrollListener)

        mJobsAdapter = JobsItemAdapter(applicationContext, mPresenter)
        rvJobs.adapter = mJobsAdapter

        mPresenter.getJobListLD().observe(this, Observer<List<JobPostVO>> { data -> displayJobsListData(data!!) })

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("88111429943-ojccbt1c4krgcotpenap786o27bie46s.apps.googleusercontent.com")
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(applicationContext)
                .enableAutoManage(this /*FragmentActivity*/, this /*OnConnectionFailedListener*/)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build()

    }

    private fun displayJobsListData(jobsList: List<JobPostVO>) {
        mJobsAdapter.appendNewData(jobsList)
    }

    override fun lauchJobDetailScreen(jobPostId: Int) {
        val intent = Intent(applicationContext, JobDetailActivity::class.java)
        intent.putExtra("jobPostId", jobPostId)
        startActivity(intent)
    }

    override fun onTapApplyJobButton() {
        if (JobListModel.getInstance().isUserSignIn()) {
            Toast.makeText(applicationContext, "You've applied for this job!", Toast.LENGTH_SHORT).show()
        } else {
            Snackbar.make(rvJobs, "You need to Sign in First!", Snackbar.LENGTH_LONG).setAction("Sign In", object : View.OnClickListener {
                override fun onClick(v: View?) {
                    signInWithGoogle()
                }
            }).show()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            processGoogleSignInResult(googleSignInResult)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    private fun processGoogleSignInResult(signInResult: GoogleSignInResult) {
        if (signInResult.isSuccess) {
            // Google Sign-In was successful, authenticate with Firebase
            val account = signInResult.signInAccount
            JobListModel.getInstance().authenticateUserWithGoogleAccount(account!!, object : JobListModel.SignInWithGoogleAccountDelegate {
                override fun onSuccessSignIn(signInAccount: GoogleSignInAccount) {

                }

                override fun onFailureSignIn(msg: String) {

                }
            })
        } else {
            // Google Sign-In failed
            Snackbar.make(rvJobs, "Your Google sign-in failed.", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }
}
