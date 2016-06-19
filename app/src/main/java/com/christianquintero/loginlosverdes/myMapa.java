package com.christianquintero.loginlosverdes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class myMapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=
        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            return;
        }
        mMap.setMyLocationEnabled(true);


        mLocation = mMap.getMyLocation();

        // Add a marker in Sydney and move the camera
        /*LatLng me = new LatLng( mLocation.getLatitude(), mLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(me).title("Tú"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 20));*/

        //Locaciones
        LatLng bosquePlaza = new LatLng( 6.269004158183917,-75.56528717279434);
        mMap.addMarker(new MarkerOptions().position(bosquePlaza).title("Centro Comercial Bosque Plaza").icon(BitmapDescriptorFactory.fromResource(R.drawable.logo)));

        LatLng buenosAires = new LatLng( 6.240673468523409,-75.55237703025341);
        mMap.addMarker(new MarkerOptions().position(buenosAires).title("Buenos Aires").icon(BitmapDescriptorFactory.fromResource(R.drawable.logo)));

        LatLng bulerias = new LatLng( 6.2391187383738265,-75.58962419629097);
        mMap.addMarker(new MarkerOptions().position(bulerias).title("Bulerías").icon(BitmapDescriptorFactory.fromResource(R.drawable.logo)));

        LatLng campoValdes = new LatLng( 6.270569997203637,-75.55733643472195);
        mMap.addMarker(new MarkerOptions().position(campoValdes).title("Campo Valdés").icon(BitmapDescriptorFactory.fromResource(R.drawable.logo)));

        LatLng castilla = new LatLng( 6.291871930295601,-75.57214021682739);
        mMap.addMarker(new MarkerOptions().position(castilla).title("Castilla").icon(BitmapDescriptorFactory.fromResource(R.drawable.logo)));

    }
}
