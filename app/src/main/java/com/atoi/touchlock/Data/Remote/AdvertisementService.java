package com.atoi.touchlock.Data.Remote;

import com.atoi.touchlock.POJO.Advertisement;
import com.atoi.touchlock.POJO.Photo;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AdvertisementService {

    @FormUrlEncoded
    @POST("advertise")
    Call<ResponseBody> publishAdvertisement(@Field("email") String email, @Field("userId") int userId, @Field("title") String title, @Field("type") String type,
                                            @Field("checkInDate") String checkInDate, @Field("checkOutDate") String checkOutDate, @Field("minDay") int minDay,
                                            @Field("nOfRoom") int nOfRoom, @Field("nOfBathroom") int nOfBathroom, @Field("nOfBed") int nOfBed,
                                            @Field("country") String country, @Field("city") String city, @Field("address") String address,
                                            @Field("price") int price, @Field("currency") String currency, @Field("desc") String desc,
                                            @Field("wifi") boolean wifi, @Field("petAllowed") boolean petAllowed, @Field("fireExt") boolean fireExt,
                                            @Field("airCond") boolean airCond, @Field("tv") boolean tv, @Field("basicNeeds") boolean basicNeeds);


    @FormUrlEncoded
    @POST("myAds")
    Call<List<Advertisement>> getMyAds(@Field("email") String email);


    @FormUrlEncoded
    @POST("deleteAd")
    Call<ResponseBody> deleteAd(@Field("id") int id);

    @FormUrlEncoded
    @POST("getPhotoWithName")
    Call<List<Photo>> getPhotosWithName(@Field("adName") String adName);

    @FormUrlEncoded
    @POST("search")
    Call<List<Advertisement>> findAd(@Field("country") String country, @Field("city") String city, @Field("checkInDate") String checkInDate,
                                     @Field("type") String type);
    @Multipart
    @POST("photoUpload")
    Call<ResponseBody> uploadPhoto(@Part("userId") RequestBody userId, @Part("adTitle") RequestBody adTitle,
                                   @Part MultipartBody.Part uploadfile);

}
