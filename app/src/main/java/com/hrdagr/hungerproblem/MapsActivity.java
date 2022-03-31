package com.hrdagr.hungerproblem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hrdagr.hungerproblem.databinding.ActivityMaps2Binding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMaps2Binding binding;
    private FloatingActionButton BtnAddNew;
    DatabaseReference myRef;
    FirebaseDatabase database;
    double ch[] = new double[10];
    String ch1[] = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BtnAddNew = findViewById(R.id.BtnAddNew);

        optfunct();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        BtnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Openmain();



            }
        });

    }

    private void optfunct() {
        SimpleDateFormat sd = new SimpleDateFormat("kkmmssSSS");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Hunger");
        Date d = new Date();
        CharSequence s = DateFormat.format("ddMMyyy", d.getTime());
        CharSequence s2 = sd.format(d.getTime());
        myRef = database.getReference("Hunger");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Hunger");

        rootRef.child(s.toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Openmain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng onept = new LatLng(22.080924, 82.156915);
        mMap.addMarker(new MarkerOptions().position(onept).title("Hrd's Supply for Cheap Food"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(onept));

        LatLng onept2 = new LatLng(22.080586, 82.156336);
        mMap.addMarker(new MarkerOptions().position(onept2).title("Hrd2's Supply for Free Food"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(onept2));

    }
}