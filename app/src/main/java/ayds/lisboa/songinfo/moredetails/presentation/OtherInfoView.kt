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
import ayds.lisboa.songinfo.moredetails.domain.entities.Source
import com.squareup.picasso.Picasso

class OtherInfoView : AppCompatActivity() {
    private var uiState = OtherInfoUiState(mutableListOf<CardUiState>())
    private lateinit var otherInfoPresenter: OtherInfoPresenter

    private lateinit var artistName: String

    private lateinit var artistInfoTextView1: TextView
    private lateinit var openUrlButtonView1: View
    private lateinit var imageView1: ImageView

    private lateinit var artistInfoTextView2: TextView
    private lateinit var openUrlButtonView2: View
    private lateinit var imageView2: ImageView

    private lateinit var artistInfoTextView3: TextView
    private lateinit var openUrlButtonView3: View
    private lateinit var imageView3: ImageView

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
        imageView1 = findViewById(R.id.imageView1)
        artistInfoTextView1 = findViewById(R.id.artistInfoTextView1)
        openUrlButtonView1 = findViewById(R.id.openUrlButtonView1)
    }

    private fun initWikipediaViews(){
        imageView2 = findViewById(R.id.imageView2)
        artistInfoTextView2 = findViewById(R.id.artistInfoTextView2)
        openUrlButtonView2 = findViewById(R.id.openUrlButtonView2)
    }

    private fun initNYTViews(){
        imageView3 = findViewById(R.id.imageView3)
        artistInfoTextView3 = findViewById(R.id.artistInfoTextView3)
        openUrlButtonView3 = findViewById(R.id.openUrlButtonView3)
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
        var cont: Int = 1
        if(this.uiState.cardsUiState.isEmpty()){
            showNoResult()
        }else {
            this.uiState.cardsUiState.forEach {
                setUrlButton(it.artistUrl, cont)
                updateViewInfo(it.artistInfoHTML, cont, it.imageUrl)
                cont++
            }
        }
    }

    private fun showNoResult() {
        artistInfoTextView1.text = Html.fromHtml("<h1>No Results</h1>")
    }

    private fun setUrlButton(artistUrl: String, cont: Int) {
        when(cont){
            1 -> openUrlButtonView1.setOnClickListener {
                            startActivityOnClick(artistUrl)
                        }
            2 -> openUrlButtonView2.setOnClickListener {
                             startActivityOnClick(artistUrl)
                        }
            3 -> openUrlButtonView3.setOnClickListener {
                                startActivityOnClick(artistUrl)
                            }
        }
    }

    private fun updateViewInfo(artistInfo: String, cont: Int, logo: String) {
        when(cont){
            1 -> runOnUiThread {
                            loadLogo(logo, cont)
                            artistInfoTextView1.text = Html.fromHtml(artistInfo)
                        }
            2 -> runOnUiThread {
                                loadLogo(logo, cont)
                                artistInfoTextView2.text = Html.fromHtml(artistInfo)
                            }
            3 -> runOnUiThread {
                                    loadLogo(logo, cont)
                                    artistInfoTextView3.text = Html.fromHtml(artistInfo)
                                }

        }
    }

    private fun loadLogo(logo: String, cont: Int) {
        when(cont){
            1 -> Picasso.get().load(logo).into(imageView1)

            2 -> Picasso.get().load(logo).into(imageView2)

            3 -> Picasso.get().load(logo).into(imageView3)
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