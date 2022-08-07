package com.example.tripisss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupList extends AppCompatActivity {
    ArrayList<String> list;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        ll = findViewById(R.id.Linear2);
        list = new ArrayList<String>();
        FirebaseDatabase.getInstance().getReference().child("Groups").child(pure(Globe.getInstance().user)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s:snapshot.getChildren()){
                    String var = s.getKey().toString();
                    if(list.contains(var)==false){
                        list.add(var);
                        ll.addView(create(var,list.size()-1));
                        ll.addView(space());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        lly.addView(iv);
        lly.addView(txt);
        lly.setId(100+x);
        lly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pa = new Intent(getApplicationContext(),Expense.class);
                int idx = v.getId();
                pa.putExtra("nc", list.get(idx-100));
                //Toast.makeText(GroupList.this, String.valueOf(100-idx), Toast.LENGTH_SHORT).show();
                startActivity(pa);
            }
        });
        return lly;


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