package com.br.leili_ferreira.dlarhoje;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.app.AlertDialog;

public class MainActivity extends AppCompatActivity {

    //Declaração das variáveis
    private EditText entrada_real;
    private Button botao;
    private TextView resultado;
    private TextView texto_data;
    private TextView texto_hora;
    private TextView valor_em_real;
    private ImageView imagem_relogio;
    private ImageView imagem_data;
    private ImageView bandeira_Brasil;
    private ImageView bandeira_Americana;
    private ImageView limpar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Conexão das variáveis com os IDs
        entrada_real = findViewById(R.id.input);
        botao = findViewById(R.id.botao_converter);
        valor_em_real = findViewById(R.id.valor_real);
        resultado = findViewById(R.id.resultado_conversao);
        bandeira_Brasil = findViewById(R.id.bandeira_brasil);
        bandeira_Americana = findViewById(R.id.bandeira_americana);
        imagem_relogio = findViewById(R.id.icone_relogio);
        imagem_data = findViewById(R.id.icone_data);
        texto_hora = findViewById(R.id.hora);
        texto_data = findViewById(R.id.data);
        limpar = findViewById(R.id.icone_borracha);

        // Imagens começando como invisível
        bandeira_Brasil.setImageResource(R.drawable.bandeira_brasil);
        bandeira_Brasil.setVisibility(View.INVISIBLE);

        bandeira_Americana.setImageResource(R.drawable.bandeira_americana);
        bandeira_Americana.setVisibility(View.INVISIBLE);

        imagem_relogio.setImageResource(R.drawable.icone_relogio);
        imagem_relogio.setVisibility(View.INVISIBLE);

        imagem_data.setImageResource(R.drawable.icone_calendario);
        imagem_data.setVisibility(View.INVISIBLE);

        limpar.setImageResource(R.drawable.borracha);
        limpar.setVisibility(View.INVISIBLE);

        //Atribuindo função ao botão principal (o botão de converter)
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convertCurrency();
            }
        });

        // Atribuindo função a imagem da borracha para limpar os componentes visuais da tela
        limpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bandeira_Brasil.setImageResource(R.drawable.bandeira_brasil);
                bandeira_Brasil.setVisibility(View.INVISIBLE);

                bandeira_Americana.setImageResource(R.drawable.bandeira_americana);
                bandeira_Americana.setVisibility(View.INVISIBLE);

                imagem_relogio.setImageResource(R.drawable.icone_relogio);
                imagem_relogio.setVisibility(View.INVISIBLE);

                imagem_data.setImageResource(R.drawable.icone_calendario);
                imagem_data.setVisibility(View.INVISIBLE);

                limpar.setImageResource(R.drawable.borracha);
                limpar.setVisibility(View.INVISIBLE);

                texto_hora.setText(String.valueOf(""));
                texto_data.setText(String.valueOf(""));

                valor_em_real.setText(String.valueOf(""));
                resultado.setText(String.valueOf(""));

                entrada_real.setText(String.valueOf(""));
            }
        });
    }

    private void convertCurrency() {
        String amountStr = entrada_real.getText().toString();

        if (amountStr.isEmpty()) {
            AlertDialog.Builder cxMsg = new AlertDialog.Builder(MainActivity.this);
            cxMsg.setMessage("Insira um valor!");
            cxMsg.setNeutralButton("OK", null);
            cxMsg.show();

        } else {

            bandeira_Brasil.setImageResource(R.drawable.bandeira_brasil);
            bandeira_Brasil.setVisibility(View.VISIBLE);

            bandeira_Americana.setImageResource(R.drawable.bandeira_americana);
            bandeira_Americana.setVisibility(View.VISIBLE);

            imagem_relogio.setImageResource(R.drawable.icone_relogio);
            imagem_relogio.setVisibility(View.VISIBLE);

            imagem_data.setImageResource(R.drawable.icone_calendario);
            imagem_data.setVisibility(View.VISIBLE);

            limpar.setImageResource(R.drawable.borracha);
            limpar.setVisibility(View.VISIBLE);

            double amount = Double.parseDouble(amountStr);

            valor_em_real.setText(String.valueOf("R$ " + amount));

            String apiUrl = "https://api.exchangerate-api.com/v4/latest/USD";

            new FetchExchangeRateTask().execute(apiUrl, String.valueOf(amount));
        }
    }

    private class FetchExchangeRateTask extends AsyncTask<String, Void, Double> {

        @Override
        // Este método é executado em segundo plano (background) e recebe uma URL e um valor double como parâmetros.
        protected Double doInBackground(String... params) {
            // Pega a URL da primeira posição dos parâmetros.
            String apiUrl = params[0];
            // Pega o valor double da segunda posição dos parâmetros.
            double amount = Double.parseDouble(params[1]);

            try {
                // Cria uma instância de URL com a apiUrl fornecida.
                URL url = new URL(apiUrl);
                // Abre uma conexão HTTP com a URL.
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // Define o método da requisição HTTP como GET.
                connection.setRequestMethod("GET");

                // Obtém a entrada de fluxo de dados (input stream) da conexão HTTP.
                InputStream inputStream = connection.getInputStream();
                // Cria um leitor para ler os dados do fluxo de entrada.
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                // Cria um StringBuilder para armazenar os dados lidos.
                StringBuilder result = new StringBuilder();
                String line;

                // Lê linha por linha do fluxo de entrada e anexa ao StringBuilder "result".
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                // Converte os dados lidos (formato JSON) em um objeto JSONObject.
                JSONObject jsonObject = new JSONObject(result.toString());
                // Obtém o objeto "rates" do objeto JSON.
                JSONObject rates = jsonObject.getJSONObject("rates");
                // Obtém a taxa de câmbio para BRL (Real Brasileiro) do objeto "rates".
                double exchangeRate = rates.getDouble("BRL");

                // Calcula a conversão do valor fornecido para a moeda local usando a taxa de câmbio.
                return amount / exchangeRate;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            // Retorna null caso ocorra uma exceção ou erro.
            return null;
        }

        @Override
        protected void onPostExecute(Double result) {
            if (result != null) {
                resultado.setText(String.format("US$ %.2f ", result));
            } else {
                resultado.setText("Erro ao buscar a taxa de câmbio.");
            }

            // Obtém a hora e a data atuais
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date currentDate = new Date();

            // Define os valores nos TextViews
            texto_data.setText("Data: " + dateFormat.format(currentDate));
            texto_hora.setText("Hora: " + timeFormat.format(currentDate));
        }
    }
}