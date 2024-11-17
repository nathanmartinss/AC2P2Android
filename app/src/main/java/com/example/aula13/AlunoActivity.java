package com.example.aula13;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aula13.models.Aluno;
import com.example.aula13.models.Endereco;
import com.example.aula13.services.ApiClient;
import com.example.aula13.services.AlunoService;
import com.example.aula13.services.ViaCepService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlunoActivity extends AppCompatActivity {

    Button btnSalvar;
    AlunoService apiService;
    EditText txtNome, txtRa, txtCep, txtLogradouro, txtComplemento, txtBairro, txtCidade, txtUf;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno);

        btnSalvar = findViewById(R.id.btnSalvar);
        txtNome = findViewById(R.id.txtNomeAluno);
        txtRa = findViewById(R.id.txtRaAluno);
        txtCep = findViewById(R.id.txtCepAluno);
        txtLogradouro = findViewById(R.id.txtLogradouroAluno);
        txtComplemento = findViewById(R.id.txtComplementoAluno);
        txtBairro = findViewById(R.id.txtBairroAluno);
        txtCidade = findViewById(R.id.txtCidadeAluno);
        txtUf = findViewById(R.id.txtUfAluno);

        apiService = ApiClient.getAlunoService();

        txtCep.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String cep = txtCep.getText().toString();
                buscarEnderecoPorCep(cep);
            }
        });

        id = getIntent().getIntExtra("id", 0);
        if (id > 0) {
            carregarDadosAluno(id);
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Aluno aluno = new Aluno();
                aluno.setRa(Integer.parseInt(txtRa.getText().toString()));
                aluno.setNome(txtNome.getText().toString());
                aluno.setCep(txtCep.getText().toString());
                aluno.setLogradouro(txtLogradouro.getText().toString());
                aluno.setComplemento(txtComplemento.getText().toString());
                aluno.setBairro(txtBairro.getText().toString());
                aluno.setCidade(txtCidade.getText().toString());
                aluno.setUf(txtUf.getText().toString());

                if (id == 0)
                    inserirAluno(aluno);
                else {
                    aluno.setId(id);
                    editarAluno(aluno);
                }
            }
        });
    }

    private void carregarDadosAluno(int id) {
        apiService.getAlunoPorId(id).enqueue(new Callback<Aluno>() {
            @Override
            public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                if (response.isSuccessful()) {
                    Aluno aluno = response.body();
                    if (aluno != null) {
                        txtRa.setText(String.valueOf(aluno.getRa()));
                        txtNome.setText(aluno.getNome());
                        txtCep.setText(aluno.getCep());
                        txtLogradouro.setText(aluno.getLogradouro());
                        txtComplemento.setText(aluno.getComplemento());
                        txtBairro.setText(aluno.getBairro());
                        txtCidade.setText(aluno.getCidade());
                        txtUf.setText(aluno.getUf());
                    }
                }
            }

            @Override
            public void onFailure(Call<Aluno> call, Throwable t) {
                Log.e("Carregar aluno", "Erro ao obter aluno: " + t.getMessage());
            }
        });
    }

    private void buscarEnderecoPorCep(String cep) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ViaCepService cepService = retrofit.create(ViaCepService.class);
        Call<Endereco> call = cepService.getEndereco(cep);

        call.enqueue(new Callback<Endereco>() {
            @Override
            public void onResponse(Call<Endereco> call, Response<Endereco> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Endereco endereco = response.body();
                    txtLogradouro.setText(endereco.getLogradouro());
                    txtComplemento.setText(endereco.getComplemento());
                    txtBairro.setText(endereco.getBairro());
                    txtCidade.setText(endereco.getLocalidade());
                    txtUf.setText(endereco.getUf());
                } else {
                    Toast.makeText(AlunoActivity.this, "CEP não encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Endereco> call, Throwable t) {
                Log.e("ViaCEP", "Erro ao buscar CEP: " + t.getMessage());
            }
        });
    }

    private void inserirAluno(Aluno aluno) {
        apiService.postAluno(aluno).enqueue(new Callback<Aluno>() {
            @Override
            public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AlunoActivity.this, "Inserido com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e("Inserir", "Erro ao criar aluno: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Aluno> call, Throwable t) {
                Log.e("Inserir", "Erro ao criar aluno: " + t.getMessage());
            }
        });
    }

    private void editarAluno(Aluno aluno) {
        apiService.putAluno(id, aluno).enqueue(new Callback<Aluno>() {
            @Override
            public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AlunoActivity.this, "Editado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e("Editar", "Erro ao editar aluno: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Aluno> call, Throwable t) {
                Log.e("Editar", "Erro ao editar aluno: " + t.getMessage());
            }
        });
    }
}
