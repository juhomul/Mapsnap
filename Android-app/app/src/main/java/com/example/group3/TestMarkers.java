package com.example.group3;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TestMarkers extends MapsActivity {

    private GoogleMap mMap;

    TestMarkers test1 = new TestMarkers("Oulu1", 65.0121, 25.4651);
    TestMarkers test2 = new TestMarkers("Oulu2", 65.1121, 25.5651);
    TestMarkers test3 = new TestMarkers("Oulu3", 65.2121, 25.3651);
    TestMarkers test4 = new TestMarkers("Oulu4", 65.0121, 25.2651);

    public TestMarkers(GoogleMap mMap){
        this.mMap = mMap;
    }

    public TestMarkers(String markTitle, double markLat, double markLong){

        LatLng markLatLon = new LatLng(markLat, markLong);
        MarkerOptions marker = new MarkerOptions().position(markLatLon)
                .title(markTitle);

        mMap.addMarker(marker);
    }
    /**
     * Ei toimi ei wörkki
     *
     */
}
