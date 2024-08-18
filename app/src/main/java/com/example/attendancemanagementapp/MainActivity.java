package com.example.attendancemanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    classAdapter classAdapter;
    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;
    DbHelper dbHelper;
    ArrayList<classItem> classItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DbHelper(this);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.floatingActionBtn);
        fab.setOnClickListener(v -> showDialog());
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        classAdapter = new classAdapter(this, classItems);
        recyclerView.setAdapter(classAdapter);
        classAdapter.setOnItemClickListener(position -> gotoItemActivity(position));
        setToolbar();
        loadData();
    }

    private void loadData() {
        Cursor cursor = dbHelper.getClassTable();

        classItems.clear();

        // Log column names
        int idColumnIndex = cursor.getColumnIndex(DbHelper.C_ID);
        int classNameColumnIndex = cursor.getColumnIndex(DbHelper.CLASS_NAME_KEY);
        int subjectNameColumnIndex = cursor.getColumnIndex(DbHelper.SUBJECT_NAME_KEY);

        Log.d("ColumnNames", "ID: " + DbHelper.C_ID);
        Log.d("ColumnNames", "ClassName: " + DbHelper.CLASS_NAME_KEY);
        Log.d("ColumnNames", "SubjectName: " + DbHelper.SUBJECT_NAME_KEY);

        while (cursor.moveToNext()){
            int id = cursor.getInt(idColumnIndex);
            String className =cursor.getString(classNameColumnIndex);
            String subjectName =cursor.getString(subjectNameColumnIndex);

            classItems.add(new classItem(id,className,subjectName));
        }
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton save = toolbar.findViewById(R.id.save);
        ImageButton back = toolbar.findViewById(R.id.back);

        title.setText("Attendance App");
        subtitle.setVisibility(View.GONE);
        save.setVisibility(View.INVISIBLE);
        back.setVisibility(View.INVISIBLE);
    }

    private void gotoItemActivity(int position) {
        Intent intent = new Intent(this, StudentActivity.class);

        intent.putExtra("ClassName", classItems.get(position).getClassName());
        intent.putExtra("SubjectName", classItems.get(position).getSubjectName());
        intent.putExtra("position", position);
        intent.putExtra("cid", classItems.get(position).getCid());
        startActivity(intent);
    }

    private void showDialog() {

        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), MyDialog.CLASS_ADD_DIALOG);
        dialog.setListener((className, subjectName) -> addClass(className, subjectName));
    }


    private void addClass(String className, String subjectName) {
        long cid = dbHelper.addClass(className,subjectName);
        classItem classItem = new classItem(cid,className, subjectName);
        classItems.add(classItem);
        classAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case 0:
                showUpdateDialog(item.getGroupId());
                break;
            case 1:
                deleteClass(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(int position) {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.CLASS_UPDATE_DIALOG);
        dialog.setListener((className,subjectName)->updateClass(position,className,subjectName));

    }

    private void updateClass(int position, String className, String subjectName) {
        dbHelper.updateClass(classItems.get(position).getCid(),className,subjectName);
        classItems.get(position).setClassName(className);
        classItems.get(position).setSubjectName(subjectName);
        classAdapter.notifyItemChanged(position);
    }

    private void deleteClass(int position) {
        dbHelper.deleteClass(classItems.get(position).getCid());
        classItems.remove(position);
        classAdapter.notifyItemChanged(position);
    }
}