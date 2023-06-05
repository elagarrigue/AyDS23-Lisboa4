package ayds.lisboa.songinfo.moredetails.data.external.proxys

import ayds.lisboa.songinfo.moredetails.domain.entities.Card

interface CardProxy {
    fun getCard(artistName: String): Card?
}