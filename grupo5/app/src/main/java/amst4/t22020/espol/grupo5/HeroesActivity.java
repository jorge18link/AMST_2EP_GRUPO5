package amst4.t22020.espol.grupo5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HeroesActivity extends AppCompatActivity {

    String token="10220694825898823";
    private RequestQueue ListaRequest = null;

    private String nombreHeroe;

    private LinearLayout contenedor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heroes);
        ListaRequest = Volley.newRequestQueue(this);

        nombreHeroe = getIntent().getExtras().getString("heroe");
        contenedor = findViewById(R.id.layout_linear);

        Toast.makeText(this,nombreHeroe,Toast.LENGTH_LONG).show();

        CosultarHeroes(nombreHeroe);
    }

    public void CosultarHeroes(String palabra){
        String url_registros = "https://superheroapi.com/api/"+this.token+"/search/"+palabra;

        JsonObjectRequest requestRegistros = new JsonObjectRequest( Request.Method.GET, url_registros, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mostrarHeroes(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "JWT " + token);
                return params;
            }
        };
        ListaRequest.add(requestRegistros);
    }


    private void mostrarHeroes(JSONObject heroes)  {
        //String registroId;
        String hola = "hi";
        JSONObject registroHeroes;
        TextView viewHeroName;
        String heroName;
        JSONArray jsonArray =null;
        try{
            jsonArray = heroes.getJSONArray("results");
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }


        LinearLayout.LayoutParams parametrosLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        try {

            viewHeroName =  new TextView(this);
            viewHeroName.setLayoutParams(parametrosLayout);
            viewHeroName.setText("Resultados: "+jsonArray.length());
            contenedor.addView(viewHeroName);

            for (int i = 0; i < jsonArray.length(); i++) {
                registroHeroes = (JSONObject) jsonArray.get(i);
                //registroId = registroTemp.getString("id");
                final String id = registroHeroes.getString("id");

                heroName =  registroHeroes.getString("name");
                viewHeroName =  new TextView(this);
                viewHeroName.setLayoutParams(parametrosLayout);
                viewHeroName.setText(heroName);

                viewHeroName.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        irSiguiente(id);
                    }
                });

                contenedor.addView(viewHeroName);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("error");
        }

    }

    public void irSiguiente(String id){
        Intent intent = new Intent(HeroesActivity.this, HabilityHeroActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }
}