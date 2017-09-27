package in.gsav.mammouth.mastodon;

/**
 * Created by guilhelm.savin on 23/9/17.
 */

public interface ResponseHandler<T> {
    void onSuccess(T data);

    void onError(Throwable error);
}
