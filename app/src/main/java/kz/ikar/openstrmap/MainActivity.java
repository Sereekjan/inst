package kz.ikar.openstrmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.access_token));

        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;
                updateMap(43.242780, 76.940002, 10);
                loadFakeMarkers();
            }
        });
    }

    private void loadFakeMarkers() {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(43.198717, 76.876163))
                .title("Средняя школа №45"));
        map.addMarker(new MarkerOptions()
                .position(new LatLng(43.197503, 76.884333))
                .title("Лингвистическая гимназия №68"));
        map.addMarker(new MarkerOptions()
                .position(new LatLng(43.225806, 76.963596))
                .title("Гимназия №30"));
        map.addMarker(new MarkerOptions()
                .position(new LatLng(43.249756, 76.858164))
                .title("Гимназия №130"));
    }

    private void updateMap(double latitude, double longitude, int zoom) {
        map.addMarker(new MarkerOptions()
        .position(new LatLng(latitude, longitude))
        .title("Some place"));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(zoom)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                5000,
                null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
