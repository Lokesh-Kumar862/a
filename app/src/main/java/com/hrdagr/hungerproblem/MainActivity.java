package com.hrdagr.hungerproblem;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    DatabaseReference myRef;
    FirebaseDatabase database;

    private EditText TxtName, TxtDur, TxtNarr, TxtType;
    private TextView TxtLong, TxtLati;
    private Button BtnGetLoc, BtnCreate;
    double Lati,Long;
    LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions( this,
        new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        TxtName = (EditText) findViewById(R.id.TxtName);
        TxtDur = (EditText) findViewById(R.id.TxtDur);
        TxtNarr = (EditText) findViewById(R.id.TxtNarr);
        TxtType = (EditText) findViewById(R.id.TxtType);
        TxtLong = (TextView) findViewById(R.id.TxtLong);
        TxtLati = (TextView) findViewById(R.id.TxtLati);
        BtnGetLoc = (Button) findViewById(R.id.BtnGetLoc);
        BtnCreate = (Button) findViewById(R.id.BtnCreate);
        SimpleDateFormat sd = new SimpleDateFormat("kkmmssSSS");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Hunger");
        Date d = new Date();
        CharSequence s = DateFormat.format("ddMMyyy", d.getTime());
        CharSequence s2 = sd.format(d.getTime());
        myRef = database.getReference("Hunger");

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Hunger");
        DatabaseReference userNameRef = rootRef.child(s.toString()).child(s2.toString());


        BtnCreate.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Lati != -1
                        && TxtName.getText().toString() != ""
                        && TxtDur.getText().toString() != ""
                        && TxtType.getText().toString() != ""
                    ){
                    rootRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userNameRef.child("Author").setValue(TxtName.getText().toString());
                            userNameRef.child("Lati").setValue(Lati);
                            userNameRef.child("Long").setValue(Long);
                            userNameRef.child("Dur").setValue(Integer.parseInt( TxtDur.getText().toString()));
                            Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "Their was an unusual", Toast.LENGTH_SHORT).show();

                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "Pls Check All Values", Toast.LENGTH_SHORT).show();
                }
            }
        }));

        BtnGetLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {
                    getLocation();
                }
            }
        });
    }
    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                Lati = locationGPS.getLatitude();
                Long = locationGPS.getLongitude();
                TxtLong.setText( Long +"");
                TxtLati.setText( Lati +"");
            } else {
                Toast.makeText(this, "Unable to find location. Looking For last Location", Toast.LENGTH_SHORT).show();

                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    Toast.makeText(MainActivity.this, "Unable to find last location. Pls Try Again", Toast.LENGTH_SHORT).show();
                                }
                                Lati = location.getLatitude();
                                Long = location.getLongitude();
                                TxtLong.setText( Long +"");
                                TxtLati.setText( Lati +"");
                            }
                        });

            }
        }
    }

}