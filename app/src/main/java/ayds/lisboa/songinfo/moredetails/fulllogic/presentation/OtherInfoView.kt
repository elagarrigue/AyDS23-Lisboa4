package ayds.lisboa.songinfo.moredetails.fulllogic.presentation

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.fulllogic.Biography
import ayds.lisboa.songinfo.moredetails.fulllogic.OtherInfoWindow
import ayds.lisboa.songinfo.moredetails.fulllogic.PREFIX
import ayds.lisboa.songinfo.moredetails.fulllogic.URL_LAST_FM_IMAGE
import com.squareup.picasso.Picasso

interface OtherInfoView {
    fun updateArtistInfoView()
}

class OtherInfoViewImpl : AppCompatActivity(), OtherInfoView {
    private lateinit var artistInfoTextView: TextView
    private lateinit var artistName: String
    private lateinit var openUrlButtonView: View
    private lateinit var lastFmImageView: ImageView
    private val otherInfoPresenter: Presenter = Presenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()
        initViews()
        updateArtistInfoView()
    }

    private fun setContentView() {
        setContentView(R.layout.activity_other_info)
    }

    private fun initViews() {
        lastFmImageView = findViewById(R.id.imageView)
        artistInfoTextView = findViewById(R.id.artistInfoTextView)
        openUrlButtonView = findViewById(R.id.openUrlButton)
    }

    private fun updateArtistInfoView() {
        updateArtistName()
        searchArtistInfo()
    }

    private fun updateArtistName() {
        artistName = intent.getStringExtra(OtherInfoWindow.ARTIST_NAME_EXTRA).toString()
    }

    private fun searchArtistInfo() {
        Thread {
            updateArtistInfo()
        }.start()
    }

    private fun updateArtistInfo() {
        val biography = getArtistBiography()
        when (biography.isLocallyStored) {
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

    private fun updateViewInfo(artistBiography: Biography) {
        runOnUiThread {
            loadLastFMLogo()
            val formattedArtistInfo = getFormattedArtistInfo(artistBiography)
            loadArtistInfo(formattedArtistInfo)
        }
    }

    private fun loadLastFMLogo() {
        Picasso.get().load(URL_LAST_FM_IMAGE).into(lastFmImageView)
    }


    private fun getFormattedArtistInfo(artistBiography: Biography): String {
        val prefix =
            if (artistBiography.isLocallyStored)
                PREFIX
            else
                ayds.lisboa.songinfo.moredetails.fulllogic.DEFAULT_STRING
        return "$prefix${artistBiography.artistInfo}"
    }

    private fun loadArtistInfo(artistInfo: String) {
        artistInfoTextView.text = Html.fromHtml(artistInfo)
    }

    private fun loadArtistInfo(artistInfo: String) {
        artistInfoTextView.text = Html.fromHtml(artistInfo)
    }

}