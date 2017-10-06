package com.example.rhshukla.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static android.R.attr.data;
import static android.R.attr.start;

public class MainActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button signup;
    SignInButton googleLogin;
    private FirebaseAuth mAuth;
    private FirebaseAuth gAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final static int RC_SIGN_IN=1;
    GoogleApiClient mGoogleApiClient;



    @Override
    public void onStart() {
        super.onStart();
        gAuth.addAuthStateListener(mAuthListener);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                if(firebaseAuth.getCurrentUser()!=null)
                {
                    startActivity(new Intent(MainActivity.this, SecondOne.class));
                }

            }
        };

        mAuth = FirebaseAuth.getInstance();
        gAuth=FirebaseAuth.getInstance();

         email=(EditText) findViewById(R.id.email);
         password=(EditText) findViewById(R.id.password);
          signup=(Button)findViewById(R.id.button);
          googleLogin=(SignInButton) findViewById(R.id.gmail);

        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });





            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

                  mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MainActivity.this,"Somthig is Worng",Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    //Gmail


             private void signIn() {
                 Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                 startActivityForResult(signInIntent, RC_SIGN_IN);
             }

             @Override

                 public void onActivityResult(int requestCode, int resultCode, Intent data)
                 {
                     super.onActivityResult(requestCode, resultCode, data);

                     // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
                     if (requestCode == RC_SIGN_IN) {
                         GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                         if (result.isSuccess()) {
                             // Google Sign In was successful, authenticate with Firebase
                             GoogleSignInAccount account = result.getSignInAccount();
                             firebaseAuthWithGoogle(account);
                         } else {
                             Toast.makeText(MainActivity.this,"Somthig is Worng",Toast.LENGTH_LONG).show();
                         }
                     }
                 }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        gAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = gAuth.getCurrentUser();
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // ...
                    }
                });
    }



    //Mail
    public  void SignUp(View v)
    {
        final String useremail=email.getText().toString();
        final String userpassword=password.getText().toString();

        mAuth.createUserWithEmailAndPassword(useremail,userpassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if(task.isSuccessful())
                        {
                            FirebaseUser user=mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this,"done",
                                    Toast.LENGTH_SHORT).show();

                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,"auth_failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
