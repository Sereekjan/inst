package kz.ikar.openstrmap;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.cryse.widget.persistentsearch.PersistentSearchView;
import org.cryse.widget.persistentsearch.SearchItem;

import java.util.ArrayList;
import java.util.List;

import kz.ikar.openstrmap.search.SampleSuggestionsBuilder;
import kz.ikar.openstrmap.search.SearchResult;
import kz.ikar.openstrmap.search.SearchResultAdapter;
import kz.ikar.openstrmap.search.SimpleAnimationListener;

public class MainActivity extends AppCompatActivity{

    private MapView mapView;
    private MapboxMap map;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private PersistentSearchView searchView;
    private SearchResultAdapter searchAdapter;
    private RecyclerView recyclerView;
    private View searchTintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.access_token));

        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.mapView);
        searchTintView = findViewById(R.id.view_search_tint);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_search_result);
        searchView = (PersistentSearchView) findViewById(R.id.searchview);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;
                updateMap(43.242780, 76.940002, 10);
                loadFakeMarkers();
            }
        });

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchAdapter = new SearchResultAdapter(new ArrayList<SearchResult>());
        recyclerView.setAdapter(searchAdapter);

        searchView.setHomeButtonListener(new PersistentSearchView.HomeButtonListener() {
            @Override
            public void onHomeButtonClick() {
                Toast.makeText(MainActivity.this, "Menu click", Toast.LENGTH_LONG).show();
            }
        });
        searchTintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.cancelEditing();
            }
        });
        searchView.setSuggestionBuilder(new SampleSuggestionsBuilder(this));
        searchView.setSearchListener(new PersistentSearchView.SearchListener() {
            @Override
            public boolean onSuggestion(SearchItem searchItem) {
                return false;
            }

            @Override
            public void onSearchCleared() {

            }

            @Override
            public void onSearchTermChanged(String term) {

            }

            @Override
            public void onSearch(String query) {
                recyclerView.setVisibility(View.VISIBLE);
                fillResultToRecyclerView(query);
            }

            @Override
            public void onSearchEditOpened() {
                searchTintView.setVisibility(View.VISIBLE);
                searchTintView
                        .animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new SimpleAnimationListener())
                        .start();
            }

            @Override
            public void onSearchEditClosed() {
                searchTintView
                        .animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new SimpleAnimationListener() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                searchTintView.setVisibility(View.GONE);
                            }
                        })
                        .start();
            }

            @Override
            public boolean onSearchEditBackPressed() {
                return false;
            }

            @Override
            public void onSearchExit() {
                searchAdapter.clear();
                if(recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });

        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);

        initNav();
    }

    private void fillResultToRecyclerView(String query) {
        List<SearchResult> newResults = new ArrayList<>();
        for(int i =0; i< 10; i++) {
            SearchResult result = new SearchResult(query, query + Integer.toString(i), "");
            newResults.add(result);
        }
        searchAdapter.replaceWith(newResults);
    }

    @Override
    public void onBackPressed() {
        if(searchView.isSearching()) {
            searchView.closeSearch();
        } else if(recyclerView.getVisibility() == View.VISIBLE) {
            searchAdapter.clear();
            recyclerView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    private void initNav(){
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView=(NavigationView) findViewById(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item1:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.item2:
                        break;
                    case R.id.item3:
                        break;
                    case R.id.sub_item1:
                        break;
                    case R.id.sub_item2:
                        break;
                }
                return false;
            }
        });

        //toolbar=(Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
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
