package kz.ikar.almatyinstitutes;

import android.animation.Animator;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.cryse.widget.persistentsearch.PersistentSearchView;
import org.cryse.widget.persistentsearch.SearchItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import kz.ikar.almatyinstitutes.db.DBHelper;
import kz.ikar.almatyinstitutes.classes.Category;
import kz.ikar.almatyinstitutes.classes.Institute;
import kz.ikar.almatyinstitutes.classes.Point;
import kz.ikar.almatyinstitutes.classes.Type;
import kz.ikar.almatyinstitutes.db.InstituteDao;
import kz.ikar.almatyinstitutes.db.TypeCategoryPointDao;
import kz.ikar.almatyinstitutes.retrofit_api.MyClass;
import kz.ikar.almatyinstitutes.retrofit_api.Result;
import kz.ikar.almatyinstitutes.retrofit_api.RetrofitApi;
import kz.ikar.almatyinstitutes.search.SampleSuggestionsBuilder;
import kz.ikar.almatyinstitutes.search.SearchResult;
import kz.ikar.almatyinstitutes.search.SearchInstitutesAdapter;
import kz.ikar.almatyinstitutes.search.SimpleAnimationListener;
import kz.ikar.almatyinstitutes.search.TopInstitutesAdapter;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.lang.System.in;

public class MainActivity extends AppCompatActivity{
    private Bundle savedInstanceState;

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
    //private List<Institute> institutes;

    private double defLat = 43.271780;
    private double defLng = 76.915002;

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

        this.savedInstanceState = savedInstanceState;
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;

