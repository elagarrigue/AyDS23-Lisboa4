package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.lisboa.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

class OtherInfoWindow : AppCompatActivity() {
    private lateinit var textPane: TextView
    private lateinit var dataBase: DataBase
    private lateinit var artistName: String
    private lateinit var retrofit: Retrofit
    private lateinit var lastFMAPI: LastFMAPI
    private lateinit var artistInfo: String
    private lateinit var artistUrl: String
    private lateinit var imageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()
        initTextView()
        initDatabase()
        getArtistInfo()
    }

    private fun setContentView() {
        setContentView(R.layout.activity_other_info)
    }

    private fun initTextView() {
        textPane = findViewById(R.id.textPane)
    }

    private fun initDatabase() {
        dataBase = DataBase(this)
    }

    private fun getArtistInfo() {
        getArtistName()
        createRetrofit()
        createLastFMAPI()
        createThread()
    }

    private fun getArtistName() {
        artistName = intent.getStringExtra(ARTIST_NAME_EXTRA).toString()
    }

    private fun createRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    private fun createLastFMAPI() {
        lastFMAPI = retrofit.create(LastFMAPI::class.java)
    }

    private fun createThread() {
        Thread {
            obtainArtistInfo()
            updateView()
        }.start()
    }

    private fun obtainArtistInfo() {
        getArtistInfoFromDataBase()
        if (artistInfo.isEmpty()) {
            getArtistInfoFromLastFMAPI()
            setUrlButton()
        }
    }

    private fun getArtistInfoFromDataBase() {
        artistInfo = dataBase.getInfo(artistName) ?: ""
    }

    private fun getArtistInfoFromLastFMAPI() { //mirar
        val callLastAPIResponse = lastFMAPI.getArtistInfo(artistName).execute()
        val gson = Gson()
        val jobj = gson.fromJson(callLastAPIResponse.body(), JsonObject::class.java)
        val artist = jobj["artist"].asJsonObject
        val bio = artist["bio"].asJsonObject
        val bioContent = bio["content"]
        setArtistUrl(artist)
        if (bioContent != null) {
            artistInfo = bioContent.asString.replace("\\n", "\n")
            artistInfo = textToHtml(artistInfo, artistName)
            saveArtistInfoInDataBase()
        }
    }

    private fun setArtistUrl(artist: JsonObject) {
        artistUrl = artist["url"].asString
    }

    private fun textToHtml(text: String, term: String?): String { //mirar
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

    private fun saveArtistInfoInDataBase() {
        dataBase.saveArtist(artistName, artistInfo)
    }

    private fun setUrlButton() { //mirar
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(artistUrl)
            startActivity(intent)
        }
    }

    private fun updateView() {
        setImageUrl()
        runUiThread()
    }
    private fun setImageUrl(){
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    }
    private fun runUiThread(){
        runOnUiThread {
            loadLastFMLogo()
            loadArtistInfo()
        }
    }
    private fun loadLastFMLogo(){
        Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
    }
    private fun loadArtistInfo(){
        textPane.text = Html.fromHtml(artistInfo)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

}