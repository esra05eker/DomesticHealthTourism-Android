package com.esraeker.dht;

import com.esraeker.dht.entities.Appoinment;
import com.esraeker.dht.entities.City;
import com.esraeker.dht.entities.Clinic;
import com.esraeker.dht.entities.District;
import com.esraeker.dht.entities.Doctor;
import com.esraeker.dht.entities.Hospital;
import com.esraeker.dht.entities.Patient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DbService {

    @GET("login")
    Call<Patient> login(@Query("tcNo") String tcNo, @Query("password") String password);

    @POST("updateProfile")
    Call<Patient> updateProfile(@Body Patient patient);

    @POST("register")
    Call<Patient> register(@Body Patient patient);

    @GET("getCities")
    Call<List<City>> getCities();

    @GET("getDistrics")
    Call<List<District>> getDistrics(@Query("cityId") int cityId);

    @GET("getHospitals")
    Call<List<Hospital>> getHospitals(@Query("districtId") int districtId);

    @GET("getClinics")
    Call<List<Clinic>> getClinics(@Query("hospitalId") int hospitalId);

    @GET("getDoctors")
    Call<List<Doctor>> getDoctors(@Query("hospitalClinicId") int hospitalClinicId);

    @GET("searchAppoinment")
    Call<List<Appoinment>> searchAppoinment(@Query("doctorId") int doctorId, @Query("date") String date);

    @GET("makeAppoinment")
    Call<Appoinment> makeAppoinment(@Query("doctorId") int doctorId, @Query("patientId") int patientId, @Query("dateTime") String dateTime);

    @GET("cancelAppoinment")
    Call<Boolean> cancelAppoinment(@Query("id") int id);

    @GET("myAppoinments")
    Call<List<Appoinment>> myAppoinments(@Query("patientId") int patientId);


}
