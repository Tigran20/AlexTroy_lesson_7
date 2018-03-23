package ru.tinkoff.ru.seminar.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.tinkoff.ru.seminar.api.model.ApiResponse;

public interface Api {

    @GET("latest?base=RUB")
    Call<ApiResponse> getRates(@Query("symbols") String currency);

}