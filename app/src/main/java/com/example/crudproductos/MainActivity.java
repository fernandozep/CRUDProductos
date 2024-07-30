package com.example.crudproductos;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etProductName, etUpdateProductName;
    private Button btnLogin, btnAddProduct, btnDeleteProduct, btnUpdateProduct;
    private ListView lvProducts;
    private RequestQueue queue;
    private ArrayAdapter<String> productAdapter;
    private ArrayList<String> productsList;
    private ArrayList<Integer> productIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views and queue
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etProductName = findViewById(R.id.etProductName);
        etUpdateProductName = findViewById(R.id.etUpdateProductName);
        btnLogin = findViewById(R.id.btnLogin);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        lvProducts = findViewById(R.id.lvProducts);
        queue = Volley.newRequestQueue(this);

        productsList = new ArrayList<>();
        productIds = new ArrayList<>();
        productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productsList);
        lvProducts.setAdapter(productAdapter);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                ApiClient.login(queue, username, password, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String token = response.getString("token");
                            ApiClient.setAuthToken(token);
                            Toast.makeText(MainActivity.this, "L", Toast.LENGTH_SHORT).show();
                            loadProducts(); // Carga los productos después del login
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "d", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = lvProducts.getCheckedItemPosition();
                if (position >= 0) {
                    int productId = productIds.get(position);
                    ApiClient.deleteProduct(queue, productId, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(MainActivity.this, "Pd", Toast.LENGTH_SHORT).show();
                            loadProducts();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "F", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Pl", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void loadProducts() {
        ApiClient.getProducts(queue, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.d("LOAD_PRODUCTS", "Response: " + response.toString());

                    productsList.clear();
                    productIds.clear();  // Asegúrate de que `productIds` esté definido

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject product = response.getJSONObject(i);
                        String productName = product.getString("name");
                        int productId = product.getInt("id");
                        productsList.add(productName);
                        productIds.add(productId);  // Asegúrate de que `productIds` esté definido
                    }
                    productAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
    }



}
