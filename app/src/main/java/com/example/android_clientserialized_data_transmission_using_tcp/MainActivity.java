/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * @author Nicolás Penagos Montoya
 * nicolas.penagosm98@gmail.com
 **~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package com.example.android_clientserialized_data_transmission_using_tcp;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android_clientserialized_data_transmission_using_tcp.model.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    // -------------------------------------
    // XML references
    // -------------------------------------
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    // -------------------------------------
    // Global variables
    // -------------------------------------
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private Gson gson;

    // -------------------------------------
    // Attributes
    // -------------------------------------
    private boolean isAlive;

    // -------------------------------------
    // API Android methods
    // -------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        loginButton = findViewById(R.id.loginButton);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        gson = new Gson();
        isAlive = true;
        initClient();

        loginButton.setOnClickListener(

                (view)->{

                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();

                    if(checkData(username, password)){

                        new Thread(

                                ()->{

                                    User user = new User(username, password);
                                    String json = gson.toJson(user);
                                    sendMsg(json);

                                }

                        ).start();

                    }else {
                        Toast.makeText(this, "There can be no empty fields", Toast.LENGTH_SHORT).show();
                    }

                }

        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10&&resultCode==RESULT_OK){
            new Thread(()->sendMsg("logout")).start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        usernameEditText.setText("");
        passwordEditText.setText("");

    }

    // -------------------------------------
    // Connection method
    // -------------------------------------
    private void initClient() {

        new Thread(

                ()->{

                    try {

                        socket = new Socket("192.168.20.25", 5000);
                        InputStream inputStream = socket.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        reader = new BufferedReader(inputStreamReader);

                        OutputStream ouputStream = socket.getOutputStream();
                        OutputStreamWriter ouputStreamWriter = new OutputStreamWriter(ouputStream);
                        writer = new BufferedWriter(ouputStreamWriter);

                        while(isAlive) {

                            String line = reader.readLine();
                            boolean matched = Boolean.parseBoolean(line);

                            if(matched){

                                runOnUiThread(()->{

                                    String username = usernameEditText.getText().toString();

                                    Intent i = new Intent(this, WelcomeActivity.class);
                                    i.putExtra("username", username);
                                    startActivityForResult(i, 10);

                                });

                            }else{
                                runOnUiThread(()->Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show());
                            }


                        }

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }


        ).start();

    }

    private void sendMsg(String msg) {

        try {

            Log.e("debug", "sendMsg: "+msg);
            writer.write(msg+"\n");
            writer.flush();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    // -------------------------------------
    // Logic methods
    // -------------------------------------
    public boolean checkData(String username, String password){
        return !username.equals("") && !username.equals("");
    }

}