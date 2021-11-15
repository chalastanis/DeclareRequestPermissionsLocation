package com.example.declarerequestpermissionslocation;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


/* this is going to be an example app for how to ask for permissions
that needed to fetch the current location */
public class MainActivity extends AppCompatActivity {
    private TextView textLocation;
    private Button buttonGetLoc;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonGetLoc = findViewById(R.id.buttonGetLoc);
        textLocation = findViewById(R.id.textLocation);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        
        buttonGetLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchLocation();
            }
        });
    }

    // In this method we will ask for permissions. When permissions is granted, we can apply the
    // code that we need to get the location
    private void fetchLocation() {
        // here we check if we have permissions
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
//            performAction(...);

            // Here we use the getLastLocation to show the user the location
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                String latitude = String.valueOf(location.getLatitude());
                                String longitude = String.valueOf(location.getLongitude());
                                textLocation.setText("Lat: "+latitude+"\nLon: "+longitude);
                            }
                        }
                    });
            // Here is the situation that user has denied to give permission at first so when user will go
            // to press the button to get location we alert him that he have to accept the permissions
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            // showInContextUI(...);
            new AlertDialog.Builder(this)
                    .setTitle("Required Location Permission")
                    .setMessage("You have to give this permission to access this feature")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Ask for permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
            // here is the situation that the first check (if statement) failed because the user comes
            // for first time to use the app so we will ask for permissions we need.
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            // requestPermissionLauncher.launch(
            // Manifest.permission.REQUESTED_PERMISSION);

            // Ask for permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);


        }

    }
        // here we put some code just in case we need to to something
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //here we know that permission granted
            }else{
                //here permission denied
            }
        }
    }

}
