package com.rhrmaincard.todoapp.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

//    private String TAG = "SIMPLE-REQUEST";
    public RequestQueue requestQueue;
    public static VolleySingleton vs;
//    Context context;

    public VolleySingleton(Context context) {
//        this.context = context;
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized VolleySingleton getInstance(Context context){
        if(vs==null){
            vs = new VolleySingleton(context);
        }
        return vs;
    }

    public RequestQueue getRequestQueue(){
        return requestQueue;
    }

//
//    public <T> void addToRequestQueue(Request<T> req, String tag) {
//        // set the default tag if tag is empty
//        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
//        getRequestQueue().add(req);
//    }
//
//    public <T> void addToRequestQueue(Request<T> req) {
//        req.setTag(TAG);
//        getRequestQueue().add(req);
//    }
//
//    public void cancelPendingRequests(Object tag) {
//        if (requestQueue != null) {
//            requestQueue.cancelAll(tag);
//        }
//    }

}
