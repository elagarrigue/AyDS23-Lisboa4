package ayds.lisboa.songinfo.moredetails.presentation

import ayds.lisboa.songinfo.moredetails.domain.entities.Source

interface SourceEnumHelper{
    fun sourceEnumToString(source: Source): String
}
internal class SourceEnumHelperImpl: SourceEnumHelper {

    override fun sourceEnumToString(source: Source): String {
        return when(source){
            Source.LastFm -> "Last FM"
            Source.NewYorkTimes -> "New York Times"
            Source.Wikipedia -> "Wikipedia"
        }
    }

}