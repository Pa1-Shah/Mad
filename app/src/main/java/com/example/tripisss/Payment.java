package com.example.tripisss;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Payment extends AppCompatActivity {
    TextView group;
    EditText amount;
    Button payment;
    double total,paid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        String city = getIntent().getStringExtra("un");
        String tot = getIntent().getStringExtra("total");
        String pd = getIntent().getStringExtra("paid");
        total = Double.parseDouble(tot);
        paid = Double.parseDouble(pd);
        group = findViewById(R.id.groupName);
        amount = findViewById(R.id.amt);
        payment = findViewById(R.id.payBtn);
        group.setText(city);
        payment.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String kl = amount.getText().toString();
                double curr = Double.parseDouble(kl);
                total+=curr;
                paid+=curr;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String time = dtf.format(now).toString();
                Transaction tr = new Transaction(Globe.getInstance().user, kl,time);
                FirebaseDatabase.getInstance().getReference().child(city).child("History")
                        .push().setValue(tr);
                FirebaseDatabase.getInstance().getReference().child(city).child("total").setValue(total);
                FirebaseDatabase.getInstance().getReference().child("Groups").child(pure(Globe.getInstance().user)).child(city).setValue(paid);
                Intent x = new Intent(getApplicationContext(),Expense.class);
                x.putExtra("nc",city);
                startActivity(x);
            }
        });

    }
    public String pure(String op){
        String kl = "";
        for(int i=0; i<op.length();i++) {
            if (op.charAt(i) != '@') {
                kl += op.charAt(i);
            } else {
                break;
            }
        }
        return kl;
    }
}