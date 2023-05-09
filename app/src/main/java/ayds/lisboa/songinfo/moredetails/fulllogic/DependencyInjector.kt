package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.Context
import ayds.lisboa.songinfo.moredetails.fulllogic.data.BiographyRepositoryImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFMAPI
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFMAPIToBiographyResolver
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFMService
import ayds.lisboa.songinfo.moredetails.fulllogic.data.external.LastFMServiceImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.local.sqldb.CursorToArtistMapperImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.data.local.sqldb.LastFMLocalStorage
import ayds.lisboa.songinfo.moredetails.fulllogic.data.local.sqldb.LastFMLocalStorageImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.domain.BiographyRepository
import ayds.lisboa.songinfo.moredetails.fulllogic.presentation.OtherInfoPresenter
import ayds.lisboa.songinfo.moredetails.fulllogic.presentation.OtherInfoPresenterImpl
import ayds.lisboa.songinfo.moredetails.fulllogic.presentation.OtherInfoView
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val URL_BASE_API = "https://ws.audioscrobbler.com/2.0/"
object DependencyInjector {

    private lateinit var otherInfoView: OtherInfoView
    private lateinit var otherInfoPresenter: OtherInfoPresenter
    private lateinit var lastFMLocalStorage: LastFMLocalStorage
    private lateinit var lastFMService: LastFMService
    private lateinit var biographyRepository: BiographyRepository
    fun init(otherInfoView: OtherInfoView){
        setOtherInfoView(otherInfoView)
        createLastFmLocalStorage()
        createLastFmService()
        createBiographyRepository()
        createOtherInfoPresenter()
    }

    private fun setOtherInfoView(otherInfoView: OtherInfoView) {
        this.otherInfoView = otherInfoView
    }

    private fun createLastFmLocalStorage() {
        val cursorToArtistMapper = CursorToArtistMapperImpl()
        lastFMLocalStorage = LastFMLocalStorageImpl(otherInfoView as Context, cursorToArtistMapper)

    }

    private fun createLastFmService() {
        val retrofit = createRetrofit()
        val lastFMAPI = createLastFMAPI(retrofit)
        val lastFMAPIToBiographyResolver = LastFMAPIToBiographyResolver()
        lastFMService = LastFMServiceImpl(lastFMAPIToBiographyResolver, lastFMAPI)
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL_BASE_API)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    private fun createLastFMAPI(retrofit: Retrofit): LastFMAPI{
        return retrofit.create(LastFMAPI::class.java)
    }

    private fun createBiographyRepository() {
        biographyRepository = BiographyRepositoryImpl(lastFMLocalStorage, lastFMService)
    }

    private fun createOtherInfoPresenter() {
        otherInfoPresenter = OtherInfoPresenterImpl(biographyRepository)
    }

    fun getPresenter(): OtherInfoPresenter {
        return otherInfoPresenter
    }


}