package kz.ikar.openstrmap;

import android.animation.Animator;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.cryse.widget.persistentsearch.PersistentSearchView;
import org.cryse.widget.persistentsearch.SearchItem;

import java.util.ArrayList;
import java.util.List;

import kz.ikar.openstrmap.classes.Category;
import kz.ikar.openstrmap.classes.Institute;
import kz.ikar.openstrmap.classes.Point;
import kz.ikar.openstrmap.classes.Type;
import kz.ikar.openstrmap.search.SampleSuggestionsBuilder;
import kz.ikar.openstrmap.search.SearchResult;
import kz.ikar.openstrmap.search.SearchInstitutesAdapter;
import kz.ikar.openstrmap.search.SimpleAnimationListener;
import kz.ikar.openstrmap.search.TopInstitutesAdapter;

public class MainActivity extends AppCompatActivity{

    private MapView mapView;
    private MapboxMap map;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private PersistentSearchView searchView;
    private SearchInstitutesAdapter searchAdapter;
    private RecyclerView searchRecyclerView;
    private View searchTintView;

    private RecyclerView topRecyclerView;
    private TopInstitutesAdapter topInstitutesAdapter;

    private CardView topCardView;
    private TextView topTextView;

    private int recyclerViewHeight;
    private List<Institute> institutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.access_token));

        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.mapView);
        searchTintView = findViewById(R.id.view_search_tint);
        searchRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_search_result);
        searchView = (PersistentSearchView) findViewById(R.id.searchview);
        topRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_top);
        topCardView = (CardView) findViewById(R.id.cardview_top);
        topTextView = (TextView) findViewById(R.id.textview_top);

        recyclerViewHeight = topRecyclerView.getHeight();
        institutes = Institute.getFakeInstitutes();

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(43.242780, 76.940002))
                        .zoom(10)
                        .build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                        3000,
                        null);
                loadFakeMarkers();
            }
        });

        searchRecyclerView.setItemAnimator(new DefaultItemAnimator());
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        topRecyclerView.setItemAnimator(new DefaultItemAnimator());
        topRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        topRecyclerView.setVisibility(View.GONE);

        searchAdapter = new SearchInstitutesAdapter(new ArrayList<Institute>());
        searchRecyclerView.setAdapter(searchAdapter);

        topInstitutesAdapter = new TopInstitutesAdapter(institutes, this);
        topRecyclerView.setAdapter(topInstitutesAdapter);

        searchView.setHomeButtonListener(new PersistentSearchView.HomeButtonListener() {
            @Override
            public void onHomeButtonClick() {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        searchTintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.cancelEditing();
            }
        });
        searchView.setSuggestionBuilder(new SampleSuggestionsBuilder(this, institutes));
        searchView.setSearchListener(new PersistentSearchView.SearchListener() {
            @Override
            public boolean onSuggestion(SearchItem searchItem) {
                for (Institute inst : institutes) {
                    if (searchItem.getTitle().equals(inst.getName())) {
                        pickLocation(inst);
                        onBackPressed();
                        return false;
                    }
                }
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
                searchRecyclerView.setVisibility(View.VISIBLE);
                //fillResultToRecyclerView(query);
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
                topCardView.setVisibility(View.INVISIBLE);
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
                if(searchRecyclerView.getVisibility() == View.VISIBLE) {
                    searchRecyclerView.setVisibility(View.GONE);
                }
                topCardView.setVisibility(View.VISIBLE);
            }
        });
        topTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topRecyclerView.getVisibility() == View.VISIBLE) {
                    topRecyclerView.setVisibility(View.GONE);
                } else {
                    topRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);

        initNav();
    }

    public void pickLocation(Institute inst) {
        map.clear();
        topRecyclerView.setVisibility(View.GONE);
        updateMap(inst.getPoint().getLatitude(),
                inst.getPoint().getLongitude(),
                inst.getName(),
                6000,
                16);
    }

    private void fillResultToRecyclerView(String query) {
        List<SearchResult> newResults = new ArrayList<>();
        for(int i =0; i< 10; i++) {
            SearchResult result = new SearchResult(query, query + Integer.toString(i), "");
            newResults.add(result);
        }
        searchAdapter.replaceWith(institutes);
    }

    @Override
    public void onBackPressed() {
        if(searchView.isSearching()) {
            searchView.closeSearch();
        } else if(searchRecyclerView.getVisibility() == View.VISIBLE) {
            searchAdapter.clear();
            searchRecyclerView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    private void saveToFirebase(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=database.getReference("Institutes");
        for(int i=0;i<10;i++){
            Point point=new Point(i,i);
            Type type=new Type(i,"type"+i);
            Category category=new Category(i,"category"+i);
            Institute institute=new Institute(i,"name"+i,"address"+i,"phone"+i,point,"head"+i,type,category,true);
            databaseReference.child("intitute"+i).setValue(institute);
        }
    }

    private void saveCommentToFirebase(){

    }

    private void loadFromFirebase(){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=database.getReference("Institutes");
        final GenericTypeIndicator<List<Institute>> genericTypeIndicator=new GenericTypeIndicator<List<Institute>>() {};
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Institute> list=new ArrayList<Institute>();
                for (DataSnapshot child:dataSnapshot.getChildren()){
                    list.add(child.getValue(Institute.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                        loadFromFirebase();
                        break;
                    case R.id.item3:
                        saveToFirebase();
                        break;
                    case R.id.sub_item1:
                        break;
                    case R.id.sub_item2:
                        break;
                }
                return false;
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

    private void updateMap(double latitude, double longitude, String title, int duration, int zoom) {
        map.resetNorth();
        map.addMarker(new MarkerOptions()
        .position(new LatLng(latitude, longitude))
        .title(title));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(zoom)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                duration,
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
