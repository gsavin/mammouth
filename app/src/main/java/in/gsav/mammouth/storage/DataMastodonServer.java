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

import android.content.ContentValues;
import android.database.Cursor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import in.gsav.mammouth.mastodon.MastodonServer;

/**
 * Created by guilhelm.savin on 25/9/17.
 */

public class DataMastodonServer implements MastodonServer {
    public static List<DataMastodonServer> loadFrom(Cursor cursor) {
        int nameIndex = cursor.getColumnIndex("name");
        int urlIndex = cursor.getColumnIndex("url");

        List<DataMastodonServer> list = new LinkedList<>();

        while (!cursor.isLast() && !cursor.isClosed()) {
            try {
                String name = cursor.getString(nameIndex);
                URL url = new URL(cursor.getString(urlIndex));

                list.add(new DataMastodonServer(name, url));
            } catch (MalformedURLException e) {
                System.err.printf("Bad server url %s%n", e.getMessage());
            }

        }

        return list;
    }

    private String name;
    private URL url;

    public DataMastodonServer(String name, URL url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public URL getURL() {
        return this.url;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("name", this.name);
        values.put("url", this.url.toString());

        return values;
    }
}
