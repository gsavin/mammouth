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
