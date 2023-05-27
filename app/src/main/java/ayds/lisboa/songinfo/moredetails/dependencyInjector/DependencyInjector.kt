package ayds.lisboa.songinfo.moredetails.dependencyInjector

import android.content.Context
import ayds.lisboa.songinfo.moredetails.data.BiographyRepositoryImpl
import ayds.lisboa.songinfo.moredetails.data.local.sqldb.CursorToCardMapperImpl
import ayds.lisboa.songinfo.moredetails.data.local.sqldb.LastFMLocalStorage
import ayds.lisboa.songinfo.moredetails.data.local.sqldb.LastFMLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.domain.repository.BiographyRepository
import ayds.lisboa.songinfo.moredetails.presentation.*
import lisboa4LastFM.*

object DependencyInjector {

    private lateinit var otherInfoView: OtherInfoView
    private lateinit var otherInfoPresenter: OtherInfoPresenter
    private lateinit var lastFMLocalStorage: LastFMLocalStorage
    private lateinit var lastFMService: LastFMService
    private lateinit var biographyRepository: BiographyRepository
    private lateinit var otherInfoHtmlHelper: OtherInfoHtmlHelper
    fun init(otherInfoView: OtherInfoView){
        setOtherInfoView(otherInfoView)
        createLastFmLocalStorage()
        getLastFMService()
        createBiographyRepository()
        createOtherInfoPresenter()
    }

    private fun setOtherInfoView(otherInfoView: OtherInfoView) {
        this.otherInfoView = otherInfoView
    }

    private fun createLastFmLocalStorage() {
        val cursorToArtistMapper = CursorToCardMapperImpl()
        lastFMLocalStorage = LastFMLocalStorageImpl(otherInfoView as Context, cursorToArtistMapper)
    }

    private fun getLastFMService(){
        lastFMService = LastFMInjector.getLastFmService()
    }

    private fun createBiographyRepository() {
        biographyRepository = BiographyRepositoryImpl(lastFMLocalStorage, lastFMService)
    }

    private fun createOtherInfoPresenter() {
        otherInfoHtmlHelper = OtherInfoHtmlHelperImpl()
        otherInfoPresenter = OtherInfoPresenterImpl(biographyRepository, otherInfoHtmlHelper)
    }

    fun getPresenter(): OtherInfoPresenter {
        return otherInfoPresenter
    }


}