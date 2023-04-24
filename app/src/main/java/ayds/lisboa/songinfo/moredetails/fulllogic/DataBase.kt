package ayds.lisboa.songinfo.moredetails.fulllogic

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

private const val DATABASE_NAME = "dictionary.db"
private const val DATABASE_VERSION = 1

private const val ARTISTS_TABLE = "artists"
private const val ID_COLUMN = "id"
private const val ARTIST_COLUMN = "artist"
private const val INFO_COLUMN = "info"
private const val SOURCE_COLUMN = "source"

class DataBase(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    private val projection = arrayOf(
        "$ID_COLUMN",
        "$ARTIST_COLUMN",
        "$INFO_COLUMN"
    )

    override fun onCreate(db: SQLiteDatabase)
    {
        val query: String =
            "CREATE TABLE $ARTISTS_TABLE (" +
                    "$ID_COLUMN string PRIMARY KEY, " +
                    "$ARTIST_COLUMN string, " +
                    "$INFO_COLUMN string, " +
                    "$SOURCE_COLUMN integer" +
                    ")"

        db.execSQL(query)

        Log.i("DB", "DB created")
    }

    fun getInfo(dbHelper: DataBase, artist: String): String? {

        val db = dbHelper.readableDatabase

        // How you want the results sorted in the resulting Cursor
        val sortOrder = "$ARTIST_COLUMN DESC"
        val cursor = db.query(
            "$ARTISTS_TABLE",  // The table to query
            projection,  // The array of columns to return (pass null to get all)
            "$ARTIST_COLUMN  = ?",  // The columns for the WHERE clause
            arrayOf(artist),  // The values for the WHERE clause
            null,  // don't group the rows
            null,  // don't filter by row groups
            "$ARTIST_COLUMN DESC" // The sort order
        )
        val items: MutableList<String> = ArrayList()
        while (cursor.moveToNext()) {
            val info = cursor.getString(
                cursor.getColumnIndexOrThrow("$INFO_COLUMN")
            )
            items.add(info)
        }
        cursor.close()
        return if (items.isEmpty()) null else items[0]
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}