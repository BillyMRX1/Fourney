package com.bearbrand.fourney;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bearbrand.fourney.model.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeoConstants {
    static CollectionReference ref;
    private static final String TAG = GeoConstants.class.getSimpleName();

    private GeoConstants() {
    }

    private static final String PACKAGE_NAME = "com.google.android.gms.location.Geofence";

    static final String GEOFENCES_ADDED_KEY = PACKAGE_NAME + ".GEOFENCES_ADDED_KEY";

    /**
     * Used to set an expiration time for a geofence. After this amount of time Location Services
     * stops tracking the geofence.
     */
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;

    /**
     * For this sample, geofences expire after twelve hours.
     */
    static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * 60 * 60 * 1000;
    static final float GEOFENCE_RADIUS_IN_METERS = 10; // 1 mile, 1.6 km

    /**
     * Map for storing information about airports in the San Francisco bay area.
     */
    static HashMap<String, LatLng>  BAY_AREA_LANDMARKS = new HashMap<>();

//
    static {
//    ref = FirebaseFirestore.getInstance().collection("place");
//    ref.addSnapshotListener (new EventListener<QuerySnapshot>() {
//
//        @Override
//        public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
//
//            if (value != null){
//                for (DocumentSnapshot document : value.getDocuments()) {
//                    Log.d(TAG, document.getId()+ " => " + document.getString("title"));
//                    BAY_AREA_LANDMARKS.put(document.getString("title"), new LatLng(37.621313, -122.378955));
//
//                }
//
//            }
//        }
//
//    });
////           ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
////               @Override
////               public void onComplete(@NonNull Task<QuerySnapshot> task) {
////                   TEMP_AREA_LANDMARKS = new HashMap<>();
////                   if (task.isSuccessful()) {
////                           for (QueryDocumentSnapshot document : task.getResult()) {
////                               Log.d(TAG, document.getId()+ " => " + document.getString("title"));
////                               TEMP_AREA_LANDMARKS.put(document.getString("title"), new LatLng(37.621313, -122.378955));
////
////                       }
////                   }
////               }
////
////           });
////        BAY_AREA_LANDMARKS.putAll(LandmarkData.getListData());

        // San Francisco International Airport.
        BAY_AREA_LANDMARKS.put("JATIM_PARK_1", new LatLng(37.621313, -122.378955));

        // Googleplex.
        BAY_AREA_LANDMARKS.put("RUMAH_SASI", new LatLng(0.5500, 117.5731));

    }
}
