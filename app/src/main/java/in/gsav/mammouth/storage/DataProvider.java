/*
 * This file is part of Mammouth.
 *
 * Mammouth is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mammouth is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package in.gsav.mammouth.storage;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * Provider for data stored in the local database.
 * 
 */
public class DataProvider extends ContentProvider {
    private static final String TAG = "MammouthDataProvider";
    private static final String DATABASE_NAME = "mammouth.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SERVERS_TABLE_NAME = "servers";

    private static final UriMatcher sUriMatcher;
    private static HashMap<String, String> sServersProjectionMap;

    private static final int SERVERS = 1;
    private static final int SERVER_ID = 2;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + SERVERS_TABLE_NAME + " ("
                    + DataDefinitions.ServerDefinitions._ID + " INTEGER PRIMARY KEY,"
                    + DataDefinitions.ServerDefinitions.NAME + " TEXT NOT NULL,"
                    + DataDefinitions.ServerDefinitions.URL + " TEXT NOT NULL,"
                    + ");");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)) {
            case SERVERS:
                qb.setTables(SERVERS_TABLE_NAME);
                qb.setProjectionMap(sServersProjectionMap);
                break;
            case SERVER_ID:
                qb.setTables(SERVERS_TABLE_NAME);
                qb.setProjectionMap(sServersProjectionMap);
                qb.appendWhere(DataDefinitions.ServerDefinitions._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (TextUtils.isEmpty(sortOrder)) {
            sortOrder = DataDefinitions.ServerDefinitions.DEFAULT_SORT_ORDER;
        }

        // Get the database and run the query
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        // Tell the cursor what uri to watch, so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case SERVERS:
                return DataDefinitions.ServerDefinitions.CONTENT_TYPE;
            case SERVER_ID:
                return DataDefinitions.ServerDefinitions.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        String tableName;
        String nullColumnHack = null;

        if (contentValues == null) {
            contentValues = new ContentValues();
        }

        switch (sUriMatcher.match(uri)) {
            case SERVERS:
                if (!contentValues.containsKey(DataDefinitions.ServerDefinitions.NAME)) {
                    throw new IllegalArgumentException("Missing name");
                }

                if (!contentValues.containsKey(DataDefinitions.ServerDefinitions.NAME)) {
                    throw new IllegalArgumentException("Missing url");
                }

                tableName = SERVERS_TABLE_NAME;
                nullColumnHack = DataDefinitions.ServerDefinitions.NAME;

                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long rowId = db.insert(tableName, nullColumnHack, contentValues);

        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(DataDefinitions.ServerDefinitions.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String where, @Nullable String[] whereArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count;

        switch (sUriMatcher.match(uri)) {
            case SERVERS:
                count = db.delete(SERVERS_TABLE_NAME, where, whereArgs);
                break;
            case SERVER_ID:
                String serverId = uri.getPathSegments().get(1);
                count = db.delete(SERVERS_TABLE_NAME, DataDefinitions.ServerDefinitions._ID + "=" + serverId
                        + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String where, @Nullable String[] whereArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count;

        switch (sUriMatcher.match(uri)) {
            case SERVERS:
                count = db.update(SERVERS_TABLE_NAME, values, where, whereArgs);
                break;
            case SERVER_ID:
                String noteId = uri.getPathSegments().get(1);
                count = db.update(SERVERS_TABLE_NAME, values, DataDefinitions.ServerDefinitions._ID + "=" + noteId
                        + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(DataDefinitions.AUTHORITY, "servers", SERVERS);
        sUriMatcher.addURI(DataDefinitions.AUTHORITY, "servers/#", SERVER_ID);

        sServersProjectionMap = new HashMap<String, String>();
        sServersProjectionMap.put(DataDefinitions.ServerDefinitions._ID, DataDefinitions.ServerDefinitions._ID);
        sServersProjectionMap.put(DataDefinitions.ServerDefinitions.NAME, DataDefinitions.ServerDefinitions.NAME);
        sServersProjectionMap.put(DataDefinitions.ServerDefinitions.URL, DataDefinitions.ServerDefinitions.URL);
    }
}
