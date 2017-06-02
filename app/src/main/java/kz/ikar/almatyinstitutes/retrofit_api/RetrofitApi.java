package kz.ikar.almatyinstitutes.retrofit_api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by VH on 01.06.2017.
 */

public interface RetrofitApi {
    @GET
    Call<MyClass> getMyClass(@Url String url);

}
