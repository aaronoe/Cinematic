package de.aaronoe.cinematic.ui.login

/**
 * Created by private on 7/9/17.
 *
 */
public class LoginContract {

    interface View {
        fun showMessage(message : String)
        fun finishLogin()
    }

    interface Presenter {
        fun getRequestToken()
        fun getAccessToken()
    }

}