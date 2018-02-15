package com.example.harshil.userphp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        if(SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            return;
        }

        editTextUsername = findViewById(R.id.editTextLoginUname);
        editTextPassword = findViewById(R.id.editTextLoginPwd);
        progressBar = findViewById(R.id.progressRegister);
        progressBar.setVisibility(View.INVISIBLE);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        Button btnReg = findViewById(R.id.btnReg);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
    }


    private void userLogin()
    {
        final String username=editTextUsername.getText().toString().trim();
        final String password=editTextPassword.getText().toString().trim();

        if(username.trim().equals("") || password.trim().equals(""))
        {
            StyleableToast.makeText(getApplicationContext(),"Required Fields are missing !",R.style.Error).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest= new StringRequest(Request.Method.POST, Constants.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.INVISIBLE);
                try {

                    JSONObject jsonObject= new JSONObject(response);

                    if(!jsonObject.getBoolean("error"))
                    {

                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(jsonObject.getInt("id"),jsonObject.getString("username"),jsonObject.getString("email"));
                        StyleableToast.makeText(getApplicationContext(),jsonObject.getString("message"),R.style.Success).show();

                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        finish();

                    }
                    else {
                        StyleableToast.makeText(getApplicationContext(),jsonObject.getString("message"),R.style.Error).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    StyleableToast.makeText(getApplicationContext(),e.getMessage(),R.style.Error).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressBar.setVisibility(View.INVISIBLE);
                StyleableToast.makeText(getApplicationContext(),"Network issue",R.style.Error).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params= new HashMap<>();
                params.put("username",username);
                params.put("password",password);
                return params;

            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }
}
