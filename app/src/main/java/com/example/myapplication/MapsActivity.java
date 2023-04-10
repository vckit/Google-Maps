package com.example.myapplication;
import android.Manifest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Проверяем, поддерживается ли устройство сканер отпечатков пальцев.
        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
        if (fingerprintManager.isHardwareDetected()) {

            // Проверяем, имеет ли ваше приложение разрешение на использование сканера отпечатков пальцев.
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED) {

                // Аутентифицируем пользователя через сканер отпечатков пальцев.
                fingerprintManager.authenticate(null, null, 0, new FingerprintManager.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);

                        // Открываем карту, если аутентификация прошла успешно.
                        setContentView(R.layout.activity_maps);

                        // Получаем объект SupportMapFragment и уведомляем о том, что карта готова для использования.
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(MapsActivity.this);
                    }
                }, null);
            }
        }
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
                    TextView selectedAddressTextView = findViewById(R.id.selected_address_text_view);
                    selectedAddressTextView.setText(address.getAddressLine(0));
                }
            }
        });
    }
}
