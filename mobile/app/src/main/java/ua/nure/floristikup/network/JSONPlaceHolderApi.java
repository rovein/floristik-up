package ua.nure.floristikup.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ua.nure.floristikup.data.FloristShop;
import ua.nure.floristikup.data.FlowerStorage;
import ua.nure.floristikup.data.Flower;
import ua.nure.floristikup.data.Placement;
import ua.nure.floristikup.data.RedistributionResponseDto;
import ua.nure.floristikup.data.SmartDevice;

public interface JSONPlaceHolderApi {

    @POST("/auth/login")
    Call<FloristShop> login(@Body FloristShop floristShop);

    @GET("/florist-shops/{email}")
    Call<FloristShop> getFloristShopData(@Header("Authorization") String token, @Path("email") String email);

    @GET("/flowers")
    Call<ArrayList<Flower>> getFlowersData(@Header("Authorization") String token);

    @GET("/florist-shops/{email}/rooms")
    Call<ArrayList<Placement>> getPlacementsData(@Header("Authorization") String token, @Path("email") String email);

    @GET("/flower-storages/storage-room/{id}")
    Call<ArrayList<FlowerStorage>> getFlowerStoragesByRoom(@Header("Authorization") String token, @Path("id") Integer id);

    @PUT("/flowers")
    Call<Flower> updateFlower(@Header("Authorization") String token, @Body Flower flower);

    @PUT("/florist-shops")
    Call<FloristShop> updateFloristShop(@Header("Authorization") String token, @Body FloristShop floristShop);

    @GET("/flowers/{id}")
    Call<Flower> getFlower(@Header("Authorization") String token, @Path("id") Integer id);

    @DELETE("/flowers/{id}")
    Call<Flower> deleteFlower(@Header("Authorization") String token, @Path("id") Integer id);

    @PUT("/florist-shops/{email}/rooms")
    Call<Placement> updatePlacement(@Header("Authorization") String token, @Path("email") String email, @Body Placement placement);

    @GET("/florist-shops/rooms/{id}")
    Call<Placement> getPlacement(@Header("Authorization") String token, @Path("id") Integer id);

    @DELETE("/florist-shops/rooms/{id}")
    Call<Placement> deletePlacement(@Header("Authorization") String token, @Path("id") Integer id);

    @POST("/florist-shops/{email}/rooms")
    Call<Placement> addPlacement(@Header("Authorization") String token, @Path("email") String email, @Body Placement placement);

    @POST("/flowers")
    Call<Flower> addFlower(@Header("Authorization") String token, @Body Flower flower);

    @POST("/auth/register/florist-shop")
    Call<FloristShop> signUpFloristShop(@Body FloristShop floristShop);

    @POST("/flower-storages")
    Call<FlowerStorage> createFlowerStorage(@Header("Authorization") String token, @Body FlowerStorage flowerStorage);

    @POST("/device")
    Call<ArrayList<RedistributionResponseDto>> redistribute(@Header("Authorization") String token, @Body SmartDevice smartDevice);

    @DELETE("/flower-storages/{id}")
    Call<FlowerStorage> deleteStorage(@Header("Authorization") String token, @Path("id") Integer id);

}
