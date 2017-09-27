package in.gsav.mammouth.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import in.gsav.mammouth.mastodon.MastodonServer;

/**
 * Created by guilhelm.savin on 25/9/17.
 */

public class DataStorage extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mammouth";

    private static final String SERVERS_TABLE_NAME = "servers";
    private static final String SERVERS_TABLE_CREATE =
            "CREATE TABLE " + SERVERS_TABLE_NAME + " (name TEXT NOT NULL, url TEXT NOT NULL);";
    private static final String SERVERS_SELECT = "SELECT name, url FROM " + SERVERS_TABLE_NAME + ";";

    DataStorage(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SERVERS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        switch (newVersion) {
            case 1:
            default:
                break;
        }
    }

    public List<? extends MastodonServer> getServers() {
        Cursor cursor = getReadableDatabase().query(SERVERS_TABLE_NAME, new String[]{"name", "url"}, null, null, null, null, null);
        return DataMastodonServer.loadFrom(cursor);
    }

    public void addServer(DataMastodonServer server) {
        getWritableDatabase().insertOrThrow(SERVERS_TABLE_CREATE, null, server.toContentValues());
    }
}
