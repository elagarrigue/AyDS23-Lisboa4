package ayds.lisboa.songinfo.moredetails.fulllogic.model.repository.local

import ayds.lisboa.songinfo.home.model.entities.Song

interface ArtistLocalStorage {

    fun saveArtist(query: String, )

    fun getArtistInfo(term: String):
}