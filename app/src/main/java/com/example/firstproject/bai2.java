package com.example.firstproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class bai2 extends AppCompatActivity {

    EditText edPhoneNumber, edOtp;
    Button btnGetOtp, btnLogin2;
    private FirebaseAuth mAuth;
    String phoneNumber, mVerificationId;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bai2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        init();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                edOtp.setText(phoneAuthCredential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("PhoneAuth", "Verification failed" + e.getMessage());
                Toast.makeText(bai2.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_LONG).show();

            }

            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Toast.makeText(bai2.this, "OTP sent!", Toast.LENGTH_SHORT).show();
                mVerificationId = verificationId;
            }

        };
    }



    public void init(){
        edOtp = findViewById(R.id.edOtp);
        edPhoneNumber = findViewById(R.id.edPhoneNumber);
        btnGetOtp = findViewById(R.id.btnGetOtp);
        btnLogin2 = findViewById(R.id.btnLogin2);

        mAuth = FirebaseAuth.getInstance();

        btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = edPhoneNumber.getText().toString();
                getOTP(phoneNumber);
            }
        });
    }
    private void getOTP(String phoneNumber){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(bai2.this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}