package ayds.lisboa.songinfo.moredetails.dependencyInjector

import android.content.Context
import ayds.NY1.NewYorkTimes.external.DependenciesInjector
import ayds.NY1.NewYorkTimes.external.NYTArtistInfoService
import ayds.lisboa.songinfo.moredetails.data.BiographyRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.external.Broker
import ayds.lisboa.songinfo.moredetails.data.external.BrokerImp
import ayds.lisboa.songinfo.moredetails.data.external.proxys.LastFmProxy
import ayds.lisboa.songinfo.moredetails.data.external.proxys.NewYorkTimesProxy
import ayds.lisboa.songinfo.moredetails.data.external.proxys.Proxy
import ayds.lisboa.songinfo.moredetails.data.external.proxys.WikipediaProxy
import ayds.lisboa.songinfo.moredetails.data.local.sqldb.CursorToCardMapperImpl
import ayds.lisboa.songinfo.moredetails.data.local.sqldb.LocalStorage
import ayds.lisboa.songinfo.moredetails.data.local.sqldb.LocalStorageImpl
import ayds.lisboa.songinfo.moredetails.domain.repository.BiographyRepository
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
    private lateinit var proxyCollection: MutableCollection<Proxy>
    private lateinit var broker: Broker
    private lateinit var biographyRepository: BiographyRepository
    private lateinit var otherInfoHtmlHelper: OtherInfoHtmlHelper
    fun init(otherInfoView: OtherInfoView){
        setOtherInfoView(otherInfoView)
        createLastFmLocalStorage()
        getExternalServices()
        createProxyCollection()
        createBroker()
        createBiographyRepository()
        createOtherInfoPresenter()
    }

    private fun setOtherInfoView(otherInfoView: OtherInfoView) {
        this.otherInfoView = otherInfoView
    }

    private fun createLastFmLocalStorage() {
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
        proxyCollection = mutableListOf()

        val lastFmProxy: Proxy = LastFmProxy(lastFmService)
        proxyCollection.add(lastFmProxy)

        val wikipediaProxy: Proxy = WikipediaProxy(wikipediaService)
        proxyCollection.add(wikipediaProxy)

        val newYorkTimesProxy: Proxy = NewYorkTimesProxy(newYorkTimesService)
        proxyCollection.add(newYorkTimesProxy)
    }

    private fun createBroker() {
        broker = BrokerImp(proxyCollection)
    }

    private fun createBiographyRepository() {
        biographyRepository = BiographyRepositoryImpl(localStorage, broker)
    }

    private fun createOtherInfoPresenter() {
        otherInfoHtmlHelper = OtherInfoHtmlHelperImpl()
        otherInfoPresenter = OtherInfoPresenterImpl(biographyRepository, otherInfoHtmlHelper)
    }

    fun getPresenter(): OtherInfoPresenter {
        return otherInfoPresenter
    }


}