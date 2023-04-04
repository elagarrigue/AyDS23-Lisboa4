package ayds.lisboa.songinfo.home.view

import ayds.lisboa.songinfo.home.model.entities.Song.EmptySong
import ayds.lisboa.songinfo.home.model.entities.Song
import ayds.lisboa.songinfo.home.model.entities.Song.SpotifySong

interface SongDescriptionHelper {
    fun getSongDescriptionText(song: Song = EmptySong): String
}

internal class SongDescriptionHelperImpl : SongDescriptionHelper {
    override fun getSongDescriptionText(song: Song): String {
        return when (song) {
            is SpotifySong ->
                "${
                    "Song: ${song.songName} " +
                            if (song.isLocallyStored) "[*]" else ""
                }\n" +
                        "Artist: ${song.artistName}\n" +
                        "Album: ${song.albumName}\n" +
                        "Release date: ${buildReleaseDate(song)}"
            else -> "Song not found"
        }
    }

    private fun buildReleaseDate(song: SpotifySong): String {
        val releaseDate = song.releaseDate
        val precision = song.releaseDatePrecision
        val splitReleaseDate: List<String> = song.releaseDate.split("-");
        val year = splitReleaseDate.first()
        val month = splitReleaseDate.getOrNull(1)
        val day = splitReleaseDate.getOrNull(2)
        return when (precision) {
            "year" -> year
            "month" -> "${numberToMonthName(month?.toIntOrNull())}, $year"
            "day" -> "${day}/${month}/${year}"
            else -> ""
        }
    }

    private fun numberToMonthName(month: Int?) =
        when (month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "Invalid month"
        }

    fun isLeapYear(year: Int) = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))
}