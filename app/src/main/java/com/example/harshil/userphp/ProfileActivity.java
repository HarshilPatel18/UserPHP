package com.example.harshil.userphp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewUsername, textViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile);

        if(!SharedPrefManager.getInstance(this).isLoggedIn())
        {
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        }

        Button btnLogout = findViewById(R.id.buttonLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        textViewUsername = findViewById(R.id.textViewUsername);
        textViewEmail = findViewById(R.id.textViewEmail);

        textViewUsername.setText(SharedPrefManager.getInstance(this).getUsername());
        textViewEmail.setText(SharedPrefManager.getInstance(this).getEmail());

    }
}
