package com.example.epicture

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import android.widget.LinearLayout
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_second.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import java.io.IOException

/**
 * This activity is launched after the user's connection,
 * it displays the username and the user's uploaded pictures
 */
class SecondActivity : AppCompatActivity() {

    companion object {
        /**
         * Access token, will be used in requests headers
         */
        var access_token = ""
        /**
         * Username, will be used when getting images from the account
         */
        var username = ""
        /**
         * Account ID, will be used as a request parameter
         */
        var account_id = ""
        /**
         * Contains the response of a request
         */
        var resp = ""
        var index = 0
    }

    /**
     * onCreate function called when the activity starts
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        recyclerView_second.layoutManager = LinearLayoutManager(this)
    }

    /**
     * Is called when the user is connected
     */
    override fun onResume() {
        val uri = intent.data

        if (uri !== null) {
            setAccessToken(uri)
            setUsername(uri)
            setAccountId(uri)
            findViewById<TextView>(R.id.username_logged).text = username
            fetchJson()
        }
        super.onResume()
    }

    /**
     * Sets the account ID
     * @param uri the request response
     */
    private fun setAccountId(uri: Uri?) {
        val regex = Regex("(?<=account_id=).*\$")
        val result: MatchResult? = regex.find(uri.toString())
        account_id = result?.value!!
    }

    /**
     * Sets the username
     * @param uri the request response
     */
    private fun setUsername(uri: Uri?) {
        val firstRegex = Regex("(?<=account_username=).*\$")
        val result: MatchResult? = firstRegex.find(uri.toString())
        val secondRegex = Regex(".+?(?=&account_id)")
        val token: MatchResult? = secondRegex.find(result?.value.toString())
        username = token?.value!!
    }

    /**
     * Sets the access token
     * @param uri the request response
     */
    private fun setAccessToken(uri: Uri?) {
        val firstRegex = Regex("(?<=access_token=).*\$")
        val result: MatchResult? = firstRegex.find(uri.toString())
        val secondRegex = Regex(".+?(?=&expires)")
        val token: MatchResult? = secondRegex.find(result?.value.toString())
        access_token = token?.value!!
    }

    /**
     * Displays request response
     */
    fun displayResp() {
        val myToast = Toast.makeText(this, resp, Toast.LENGTH_SHORT)
        myToast.show()
    }

    /**
     * Sends a request to the Imgur API to get the user's uploaded images
     */
    fun fetchJson() {
        val url = "https://api.imgur.com/3/account/".plus(username).plus("/images/")
        val request = okhttp3.Request.Builder().addHeader("Authorization", "Bearer ".plus(
            access_token)).url(url).build()
        val client = OkHttpClient()


        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body()?.string()

                val gson = GsonBuilder().create()

                val accountFeed = gson.fromJson(body, AccountFeed::class.java)

                runOnUiThread {
                    recyclerView_second.adapter = Adapter(accountFeed)
                }
                println(body)
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }
}

class AccountFeed(val data : List<AccountImage>)

class AccountImage(val link : String, val description : String, val views : Int)