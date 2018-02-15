package com.example.harshil.userphp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPassword;
    private Button buttonRegister;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        if(SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
            return;
        }

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPass);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        progressBar = findViewById(R.id.progressRegister);
        progressBar.setVisibility(View.INVISIBLE);

        buttonRegister = (Button) findViewById(R.id.button);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser(){

        final String username=editTextUsername.getText().toString().trim();
        final String password=editTextPassword.getText().toString().trim();
        final String email=editTextEmail.getText().toString().trim();


//===============Data Checking===============
        if(username.trim().equals("") || password.trim().equals("") || email.trim().equals(""))
        {
            StyleableToast.makeText(getApplicationContext(),"Required Fields are missing !",R.style.Error).show();
            return;
        }
        Pattern pattern = Pattern.compile("[a-z0-9._+-]+@[a-z0-9.-]+\\.[a-z]{2,3}$",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches())
        {
            StyleableToast.makeText(getApplicationContext()," Invalid i `Email ",R.style.Error).show();
            return;
        }

//===============Request==============
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Constants.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    progressBar.setVisibility(View.INVISIBLE);
                    JSONObject jsonObject= new JSONObject(response);

                    if(jsonObject.getBoolean("error"))
                    {
                        StyleableToast.makeText(getApplicationContext(),jsonObject.getString("message"),R.style.Error).show();
                    }
                    else {
                        StyleableToast.makeText(getApplicationContext(),jsonObject.getString("message"),R.style.Success).show();
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
                params.put("email",email);
                return params;

            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }
}
