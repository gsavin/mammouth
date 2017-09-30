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
package in.gsav.mammouth.mastodon.entities;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by guilhelm.savin on 23/9/17.
 */
public class Account {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final SimpleDateFormat DATE_PARSER = new SimpleDateFormat(DATE_FORMAT);

    private final int id;

    private final String username;

    /**
     * Equals username for local users, includes @domain for remote ones.
     */
    private final String acct;

    /**
     * The account's display name.
     */
    private final String displayName;

    /**
     * Boolean for when the account cannot be followed without waiting for approval first.
     */
    private final boolean locked;

    /**
     * The time the account was created.
     */
    private final Date createdAt;

    /**
     * The number of followers for the account.
     */
    private final int followersCount;

    /**
     * The number of accounts the given account is following.
     */
    private final int followingCount;

    /**
     * The number of statuses the account has made.
     */
    private final int statusesCount;

    /**
     * Biography of user.
     */
    private final String note;

    /**
     * URL of the user's profile page (can be remote).
     */
    private final URL url;

    /**
     * URL to the avatar image.
     */
    private final URL avatar;

    /**
     * URL to the avatar static image (gif).
     */
    private final URL avatarStatic;

    /**
     * URL to the header image.
     */
    private final URL header;

    /**
     * URL to the header static image (gif).
     */
    private final URL headerStatic;

    public Account(JSONObject json) throws JSONException, MalformedURLException, ParseException {
        this.id = json.getInt("id");
        this.username = json.getString("username");
        this.acct = json.getString("acct");
        this.displayName = json.getString("display_name");
        this.createdAt = DATE_PARSER.parse(json.getString("created_at"));
        this.locked = json.getBoolean("locked");
        this.followersCount = json.getInt("followers_count");
        this.followingCount = json.getInt("following_count");
        this.statusesCount = json.getInt("statuses_count");
        this.note = json.getString("note");
        this.url = new URL(json.getString("url"));
        this.avatar = new URL(json.getString("avatar"));
        this.avatarStatic = new URL(json.getString("avatar_static"));
        this.header = new URL(json.getString("header"));
        this.headerStatic = new URL(json.getString("header_static"));
    }

    public int getID() {
        return this.id;
    }

    public String getUsername() {
        return username;
    }

    public String getAcct() {
        return acct;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isLocked() {
        return locked;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    public String getNote() {
        return note;
    }

    public URL getUrl() {
        return url;
    }

    public URL getAvatar() {
        return avatar;
    }

    public URL getAvatarStatic() {
        return avatarStatic;
    }

    public URL getHeader() {
        return header;
    }

    public URL getHeaderStatic() {
        return headerStatic;
    }
}
