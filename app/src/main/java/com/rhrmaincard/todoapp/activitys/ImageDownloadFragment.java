package com.rhrmaincard.todoapp.activitys;

import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.rhrmaincard.todoapp.R;
import com.rhrmaincard.todoapp.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;


public class ImageDownloadFragment extends Fragment {

    private RequestQueue requestQueue;
    private String TAG  = "Fragment-Simple-Request";
    private String Image_Tag  = "Fragment-Image-Request";
    View view;

    ImageView one, two, three, four;

    public ImageDownloadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_download, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        one = view.findViewById(R.id.iv_one);
        two = view.findViewById(R.id.iv_two);
        three = view.findViewById(R.id.iv_three);
        four = view.findViewById(R.id.iv_four);
        this.view = view;
        requestQueue = VolleySingleton.getInstance(requireContext()).getRequestQueue();

    }

    @Override
    public void onResume() {
        super.onResume();
        requestQueue.getCache().clear();
        if(requestQueue!= null){
            requestQueue.cancelAll(TAG);
            requestQueue.cancelAll(Image_Tag);
        }
        webApiCommunication();
    }

    private void webApiCommunication() {

        String url = "https://www.google.com";
        String imgUrl = "https://picsum.photos/200/300?random=1";

//        requestQueue = Volley.newRequestQueue(requireContext());

        /**
         *  String request
         */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);

        /**
         *  object request
         */
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        objectRequest.setTag(TAG);
        requestQueue.add(objectRequest);

        /**
         *  array request
         */
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        arrayRequest.setTag(TAG);
        requestQueue.add(arrayRequest);

        /**
         * image request
         */
        ImageRequest imageRequest = new ImageRequest(imgUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                one.setImageBitmap(response);
//                two.setImageBitmap(response);
                three.setImageBitmap(response);
                four.setImageBitmap(response);

                Uri uri = saveImageToInternalStorage(response);
                two.setImageURI(uri);

            }
        }, 150,
                150,
                ImageView.ScaleType.CENTER_CROP,
                Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(view,"Image cant set", Snackbar.LENGTH_LONG).show();
                    }
                });
        imageRequest.setTag(Image_Tag);
        requestQueue.add(imageRequest);

    }

    // Custom method to save a bitmap into internal storage
    protected Uri saveImageToInternalStorage(Bitmap bitmap){
        // Initialize ContextWrapper
        ContextWrapper wrapper = new ContextWrapper(getContext());

        // Initializing a new file
        // The bellow line return a directory in internal storage
        File file = wrapper.getDir("images",MODE_PRIVATE);
        Random random = new Random();
        int i = random.nextInt(1000);

        // Create a file to save the image
        file = new File(file, "UniqueFileName"+i+".jpg");

        try{
            // Initialize a new OutputStream
            OutputStream stream = null;

            // If the output file exists, it can be replaced or appended to it
            stream = new FileOutputStream(file);

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

            // Flushes the stream
            stream.flush();

            // Closes the stream
            stream.close();

        }catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the gallery image url to uri
        Uri savedImageURI = Uri.parse(file.getAbsolutePath());

        // Return the saved image Uri
        return savedImageURI;
    }

    @Override
    public void onPause() {
        super.onPause();
        requestQueue.getCache().clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(requestQueue!= null){
            requestQueue.cancelAll(TAG);
            requestQueue.cancelAll(Image_Tag);
        }
    }
}