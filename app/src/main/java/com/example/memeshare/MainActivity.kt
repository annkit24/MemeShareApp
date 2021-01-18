package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var currentMemeUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            loadMemer()
    }
    private fun loadMemer(){
        nextButton.isEnabled = false
        shareButton.isEnabled = false
        progressBar.visibility = View.VISIBLE
        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                currentMemeUrl = response.getString("url")

                Glide.with(this).load(currentMemeUrl).listener(object : RequestListener<Drawable>{
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        nextButton.isEnabled = true
                        shareButton.isEnabled = true
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }
                }).into(memeImageView)
            },
            Response.ErrorListener {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            })

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun shareMeme(view: View) {
        var j =Intent(Intent.ACTION_SEND)
        j.type = "text/plain"
        j.putExtra(Intent.EXTRA_TEXT,"Hello,Checkout this Meme $currentMemeUrl")
        startActivity(Intent.createChooser(j,"Share this meme with"))

    }
    fun showNextMeme(view: View) {
        loadMemer()
    }


}