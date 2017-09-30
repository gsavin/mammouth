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
package in.gsav.mammouth;

import android.app.Application;

import in.gsav.mammouth.mastodon.API;

/**
 * Created by guilhelm.savin on 23/9/17.
 */

public class Mammouth extends Application {
    private final API mastodonAPI = new API(this);

    public API getMastodonAPI() {
        return this.mastodonAPI;
    }
}
