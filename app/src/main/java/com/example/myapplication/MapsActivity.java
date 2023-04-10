package com.example.myapplication;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView mSelectedAddressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mSelectedAddressTextView = findViewById(R.id.selected_address_text_view);

        // Получаем объект SupportMapFragment и уведомляем о том, что карта готова для использования.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Вызывается после того, как карта готова для использования.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Устанавливаем слушатель на карту для получения координат при выборе адреса.
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Преобразуем координаты в адрес.
                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Обновляем текстовое поле с выбранным адресом.
                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    mSelectedAddressTextView.setText(address.getAddressLine(0));
                }
            }
        });
    }
}
