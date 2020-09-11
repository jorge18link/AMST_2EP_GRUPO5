package amst4.t22020.espol.grupo5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {



    TextInputEditText heroeSearch;
    Button btnBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnBuscar = findViewById(R.id.btn_buscar);
        heroeSearch = (TextInputEditText) findViewById(R.id.input_buscar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                irSiguiente();
            }
        });


    }

    public void irSiguiente(){
        String nombre = heroeSearch.getText().toString();
        Intent intent = new Intent(MainActivity.this, HeroesActivity.class);
        intent.putExtra("heroe",nombre);
        startActivity(intent);
    }



}