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

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Definitions of data stored in the local database.
 *
 * Created by guilhelm.savin on 30/9/17.
 */
public class DataDefinitions {
    public static final String AUTHORITY = "in.gsav.mammouth.provider.Mammouth";

    private DataDefinitions() {}

    /**
     * Definition of the server structure.
     */
    public static class ServerDefinitions implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/servers");

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.in.gsav.mammouth.server";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.in.gsav.mammouth.server";

        public static final String DEFAULT_SORT_ORDER = "name ASC";

        public static final String NAME = "name";
        public static final String URL = "url";
    }
}
