package de.aaronoe.cinematic.auth

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import de.aaronoe.cinematic.CinematicApp

/**
 * Created by private on 7/9/17.
 *
 */
class AuthManager(application : Application) {

    var mSharedPreferences : SharedPreferences
    var mAccessToken: String
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
        mAccessToken = mSharedPreferences.getString(KEY_ACCESS_TOKEN, NOT_SET)
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

        mAccessToken = NOT_SET
        mUsername = NOT_SET
        mName = NOT_SET
        mUserId = ID_NOT_SET
        loggedIn = false
    }

}