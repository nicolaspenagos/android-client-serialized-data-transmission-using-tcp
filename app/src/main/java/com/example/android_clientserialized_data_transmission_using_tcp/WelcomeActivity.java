/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * @author Nicolás Penagos Montoya
 * nicolas.penagosm98@gmail.com
 **~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package com.example.android_clientserialized_data_transmission_using_tcp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    // -------------------------------------
    // XML references
    // -------------------------------------
    private TextView usernameTextView;
    private Button logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        usernameTextView = findViewById(R.id.nameTextView);
        logOutButton = findViewById(R.id.logOutButton);

        logOutButton.setOnClickListener(

                (view)->{

                    Intent i = new Intent(this, MainActivity.class);
                    setResult(RESULT_OK, i);
                    finish();

                }

        );

    }

    @Override
    protected void onResume() {
        super.onResume();
        usernameTextView.setText(getIntent().getExtras().getString("username", "NO_VALUE"));
    }
}