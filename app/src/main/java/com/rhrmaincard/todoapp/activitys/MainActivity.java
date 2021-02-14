package com.rhrmaincard.todoapp.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rhrmaincard.todoapp.R;
import com.rhrmaincard.todoapp.adapter.TaskAdapter;
import com.rhrmaincard.todoapp.databases.Repositorys.TaskRepo;
import com.rhrmaincard.todoapp.databases.entitys.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    /***
     *      We do not use this [activity]
     *      we have [all_tasks_fragment]
     *
     */


    private FloatingActionButton buttonAddTask;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonAddTask = findViewById(R.id.floating_button_add);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });

        getTasks();

    }


    private void getTasks() {
        TaskRepo taskRepository = new TaskRepo(this);
        List<Task> taskList = taskRepository
                .getAll();

        TaskAdapter adapter = new TaskAdapter(MainActivity.this, taskList);
        recyclerView.setAdapter(adapter);

    }
}