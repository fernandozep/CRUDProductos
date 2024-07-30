package com.example.crudproductos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class RegisterProductActivity extends AppCompatActivity {

    private EditText etProductName;
    private EditText etProductDescription;
    private EditText etProductPrice;
    private Button btnAddProduct;
    private Button btnViewProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_product);

        etProductName = findViewById(R.id.etProductName);
        etProductDescription = findViewById(R.id.etProductDescription);
        etProductPrice = findViewById(R.id.etProductPrice);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnViewProducts = findViewById(R.id.btnViewProducts);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etProductName.getText().toString().trim();
                String description = etProductDescription.getText().toString().trim();
                String priceStr = etProductPrice.getText().toString().trim();

                if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
                    Toast.makeText(RegisterProductActivity.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                double price;
                try {
                    price = Double.parseDouble(priceStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(RegisterProductActivity.this, "El precio debe ser un número válido", Toast.LENGTH_SHORT).show();
                    return;
                }

                RequestQueue queue = Volley.newRequestQueue(RegisterProductActivity.this);
                ApiClient.addProduct(queue, name, description, price, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(RegisterProductActivity.this, "Producto registrado con éxito", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterProductActivity.this, ProductListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterProductActivity.this, "Error al registrar el producto", Toast.LENGTH_SHORT).show();
                        Log.e("AddProductActivity", "Error: " + error.getMessage());
                    }
                });
            }
        });

        btnViewProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterProductActivity.this, ProductListActivity.class);
                startActivity(intent);
            }
        });
    }
}
