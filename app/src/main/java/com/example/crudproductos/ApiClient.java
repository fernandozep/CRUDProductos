package com.example.crudproductos;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiClient {

    private static String AUTH_TOKEN = "";
    private static final String BASE_URL = "http://34.125.8.146/";

    public static void login(RequestQueue queue, String username, String password,
                             Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "login.php";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                listener, errorListener);
        queue.add(request);
    }

    public static void setAuthToken(String token) {
        AUTH_TOKEN = token;
    }

    public static void addProduct(RequestQueue queue, String name, String description, double price,
                                  Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "create_product.php";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
            jsonBody.put("description", description);
            jsonBody.put("price", price);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                listener, errorListener) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + AUTH_TOKEN);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(request);
    }

    public static void getProducts(RequestQueue queue,
                                   Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "get_products.php";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                listener, errorListener) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + AUTH_TOKEN);
                Log.d("API_CLIENT", "Authorization Header: " + "Bearer " + AUTH_TOKEN);  // Mensaje de depuración
                return headers;
            }
        };
        queue.add(request);
    }



    public static void deleteProduct(RequestQueue queue, int productId,
                                     Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        String url = BASE_URL + "delete_product.php";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", productId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                listener, errorListener) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + AUTH_TOKEN);
                return headers;
            }
        };
        queue.add(request);
    }

    public static void updateProduct(RequestQueue queue, int productId, String name, String description, double price,
                                     Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "update_product.php";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", productId);
            jsonBody.put("name", name);
            jsonBody.put("description", description);
            jsonBody.put("price", price);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                listener, errorListener) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + AUTH_TOKEN);
                return headers;
            }
        };
        queue.add(request);
    }
}
