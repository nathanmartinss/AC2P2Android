package com.example.aula13.services;

import com.example.aula13.models.Endereco;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ViaCepService {
    @GET("{cep}/json/")
    Call<Endereco> getEndereco(@Path("cep") String cep);
}
