package com.example.aula13.services;

import com.example.aula13.models.Aluno;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AlunoService {

    // Método para obter a lista de alunos
    @GET("aluno")
    Call<List<Aluno>> getAlunos();

    // Método para adicionar um novo aluno
    @POST("aluno")
    Call<Aluno> postAluno(@Body Aluno aluno);

    // Método para excluir um aluno pelo ID
    @DELETE("aluno/{id}")
    Call<Void> deleteAluno(@Path("id") int idAluno);

    // Método para obter um aluno específico pelo ID
    @GET("aluno/{id}")
    Call<Aluno> getAlunoPorId(@Path("id") int idAluno);

    // Método para atualizar um aluno específico pelo ID
    @PUT("aluno/{id}")
    Call<Aluno> putAluno(@Path("id") int idAluno, @Body Aluno aluno);
}
