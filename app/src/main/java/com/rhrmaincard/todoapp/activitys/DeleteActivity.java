package com.rhrmaincard.todoapp.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.rhrmaincard.todoapp.configs.AppConfig;
import com.rhrmaincard.todoapp.databases.Repositorys.TaskRepo;
import com.rhrmaincard.todoapp.databases.entitys.Task;

import java.util.List;

public class DeleteActivity extends AppCompatActivity {

    private TextToSpeech tts;

    Task deletingTask;
    TaskRepo myRepo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getBundleExtra("delete-args")!=null){
            Bundle args = getIntent().getBundleExtra("delete-args");
            deletingTask = (Task) args.getSerializable("delete-task");
        }
        else{
            Log.e("task not found in action delete activity","delete activity for action");
        }

        myRepo = new TaskRepo(this);
        Log.e("delete task obj",deletingTask+"");
        Log.e("delete task name",deletingTask.getTask()+"");
        Log.e("delete task desc",deletingTask.getDesc()+"");
        Log.e("delete task time",deletingTask.getDue_time()+"");

            myRepo.deleteTask(deletingTask);
            Log.e("Deleted from onCreate if else","task deleted");
            List<Task> mylist = myRepo.getAll();
            for (int i=0;i<mylist.size();i++){
                Log.e("list val index: "+i+" = ",mylist.get(i).getTask()+"");
            }
//            finish();

        Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
        finish();
        Log.e("Deleted from delete activity on create func","task deleted");
        startActivity(new Intent(DeleteActivity.this, NavMainActivity.class));


//            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
//            finish();
//            startActivity(new Intent(UpdateTaskActivity.this, NavMainActivity.class));

//            Toast.makeText(getApplicationContext(), "Deleted from onCreate if else", Toast.LENGTH_LONG).show();
//            finish();
//            startActivity(new Intent(UpdateTaskActivity.this, NavMainActivity.class));
        }




}