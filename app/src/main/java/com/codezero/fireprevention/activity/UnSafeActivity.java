package com.codezero.fireprevention.activity;

/**
 * Created by GyungDal on 2016-04-21.
 * TODO : Daum 지도 API를 사용하여 마크를 찍음
 * http://apis.map.daum.net/android/guide/
 */
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.codezero.fireprevention.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
/**
 * Created by GyungDal on 2016-04-21.
 * TODO : 다른 기기들에서 주소를 받아서 마커 생성
 */
public class UnSafeActivity extends AppCompatActivity implements
                            MapView.MapViewEventListener, MapView.POIItemEventListener,
                            MapView.OpenAPIKeyAuthenticationResultListener {

    private static final String TAG = "UnSafe Acitivty";
    private MapPoint Center = MapPoint.mapPointWithGeoCoord(36.391883, 127.363350);
    private static final MapPoint SCHOOL = MapPoint.mapPointWithGeoCoord(36.391883, 127.363350);
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsafe);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setToolbar();

        MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey(this.getString(R.string.DAUM_MAP_KEY));
        mapView.zoomIn(true);
        mapView.zoomOut(true);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this); //this에 MapView.MapViewEventListener 구현
        mapView.setPOIItemEventListener(this);
        mapView.setMapCenterPointAndZoomLevel(SCHOOL, 0, true);

        MapPOIItem mSchool = new MapPOIItem();
        mSchool.setItemName("부리야!!!!");
        mSchool.setTag(0);
        mSchool.setMapPoint(Center);
        mSchool.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        mSchool.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(mSchool);


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }
    private void setToolbar(){
        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Log.i(TAG,"Unsafe Activity Finish");
                }
            });
        }
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
        Snackbar.make(getCurrentFocus(), "???", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
        Log.i(TAG, "X : " + mapPoint.getMapPointScreenLocation().x + ", Y : " + mapPoint.getMapPointScreenLocation().y);
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        Log.i(TAG, "X : " + mapPoint.getMapPointScreenLocation().x + ", Y : " + mapPoint.getMapPointScreenLocation().y);
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}
