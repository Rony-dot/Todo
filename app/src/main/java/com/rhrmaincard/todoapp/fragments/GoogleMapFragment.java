package com.rhrmaincard.todoapp.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rhrmaincard.todoapp.R;


public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {


    GoogleMap googleMap;
    MapView googleMapView;

    private FusedLocationProviderClient client;


    public GoogleMapFragment() {
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
        return inflater.inflate(R.layout.fragment_google_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        googleMapView = view.findViewById(R.id.google_map_view);
        googleMapView.onCreate(savedInstanceState);
        googleMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setCompassEnabled(true);
        this.googleMap.getUiSettings().setAllGesturesEnabled(true);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.googleMap.getUiSettings().setZoomGesturesEnabled(true);
        this.googleMap.setTrafficEnabled(true);

        myCurrentLocation();
    }

    private void myCurrentLocation() {
        client = LocationServices.getFusedLocationProviderClient(getContext());
        if (client == null) return;


            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                return;
            }
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

//                    ImageView image = (ImageView) findViewById(R.id.test_image);
//                    Bitmap bMap = BitmapFactory.decodeFile("/Phone/rony.jpg");
//                    image.setImageBitmap(bMap);

//                    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.location);

                    googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat,lon))
                            .title("My Location")
//                    .icon(BitmapDescriptorFactory.fromBitmap(bMap))
                    .draggable(true)
                    .snippet("Banani BTCL Colony")
                    .flat(true)
                    .rotation(45.0f)
                    .zIndex(1.5f));

                    googleMap.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(new LatLng(lat,lon),15));
                }
            });

    }

    @Override
    public void onPause() {
        super.onPause();
        googleMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        googleMapView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        googleMapView.onStop();
    }
}