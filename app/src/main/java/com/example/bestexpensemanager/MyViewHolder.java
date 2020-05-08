package com.example.bestexpensemanager;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MyViewHolder extends RecyclerView.ViewHolder {
    TextView date,type,note,amount;
    View view;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.date_text_income);
        type=itemView.findViewById(R.id.type_text_income);
        note =itemView.findViewById(R.id.note_text_income);
        amount=itemView.findViewById(R.id.amount_text_income);
        view=itemView;
    }
}
