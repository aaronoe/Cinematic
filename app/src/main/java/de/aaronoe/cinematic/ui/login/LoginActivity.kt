package de.aaronoe.cinematic.ui.login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.widget.Button
import android.widget.ImageView
import butterknife.ButterKnife

import de.aaronoe.cinematic.R
import de.aaronoe.cinematic.util.DisplayUtils
import de.aaronoe.seek.util.bindView

class LoginActivity : AppCompatActivity() {

    val loginButton: Button by bindView(R.id.login_button)
    val registerButton: Button by bindView(R.id.register_button)
    val loginContainer : ConstraintLayout by bindView(R.id.login_container)
    val loginImageView : ImageView by bindView(R.id.login_background_iv)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ButterKnife.bind(this)

        DisplayUtils.startSaturationAnimation(this, loginImageView, 3000)
    }
}
