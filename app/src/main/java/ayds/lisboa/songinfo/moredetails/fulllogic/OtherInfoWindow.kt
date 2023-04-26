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
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

private const val URL_BASE_API = "https://ws.audioscrobbler.com/2.0/"
class OtherInfoWindow : AppCompatActivity() {
    private lateinit var artistInfoTextView: TextView
    private lateinit var dataBase: DataBase
    private lateinit var artistName: String
    private lateinit var retrofit: Retrofit
    private lateinit var lastFMAPI: LastFMAPI
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
        artistInfoTextView = findViewById(R.id.artistInfoTextView)
    }

    private fun initDatabase() {
        dataBase = DataBase(this)
    }

    private fun getArtistInfo() {
        getArtistName()
        createRetrofit()
        createLastFMAPI()
        searchArtistInfo()
    }

    private fun getArtistName() {
        artistName = intent.getStringExtra(ARTIST_NAME_EXTRA).toString()
    }

    private fun createRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl(URL_BASE_API)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    private fun createLastFMAPI() {
        lastFMAPI = retrofit.create(LastFMAPI::class.java)
    }

    private fun searchArtistInfo() {
        Thread {
            updateArtistInfo()
        }.start()
    }

    private fun updateArtistInfo() {
        var artistInfo = getArtistInfoFromDataBase()
        if(!artistInfo.isEmpty()){
            artistInfo = "[*]$artistInfo"
        } else {
            artistInfo = getArtistInfoFromLastFMAPI()
            if(artistInfo.isNotEmpty())
                saveArtistInfoInDataBase(artistInfo)
        }
        updateView(artistInfo)
    }

    private fun getArtistInfoFromLastFMAPI(): String {
        val artist = getArtistFromLastFMAPI()
        val artistInfo = getArtistInfoFromJsonResponse(artist)
        val artistUrl = getArtistUrl(artist)
        setUrlButton(artistUrl)
        return artistInfo
    }

    private fun getArtistUrl(artist: JsonObject): String {
        return artist["url"].asString
    }
    private fun getArtistInfoFromDataBase(): String {
        return dataBase.getArtistInfo(artistName) ?: ""
    }

    private fun getArtistInfoFromJsonResponse(artist: JsonObject): String {
        val bioContent = getBioContent(artist)
        return  if (bioContent != null) {
                    var artistInfo = bioContent.asString.replace("\\n", "\n")
                    textToHtml(artistInfo, artistName)
                } else
                    ""
    }

    private fun getArtistFromLastFMAPI(): JsonObject {
        val callLastAPIResponse = lastFMAPI.getArtistInfo(artistName).execute()
        val gson = Gson()
        val jobj = gson.fromJson(callLastAPIResponse.body(), JsonObject::class.java)
        return jobj["artist"].asJsonObject
    }

    private  fun getBioContent(artist: JsonObject): JsonElement{
        val bio = artist["bio"].asJsonObject
        return bio["content"]
    }

    private fun textToHtml(text: String, term: String?): String {
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

    private fun saveArtistInfoInDataBase(artistInfo: String) {
        dataBase.saveArtist(artistName, artistInfo)
    }

    private fun setUrlButton(artistUrl: String) {
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(artistUrl)
            startActivity(intent)
        }
    }

    private fun updateView(artistInfo: String) {
        setImageUrl()
        runUiThread(artistInfo)
    }
    private fun setImageUrl(){
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
    }
    private fun runUiThread(artistInfo: String){
        runOnUiThread {
            loadLastFMLogo()
            loadArtistInfo(artistInfo)
        }
    }
    private fun loadLastFMLogo(){
        Picasso.get().load(imageUrl).into(findViewById<View>(R.id.imageView) as ImageView)
    }
    private fun loadArtistInfo(artistInfo: String){
        artistInfoTextView.text = Html.fromHtml(artistInfo)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }

}