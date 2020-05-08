package com.example.bestexpensemanager;


import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bestexpensemanager.Module.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private FloatingActionButton fab_main_btn, fab_income_btn, fab_expence_btn;
    private TextView fab_income_txt, fab_expense_txt;
    boolean isOpen = false;
    private Animation fadeOpen, fadeClose;
    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;
    private TextView totalIncomeResult;
    private TextView totalExpenseResult;


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser muser= mAuth.getCurrentUser();
        String uid = muser.getUid();
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase= FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);
        fab_main_btn=myView.findViewById(R.id.fb_main_plus_btn);
        fab_expence_btn=myView.findViewById(R.id.expense_ft_btn);
        fab_income_btn=myView.findViewById(R.id.income_ft_btn);

        fab_income_txt=myView.findViewById(R.id.income_ft_text);
        fab_expense_txt=myView.findViewById(R.id.expense_ft_text);
        fadeOpen= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        fadeClose= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);

        totalIncomeResult=myView.findViewById(R.id.income_set_result);
        totalExpenseResult=myView.findViewById(R.id.expence_set_result);
        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addData();
                if(isOpen){


                    fab_expence_btn.startAnimation(fadeClose);
                    fab_income_btn.startAnimation(fadeClose);
                    fab_income_btn.setClickable(false);
                    fab_expence_btn.setClickable(false);

                    fab_income_txt.startAnimation(fadeClose);
                    fab_expense_txt.startAnimation(fadeClose);
                    fab_expense_txt.setClickable(false);
                    fab_income_txt.setClickable(false);
                    isOpen=false;

                }else{
                    fab_expence_btn.startAnimation(fadeOpen);
                    fab_income_btn.startAnimation(fadeOpen);
                    fab_income_btn.setClickable(true);
                    fab_expence_btn.setClickable(true);

                    fab_income_txt.startAnimation(fadeOpen);
                    fab_expense_txt.startAnimation(fadeOpen);
                    fab_expense_txt.setClickable(true);
                    fab_income_txt.setClickable(true);
                    isOpen=true;

                }
            }

        });

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalSum=0;
                for(DataSnapshot mysnap: dataSnapshot.getChildren()){
                    Data data=mysnap.getValue(Data.class);
                    totalSum+=data.getAmount();
                    String stResult = String.valueOf(totalSum);
                    totalIncomeResult.setText(stResult);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalSum=0;
                for(DataSnapshot mysnap: dataSnapshot.getChildren()){
                    Data data=mysnap.getValue(Data.class);
                    totalSum=totalSum+data.getAmount();
                    String stResult = String.valueOf(totalSum);
                    totalExpenseResult.setText(stResult);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return myView;
    }


    private void addData() {


        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            incomeDataInsert();

            }
        });
        fab_expence_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ExpenseDataInsert();
            }
        });
    }
    public void incomeDataInsert(){

        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        myDialog.setView(myView);
        final AlertDialog dialog = myDialog.create();

        final EditText edtAmount = myView.findViewById(R.id.amount_edt);
        final EditText edtType = myView.findViewById(R.id.type_edt);
        final EditText edtNote = myView.findViewById(R.id.note_edt);
        dialog.setCancelable(false);

        Button btnSave= myView.findViewById(R.id.btnSave);
        Button btnCancel = myView.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type  = edtType.getText().toString().trim();
                String amount= edtAmount.getText().toString().trim();
                String note = edtNote.getText().toString().trim();

                if(TextUtils.isEmpty(type)){
                    edtType.setError("Required field");
                    return;
                }

                if(TextUtils.isEmpty(amount)){
                    edtType.setError("Required amount");
                    return;
                }

                int ourAmountInt=  Integer.parseInt(amount);
                if(TextUtils.isEmpty(note)){
                    edtType.setError("Required note");
                    return;
                }

                String id = mIncomeDatabase.push().getKey();

                String mDate = DateFormat.getDateInstance().format(new Date());

                Data data = new Data(ourAmountInt,type,note,id,mDate);

                mIncomeDatabase.child(id).setValue(data);

                Toast.makeText(getActivity(), "Data added", Toast.LENGTH_SHORT).show();

                dialog.dismiss();


            }

        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    public  void ExpenseDataInsert(){

        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView =inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        mydialog.setView(myView);
        final AlertDialog dialog = mydialog.create();

        final EditText edtAmount = myView.findViewById(R.id.amount_edt);
        final EditText edtType = myView.findViewById(R.id.type_edt);
        final EditText edtNote = myView.findViewById(R.id.note_edt);
        dialog.setCancelable(false);

        Button btnSave= myView.findViewById(R.id.btnSave);
        Button btnCancel = myView.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type  = edtType.getText().toString().trim();
                String amount= edtAmount.getText().toString().trim();
                String note = edtNote.getText().toString().trim();

                if(TextUtils.isEmpty(type)){
                    edtType.setError("Required field");
                    return;
                }

                if(TextUtils.isEmpty(amount)){
                    edtType.setError("Required amount");
                    return;
                }

                int ourAmountInt=  Integer.parseInt(amount);
                if(TextUtils.isEmpty(note)){
                    edtType.setError("Required note");
                    return;
                }

                String id = mExpenseDatabase.push().getKey();

                String mDate = DateFormat.getDateInstance().format(new Date());

                Data dataa = new Data(ourAmountInt,type,note,id,mDate);

                mExpenseDatabase.child(id).setValue(dataa);
                Toast.makeText(getActivity(), "Data added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();


            }

        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

}
