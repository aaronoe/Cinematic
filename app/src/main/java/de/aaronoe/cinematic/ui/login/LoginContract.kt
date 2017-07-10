package de.aaronoe.cinematic.ui.login

/**
 * Created by private on 7/9/17.
 *
 */
class LoginContract {

    interface View {
        fun showMessage(message : String)
    }

    interface Presenter {
        fun getRequestToken()
        fun getAccessToken()
    }

}