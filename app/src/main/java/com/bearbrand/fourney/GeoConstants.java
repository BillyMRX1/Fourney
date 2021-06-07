package com.bearbrand.fourney;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bearbrand.fourney.model.Place;
import com.google.android.gms.location.Geofence;
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

import org.jetbrains.annotations.NotNull;

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
    static final float GEOFENCE_RADIUS_IN_METERS = 500;


    static HashMap<String, LatLng>  BAY_AREA_LANDMARKS = new HashMap<>();

//
    static {
//        BAY_AREA_LANDMARKS.put("Predator Fun Park", new LatLng(-7.912344094580394, 112.5484513703529));
//        BAY_AREA_LANDMARKS.put("Jatim Park 1", new LatLng(-7.884546331605805, 112.52488841751503));
//        BAY_AREA_LANDMARKS.put("My Home", new LatLng(-0.4663570833006945, 116.94818693131232));
//        BAY_AREA_LANDMARKS.put("Rumah Sasi", new LatLng(0.5500, 117.5731));



    }
}
