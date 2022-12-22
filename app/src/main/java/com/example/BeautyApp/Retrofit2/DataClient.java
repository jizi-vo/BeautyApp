package com.example.BeautyApp.Retrofit2;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface DataClient {
    @Multipart
    @POST("uploadimguser.php")
    Call<String> upLoadImg(@Part MultipartBody.Part img);

    @Multipart
    @POST("uploadimgmon.php")
    Call<String> upLoadImgMon(@Part MultipartBody.Part img);

    @Multipart
    @POST("uploadimgcategory.php")
    Call<String> uploadImgCategory(@Part MultipartBody.Part img);

    @FormUrlEncoded
    @POST("uploadMon.php")
    Call<String> uploadMon(@Field("tenmon") String tenmon,
                            @Field("gia") String gia,
                           @Field("maloai") String maloai,
                            @Field("hinhanh") String hinhanh);

    @FormUrlEncoded
    @POST("updatemon.php")
    Call<String> updateMon(@Field("mamon") String mamon,
                           @Field("tenmon") String tenmon,
                           @Field("gia") String gia,
                           @Field("maloai") String maloai,
                           @Field("hinhanh") String hinhanh);

    @FormUrlEncoded
    @POST("uploadrating.php")
    Call<String> uploadRating(@Field("mamon") String mamon,
                           @Field("matk") String matk,
                           @Field("rating") String rating,
                           @Field("cmt") String cmt,
                           @Field("date") String date);

    @FormUrlEncoded
    @POST("updateratinguser.php")
    Call<String> updateratinguser(@Field("mamon") String mamon,
                              @Field("matk") String matk,
                              @Field("rating") String rating,
                              @Field("cmt") String cmt,
                              @Field("date") String date);

    @FormUrlEncoded
    @POST("updateuser.php")
    Call<String> updateUser(@Field("matk") String matk,
                            @Field("tentk") String tentk,
                            @Field("diachi") String diachi,
                            @Field("hinhanh") String hinhanh);
    @FormUrlEncoded
    @POST("updateusernoimg.php")
    Call<String> updateUserNoImg(@Field("matk") String matk,
                            @Field("tentk") String tentk,
                            @Field("diachi") String diachi);

    @FormUrlEncoded
    @POST("changepass.php")
    Call<String> changePass(@Field("matk") String matk,
                            @Field("mk") String mk);
    @FormUrlEncoded
    @POST("updateQuyen.php")
    Call<String> updateQuyen(@Field("sdt") String sdt,
                            @Field("quyen") String quyen);

    @FormUrlEncoded
    @POST("xoamon.php")
    Call<String> xoaMon(@Field("mamon") String mamon);

    @FormUrlEncoded
    @POST("updatettmon.php")
    Call<String> updatettmon(@Field("tt") int tt,
                             @Field("mamon") String mamon);

    @FormUrlEncoded
    @POST("updaterating.php")
    Call<String> updaterating(@Field("mamon") String mamon,
                              @Field("rating") String rating);

    @FormUrlEncoded
    @POST("xoaloaimon.php")
    Call<String> deleteCategory(@Field("maloai") String maloai);

    @FormUrlEncoded
    @POST("updatetrangthai.php")
    Call<String> updateTrangThai(@Field("mahd") String mhd,
                                 @Field("tt") String tt);

    @FormUrlEncoded
    @POST("updatetrangthaicthd.php")
    Call<String> updateTrangThaiCTHD(@Field("mahd") String mhd,
                                 @Field("tt") String tt);

    @FormUrlEncoded
    @POST("uploadloaimon.php")
    Call<String> uploadCategory(@Field("tenloai") String tenloai,
                                 @Field("hinhanh") String hinhanh);

    @FormUrlEncoded
    @POST("updateloaimon.php")
    Call<String> updateCategory(@Field("maloai") String maloai,
                                @Field("tenloai") String tenloai,
                                @Field("hinhanh") String hinhanh);

    @FormUrlEncoded
    @POST("checkaccount.php")
    Call<String> checkaccount(@Field("sdt") String sdt);

    @FormUrlEncoded
    @POST("forgotpass.php")
    Call<String> forgotpass(@Field("sdt") String sdt,
                            @Field("pass") String pass);

    @FormUrlEncoded
    @POST("loadratingupdate.php")
    Call<String> loadRatingUpdate(@Field("mamon") String mamon,
                            @Field("matk") String matk);
}
