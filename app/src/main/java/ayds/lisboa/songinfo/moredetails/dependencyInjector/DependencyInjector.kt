package ayds.lisboa.songinfo.moredetails.dependencyInjector

import android.content.Context
import ayds.NY1.NewYorkTimes.external.DependenciesInjector
import ayds.NY1.NewYorkTimes.external.NYTArtistInfoService
import ayds.lisboa.songinfo.moredetails.data.CardRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.external.CardsBroker
import ayds.lisboa.songinfo.moredetails.data.external.CardsBrokerImp
import ayds.lisboa.songinfo.moredetails.data.external.proxys.LastFmProxy
import ayds.lisboa.songinfo.moredetails.data.external.proxys.NewYorkTimesProxy
import ayds.lisboa.songinfo.moredetails.data.external.proxys.CardProxy
import ayds.lisboa.songinfo.moredetails.data.external.proxys.WikipediaProxy
import ayds.lisboa.songinfo.moredetails.data.local.sqldb.CursorToCardMapperImpl
import ayds.lisboa.songinfo.moredetails.data.local.sqldb.LocalStorage
import ayds.lisboa.songinfo.moredetails.data.local.sqldb.LocalStorageImpl
import ayds.lisboa.songinfo.moredetails.domain.repository.CardRepository
import ayds.lisboa.songinfo.moredetails.presentation.*
import lisboa4LastFM.*
import wikipedia.external.external.WikipediaArticleService
import wikipedia.external.external.WikipediaInjector

object DependencyInjector {

    private lateinit var otherInfoView: OtherInfoView
    private lateinit var otherInfoPresenter: OtherInfoPresenter
    private lateinit var localStorage: LocalStorage
    private lateinit var lastFmService: LastFMService
    private lateinit var wikipediaService: WikipediaArticleService
    private lateinit var newYorkTimesService: NYTArtistInfoService
    private lateinit var proxyCollection: MutableList<CardProxy>
    private lateinit var cardsBroker: CardsBroker
    private lateinit var biographyRepository: CardRepository
    private lateinit var otherInfoHtmlHelper: OtherInfoHtmlHelper
    private lateinit var otherInfoSourceEnumHelper: SourceEnumHelper
    fun init(otherInfoView: OtherInfoView){
        setOtherInfoView(otherInfoView)
        createLocalStorage()
        getExternalServices()
        createProxyCollection()
        createBroker()
        createBiographyRepository()
        createOtherInfoPresenter()
    }

    private fun setOtherInfoView(otherInfoView: OtherInfoView) {
        this.otherInfoView = otherInfoView
    }

    private fun createLocalStorage() {
        val cursorToArtistMapper = CursorToCardMapperImpl()
        localStorage = LocalStorageImpl(otherInfoView as Context, cursorToArtistMapper)
    }

    private fun getExternalServices() {
        lastFmService = getLastFMService()
        wikipediaService = getWikipediaService()
        newYorkTimesService = getNewYorkTimesService()
    }

    private fun getLastFMService(): LastFMService {
        return LastFMInjector.getLastFmService()
    }

    private fun getWikipediaService(): WikipediaArticleService {
        return WikipediaInjector.generateWikipediaService()
    }

    private fun getNewYorkTimesService(): NYTArtistInfoService {
        return DependenciesInjector.init()
    }

    private fun createProxyCollection() {
        proxyCollection = mutableListOf<CardProxy>()

        val lastFmProxy: CardProxy = LastFmProxy(lastFmService)
        proxyCollection.add(lastFmProxy)

        val wikipediaProxy: CardProxy = WikipediaProxy(wikipediaService)
        proxyCollection.add(wikipediaProxy)

        val newYorkTimesProxy: CardProxy = NewYorkTimesProxy(newYorkTimesService)
        proxyCollection.add(newYorkTimesProxy)
    }

    private fun createBroker() {
        cardsBroker = CardsBrokerImp(proxyCollection)
    }

    private fun createBiographyRepository() {
        biographyRepository = CardRepositoryImpl(localStorage, cardsBroker)
    }

    private fun createOtherInfoPresenter() {
        otherInfoHtmlHelper = OtherInfoHtmlHelperImpl()
        otherInfoSourceEnumHelper = SourceEnumHelperImpl()
        otherInfoPresenter = OtherInfoPresenterImpl(biographyRepository, otherInfoHtmlHelper, otherInfoSourceEnumHelper)
    }

    fun getPresenter(): OtherInfoPresenter {
        return otherInfoPresenter
    }


}