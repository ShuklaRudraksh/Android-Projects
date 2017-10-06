package com.example.rhshukla.firebase;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SecondOne extends AppCompatActivity {

    Button button;
    TextView rockt;
    Button rock,paper,sissor;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference gameRef = myRef.child("game");


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        gameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text=dataSnapshot.getValue().toString();
                rockt.setText(text);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Error","Error");

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_one);

        rockt=(TextView)findViewById(R.id.rockt);
        rock=(Button) findViewById(R.id.rock);
        paper=(Button) findViewById(R.id.paper);
        sissor=(Button) findViewById(R.id.sissor);



        button = (Button) findViewById(R.id.logout);
        mAuth = FirebaseAuth.getInstance();

        rock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameRef.setValue("rock");
            }
        });


        paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameRef.setValue("paper");
            }
        });

        sissor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameRef.setValue("sissor");
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(SecondOne.this, MainActivity.class));
                }
            }
        };

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

    }
}
