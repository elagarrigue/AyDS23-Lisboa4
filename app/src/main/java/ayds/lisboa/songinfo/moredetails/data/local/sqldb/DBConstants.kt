package ayds.lisboa.songinfo.moredetails.data.local.sqldb

const val DATABASE_NAME = "dictionary.db"
const val DATABASE_VERSION = 1
const val ARTISTS_TABLE = "artists"
const val ID_COLUMN = "id"
const val ARTIST_COLUMN = "artist"
const val INFO_COLUMN = "info"
const val SOURCE_COLUMN = "source"
const val URL_COLUMN = "url"
const val LOGO_URL_COLUMN = "logo_url"

const val createArtistTableQuery: String =
    "create table $ARTISTS_TABLE (" +
            "$ID_COLUMN integer PRIMARY KEY AUTOINCREMENT, " +
            "$ARTIST_COLUMN string, " +
            "$INFO_COLUMN string, " +
            "$SOURCE_COLUMN string, " +
            "$URL_COLUMN string, " +
            "$LOGO_URL_COLUMN string)"

