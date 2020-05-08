package com.example.bestexpensemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class updateDeleteIncome extends AppCompatActivity {
    DatabaseReference ref;
    EditText amount,type,note;
    Button delete,update;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_income);
        amount=findViewById(R.id.amount_edt);
        type=findViewById(R.id.type_edt);
        note=findViewById(R.id.note_edt);
        delete=findViewById(R.id.btnDelete);
        update=findViewById(R.id.btnUpdate);
        final String key = getIntent().getStringExtra("Key");
        String uId = getIntent().getStringExtra("uid");
        String given=getIntent().getStringExtra("given");
        String note_=getIntent().getStringExtra("note");
        String amount_=getIntent().getStringExtra("amount");
        String type_=getIntent().getStringExtra("type");
        ref= FirebaseDatabase.getInstance().getReference().child(given).child(uId);
        amount.setText(amount_);
        note.setText(note_);
        type.setText(type_);
//        ref.child(key).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Object object = dataSnapshot.child("amount").getValue();
//                String note_ = dataSnapshot.child("note").getValue().toString();
//                String type_ = dataSnapshot.child("type").getValue().toString();
//
//                amount.setText(amount_);
//                note.setText(note_);
//                type.setText(type_);
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Atype  = type.getText().toString().trim();
                String Aamount= amount.getText().toString().trim();
                String Anote = note.getText().toString().trim();
                int ourAmountInt=  Integer.parseInt(Aamount);
                ref.child(key).child("amount").setValue(ourAmountInt);
                ref.child(key).child("note").setValue(Anote);
                ref.child(key).child("type").setValue(Atype);
                Toast.makeText(updateDeleteIncome.this, "Done", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  ref.child(key).removeValue();
                 Toast.makeText(updateDeleteIncome.this, "Data removed", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

    }

}
