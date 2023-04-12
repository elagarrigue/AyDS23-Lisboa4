package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.home.model.entities.Song
import ayds.lisboa.songinfo.home.model.entities.Song.EmptySong
import ayds.lisboa.songinfo.home.model.entities.Song.SpotifySong

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl(private val releaseDateFactory: ReleaseDateFactory) :
    SongDescriptionHelper {

    //private lateinit var releaseDateFactory: ReleaseDateFactory
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong -> buildSpotifySongDescription(song)
            else -> "Song not found"
        }
    }

    private fun buildSpotifySongDescription(song: SpotifySong): String {
        return "${
            "Song: ${song.songName} " +
                    if (song.isLocallyStored) "[*]" else ""
        }\n" +
                "Artist: ${song.artistName}\n" +
                "Album: ${song.albumName}\n" +
                "Release date: ${releaseDateFactory.get(song).formatDate()}"//separarfuncion
    }

}
