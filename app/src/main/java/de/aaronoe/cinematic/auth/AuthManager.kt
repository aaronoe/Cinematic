package de.aaronoe.cinematic.auth

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import de.aaronoe.cinematic.CinematicApp
import de.aaronoe.cinematic.model.remote.ApiInterface
import de.aaronoe.cinematic.model.user.User
import javax.inject.Inject

/**
 * Created by private on 7/9/17.
 *
 */
public class AuthManager(application : Application) {

    @Inject
    lateinit var apiService : ApiInterface

    var mSharedPreferences : SharedPreferences
    var sessionId: String
    var mUsername : String
    var mName : String
    var mUserId : Int
    var loggedIn : Boolean

    companion object {
        val PREFERENCE_NAME = "cinematic_auth_manager"
        val KEY_ACCESS_TOKEN = "key_session"
        val KEY_USERNAME = "key_username"
        val KEY_NAME = "key_name"
        val KEY_USER_ID = "key_user_id"
        val KEY_LOGGED_IN = "key_logged_in"
        val NOT_SET = "not_set"
        val ID_NOT_SET = -1
    }

    init {
        mSharedPreferences = application.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        sessionId = mSharedPreferences.getString(KEY_ACCESS_TOKEN, NOT_SET)
        mUsername = mSharedPreferences.getString(KEY_USERNAME, NOT_SET)
        mName = mSharedPreferences.getString(KEY_NAME, NOT_SET)
        mUserId = mSharedPreferences.getInt(KEY_USER_ID, ID_NOT_SET)
        loggedIn = mSharedPreferences.getBoolean(KEY_LOGGED_IN, false)
        (application as CinematicApp).netComponent.inject(this)
    }

    fun logout() {
        mSharedPreferences.edit()
                .putString(KEY_ACCESS_TOKEN, NOT_SET)
                .putString(KEY_USERNAME, NOT_SET)
                .putString(KEY_NAME, NOT_SET)
                .putInt(KEY_USER_ID, ID_NOT_SET)
                .putBoolean(KEY_LOGGED_IN, false)
                .apply()

        sessionId = NOT_SET
        mUsername = NOT_SET
        mName = NOT_SET
        mUserId = ID_NOT_SET
        loggedIn = false
    }

    fun login(sessionId: String, user: User) {
        loggedIn = true
        mUserId = user.id
        mName = user.name
        mUsername = user.username
        this.sessionId = sessionId

        mSharedPreferences.edit()
                .putString(KEY_ACCESS_TOKEN, sessionId)
                .putString(KEY_USERNAME, mUsername)
                .putString(KEY_NAME, mName)
                .putInt(KEY_USER_ID, mUserId)
                .putBoolean(KEY_LOGGED_IN, true)
                .apply()
    }

}