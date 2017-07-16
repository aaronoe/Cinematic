package de.aaronoe.cinematic.ui.login

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import butterknife.ButterKnife
import de.aaronoe.cinematic.CinematicApp
import de.aaronoe.cinematic.R
import de.aaronoe.cinematic.auth.AuthManager
import de.aaronoe.cinematic.model.remote.ApiInterface
import de.aaronoe.cinematic.ui.NavigationActivity
import de.aaronoe.cinematic.util.DisplayUtils
import de.aaronoe.cinematic.util.bindView
import org.jetbrains.anko.toast
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), LoginContract.View {

    val loginButton: Button by bindView(R.id.login_button)
    val registerButton: Button by bindView(R.id.register_button)
    val loginContainer : ConstraintLayout by bindView(R.id.login_container)
    val loginImageView : ImageView by bindView(R.id.login_background_iv)


    lateinit var authManager : AuthManager
    @Inject
    lateinit var apiService : ApiInterface
    lateinit var presenter : LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        (application as CinematicApp).netComponent.inject(this)
        authManager = (application as CinematicApp).mAuthManager
        ButterKnife.bind(this)

        DisplayUtils.startSaturationAnimation(this, loginImageView, 3000)
        presenter = LoginPresenter(this, this, apiService)
        initViews()
    }

    fun initViews() {
        if (authManager.loggedIn) {
            loginButton.text = getString(R.string.logout)
            registerButton.text = getString(R.string.my_profile)

            loginButton.setOnClickListener {
                authManager.logout()
                finishLogin()
            }

            registerButton.setOnClickListener { toast("Coming soon") }

        } else {
            loginButton.setOnClickListener { presenter.getRequestToken() }
            registerButton.visibility = View.INVISIBLE
        }
    }

    override fun showMessage(message: String) {
        val snackbar = Snackbar.make(loginContainer, message, Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE)

        (snackbar.view.findViewById(android.support.design.R.id.snackbar_text) as TextView).setTextColor(Color.WHITE)
        snackbar.show()
    }


    public override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null
                && intent.data != null
                && !TextUtils.isEmpty(intent.data.authority)
                && CinematicApp.TMDB_LOGIN_CALLBACK == intent.data.authority) {
            loginButton.visibility = View.INVISIBLE
            registerButton.visibility = View.INVISIBLE
            showMessage("Success")
            presenter.getAccessToken()

        }
    }

    override fun finishLogin() {
        val intent = Intent(this, NavigationActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        finish()
        startActivity(intent)
    }

}
