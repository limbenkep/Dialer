package se.miun.holi1900.dt031g.dialer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import se.miun.holi1900.dt031g.dialer.database.CallInfo;
import se.miun.holi1900.dt031g.dialer.database.CallInfoRepository;
import se.miun.holi1900.dt031g.dialer.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private ActivityMapsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLngBounds latLngBounds = new LatLngBounds( new LatLng(55.001099, 11.10694), new LatLng(69.063141,
                24.16707));
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
        markCallLocations();
        mMap.setOnInfoWindowClickListener(this);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);

    }

    /**
     * Marks locations calls were made on the map and
     * when the location icon is clicked the phone number and
     * the date is displayed
     */
    private void markCallLocations(){
        new CallInfoRepository(this)
                .getAllCallInfo().observe(this, callData->{
                    if(callData.size()!=0){
                        for(int i=0; i<callData.size(); i++){
                            CallInfo callInfo = callData.get(i);
                            if(callInfo.latitude!=0 && callInfo.longitude!=0){
                                LatLng latLng = new LatLng(callInfo.latitude, callInfo.longitude);
                                mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title(callInfo.phoneNumber)
                                        .snippet(callInfo.date));
                            }
                        }
                    }
                });
    }


    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        marker.showInfoWindow();
    }
}