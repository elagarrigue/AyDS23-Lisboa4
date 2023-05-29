package ayds.lisboa.songinfo.moredetails.data.external.proxys

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface Proxy {
    fun getArtistBiography(artistName: String): Card?
}