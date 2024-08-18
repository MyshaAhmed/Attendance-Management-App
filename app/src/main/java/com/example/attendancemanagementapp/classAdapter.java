package com.example.attendancemanagementapp;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class classAdapter extends RecyclerView.Adapter<classAdapter.ClassViewHolder> {
    ArrayList<classItem> classItems;
    Context context;
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onclick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public classAdapter(Context context, ArrayList<classItem> classItems) {
        this.classItems = classItems;
    }
    public static class ClassViewHolder extends  RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView className;
        TextView subjectName;

        public ClassViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            className= itemView.findViewById(R.id.class_tv);
            subjectName= itemView.findViewById(R.id.subject_tv);
            itemView.setOnClickListener(v->onItemClickListener.onclick(getAdapterPosition()));
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(getAdapterPosition(),0,0,"EDIT");
                menu.add(getAdapterPosition(),1,0,"DELETE");

        }
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemVIew = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item,parent,false);
        return new ClassViewHolder(itemVIew,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        holder.className.setText(classItems.get(position).getClassName());
        holder.subjectName.setText(classItems.get(position).getSubjectName());
}

    @Override
    public int getItemCount() {
        return classItems.size();
    }
}
