package com.example.aula13;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aula13.adapters.AlunoAdapter;
import com.example.aula13.models.Aluno;
import com.example.aula13.services.ApiClient;
import com.example.aula13.services.AlunoService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerAluno;
    AlunoAdapter alunoAdapter;
    AlunoService apiService;
    List<Aluno> listaAlunos = new ArrayList<>();
    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializando os componentes
        recyclerAluno = findViewById(R.id.recyclerAluno);
        btnAdd = findViewById(R.id.btnAdd);

        // Configurando o RecyclerView
        configurarRecycler();

        // Configurando ação do botão de adicionar
        btnAdd.setOnClickListener(view -> {
            startActivity(new Intent(this, AlunoActivity.class));
        });

        // Inicializando o serviço API
        apiService = ApiClient.getAlunoService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        obterAlunos();
    }

    private void configurarRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerAluno.setLayoutManager(layoutManager);
        alunoAdapter = new AlunoAdapter(listaAlunos, this);
        recyclerAluno.setAdapter(alunoAdapter);
        recyclerAluno.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void obterAlunos() {
        Call<List<Aluno>> call = apiService.getAlunos();
        call.enqueue(new Callback<List<Aluno>>() {
            @Override
            public void onResponse(Call<List<Aluno>> call, Response<List<Aluno>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaAlunos.clear();
                    listaAlunos.addAll(response.body());
                    alunoAdapter.notifyDataSetChanged();
                    Log.d("MainActivity", "Dados carregados com sucesso");
                } else {
                    Log.e("MainActivity", "Erro na resposta: " + response.code());
                    Toast.makeText(MainActivity.this, "Erro ao carregar dados: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Aluno>> call, Throwable t) {
                Log.e("MainActivity", "Erro ao obter os alunos: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
