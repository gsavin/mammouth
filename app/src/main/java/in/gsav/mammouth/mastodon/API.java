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
package in.gsav.mammouth.mastodon;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import in.gsav.mammouth.mastodon.entities.Account;

/**
 * Created by guilhelm.savin on 23/9/17.
 */

public class API {
    public static String CLIENT_ID = "";
    public static String CLIENT_SECRET = "";

    protected String server;
    protected final RequestQueue queue;
    protected String token = "";

    public API(Application app) {
        this.queue = Volley.newRequestQueue(app);
    }

    protected <T> void doObjectRequest(int method, String path, final Class<T> responseType, final ResponseHandler<T> handler) {
        final String url = server + path;

        JsonObjectRequest request = new JsonObjectRequest(method, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Constructor<T> constructor = responseType.getConstructor(JSONObject.class);
                    T responseObject = constructor.newInstance(response);

                    handler.onSuccess(responseObject);
                } catch(Exception e) {
                    handler.onError(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.onError(error);
            }
        });

        this.queue.add(request);
    }

    protected <T> void doArrayRequest(int method, String path, final Class<T> responseType, final ResponseHandler<List<T>> handler) {
        final String url = server + path;

        JsonArrayRequest request = new JsonArrayRequest(method, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Constructor<T> constructor = responseType.getConstructor(JSONObject.class);
                    List<T> responseList = new LinkedList<>();

                    for (int i = 0; i < response.length(); i++) {
                        responseList.add(constructor.newInstance(response));
                    }

                    handler.onSuccess(responseList);
                } catch(Exception e) {
                    handler.onError(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handler.onError(error);
            }
        });

        this.queue.add(request);
    }

    public void getAccount(String id, final ResponseHandler<Account> handler) {
        final String path = "/api/v1/accounts/" + id;
        final int method = Request.Method.GET;

        doObjectRequest(method, path, Account.class, handler);
    }

    public void getCurrentAccount(ResponseHandler<Account> handler) {
        final String path = "/api/v1/accounts/verify_credentials";
        final int method = Request.Method.GET;

        doObjectRequest(method, path, Account.class, handler);
    }

    public void updateCurrentAccount(JSONObject patch, ResponseHandler<Boolean> handler) {
        final String path = "/api/v1/accounts/update_credentials";
        final int method = Request.Method.PATCH;
    }

    public void getAccountFollowers(Account account, ResponseHandler<List<Account>> handler) {
        final String path = "/api/v1/accounts/" + account.getID() + "/followers";
        final int method = Request.Method.GET;

        doArrayRequest(method, path, Account.class, handler);
    }
}
