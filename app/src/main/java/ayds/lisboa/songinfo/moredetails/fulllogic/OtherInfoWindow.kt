package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.lisboa.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.*

class OtherInfoWindow : AppCompatActivity() {
    private lateinit var textPane: TextView
    private lateinit var dataBase: DataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()
        initTextView()
        initDatabase()
        getArtistInfo()
    }

    private fun setContentView(){
        setContentView(R.layout.activity_other_info)
    }
    private fun initTextView(){
        textPane = findViewById(R.id.textPane)
    }
    private fun initDatabase(){
        dataBase = DataBase(this)
    }

    private fun getArtistInfo() {
        var artistName = getArtistName()
        val retrofit = newRetrofit()
        val lastFMAPI = retrofit.create(LastFMAPI::class.java)
        Log.e("TAG", "artistName $artistName")
        Thread {
            var text = DataBase.getInfo(dataBase, artistName)
            if (text != null) {
                text = "[*]$text"
            } else {
                val callResponse: Response<String>
                try {
                    callResponse = lastFMAPI.getArtistInfo(artistName).execute()
                    Log.e("TAG", "JSON " + callResponse.body())
                    val gson = Gson()
                    val jobj = gson.fromJson(callResponse.body(), JsonObject::class.java)
                    val artist = jobj["artist"].asJsonObject
                    val bio = artist["bio"].asJsonObject
                    val extract = bio["content"]
                    val url = artist["url"]
                    if (extract == null) {
                        text = "No Results"
                    } else {
                        text = extract.asString.replace("\\n", "\n")
                        text = textToHtml(text, artistName)

                        DataBase.saveArtist(dataBase, artistName, text)
                    }
                    val urlString = url.asString
                    findViewById<View>(R.id.openUrlButton).setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(urlString)
                        startActivity(intent)
                    }
                } catch (e1: IOException) {
                    Log.e("TAG", "Error $e1")
                    e1.printStackTrace()
                }
            }
            val imageUrl =
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
            Log.e("TAG", "Get Image from $imageUrl")
            val finalText = text
            runOnUiThread {
                Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
                textPane!!.text = Html.fromHtml(finalText)
            }
        }.start()
    }

    private fun getArtistName(): String? {
        return intent.getStringExtra("artistName")
    }

    private fun newRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
        fun textToHtml(text: String, term: String?): String {
            val builder = StringBuilder()
            builder.append("<html><div width=400>")
            builder.append("<font face=\"arial\">")
            val textWithBold = text
                .replace("'", " ")
                .replace("\n", "<br>")
                .replace(
                    "(?i)$term".toRegex(),
                    "<b>" + term!!.uppercase(Locale.getDefault()) + "</b>"
                )
            builder.append(textWithBold)
            builder.append("</font></div></html>")
            return builder.toString()
        }
    }
}