package com.example.crudproductos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {

    private ListView lvProductos;
    private RequestQueue queue;
    private ArrayAdapter<String> adaptadorProductos;
    private ArrayList<String> listaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        lvProductos = findViewById(R.id.lvProducts);
        Button btnRegresar = findViewById(R.id.btnRegresar);
        queue = Volley.newRequestQueue(this);
        listaProductos = new ArrayList<>();
        adaptadorProductos = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaProductos) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                android.widget.TextView textView = view.findViewById(android.R.id.text1);
                String producto = listaProductos.get(position);
                textView.setText(producto.replace(";", "\n"));
                return view;
            }
        };
        lvProductos.setAdapter(adaptadorProductos);

        lvProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] detallesProducto = listaProductos.get(position).split("\n");
                if (detallesProducto.length == 4) {
                    int idProducto = Integer.parseInt(detallesProducto[0]);
                    String nombreProducto = detallesProducto[1];
                    String descripcionProducto = detallesProducto[2];
                    double precioProducto = Double.parseDouble(detallesProducto[3]);

                    Intent intent = new Intent(ProductListActivity.this, UpdateProductActivity.class);
                    intent.putExtra("productId", idProducto);
                    intent.putExtra("productName", nombreProducto);
                    intent.putExtra("productDescription", descripcionProducto);
                    intent.putExtra("productPrice", precioProducto);
                    startActivity(intent);
                } else {
                    Toast.makeText(ProductListActivity.this, "Error: Datos del producto no válidos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this, RegisterProductActivity.class);
                startActivity(intent);
            }
        });
        cargarProductos();
    }

    private void cargarProductos() {
        ApiClient.getProducts(queue, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    listaProductos.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject producto = response.getJSONObject(i);
                        int idProducto = producto.getInt("id");
                        String nombreProducto = producto.getString("name");
                        String descripcionProducto = producto.getString("description");
                        double precioProducto = producto.getDouble("price");
                        String textoProducto = idProducto + "\n" + nombreProducto + "\n" + descripcionProducto + "\n" + precioProducto;
                        listaProductos.add(textoProducto);
                    }
                    adaptadorProductos.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductListActivity.this, "Error al cargar productos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarProductos();
    }
}
