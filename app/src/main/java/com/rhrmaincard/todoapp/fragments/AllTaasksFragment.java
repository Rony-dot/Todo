package com.rhrmaincard.todoapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rhrmaincard.todoapp.R;
import com.rhrmaincard.todoapp.activitys.AddTaskActivity;
import com.rhrmaincard.todoapp.activitys.MainActivity;
import com.rhrmaincard.todoapp.adapter.TaskAdapter;
import com.rhrmaincard.todoapp.databases.Repositorys.TaskRepo;
import com.rhrmaincard.todoapp.databases.entitys.Task;

import java.util.List;

public class AllTaasksFragment extends Fragment {
    private FloatingActionButton buttonAddTask;
    private RecyclerView recyclerView;

    public AllTaasksFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_taasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerview_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        buttonAddTask = view.findViewById(R.id.floating_button_add);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getTasks();
    }


    private void getTasks() {
        TaskRepo taskRepository = new TaskRepo(getContext());
        List<Task> taskList = taskRepository
                .getAll();
        TaskAdapter adapter  = new TaskAdapter(getActivity(), taskList);
        recyclerView.setAdapter(adapter);
    }

}