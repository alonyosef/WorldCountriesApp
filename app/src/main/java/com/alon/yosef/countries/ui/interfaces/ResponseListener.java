package com.alon.yosef.countries.ui.interfaces;

/**
 * Created by alony on 18/07/2019.
 */

public interface ResponseListener<T> {
    void onSuccess(T response);
    void onError(Exception ex);
}
