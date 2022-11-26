package com.clinicscluster.helper

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.util.Base64
import android.util.Log
import com.clinicscluster.SharedPrefData
import com.clinicscluster.activity.SplashScreen
import com.clinicscluster.model.UserModel
import com.google.gson.Gson
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class ApplicationInit : Application() {


    fun setMyappContext(mContext: Context?) {
        myappContext = mContext
    }

    override fun onCreate() {
        super.onCreate()
        setMyappContext(applicationContext)
        instance = this
    }

    fun printHashKey() {
        try {
            val info = packageManager.getPackageInfo(
                "com.example.finance",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }

    /* fun setConnectivityListener(listener: ConnectivityReceiver.ConnectivityReceiverListener) {
         ConnectivityReceiver.connectivityReceiverListener = listener
     }*/

    companion object {
        val TAG = Application::class.java.simpleName
        var myappContext: Context? = null
        var MY_APP_SHARED_PREFERENCES = "pubhub"
        var mPreferences: SharedPreferences? = null
        lateinit var instance: ApplicationInit

        /*@get:Synchronized
        var instance: MdpPratice? = null
            private set*/
        fun getInstance1(): ApplicationInit {
            return instance
        }

        fun hasNetwork(): Boolean {
            return instance.isNetworkConnected()
        }


        fun getSharedPreferences(context: Context?): SharedPreferences {
            return context!!.getSharedPreferences(MY_APP_SHARED_PREFERENCES, 0)
        }

        fun writeUser(value: String) {
            mPreferences = getSharedPreferences(myappContext)
            val mEditor = mPreferences!!.edit()
            mEditor.putString("user", value)
            mEditor.commit()
        }

        fun setLogIn() {
            mPreferences = getSharedPreferences(myappContext)
            val mEditor = mPreferences!!.edit()
            mEditor.putBoolean("user_login", true)
            mEditor.commit()
        }

        fun readUser(): UserModel? {
            mPreferences = getSharedPreferences(myappContext)
            val obj: UserModel = Gson().fromJson(mPreferences!!.getString("user", ""), UserModel::class.java)
            return obj
        }

        fun isLogIn(): Boolean {
            mPreferences = getSharedPreferences(myappContext)
            return mPreferences!!.getBoolean("user_login", false)
        }

        fun writeIntPreference(key: String?, value: Int) {
            mPreferences = getSharedPreferences(myappContext)
            val mEditor = mPreferences!!.edit()
            mEditor.putInt(key, value)
            mEditor.commit()
        }

        fun writeStringPreference(key: String?, value: String?) {
            mPreferences = getSharedPreferences(myappContext)
            val mEditor = mPreferences!!.edit()
            mEditor.putString(key, value)
            mEditor.commit()
        }

        fun ReadStringPreferences(key: String?): String? {
            mPreferences = getSharedPreferences(myappContext)
            return mPreferences!!.getString(key, "")
        }

        fun ReadIntPreferences(key: String?): Int {
            mPreferences = getSharedPreferences(myappContext)
            return mPreferences!!.getInt(key, 0)
        }

        fun logout(confirm: Boolean) {
            if (!confirm) return
            writeIntPreference(SharedPrefData.PREF_IsLogin, 0)
            clearPrefrences()
        }

        fun clearPrefrences() {
            mPreferences = getSharedPreferences(myappContext)
            val mEditor = mPreferences!!.edit()
            mEditor.clear()
            mEditor.commit()
            val intent = Intent(myappContext, SplashScreen::class.java)
            intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            myappContext!!.startActivity(intent)
        }
    }

    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetwork = cm?.activeNetworkInfo
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting
    }
}