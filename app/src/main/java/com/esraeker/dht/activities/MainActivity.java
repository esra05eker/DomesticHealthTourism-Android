package com.esraeker.dht.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.core.app.ActivityCompat;

import com.esraeker.dht.DhtApplication;
import com.esraeker.dht.R;
import com.esraeker.dht.adapters.CityAdapter;
import com.esraeker.dht.adapters.ClinicAdapter;
import com.esraeker.dht.adapters.DistrictAdapter;
import com.esraeker.dht.adapters.DoctorAdapter;
import com.esraeker.dht.adapters.HospitalAdapter;
import com.esraeker.dht.entities.City;
import com.esraeker.dht.entities.Clinic;
import com.esraeker.dht.entities.District;
import com.esraeker.dht.entities.Doctor;
import com.esraeker.dht.entities.Hospital;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    static final int MY_ACCESS_FINE_LOCATIONREQUEST_CODE = 100;

    Spinner spnCity, spnDistrict, spnHospital, spnClinic, spnDoctor;
    EditText txtAppoinmentDate;
    Button btnSearch;

    private CityAdapter cityAdapter;
    private DistrictAdapter districtAdapter;
    private HospitalAdapter hospitalAdapter;
    private ClinicAdapter clinicAdapter;
    private DoctorAdapter doctorAdapter;

    private Location latestLocation;

    private int locationCityId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        // Eğer giriş yapılmamış ise LoginActivity'ye yönlendir.
        if (!isLoggedIn ( )) {
            startActivity (LoginActivity.class);
            finish ( );
        }
        setTitle (R.string.make_appoinment);

        spnCity = findViewById (R.id.spnCity);
        spnDistrict = findViewById (R.id.spnDistrict);
        spnHospital = findViewById (R.id.spnHospital);
        spnClinic = findViewById (R.id.spnClinic);
        spnDoctor = findViewById (R.id.spnDoctor);
        txtAppoinmentDate = findViewById (R.id.txtAppoinmentDate);
        btnSearch = findViewById (R.id.btnSearch);

        cityAdapter = new CityAdapter (this);
        districtAdapter = new DistrictAdapter (this);
        hospitalAdapter = new HospitalAdapter (this);
        clinicAdapter = new ClinicAdapter (this);
        doctorAdapter = new DoctorAdapter (this);

        spnCity.setAdapter (cityAdapter);
        spnDistrict.setAdapter (districtAdapter);
        spnHospital.setAdapter (hospitalAdapter);
        spnClinic.setAdapter (clinicAdapter);
        spnDoctor.setAdapter (doctorAdapter);

        spnCity.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener ( ) {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // birinci elemena Seçin elemanı olduğu için onu yoksaymamız gerekiyor diğer elemanlar bizim ulşaşmak istediğimiz elemanlar
                if (position == 0) {
                    districtAdapter.clear ( );
                    return;
                } else {
                    position = position - 1;
                }
                City city = (City) parent.getItemAtPosition (position);
                fillDistrictList (city.Id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnDistrict.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener ( ) {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    hospitalAdapter.clear ( );
                    return;
                } else {
                    position = position - 1;
                }
                District district = (District) parent.getItemAtPosition (position);
                fillHospitalList (district.Id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnHospital.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener ( ) {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    clinicAdapter.clear ( );
                    return;
                } else {
                    position = position - 1;
                }
                Hospital hospital = (Hospital) parent.getItemAtPosition (position);
                fillClinicList (hospital.Id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnClinic.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener ( ) {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    doctorAdapter.clear ( );
                    return;
                } else {
                    position = position - 1;
                }
                Clinic clinic = (Clinic) parent.getItemAtPosition (position);
                fillDoctorList (clinic.Id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtAppoinmentDate.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                // Şimdiki zaman bilgilerini alıyoruz. güncel yıl, güncel ay, güncel gün.
                final Calendar takvim = Calendar.getInstance ( );
                int yil = takvim.get (Calendar.YEAR);
                int ay = takvim.get (Calendar.MONTH);
                int gun = takvim.get (Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog (MainActivity.this,
                        new DatePickerDialog.OnDateSetListener ( ) {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // ay değeri 0 dan başladığı için (Ocak=0, Şubat=1,..,Aralık=11)
                                // değeri 1 artırarak gösteriyoruz.
                                month += 1;
                                // year, month ve dayOfMonth değerleri seçilen tarihin değerleridir.
                                // Edittextte bu değerleri gösteriyoruz.
                                txtAppoinmentDate.setText (year + "-" + month + "-" + dayOfMonth);
                            }
                        }, yil, ay, gun);

                dpd.getDatePicker ( ).setMinDate (Calendar.getInstance ( ).getTimeInMillis ( ));

                // dialog penceresinin button bilgilerini ayarlıyoruz ve ekranda gösteriyoruz.
//                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Select", dpd);
//                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", dpd);
                dpd.show ( );

            }
        });

        fillCityList ( );

        btnSearch.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                try {

                    City city = (City) spnCity.getItemAtPosition (spnCity.getSelectedItemPosition ( ) - 1);
                    District district = (District) spnDistrict.getItemAtPosition (spnDistrict.getSelectedItemPosition ( ) - 1);
                    Hospital hospital = (Hospital) spnHospital.getItemAtPosition (spnHospital.getSelectedItemPosition ( ) - 1);
                    Clinic clinic = (Clinic) spnClinic.getItemAtPosition (spnClinic.getSelectedItemPosition ( ) - 1);
                    Doctor doctor = (Doctor) spnDoctor.getItemAtPosition (spnDoctor.getSelectedItemPosition ( ) - 1);

                    if (city == null ||
                            district == null ||
                            hospital == null ||
                            clinic == null ||
                            doctor == null ||
                            TextUtils.isEmpty (txtAppoinmentDate.getText ( ))) {
                        showToast (getString (R.string.select_doctor_and_date));
                        return;
                    }

                    String appoinmentDate = txtAppoinmentDate.getText ( ).toString ( );

                    Intent i = new Intent (MainActivity.this, ListingDoctorsActivity.class);
                    i.putExtra ("cityId", city.Id);
                    i.putExtra ("districtId", district.Id);
                    i.putExtra ("hospitalId", hospital.Id);
                    i.putExtra ("hospitalClinicId", clinic.Id);

                    i.putExtra ("doctorId", doctor.Id);
                    i.putExtra ("date", appoinmentDate);

                    startActivity (i);


                } catch (Exception e) {
                    showToast (getString (R.string.select_doctor_and_date));
                }

            }
        });

        getLocation ( );
    }

    void fillCityList() {
        DhtApplication.getDbService ( ).getCities ( ).enqueue (new Callback<List<City>> ( ) {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                cityAdapter.addNewItems (response.body ( ));
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {

            }
        });
    }

    void fillDistrictList(int cityId) {
        DhtApplication.getDbService ( ).getDistrics (cityId).enqueue (new Callback<List<District>> ( ) {
            @Override
            public void onResponse(Call<List<District>> call, Response<List<District>> response) {
                districtAdapter.addNewItems (response.body ( ));
            }

            @Override
            public void onFailure(Call<List<District>> call, Throwable t) {

            }
        });
    }

    void fillHospitalList(int districtId) {
        DhtApplication.getDbService ( ).getHospitals (districtId).enqueue (new Callback<List<Hospital>> ( ) {
            @Override
            public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
                hospitalAdapter.addNewItems (response.body ( ));
            }

            @Override
            public void onFailure(Call<List<Hospital>> call, Throwable t) {

            }
        });
    }

    void fillClinicList(int hospitalId) {
        DhtApplication.getDbService ( ).getClinics (hospitalId).enqueue (new Callback<List<Clinic>> ( ) {
            @Override
            public void onResponse(Call<List<Clinic>> call, Response<List<Clinic>> response) {
                clinicAdapter.addNewItems (response.body ( ));
            }

            @Override
            public void onFailure(Call<List<Clinic>> call, Throwable t) {

            }
        });
    }

    void fillDoctorList(int hospitalClinicId) {
        DhtApplication.getDbService ( ).getDoctors (hospitalClinicId).enqueue (new Callback<List<Doctor>> ( ) {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                doctorAdapter.addNewItems (response.body ( ));
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {

            }
        });
    }

    protected void getLocation() {
        // izin verilmiş mi kontrol et verilmiş ise
        if (ActivityCompat.checkSelfPermission (this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // son bilinen konum bilgisi al
            FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient (this);
            mFusedLocationClient.getLastLocation ( )
                    .addOnSuccessListener (this, new OnSuccessListener<Location> ( ) {
                        @Override
                        public void onSuccess(Location location) {
                            // konum var ise
                            if (location != null) {
                                // son bilinen konum değişenine ata
                                latestLocation = location;


                                Geocoder geocoder = new Geocoder (MainActivity.this, Locale.getDefault ( ));

                                try {
                                    // adresten posta kodunu bul
                                    List<Address> addresses = geocoder.getFromLocation (location.getLatitude ( ), location.getLongitude ( ), 1);
                                    for (Address address : addresses) {
                                        // posta kodundaki ilk iki sayıyı al
                                        locationCityId = Integer.parseInt (address.getPostalCode ( ).substring (0, 2));
                                        // posta kodundan alınan ilk plaka koduna göre şehiri seç

                                        if (spnCity.getAdapter ( ).getCount ( ) > locationCityId)
                                            spnCity.setSelection (locationCityId);
                                    }
                                } catch (IOException e) {

                                }

                            }
                        }
                    });

        } else {
            showToast (getString (R.string.loc_permission));
            // İzin iste
            ActivityCompat.requestPermissions (this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_ACCESS_FINE_LOCATIONREQUEST_CODE);
        }
    }


}
