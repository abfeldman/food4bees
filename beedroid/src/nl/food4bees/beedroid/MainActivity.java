package nl.food4bees.beedroid;

import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import android.os.Build;
import android.os.Bundle;

import android.app.ActivityManager;
import android.app.AlertDialog;

import android.view.View;

import android.util.Log;

import android.graphics.Color;

import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;

import android.content.pm.ConfigurationInfo;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockFragmentActivity implements OnMapClickListener,
                                                                      OnMarkerDragListener,
                                                                      OnMarkerClickListener {
    private static final String TAG = MainActivity.class.getName();

    private static final LatLng EINDHOVEN = new LatLng(51.441637, 5.469732);

    private GoogleMap mMap;

    private List<Marker> mMarkers;
    private Polygon mPolygon;

    private Menu mMenu = null;

    static final int AUTHENTICATE = 1;
    static final int PICK_PLANT_NAME = 2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (!supportsEs2) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Device Compatibility Error");
            alertDialog.setMessage("Your device does not support OpenGL ES 2.0 or higher.");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                });
            alertDialog.show();
        }

        try {
            if (!DatabaseHelper.existsDatabase("plant.db")) {
                DatabaseHelper.copyDatabase(this, "plant.db");
            }
        } catch (IOException e) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Application Error");
            alertDialog.setMessage("Error copying plants database.");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();
                    }
                });
            alertDialog.show();

            Log.e(TAG, "Plants database set-up exception: ", e);
        }

        setUpMapOnce();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (CookieStoreStore.getInstance().getCookieStore() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, AUTHENTICATE);
        }

        if (mMarkers.size() >= 3) {
            createPolygon();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        nl.food4bees.beedroid.Polygon polygon = makePolygon();

        savedInstanceState.putSerializable("polygon", (Serializable)polygon);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        nl.food4bees.beedroid.Polygon polygon = (nl.food4bees.beedroid.Polygon)savedInstanceState.getSerializable("polygon");

        mMarkers = new ArrayList<Marker>();

        for (int i = 0; i < polygon.size(); i++) {
            LatLng point = new LatLng(polygon.getPoint(i).getX(), polygon.getPoint(i).getY());
            Marker marker = mMap.addMarker(new MarkerOptions().position(point).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));

            mMarkers.add(marker);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;

        MenuInflater menuInflater = getSupportMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        if (mMarkers == null || mMarkers.size() < 3) {
            mMenu.findItem(R.id.menu_plants_names).setVisible(false);
        } else {
            mMenu.findItem(R.id.menu_plants_names).setVisible(true);
        }

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_plants_names:
                Intent intent = new Intent(this, PlantsNamesActivity.class);
                startActivityForResult(intent, PICK_PLANT_NAME);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpMapOnce() {
        if (mMap == null) {
            mMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            mMarkers = new ArrayList<Marker>();

            setUpMap();
        }
    }

    private void setUpMap() {
        if (mMap != null) {
            mMap.setOnMapClickListener(this);
            mMap.setOnMarkerClickListener(this);
            mMap.setOnMarkerDragListener(this);
            mMap.setMyLocationEnabled(true);

/*
            mMap.moveCamera(CameraUpdateFactory.newLatLng(EINDHOVEN));
*/
        }
    }

    public void reset(View view) {
        if (mMarkers != null) {
            mMarkers.clear();
        }
        if (mPolygon != null) {
            mPolygon.remove();
        }
        if (mMap != null) {
            mMap.clear();
        }
        invalidateOptionsMenu();
    }

    @Override
    public void onMapClick(LatLng point) {
        assert(mMarkers != null);

        Marker marker = mMap.addMarker(new MarkerOptions().position(point).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));

        mMarkers.add(marker);

        if (mMarkers.size() == 3) {
            createPolygon();
        } else if (mMarkers.size() > 3) {
            updatePolygon();
        }

        invalidateOptionsMenu();
    }

    private void createPolygon() {
        List<LatLng> markers = new ArrayList<LatLng>();
        for (Marker marker : mMarkers) {
            markers.add(marker.getPosition());
        }
        mPolygon = mMap.addPolygon(new PolygonOptions().addAll(markers).strokeColor(Color.argb(255, 0, 0, 255)).fillColor(Color.argb(125, 0, 0, 255)));
        mPolygon.setStrokeWidth(3);
    }

    public void updatePolygon() {
        List<LatLng> markers = new ArrayList<LatLng>();
        for (Marker marker : mMarkers) {
            markers.add(marker.getPosition());
        }

        assert(mPolygon != null);

        mPolygon.setPoints(markers);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        updatePolygon();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
/*
        int index = mMapLand.getmMarkers().indexOf(marker);
        if (index != -1) {
            mMapLand.getmMarkers().remove(index);
            marker.remove();
            if (mMapLand.getmMarkers().size() >= 3) {
                mMapLand.updatePolygon(mMapLand.getmMarkers());
            } else if (mMapLand.getmPolygon() != null) {
                mMapLand.getmPolygon().remove();
            }
        }
        invalidateOptionsMenu();
*/
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        int plant_id = -1;
        int percentage = -1;

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_PLANT_NAME:
                    plant_id = data.getIntExtra("plant_id", -1);
                    percentage = data.getIntExtra("percentage", -1);

                    assert(plant_id != -1);
                    assert(percentage != -1);

                    addVegetation(plant_id, new Double(percentage));

                    break;
                default:
                    break;
            }
        }
    }

    private void addVegetation(Integer plantId, Double percentage) {
        Vegetation vegetation = new Vegetation(plantId, makePolygon(), percentage);

        new UploadVegetationTask(this).execute(vegetation);
    }

    private nl.food4bees.beedroid.Polygon makePolygon() {
        nl.food4bees.beedroid.Polygon polygon = new nl.food4bees.beedroid.Polygon();

        for (Marker marker : mMarkers) {
            LatLng coordinate = marker.getPosition();

            polygon.addPoint(new Point(coordinate.longitude, coordinate.latitude));
        }

        return polygon;
    }
}
