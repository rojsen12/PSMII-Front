package com.example.interaktywnefiszki;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(LoginActivity.this, "Hasło musi mieć co najmniej 6 znaków", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tworzymy obiekt JSON z danymi logowania
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("login", email);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Błąd przygotowania żądania", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL do API
        String url = "http://10.0.2.2:3000/login";

        // Tworzymy JsonObjectRequest zamiast StringRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Odpowiedź z serwera: " + response.toString());
                        try {
                            String token = response.getString("token");
                            JSONObject user = response.getJSONObject("user");

                            // Zapisujemy dane użytkownika
                            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("token", token);
                            editor.putString("username", user.getString("username"));
                            editor.putString("email", user.getString("email"));
                            editor.apply();

                            Toast.makeText(LoginActivity.this, "Zalogowano pomyślnie", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Przechodzę do MainActivity");

                            // Prostsze przejście do MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            Log.e(TAG, "Błąd parsowania JSON: " + e.getMessage());
                            Toast.makeText(LoginActivity.this, "Błąd parsowania odpowiedzi", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Błąd Volley: " + error.toString());
                        String errorMessage = "Błąd logowania";

                        if (error.networkResponse != null) {
                            errorMessage += " (kod: " + error.networkResponse.statusCode + ")";
                        }

                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    public void testMainActivityTransition(View view) {
        Log.d(TAG, "Testowe przejście do MainActivity");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}