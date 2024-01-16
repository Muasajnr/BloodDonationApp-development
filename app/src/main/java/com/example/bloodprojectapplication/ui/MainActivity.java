package com.example.bloodprojectapplication.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.bloodprojectapplication.R;
import com.example.bloodprojectapplication.domain.SharePreference;
import com.example.bloodprojectapplication.model.Geocoding;
import com.example.bloodprojectapplication.model.Notification;
import com.example.bloodprojectapplication.model.User;
import com.example.bloodprojectapplication.ui.authentication.LoginActivity;
import com.example.bloodprojectapplication.ui.history.HistoryActivity2;
import com.example.bloodprojectapplication.ui.notification.NotificationActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener,
        LocationListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final LatLng mUserLocation = new LatLng(-0.42502836574115077, 36.955733708091024);
    LatLng sydney = new LatLng(0.00177069, 34.6111966);
    double latitude;
    double longitude;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    LatLng TamWorth = new LatLng(-31.083332, 150.916672);
    LatLng NewCastle = new LatLng(-32.916668, 151.750000);
    LatLng Brisbane = new LatLng(-27.470125, 153.021072);
    private ArrayList<User> mUsers;
    private DrawerLayout drawerLayout;
    private TextView nav_fullnames, nav_email, nav_bloodgroups, nav_type;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<LatLng> locationArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            checkLocationPermission();

        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Blood Donation App");


        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView nav_view = findViewById(R.id.nav_view);

        locationArrayList = new ArrayList<>();
        mUsers = new ArrayList<>();
        locationArrayList.add(sydney);


        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(this);

        nav_fullnames = nav_view.getHeaderView(0).findViewById(R.id.nav_user_fullnames);
        nav_email = nav_view.getHeaderView(0).findViewById(R.id.nav_user_email);
        nav_bloodgroups = nav_view.getHeaderView(0).findViewById(R.id.nav_user_bloodgroups);
        nav_type = nav_view.getHeaderView(0).findViewById(R.id.nav_user_type);


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue().toString();
                    nav_fullnames.setText(name);
                    SharePreference.getINSTANCE(getApplicationContext()).setName(name);


                    String email = snapshot.child("email").getValue().toString();
                    nav_email.setText(email);
                    SharePreference.getINSTANCE(getApplicationContext()).setEmail(email);


                    String bloodGroup = snapshot.child("bloodgroup").getValue().toString();
                    nav_bloodgroups.setText(bloodGroup);
                    SharePreference.getINSTANCE(getApplicationContext()).setBloodgroup(bloodGroup);


                    String type = snapshot.child("type").getValue().toString();
                    nav_type.setText(type);
                    if (type.equals("recipient")) {
                        nav_view.getMenu().clear(); //clear old inflated items.
                        nav_view.inflateMenu(R.menu.recipient_menu);
                    }

                    String phoneNumber = snapshot.child("phonenumber").getValue().toString();
                    SharePreference.getINSTANCE(getApplicationContext()).setPhonenumber(phoneNumber);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getCoordinates(query);
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    private void getCoordinates(String location) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://google-maps-geocoding.p.rapidapi.com/geocode/json?address=" + location + "&language=en")
                .get()
                .addHeader("X-RapidAPI-Key", "e064577ed9msh168f2fe204257e9p124b32jsn69ec75e222d8")
                .addHeader("X-RapidAPI-Host", "google-maps-geocoding.p.rapidapi.com")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Gson gson = new Gson();
                Geocoding geocoding = gson.fromJson(response.body().string(), Geocoding.class);
                if (geocoding.getResults().length > 0) {
                    Double lng = geocoding.getResults()[0].getGeometry().getLocation().getLng();
                    Double lat = geocoding.getResults()[0].getGeometry().getLocation().getLat();

                    runOnUiThread(() -> {
                        LatLng mUserLocation = new LatLng(lat, lng);
                        mMap.addMarker(new MarkerOptions()
                                .position(mUserLocation)
                                .title(location));

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mUserLocation, 13));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(mUserLocation)      // Sets the center of the map to location user
                                .zoom(17)                   // Sets the zoom
                                .bearing(90)                // Sets the orientation of the camera to east
                                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    });

                    Log.i("Coordinates", response.toString());
                } else {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Location not found", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("Coordinates", e.toString());
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Location not found", Toast.LENGTH_SHORT).show());
            }

        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.donate) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Notifications")
                    .child(SharePreference.getINSTANCE(this).getBLOODGROUP());
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Random rand = new Random();
            String id = String.format("%06d", rand.nextInt(999999));
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM HH:mm");
            Date dn = new Date();
            String formatted = formatter.format(dn);
            Notification notification = new Notification(
                    id,
                    SharePreference.getINSTANCE(this).getName(),
                    uid,
                    SharePreference.getINSTANCE(this).getEmail(),
                    SharePreference.getINSTANCE(this).getBLOODGROUP(),
                    SharePreference.getINSTANCE(this).getPhonenumber(),
                    "empty", formatted);
            reference.child(uid).setValue(notification);
            FirebaseDatabase.getInstance().getReference().child("users")
                    .child(uid).child("history").child(formatted).setValue(notification);
            Toast.makeText(this, "Notification sent, a recipient will contact you.", Toast.LENGTH_LONG).show();
        }
        if (item.getItemId() == R.id.notifications) {
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity2.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("location");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    reference.child(Objects.requireNonNull(dataSnapshot.getKey())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            assert user != null;
                            String[] mLatLng = user.getLocation().split(" ");
                            LatLng userLocation = new LatLng(Double.parseDouble(mLatLng[0]), Double.parseDouble(mLatLng[1]));
                            mMap.addMarker(new MarkerOptions().position(userLocation).title(user.getUsername() + "(" + user.getContact() + ") " + user.getBloodGroup()));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 13));
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(userLocation)      // Sets the center of the map to location user
                                    .zoom(17)                   // Sets the zoom
                                    .bearing(90)                // Sets the orientation of the camera to east
                                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                                    .build();                   // Creates a CameraPosition from the builder
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this,

                Manifest.permission.ACCESS_FINE_LOCATION)

                != PackageManager.PERMISSION_GRANTED) {


            // Asking user if explanation is needed

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,

                    Manifest.permission.ACCESS_FINE_LOCATION)) {


                // Show an explanation to the user *asynchronously* -- don't block

                // this thread waiting for the user's response! After the user

                // sees the explanation, try again to request the permission.


                //Prompt the user once explanation has been shown

                ActivityCompat.requestPermissions(this,

                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},

                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,

                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},

                        MY_PERMISSIONS_REQUEST_LOCATION);

            }

            return false;

        } else {

            return true;

        }

    }


    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)

                .addConnectionCallbacks(this)

                .addOnConnectionFailedListener(this)

                .addApi(LocationServices.API)

                .build();

        mGoogleApiClient.connect();

    }

    @Override

    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(1000);

        mLocationRequest.setFastestInterval(1000);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this,

                Manifest.permission.ACCESS_FINE_LOCATION)

                == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }

    }


    @Override

    public void onConnectionSuspended(int i) {


    }


    @Override

    public void onLocationChanged(Location location) {



    }


    @Override

    public void onConnectionFailed(ConnectionResult connectionResult) {


    }


    @Override

    public void onRequestPermissionsResult(int requestCode,

                                           String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_LOCATION: {

                // If request is cancelled, the result arrays are empty.

                if (grantResults.length > 0

                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    // permission was granted. Do the

                    // contacts-related task you need to do.

                    if (ContextCompat.checkSelfPermission(this,

                            Manifest.permission.ACCESS_FINE_LOCATION)

                            == PackageManager.PERMISSION_GRANTED) {


                        if (mGoogleApiClient == null) {

                            buildGoogleApiClient();

                        }

                        mMap.setMyLocationEnabled(true);

                    }


                } else {


                    // Permission denied, Disable the functionality that depends on this permission.

                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();

                }

            }


            // other 'case' lines to check for other permissions this app might request.

            // You can add here other case statements according to your requirement.

        }

    }


    @Override

    public boolean onMarkerClick(Marker marker) {

        return false;
    }

}