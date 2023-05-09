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
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFMAPI
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

private const val URL_BASE_API = "https://ws.audioscrobbler.com/2.0/"
private const val URL_LAST_FM_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
private const val JSON_ARTIST = "artist"
private const val JSON_BIO = "bio"
private const val JSON_CONTENT = "content"
private const val JSON_URL = "url"
private const val PREFIX = "[*]"
private const val NEW_LINE = "\n"
private const val ESCAPED_NEW_LINE = "\\n"
const val DEFAULT_STRING = ""
private const val SIMPLE_QUOTE = "'"
private const val FLAG_INSENSITIVE_UPPER_LOWER_CASE = "(?i)"
private const val HTML_HTML_OPEN = "<html>"
private const val HTML_HTML_CLOSE = "</html>"
private const val HTML_FONT_FACE_ARIAL_OPEN = "<font face=\"arial\">"
private const val HTML_FONT_CLOSE = "</font>"
private const val HTML_DIV_W400_OPEN = "<div width=400>"
private const val HTML_DIV_CLOSE = "</div>"
private const val HTML_B_OPEN = "<b>"
private const val HTML_B_CLOSE = "</b>"
private const val HTML_BR = "<br>"
private const val HTML_SPACE = " "

class OtherInfoWindow : AppCompatActivity() {
    private lateinit var artistInfoTextView: TextView
    private lateinit var dataBase: DataBase
    private lateinit var artistName: String
    private lateinit var retrofit: Retrofit
    private lateinit var lastFMAPI: LastFMAPI
    private lateinit var openUrlButtonView: View
    private lateinit var lastFmImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()
        updateArtistInfoView()
    }


    private fun updateArtistInfoView() {
        updateArtistName()
        searchArtistInfo()
    }

    private fun updateArtistName() {
        artistName = intent.getStringExtra(ARTIST_NAME_EXTRA).toString()
    }


    private fun searchArtistInfo() {
        Thread {
            updateArtistInfo()
        }.start()
    }

    private fun updateArtistInfo() {
        val biography = getArtistBiography()
        when(biography.isLocallyStored) {
            false -> setUrlButton(biography.url)
            else -> {}
        }
        updateViewInfo(biography)
    }

    private fun setUrlButton(artistUrl: String) {
        openUrlButtonView.setOnClickListener {
            startActivityOnClick(artistUrl)
        }
    }

    private fun startActivityOnClick(artistUrl: String){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(artistUrl)
        startActivity(intent)
    }

    private fun updateViewInfo(artistBiography: Biography){
        runOnUiThread {
            loadLastFMLogo()
            val formattedArtistInfo = getFormattedArtistInfo(artistBiography)
            loadArtistInfo(formattedArtistInfo)
        }
    }

    private fun getFormattedArtistInfo(artistBiography: Biography): String {
        val prefix =
            if(artistBiography.isLocallyStored)
                PREFIX
            else
                DEFAULT_STRING
        return "$prefix${artistBiography.artistInfo}"
    }

    private fun loadArtistInfo(artistInfo: String){
        artistInfoTextView.text = Html.fromHtml(artistInfo)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}

class Biography(
    var artistInfo: String,
    var url: String,
    var isLocallyStored: Boolean)
