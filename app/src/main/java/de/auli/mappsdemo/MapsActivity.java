package de.auli.mappsdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    public static final int REQUEST_PERMISSION_FINE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapsactivity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_FINE);
            } else {
                doIt();
            }
        } else {
            doIt();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION_FINE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            doIt();
        }
    }

    @SuppressLint("MissingPermission")
    private void doIt() {
        mMap.setMyLocationEnabled(true);

        //LatLng bremen = new LatLng(53.0792962, 8.8016937);
        //LatLng bremen = new LatLng(0, 0);
        //mMap.addMarker(new MarkerOptions().position(bremen).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(bremen));

        MarkerOptions options = new MarkerOptions();
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String bestProvider = manager.getBestProvider(new Criteria(), true);
        Toast.makeText(this, String.format("Provider: %s", bestProvider),Toast.LENGTH_SHORT).show();
        Location location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null){
            LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
            options.position(pos);
            options.title("Hier bin ich");
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            mMap.addMarker(options);
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 18));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 18), 5000, null);
        }
    }
}
