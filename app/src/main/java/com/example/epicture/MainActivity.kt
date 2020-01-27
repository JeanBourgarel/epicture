package com.example.epicture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


/**
 * This activity is launched when the app has started, contains
 * a button that redirects to the Imgur connection page.
 */
class MainActivity : AppCompatActivity() {

    /**
     * onCreate function called when the activity starts
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * Redirects to the Imgur connection page in order to retrieve
     * data such as the access token, the client ID and the username
     */
    fun url(view: View) {
        val openURL = Intent(android.content.Intent.ACTION_VIEW)
        openURL.data = Uri.parse("https://api.imgur.com/oauth2/authorize?client_id=70f144ac7fd52f3&response_type=token&state=nothing")
        startActivity(openURL)
    }
}
