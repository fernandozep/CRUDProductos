package com.example.crudproductos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class UpdateProductActivity extends AppCompatActivity {

    private EditText etNombreProducto, etDescripcionProducto, etPrecioProducto;
    private Button btnActualizar, btnEliminar;
    private RequestQueue queue;
    private int idProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        etNombreProducto = findViewById(R.id.etProductName);
        etDescripcionProducto = findViewById(R.id.etProductDescription);
        etPrecioProducto = findViewById(R.id.etProductPrice);
        btnActualizar = findViewById(R.id.btnUpdate);
        btnEliminar = findViewById(R.id.btnDelete);
        queue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        idProducto = intent.getIntExtra("productId", -1);
        String nombreProducto = intent.getStringExtra("productName");
        String descripcionProducto = intent.getStringExtra("productDescription");
        double precioProducto = intent.getDoubleExtra("productPrice", 0);

        etNombreProducto.setText(nombreProducto);
        etDescripcionProducto.setText(descripcionProducto);
        etPrecioProducto.setText(String.valueOf(precioProducto));
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevoNombre = etNombreProducto.getText().toString();
                String nuevaDescripcion = etDescripcionProducto.getText().toString();
                double nuevoPrecio;
                try {
                    nuevoPrecio = Double.parseDouble(etPrecioProducto.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(UpdateProductActivity.this, "El precio no es valido", Toast.LENGTH_SHORT).show();
                    return;
                }
                ApiClient.updateProduct(queue, idProducto, nuevoNombre, nuevaDescripcion, nuevoPrecio, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(UpdateProductActivity.this, "Producto actualizado con exito", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UpdateProductActivity.this, "Error al actualizar producto", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiClient.deleteProduct(queue, idProducto, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(UpdateProductActivity.this, "Producto eliminado con exito", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UpdateProductActivity.this, "Error al eliminar producto", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}