                map.setOnInfoWindowClickListener(new MapboxMap.OnInfoWindowClickListener() {
                    @Override
                    public boolean onInfoWindowClick(@NonNull Marker marker) {
                        Institute currentInstitute = getByName(marker.getTitle());
                        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                        Gson gson = new Gson();
                        intent.putExtra("institute", gson.toJson(currentInstitute));
                        startActivity(intent);
                        return true;
                    }
                });

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(defLat, defLng))
                        .zoom(10)
                        .build();

                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                        3000,
                        null);

                /*for (Institute inst : getDataFromLocalDb()) {
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(inst.getPoint().getLatitude(), inst.getPoint().getLongitude()))
                            .title(inst.getName()));
                }*/
            }
        });

        searchRecyclerView.setItemAnimator(new DefaultItemAnimator());
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        topRecyclerView.setItemAnimator(new DefaultItemAnimator());
        topRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        topRecyclerView.setVisibility(View.GONE);

        searchAdapter = new SearchInstitutesAdapter(new ArrayList<Institute>(), this);
        searchRecyclerView.setAdapter(searchAdapter);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Institute> institutes = getDataFromLocalDb();

                topInstitutesAdapter = new TopInstitutesAdapter(institutes, MainActivity.this);
                topRecyclerView.setAdapter(topInstitutesAdapter);

                searchView.setSuggestionBuilder(new SampleSuggestionsBuilder(MainActivity.this, institutes));
            }
        });

        /*topInstitutesAdapter = new TopInstitutesAdapter(institutes, this);
        topRecyclerView.setAdapter(topInstitutesAdapter);*/

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
        //searchView.setSuggestionBuilder(new SampleSuggestionsBuilder(this, institutes));
        searchView.setSearchListener(new PersistentSearchView.SearchListener() {
            @Override
            public boolean onSuggestion(SearchItem searchItem) {
                for (Institute inst : getDataFromLocalDb()) {
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
                if (searchRecyclerView.getVisibility() == View.VISIBLE) {
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
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        initNav();
        FirebaseApp.initializeApp(this);

        //recreateDb();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;
            }
        });
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

    public void pickLocations(List<Institute> insts) {
        map.clear();
        map.resetNorth();
        topRecyclerView.setVisibility(View.GONE);


        for (Institute inst : insts) {
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(inst.getPoint().getLatitude(),
                            inst.getPoint().getLongitude()))
                    .title(inst.getName()));
        }

        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                //super.handleMessage(msg);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(defLat, defLng))
                        .zoom(10)
                        .build();

                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                        3000,
                        null);

                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
            }
        };
        handler.obtainMessage();
    }

    private void fillResultToRecyclerView(String query) {
        searchAdapter.replaceWith(getListByName(query));
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearching()) {
            searchView.closeSearch();
        } else if (searchRecyclerView.getVisibility() == View.VISIBLE) {
            searchAdapter.clear();
            searchRecyclerView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    /*private void loadFakeMarkers() {
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
    }*/

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


    ///////////////////////////////////////////////////////////////
    private void recreateDb() {
        getApplicationContext().deleteDatabase(DBHelper.DATABASE_NAME);
        createDb();

    }

    private void createDb() {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Log.d("DATABASE PATH", getApplicationContext().getDatabasePath("OpenStrMapDB.db").toString());
        loadFromFirebase();
    }

    private void saveToFirebase(List<Institute> institutes) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Institutes");
        for (int i = 0; i < institutes.size(); i++) {
            Institute in = institutes.get(i);
            databaseReference.child("intitute" + i).setValue(in);
        }
    }

    private void addToDb(List<Institute> instituteList) {
        for (Institute in : instituteList) {
            if (in.getPoint() != null) {
                addPointToDb(in.getPoint());
                addIntituteToDb(in);
            }
        }
    }

    private void addPointToDb(Point p) {
        TypeCategoryPointDao tcpd = new TypeCategoryPointDao(this);
        tcpd.open();
        tcpd.addPoint(p);
        tcpd.close();
    }

    private void addIntituteToDb(Institute institute) {
        InstituteDao instituteDao = new InstituteDao(this);
        instituteDao.open();
        instituteDao.addInstitute(institute);
        instituteDao.close();
    }


    private void saveCommentToFirebase() {

    }



    private void getFileFromFirebase() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://openstrmap.appspot.com/148-v1.json");
        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                String str = "";
                try {
                    str = new String(bytes, "UTF-8");
                    fromJsonToObject(str, true, 2);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getFileFromFirebase1() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://openstrmap.appspot.com/146-v1.json");
        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                String str = "";
                try {
                    str = new String(bytes, "UTF-8");
                    fromJsonToObject(str, false, 2);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getFileFromFirebase2() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://openstrmap.appspot.com/144-v1.json");
        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                String str = "";
                try {
                    str = new String(bytes, "UTF-8");
                    fromJsonToObject(str, false, 3);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getFileFromFirebase3() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://openstrmap.appspot.com/138-v1.json");
        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                String str = "";
                try {
                    str = new String(bytes, "UTF-8");
                    fromJsonToObject(str, true, 1);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private List<Institute> list = new ArrayList<>();

    public MyClass myClass;
    private Retrofit retrofit;
    private Result result;
    private String baseUrl = "https://maps.googleapis.com/maps/api/geocode/";

    private void print(List<Institute> institutes) {
        for (int i = 0; i < institutes.size(); i++) {
            Log.e("NAME::::::::       ", institutes.get(i).getName());
        }
    }

    public class AsyncDownload extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            getFileFromFirebase();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getFileFromFirebase1();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getFileFromFirebase2();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getFileFromFirebase3();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Institute inst : list) {
                try {
                    requestLatLong(inst);
                    Thread.sleep(4000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }


    private void requestLatLong(Institute inst) throws IOException {
        //https://maps.googleapis.com/maps/api/geocode/json?&address=Общеобразовательная%20школа%20№91%20алмата

        String query = inst.getName().replace(" ", "%20");
        String createdUrl = "json?&address=" + query + "%20алмата" + "&key=AIzaSyD9mkQ3VWZeUQSH74BF3x8kldpfTITZG-E";

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApi api = retrofit.create(RetrofitApi.class);
        Call<MyClass> call = api.getMyClass(createdUrl);
        myClass = call.execute().body();
        Point p = new Point();
        p.setLatitude(myClass.getResults().get(0).getGeometry().getLocation().getLat());
        p.setLongitude(myClass.getResults().get(0).getGeometry().getLocation().getLng());
        inst.setPoint(p);
        Log.d("SUCCESS",inst.getName() + "   " + String.valueOf(inst.getPoint().getLatitude()) + "   " + String.valueOf(inst.getPoint().getLongitude()));
    }

    private void fromJsonToObject(String str, boolean gov, int tmp) {
        try {
            JSONArray array = new JSONArray(str);
            for (int i = 0; i < array.length(); i++) {
                JSONObject elem = (JSONObject) array.get(i);
                Institute institute = new Institute();
                institute.setName(elem.getString("name").replaceAll("\\s+", " "));
                institute.setPhone(elem.getString("telephone"));
                institute.setHead(elem.getString("head"));
                institute.setAddress(elem.getString("address"));
                Type type = new Type();
                if (tmp == 2) {
                    type.setId(2);
                    type.setName("school");
                } else if (tmp == 3) {
                    type.setId(3);
                    type.setName("college");
                } else if (tmp == 1) {
                    type.setId(1);
                    type.setName("kindergarten");
                }
                institute.setType(type);
                institute.setGov(gov);
                list.add(institute);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void loadFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Institutes");
        final GenericTypeIndicator<List<Institute>> genericTypeIndicator = new GenericTypeIndicator<List<Institute>>() {
        };
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Institute> list = new ArrayList<Institute>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    list.add(child.getValue(Institute.class));
                }
                addToDb(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private List<Institute> getDataFromLocalDb() {
        List<Institute> inst = new ArrayList<>();
        InstituteDao instituteDao = new InstituteDao(getApplicationContext());
        instituteDao.open();
        inst = instituteDao.getAllInstitutes();
        instituteDao.close();
        print(inst);
        return inst;
    }

    public class asyncLoad extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }
    }

    /*
    * institutes=getByType(1)   list kindergarten
    * institutes=getByType(2)   list schools
    * institutes=getByType(3)   list colleges
    *
    * */

    private List<Institute> getByType(int id){
        List<Institute> inst = new ArrayList<>();
        InstituteDao instituteDao=new InstituteDao(getApplicationContext());
        instituteDao.open();
        inst = instituteDao.getByType(id);
        instituteDao.close();
        print(inst);
        return inst;
    }

    /*
    * institutes=getByGov(true)  list gov institutes
    * institutes=getByGov(false)  list notGov institutes
    * */
    private List<Institute> getByGov(boolean bool){
        List<Institute> inst = new ArrayList<>();
        InstituteDao instituteDao=new InstituteDao(getApplicationContext());
        instituteDao.open();
        inst = instituteDao.getByGov(bool);
        instituteDao.close();
        print(inst);
        return inst;
    }

    private Institute getByName(String name) {
        InstituteDao instituteDao = new InstituteDao(getApplicationContext());
        instituteDao.open();
        Institute inst = instituteDao.getByName(name);
        instituteDao.close();
        return inst;
    }

    private List<Institute> getListByName(String name) {
        List<Institute> inst = new ArrayList<>();
        InstituteDao instituteDao=new InstituteDao(getApplicationContext());
        instituteDao.open();
        inst = instituteDao.getListByName(name);
        instituteDao.close();
        print(inst);
        return inst;
    }

    private void initNav() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Thread thread;
                switch (item.getItemId()) {
                    case R.id.item1:
                        drawerLayout.closeDrawers();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                pickLocations(getByType(1));
                            }
                        });
                        break;
                    case R.id.item2:
                        drawerLayout.closeDrawers();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                pickLocations(getByType(2));
                            }
                        });
                        break;
                    case R.id.item3:
                        drawerLayout.closeDrawers();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                pickLocations(getByType(3));
                            }
                        });
                        break;
                    case R.id.sub_item1:
                        drawerLayout.closeDrawers();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                pickLocations(getByGov(true));
                            }
                        });
                        break;
                    case R.id.sub_item2:
                        drawerLayout.closeDrawers();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                pickLocations(getByGov(false));
                            }
                        });
                        break;
                    default:
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                }
                thread.start();
                return false;
            }
        });

    }

}
