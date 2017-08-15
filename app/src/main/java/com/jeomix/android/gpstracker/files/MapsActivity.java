package com.jeomix.android.gpstracker.files;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jeomix.android.gpstracker.R;
import com.jeomix.android.gpstracker.files.EventBusClasses.LocationEvents;
import com.jeomix.android.gpstracker.files.Helper.BackEndManager;
import com.jeomix.android.gpstracker.files.Helper.BackEndTracking;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends Fragment {
    private static boolean clicked=false;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000 ;
    private static final String TAG ="MapsActivity" ;
    private GoogleMap mMap;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private LocationEvents locationEvents;
    private  ValueEventListener vel;
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                getActivity().finish();
            }
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);
        MapView mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(googleMap -> {
            mMap = googleMap;
            // Add a marker in Sydney and move the cameraLatLngBounds.Builder builder = new LatLngBounds.Builder();
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                                               @Override
                                               public void onMapLongClick(LatLng latLng) {
                                                   Snackbar.make(getView(),"Map Long Pressed free movement",Snackbar.LENGTH_SHORT).show();
                                                  clicked=true;
                                               }
            });
                    emptyMap();

//                LatLng sydney = new LatLng(-34, 151);
//                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        });
        return rootView;
    }

public void emptyMap(){
    LatLngBounds.Builder builder= new LatLngBounds.Builder();
    builder.include(new LatLng(47.09194444, 18.52166666));
    builder.include(new LatLng(36.448311, 6.62555555));
    LatLngBounds bounds = builder.build();
    int width = getResources().getDisplayMetrics().widthPixels;
    int height = getResources().getDisplayMetrics().heightPixels;
    int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen
    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
    mMap.animateCamera(cu);
    final Handler handler = new Handler();
    handler.postDelayed(() -> {
        Snackbar.make(getView(),"Choose & Vehicule To Track",Snackbar.LENGTH_INDEFINITE).setActionTextColor(ContextCompat.getColor(getContext(),R.color.green)).setAction("show me", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new Boolean(true));
            }
        }).show();
    }, 400);
}
//    public void trackMoreThanOne(LocationEvents locationEvents){
//        if(locationEvents.isTrack()){
//            if(!BackEndManager.containsVehicule(locationEvents.getVehicle())){
//                database= FirebaseDatabase.getInstance();
//                myRef = database.getReference("Tracks").child(locationEvents.getVehicle().getId());
//                ValueEventListener vel = new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        BackEndTracking backEndTracking=new BackEndTracking(locationEvents.getVehicle());
//                        for (DataSnapshot data : dataSnapshot.getChildren()) {
//                            for(DataSnapshot numbers : data.getChildren())
//                            {
//                                Location l = new Location("fused");
//                                l.setLatitude(Double.parseDouble(String.valueOf(data.child(numbers.getKey()).child("latitude").getValue())));
//                                l.setLongitude(Double.parseDouble(String.valueOf(data.child(numbers.getKey()).child("longitude").getValue())));
//                                backEndTracking.addLocation(l);
//                            }
//                        }
//                        drawLine(BackEndManager.getLatlngs(locationEvents.getVehicle()));
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                };
//                myRef.addValueEventListener(vel);
//                BackEndManager.addValueListener(vel);
//
//
//            }
////TODO: SNACKBAR EVENT TO SHOW THE GUY IS ALREADY BEING TRACKED
//
//
//        }else{
//
//        }
//
//
//
//
//    }

    public void trackOne(LocationEvents locationEvents) {
        clicked=false;
        database = FirebaseDatabase.getInstance();
        if(locationEvents.isTrack()){
            if (!locationEvents.equals(this.locationEvents)) {
                this.locationEvents=locationEvents;
                if (vel != null) {
                    myRef.removeEventListener(vel);
                }
                BackEndManager.setLocations(null);
                myRef = database.getReference("Tracks").child(locationEvents.getVehicle().getId());
                vel = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int lastTrackHistory= (int) dataSnapshot.getChildrenCount();
                        int i=0;
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            i++;
                            if(lastTrackHistory==i){
                                for(DataSnapshot numbers : data.getChildren())
                                {
                                    Location l = new Location("fused");
                                    l.setLatitude(Double.parseDouble(String.valueOf(data.child(numbers.getKey()).child("latitude").getValue())));
                                    l.setLongitude(Double.parseDouble(String.valueOf(data.child(numbers.getKey()).child("longitude").getValue())));
                                    BackEndManager.addLocation(l);
                                }
                            }
                        }
                        drawLine(BackEndManager.getLatlngs());
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                myRef.addValueEventListener(vel);
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    Snackbar.make(getView(),"Auto-Camera Activated (Long Click On MAP to D/A)",Snackbar.LENGTH_INDEFINITE).setActionTextColor(ContextCompat.getColor(getContext(),R.color.green)).setAction("Deactivate", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clicked=true;
                        }
                    }).show();
                }, 400);
            }else{
                //TODO : ADD SNACKBAR THAT THIS IS ALREADY BEING TRACKED
                Snackbar.make(getView(),"This Vehicle is already being Tracked",Snackbar.LENGTH_SHORT).show();
            }
        }
        else{
            this.locationEvents=null;
            myRef.removeEventListener(vel);
            myRef=null;
            BackEndManager.setLocations(null);
            mMap.clear();
            emptyMap();

        }
    }
    private int vehicleImage(Vehicle v){
        if(v!=null){
            switch(v.getType()){
                case truck :
                    return R.drawable.map_truck;
                case motorCycle:

                    return  R.drawable.map_moto;

                case car:
                    return R.drawable.car_top;

                case bus:
                    return R.drawable.map_bus;

            }
        }
        return R.drawable.attention;
    }

    public void drawLine(List<LatLng> points) {
        if (points == null) {
            Log.e("Draw Line", "got null as parameters");
            return;
        }
        mMap.clear();
        Polyline line = mMap.addPolyline(new PolylineOptions().width(15).color(Color.RED));
        line.setPoints(points);
        prepareZoom(line);
        mMap.addMarker(new MarkerOptions().position(points.get(points.size()-1)).title("Label :"+locationEvents.getVehicle().getLabel()+" \n"+locationEvents.getVehicle().getVin()).icon(BitmapDescriptorFactory.fromResource(vehicleImage(locationEvents.getVehicle()))));

    }
    public void prepareZoom(Polyline polyline){
        boolean hasPoints = false;
        Double maxLat = null, minLat = null, minLon = null, maxLon = null;

        if (polyline != null && polyline.getPoints() != null) {
            List<LatLng> pts = polyline.getPoints();
            for (LatLng coordinate : pts) {
                // Find out the maximum and minimum latitudes & longitudes
                // Latitude
                maxLat = maxLat != null ? Math.max(coordinate.latitude, maxLat) : coordinate.latitude;
                minLat = minLat != null ? Math.min(coordinate.latitude, minLat) : coordinate.latitude;

                // Longitude
                maxLon = maxLon != null ? Math.max(coordinate.longitude, maxLon) : coordinate.longitude;
                minLon = minLon != null ? Math.min(coordinate.longitude, minLon) : coordinate.longitude;

                hasPoints = true;
            }
        }
        if (hasPoints) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(maxLat, maxLon));
            builder.include(new LatLng(minLat, minLon));
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 15));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationEvents(LocationEvents locationEvents){
        trackOne(locationEvents);



    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    //    public void LoadLocationOnMap(Polyline polyline, ArrayList<Location> location){
//
//        polyline = mMap.addPolyline(new PolylineOptions().add()
//
//    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

}
