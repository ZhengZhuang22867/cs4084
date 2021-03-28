package com.gx.emergency.ui.Escape;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gx.emergency.MyApplication;
import com.gx.emergency.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
/**
 * 逃生页面
 */
public class EscapeFragment extends Fragment implements OnMapReadyCallback{
    private Activity myActivity;
    private MyApplication myApplication;
    private GoogleMap mMap;
    private  SupportMapFragment mapFragment;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myActivity= (Activity) context;
        myApplication= (MyApplication) myActivity.getApplication();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_escape,container,false);
        initView();
        mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.incident_map);
        mapFragment.getMapAsync(this);
        return view;
    }

    private void initView() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
