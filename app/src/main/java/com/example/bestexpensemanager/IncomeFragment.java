package com.example.bestexpensemanager;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bestexpensemanager.Module.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class IncomeFragment extends Fragment {

    FirebaseAuth mAuth;
    DatabaseReference ref;
    private FirebaseRecyclerOptions<Data> options;
    private FirebaseRecyclerAdapter<Data,MyViewHolder> adapter;
    private RecyclerView recyclerView;

    private TextView incomeTotalSum;
    int totalIncome=0;
    private EditText edtAmmount,edtType,edtNote;

    private Button btnUpdate,btnDelete;
    private Context context;


    public IncomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View MyView= inflater.inflate(R.layout.fragment_income, container, false);
        mAuth=FirebaseAuth.getInstance();
        incomeTotalSum=MyView.findViewById(R.id.income_text_result);
        totalIncome=0;
        FirebaseUser mUser = mAuth.getCurrentUser();
        final String uId= mUser.getUid();
        ref= FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uId);
        recyclerView=MyView.findViewById(R.id.recycler_id_income);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        options = new FirebaseRecyclerOptions.Builder<Data>().setQuery(ref,Data.class).build();
        adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyViewHolder holder, int position, @NonNull final Data model) {

                totalIncome=totalIncome + model.getAmount();
                incomeTotalSum.setText(""+totalIncome);
                final String key = getRef(position).getKey();
               holder.amount.setText(""+model.getAmount());
               holder.note.setText(model.getNote());
               holder.type.setText(model.getType());
               holder.date.setText(model.getDate());
               holder.view.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Toast.makeText(getContext(), "i am clicked", Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(getActivity(),updateDeleteIncome.class);
                       intent.putExtra("uid",uId);
                       intent.putExtra("Key",key);
                       intent.putExtra("given","IncomeData");
                       intent.putExtra("type",model.getType());
                       intent.putExtra("note",model.getNote());
                       intent.putExtra("amount",""+model.getAmount());
                       startActivity(intent);

                   }
               });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_recycler_data,parent,false);
                return new MyViewHolder(v);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

        return MyView;
    }
    public void updateDataItem(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View myView = inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        myDialog.setView(myView);
        final AlertDialog dialog = myDialog.create();

        final EditText edtAmount = myView.findViewById(R.id.amount_edt);
        final EditText edtType = myView.findViewById(R.id.type_edt);
        final EditText edtNote = myView.findViewById(R.id.note_edt);
        dialog.setCancelable(false);

        Button btnSave= myView.findViewById(R.id.btnSave);
        Button btnCancel = myView.findViewById(R.id.btnCancel);
        Toast.makeText(getContext(), "i am clicked after", Toast.LENGTH_SHORT).show();
    }

}
