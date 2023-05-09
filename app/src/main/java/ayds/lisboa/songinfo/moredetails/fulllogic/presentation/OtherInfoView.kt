package ayds.lisboa.songinfo.moredetails.fulllogic.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.fulllogic.DependencyInjector
import ayds.lisboa.songinfo.moredetails.fulllogic.presentation.OtherInfoUiState.Companion.URL_LAST_FM_IMAGE
import com.squareup.picasso.Picasso

interface OtherInfoView {
    var uiState: OtherInfoUiState
    fun updateViewInfo(artistInfo: String)
}

class OtherInfoViewImpl : AppCompatActivity(), OtherInfoView {
    override var uiState = OtherInfoUiState()

    private lateinit var artistInfoTextView: TextView
    private lateinit var artistName: String
    private lateinit var openUrlButtonView: View
    private lateinit var lastFmImageView: ImageView
    private lateinit var otherInfoPresenter: OtherInfoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()
        initViews()
        initDependencyInjector()
        initObservers()
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

    private fun initDependencyInjector(){
        DependencyInjector.init(this)
        otherInfoPresenter = DependencyInjector.getPresenter()
    }


    private fun initObservers() {
        otherInfoPresenter.uiStateObservable
            .subscribe { value -> updateUiState(value) }
    }

    private fun updateUiState(uiState: OtherInfoUiState){
        this.uiState = uiState
        setUrlButton(this.uiState.artistUrl)
        updateViewInfo(this.uiState.artistInfoHTML)
    }

    private fun updateArtistInfoView() {
        setArtistName()
        updateArtistInfo()
    }

    private fun setArtistName() {
        artistName = intent.getStringExtra(ARTIST_NAME_EXTRA).toString()
    }

    private fun updateArtistInfo() {
        Thread {
            searchArtistInfo()
        }.start()
    }
    private fun searchArtistInfo() {
        otherInfoPresenter.searchArtistBiography(artistName)
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

    override fun updateViewInfo(artistInfo: String) {
        runOnUiThread {
            loadLastFMLogo()
            artistInfoTextView.text = Html.fromHtml(artistInfo)
        }
    }

    private fun loadLastFMLogo() {
        Picasso.get().load(URL_LAST_FM_IMAGE).into(lastFmImageView)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}