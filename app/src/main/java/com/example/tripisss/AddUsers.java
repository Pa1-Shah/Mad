package com.example.tripisss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddUsers extends AppCompatActivity {
    ArrayList<String> list;
    ArrayList<String> names;
    LinearLayout ly;
    String city;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users);
        TextView trip = findViewById(R.id.trip);
        Intent intent = getIntent();
        city = intent.getStringExtra("tn");
        city = "Trip to "+ city;
        user = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        Toast.makeText(getApplicationContext(), user, Toast.LENGTH_SHORT).show();
        trip.setText(city);
        list = new ArrayList<String>();
        names = new ArrayList<String>();
        ly = findViewById(R.id.linear);
        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s : snapshot.getChildren()){
                    String kl = s.getValue().toString();
                    if(names.contains(kl)==false)
                    {
                        names.add(s.getValue().toString());
                        ly.addView(create(kl, names.size()-1));
                        ly.addView(space());

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Button pro = findViewById(R.id.proceedBtn);
        pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference()
                        .child(city).child("max").setValue(new Pair(user, 0));

                FirebaseDatabase.getInstance().getReference()
                        .child(city).child("min").setValue(new Pair(user, 0));

                FirebaseDatabase.getInstance().getReference()
                        .child(city).child("total").setValue(0);

                FirebaseDatabase.getInstance().getReference()
                        .child(city).child("credit").setValue(0);

                Intent it = new Intent(getApplicationContext(),Expense.class);
                startActivity(it);
            }
        });
    }

    public LinearLayout space() {
        LinearLayout lly = new LinearLayout(getApplicationContext());
        lly.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80));
        return lly;
    }

    public LinearLayout create(String name, int x) {
        LinearLayout lly = new LinearLayout(getApplicationContext());
        lly.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150));
        lly.setOrientation(LinearLayout.HORIZONTAL);
        lly.setBackgroundColor(getResources().getColor(R.color.white));
        TextView txt = new TextView(getApplicationContext());
        txt.setLayoutParams(new LinearLayout.LayoutParams(750,150));
        txt.setTextSize(20);
        name = "   "+name;
        txt.setText(name);
        ImageView iv = new ImageView(getApplicationContext());
        iv.setLayoutParams(new LinearLayout.LayoutParams(120,120));
        iv.setImageResource(R.drawable.icon);
        ImageView iv1 = new ImageView(getApplicationContext());
        iv1.setLayoutParams(new LinearLayout.LayoutParams(140,140));
        iv1.setImageResource(R.drawable.add);
        iv1.setId(100+x);
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv1.setImageResource(R.drawable.ok);
                int kl = iv1.getId();
                list.add(names.get(kl-100));
                int idx = list.size()-1;
                Toast.makeText(getApplicationContext(),list.get(idx),Toast.LENGTH_LONG).show();
            }
        });
        lly.addView(iv);
        lly.addView(txt);
        lly.addView(iv1);
        return lly;
    }
}