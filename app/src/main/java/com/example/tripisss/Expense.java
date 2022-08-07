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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Expense extends AppCompatActivity {
    LinearLayout newl;
    public String city;
    TextView tot, leftTopay,cityText;
    Button btn,loc_btn;
    double total,paid;
    int members;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        city = getIntent().getStringExtra("nc");
        tot = findViewById(R.id.total);
        leftTopay = findViewById(R.id.currtot);
        total = 0.0; paid = 0.0; members = 1;
        cityText = findViewById(R.id.cityT);
        cityText.setText(city);

        loc_btn = findViewById(R.id.find_loc);
        loc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapX = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(mapX);
            }
        });

        btn = findViewById(R.id.pressPay);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(),Payment.class);
                x.putExtra("un",city);
                x.putExtra("total",String.valueOf(total));
                x.putExtra("paid", String.valueOf(paid));
                startActivity(x);
            }
        });
        newl = findViewById(R.id.linearlay);

        FirebaseDatabase.getInstance().getReference().child(city).child("total").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String un=snapshot.getValue().toString();
                tot.setText(un);
                total = Double.parseDouble(un);
                double left = (total/members)-paid;
                leftTopay.setText(String.valueOf(left));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child(city).child("Members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String un = snapshot.getValue().toString();
                members = Integer.parseInt(un);
                double left = (total/members)-paid;
                leftTopay.setText(String.valueOf(left));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Groups").child(pure(Globe.getInstance().user)).child(city).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                paid = Double.parseDouble(snapshot.getValue().toString());
                double left = (total/members)-paid;
                leftTopay.setText(String.valueOf(left));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    FirebaseDatabase.getInstance().getReference().child(city).child("History").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for(DataSnapshot s:snapshot.getChildren()){
                      Transaction tr = s.getValue(Transaction.class);
                      String his = tr.getName()+"  ";
                      his+="+"+tr.getAmt()+"   ";
                      his+=tr.getTime();
                      newl.addView(create(his,-1));
                      newl.addView(space());


            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

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
        iv.setImageResource(R.drawable.arrow);
        lly.addView(iv);
        lly.addView(txt);
        return lly;


    }

}
