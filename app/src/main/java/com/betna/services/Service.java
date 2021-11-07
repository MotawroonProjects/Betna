package com.betna.services;


import com.betna.models.CategoryDataModel;
import com.betna.models.OrderDataModel;
import com.betna.models.RateDataModel;
import com.betna.models.ServiceDataModel;
import com.betna.models.SettingDataModel;
import com.betna.models.SingleServiceDataModel;
import com.betna.models.SliderDataModel;
import com.betna.models.StatusResponse;
import com.betna.models.TypeDataModel;
import com.betna.models.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {


    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(
            @Field("phone") String phone

    );

    @FormUrlEncoded
    @POST("api/register")
    Call<UserModel> signUp(@Field("first_name") String first_name,
                           @Field("last_name") String last_name,
                           @Field("phone") String phone

    );

    @Multipart
    @POST("api/register")
    Call<UserModel> signUpWithImage(@Part("first_name") RequestBody first_name,
                                    @Part("last_name") RequestBody last_name,
                                    @Part("phone") RequestBody phone,
                                    @Part MultipartBody.Part logo


    );

    @GET("api/bestServices")
    Call<ServiceDataModel> getTopService(
    );

    @GET("api/departments")
    Call<CategoryDataModel> getDepartments(
    );

    @GET("api/searchServices")
    Call<ServiceDataModel> searchService(
            @Query("department_id") String department_id,
            @Query("title") String title
    );

    @GET("api/orders")
    Call<OrderDataModel> getOrders(
            @Query("user_id") int user_id
    );

    @GET("api/getRates")
    Call<RateDataModel> getRates(
            @Query("user_id") int user_id
    );

    @GET("api/servicesPlacesType")
    Call<TypeDataModel> getTYpe(
    );

    @FormUrlEncoded
    @POST("api/contactUs")
    Call<StatusResponse> contactUs(@Field("name") String name,
                                   @Field("email") String email,
                                   @Field("title") String title,
                                   @Field("message") String message


    );

    @GET("api/slider")
    Call<SliderDataModel> get_slider(
    );

    @FormUrlEncoded
    @POST("api/storeRate")
    Call<StatusResponse> addRate(
            @Field("user_id") String user_id,
            @Field("service_id") String service_id,
            @Field("desc") String desc,
            @Field("rate") int rate

    );

    @GET("api/servicesByDepartment")
    Call<ServiceDataModel> getservice(
            @Query("department_id") String department_id
    );

    @GET("api/oneServices")
    Call<SingleServiceDataModel> getServiceById(@Query("service_id") int service_id
    );

    @FormUrlEncoded
    @POST("api/storeOrder")
    Call<StatusResponse> storeOrder(@Field("user_id") String user_id,
                                    @Field("service_id") String service_id,
                                    @Field("type_id") String type_id,
                                    @Field("area") String area,
                                    @Field("longitude") String longitude,
                                    @Field("latitude") String latitude,
                                    @Field("notes") String notes,
                                    @Field("total") String total,
                                    @Field("date") String date

    );
    @GET("api/aboutUs")
    Call<SettingDataModel> getSetting();

}