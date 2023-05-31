package ayds.lisboa.songinfo.moredetails.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.dependencyInjector.DependencyInjector
import com.squareup.picasso.Picasso

class OtherInfoView : AppCompatActivity() {
    private var uiState = OtherInfoUiState(mutableListOf<CardUiState>())
    private lateinit var otherInfoPresenter: OtherInfoPresenter

    private lateinit var artistName: String

    private lateinit var artistInfoTextViewLastFm: TextView
    private lateinit var openUrlButtonViewLastFm: View
    private lateinit var imageViewLastFm: ImageView

    private lateinit var artistInfoTextViewNYT: TextView
    private lateinit var openUrlButtonViewNYT: View
    private lateinit var imageViewNYT: ImageView

    private lateinit var artistInfoTextViewWikipedia: TextView
    private lateinit var openUrlButtonViewWikipedia: View
    private lateinit var imageViewWikipedia: ImageView

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
        initLastFmViews()
        initWikipediaViews()
        initNYTViews()
    }

    private fun initLastFmViews(){
        imageViewLastFm = findViewById(R.id.imageViewLastFm)
        artistInfoTextViewLastFm = findViewById(R.id.artistInfoTextViewLastFm)
        openUrlButtonViewLastFm = findViewById(R.id.openUrlButtonViewLastFm)
    }

    private fun initWikipediaViews(){
        imageViewWikipedia = findViewById(R.id.imageViewWikipedia)
        artistInfoTextViewWikipedia = findViewById(R.id.artistInfoTextViewWikipedia)
        openUrlButtonViewWikipedia = findViewById(R.id.openUrlButtonViewWikipedia)
    }

    private fun initNYTViews(){
        imageViewNYT = findViewById(R.id.imageViewNYT)
        artistInfoTextViewNYT = findViewById(R.id.artistInfoTextViewNYT)
        openUrlButtonViewNYT = findViewById(R.id.openUrlButtonViewNYT)
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
        this.uiState.cardsUiState.forEach {
            setUrlButton(it.artistUrl, it.source)
            updateViewInfo(it.artistInfoHTML, it.source, it.imageUrl)
        }
    }

    private fun setUrlButton(artistUrl: String, source: String) {
        when(source){
            "LastFm" -> openUrlButtonViewLastFm.setOnClickListener {
                            startActivityOnClick(artistUrl)
                        }
            "Wikipedia" -> openUrlButtonViewWikipedia.setOnClickListener {
                             startActivityOnClick(artistUrl)
                        }
            "NewYorkTimes" -> openUrlButtonViewNYT.setOnClickListener {
                                startActivityOnClick(artistUrl)
                            }
        }
    }

    private fun updateViewInfo(artistInfo: String, source: String, logo: String) {
        when(source){
            "LastFm" -> runOnUiThread {
                            loadLogo(logo, source)
                            artistInfoTextViewLastFm.text = Html.fromHtml(artistInfo)
                        }
            "Wikipedia" -> runOnUiThread {
                                loadLogo(logo, source)
                                artistInfoTextViewWikipedia.text = Html.fromHtml(artistInfo)
                            }
            "NewYorkTimes" -> runOnUiThread {
                                    loadLogo(logo, source)
                                    artistInfoTextViewNYT.text = Html.fromHtml(artistInfo)
                                }

        }
    }

    private fun loadLogo(logo: String, source: String) {
        when(source){
            "LastFm" -> Picasso.get().load(logo).into(imageViewLastFm)

            "Wikipedia" -> Picasso.get().load(logo).into(imageViewWikipedia)

            "NewYorkTimes" -> Picasso.get().load(logo).into(imageViewNYT)
        }
    }

    private fun updateArtistInfoView() {
        setArtistName()
        updateArtistInfo()
    }

    private fun setArtistName() {
        artistName = intent.getStringExtra(ARTIST_NAME_EXTRA).toString()
    }

    private fun updateArtistInfo() {
        otherInfoPresenter.searchArtistBiography(artistName)
    }

    private fun startActivityOnClick(artistUrl: String){
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(artistUrl)
        startActivity(intent)
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}