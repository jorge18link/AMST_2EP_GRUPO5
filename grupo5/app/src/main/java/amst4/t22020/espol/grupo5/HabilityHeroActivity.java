package amst4.t22020.espol.grupo5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HabilityHeroActivity extends AppCompatActivity {

    String token="10220694825898823";
    private RequestQueue ListaRequest = null;
    private String idHero;

    public BarChart graficoBarras;

    private LinearLayout contenedor;

    private JSONObject datosGrafico;
    final ArrayList<String> xAxisLabel = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hability_hero);
        ListaRequest = Volley.newRequestQueue(this);

        idHero = getIntent().getExtras().getString("id");
        contenedor = findViewById(R.id.layout_habilidades);

        Toast.makeText(getApplicationContext(),idHero,Toast.LENGTH_LONG).show();

        xAxisLabel.add("Inteligencia");
        xAxisLabel.add("Fuerza");
        xAxisLabel.add("Velocidad");
        xAxisLabel.add("Durabilidad");
        xAxisLabel.add("Poder");
        xAxisLabel.add("Combate");

        ConsultarHeroe(idHero);


    }

    public void ConsultarHeroe(String id){
        String url_registros = "https://superheroapi.com/api/"+this.token+"/"+id;

        JsonObjectRequest requestRegistros = new JsonObjectRequest( Request.Method.GET, url_registros, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mostrarHabilidades(response);
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

    private void mostrarHabilidades(JSONObject heroe)  {

        LinearLayout.LayoutParams parametrosLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

        try {
            TextView viewHeroName =  new TextView(this);
            viewHeroName.setLayoutParams(parametrosLayout);
            viewHeroName.setText(heroe.getString("name"));
            TextView fullName =  new TextView(this);
            fullName.setLayoutParams(parametrosLayout);
            fullName.setText(heroe.getJSONObject("biography").getString("full-name"));
            datosGrafico = heroe.getJSONObject("powerstats");
            contenedor.addView(viewHeroName);
            contenedor.addView(fullName);

            iniciarGrafico();
            actualizarGrafico(datosGrafico);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void iniciarGrafico() {
        graficoBarras = findViewById(R.id.barChart);
        graficoBarras.getDescription().setEnabled(false);
        graficoBarras.setMaxVisibleValueCount(60);
        graficoBarras.setPinchZoom(false);
        graficoBarras.setDrawBarShadow(false);
        graficoBarras.setDrawGridBackground(false);
        XAxis xAxis = graficoBarras.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        graficoBarras.getAxisLeft().setDrawGridLines(false);
        graficoBarras.animateY(1500);
        graficoBarras.getLegend().setEnabled(false);
    }

    private void actualizarGrafico(JSONObject data){
        int count = 0;
        ArrayList<BarEntry> dato_temp = new ArrayList<>();

        try {
            dato_temp.add(new BarEntry(count, Integer.valueOf(data.getString("intelligence"))));
            count++;
            dato_temp.add(new BarEntry(count, Integer.valueOf(data.getString("strength"))));
            count++;
            dato_temp.add(new BarEntry(count, Integer.valueOf(data.getString("speed"))));
            count++;
            dato_temp.add(new BarEntry(count, Integer.valueOf(data.getString("durability"))));
            count++;
            dato_temp.add(new BarEntry(count, Integer.valueOf(data.getString("power"))));
            count++;
            dato_temp.add(new BarEntry(count, Integer.valueOf(data.getString("combat"))));
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("error");
        }
        llenarGrafico(dato_temp);

    }

    private void llenarGrafico(ArrayList<BarEntry> data) {
        BarDataSet heroeDataSet;
        if ( graficoBarras.getData() != null && graficoBarras.getData().getDataSetCount() > 0) {
            heroeDataSet = (BarDataSet) graficoBarras.getData().getDataSetByIndex(0);
            heroeDataSet.setValues(data);
            graficoBarras.getData().notifyDataChanged();
            graficoBarras.notifyDataSetChanged();
        } else {
            heroeDataSet = new BarDataSet(data, "Data Set");
            heroeDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
            heroeDataSet.setDrawValues(true);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(heroeDataSet);
            BarData databar = new BarData(dataSets);
            graficoBarras.setData(databar);
            graficoBarras.setFitBars(true);
        }
        graficoBarras.invalidate();
        /*
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                solicitarTemperaturas();
            }
        };
        handler.postDelayed(runnable, 3000);
        */
    }
}