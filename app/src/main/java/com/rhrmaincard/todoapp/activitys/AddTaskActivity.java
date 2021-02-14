package com.rhrmaincard.todoapp.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rhrmaincard.todoapp.R;
import com.rhrmaincard.todoapp.databases.Repositorys.TaskRepo;
import com.rhrmaincard.todoapp.databases.entitys.Task;
import com.rhrmaincard.todoapp.fragments.DatePickerFragment;
import com.rhrmaincard.todoapp.fragments.TimePickerFragment;
import com.rhrmaincard.todoapp.utils.MyAlarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, View.OnClickListener {
    private EditText editTextTask, editTextDesc, etDate, etTime;
    public TaskRepo taskRepository;
    List<Task> allTask;
    List<Task> tasks;
    DialogFragment datePicker;
    DialogFragment timePicker;
    long createdAt;
//    TimePicker timePicker;
    Calendar calendar;
    Button pick_time, saveBtn;
    ImageView ivDate, ivDateClose, ivTime, ivTimeClose;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    String currentDateString;
    String timeText;
    String ttsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ttsText ="";
        if(getIntent().getStringExtra("task-name")!=null){
            ttsText += getIntent().getStringExtra("task-name");
        }

        editTextTask = findViewById(R.id.editTextTask);
        editTextTask.setText("");
        editTextTask.setText(ttsText);
        editTextDesc = findViewById(R.id.editTextDesc);
        etDate = findViewById(R.id.et_date);
        etTime = findViewById(R.id.et_time);
        ivDate = findViewById(R.id.iv_date);
        ivTime = findViewById(R.id.iv_time);
        ivDateClose = findViewById(R.id.iv_date_close);
        ivTimeClose = findViewById(R.id.iv_time_close);
        saveBtn = findViewById(R.id.button_save);

        taskRepository = new TaskRepo(this);
        calendar = Calendar.getInstance();

//        etDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                showDateDialog();
////                year = calendar.get(Calendar.YEAR);
////                month = calendar.get(Calendar.MONTH);
////                day = calendar.get(Calendar.DAY_OF_MONTH);
////                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this,AddTaskActivity.this,year, month,day);
////                datePickerDialog.show();
//            }
//        });
        etDate.setOnClickListener(this);
        ivDate.setOnClickListener(this);
        ivDateClose.setOnClickListener(this);
        etTime.setOnClickListener(this);
        ivTime.setOnClickListener(this);
        ivTimeClose.setOnClickListener(this);
        saveBtn.setOnClickListener(this);




//        timePicker = (TimePicker) findViewById(R.id.timePicker);
//      allTask = taskRepository.getAll();

//        findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                    calendar = Calendar.getInstance();
////                    calendar.setTimeInMillis(System.currentTimeMillis());
////                    if (android.os.Build.VERSION.SDK_INT >= 23) {
////                        calendar.set(calendar.get(Calendar.YEAR),
////                                calendar.get(Calendar.MONTH),
////                                calendar.get(Calendar.DAY_OF_MONTH),
////                                timePicker.getHour(),
////                                timePicker.getMinute(), 0);
////
////                    } else {
////                        calendar.set(calendar.get(Calendar.YEAR),
////                                calendar.get(Calendar.MONTH),
////                                calendar.get(Calendar.DAY_OF_MONTH),
////                                timePicker.getCurrentHour(),
////                                timePicker.getCurrentMinute(), 0);
////                    }
//
//                saveTask();
//                }
//        });

    }

    private void showDateDialog() {
        if(datePicker!=null){
            datePicker.show(getSupportFragmentManager(), "date picker");
        }else{
            datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        }
    }

    private void showTimeDialog() {
        if(timePicker==null){
            timePicker = new TimePickerFragment();
            timePicker.show(getSupportFragmentManager(), "time picker");
        }else{
            timePicker.show(getSupportFragmentManager(), "time picker");
        }
    }


    private void saveTask() {
        final String sTask = editTextTask.getText().toString().trim();
        final String sDesc = editTextDesc.getText().toString().trim();
//        final String sFinishBy = editTextFinishBy.getText().toString().trim();

        if (sTask.isEmpty()) {
            editTextTask.setError("Task required");
            editTextTask.requestFocus();
            return;
        }

        if (sDesc.isEmpty()) {
            editTextDesc.setError("Desc required");
            editTextDesc.requestFocus();
            return;
        }

//        if (sFinishBy.isEmpty()) {
//            editTextFinishBy.setError("Finish by required");
//            editTextFinishBy.requestFocus();
//            return;
//        }
        //setting the repeating alarm that will be fired every day
        long time = calendar.getTimeInMillis();
        Log.e("Calendar getTImeMillis",calendar.getTimeInMillis()+"");


        //creating a task
        Task task = new Task();
        task.setTask(sTask);
        task.setDesc(sDesc);
        task.setCreated_at(createdAt);
        task.setDue_time(time);

//        task.setFinishBy(sFinishBy);
        task.setFinished(false);

        //adding task to database
        tasks = new ArrayList<>();
        tasks.add(task);
        taskRepository.addTask(tasks);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, MyAlarm.class);
        Bundle args = new Bundle();
        args.putSerializable("task-obj",(Serializable) task);

        i.putExtra("args-obj",args);
//        i.putExtra("str","string value");

        //creating a pending intent using the intent
        final int id = (int) System.currentTimeMillis();
        PendingIntent pi = PendingIntent.getBroadcast(this, id, i, PendingIntent.FLAG_ONE_SHOT);


        am.set(AlarmManager.RTC_WAKEUP,time, pi);
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();



        Log.e("the date i got",currentDateString);
        Log.e("the time i got",timeText);

        // broadcast sending
//        Intent intent = new Intent("Task-Send-Receiver") ;
//        intent.putExtra("Task-Receive",task);
//        getApplicationContext().sendBroadcast(intent);

        //returning to the MainActivity -> NavMainActivity
        finish();
        startActivity(new Intent(getApplicationContext(), NavMainActivity.class));
        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
    }


    void clearDateAndTime(){
        calendar.setTimeInMillis(System.currentTimeMillis());
                    if (android.os.Build.VERSION.SDK_INT >= 23) {
                        calendar.set(calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE));
                    }
//        Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int minute = c.get(Calendar.MINUTE);
//
//        calendar.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
//        calendar.set(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH,
//                    Calendar.HOUR_OF_DAY,Calendar.MINUTE,0);

        currentDateString = java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL).format(calendar.getTime());
        etDate.setText("");
        etDate.setText(currentDateString);

        timeText = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(calendar.getTime());
        etTime.setText("");
        etTime.setText(timeText);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        Calendar c = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//        java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL)

        //        TextView textView = (TextView) findViewById(R.id.textView);
//        textView.setText(currentDateString);

        myYear = year;
        myday = dayOfMonth;
        myMonth = month;

        ivDateClose.setVisibility(View.VISIBLE);
        currentDateString = java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL).format(calendar.getTime());
        etDate.setText(currentDateString);


//        Calendar c = Calendar.getInstance();
//        hour = c.get(Calendar.HOUR);
//        minute = c.get(Calendar.MINUTE);


//        TimePickerDialog timePickerDialog = new TimePickerDialog(AddTaskActivity.this, AddTaskActivity.this, hour, minute, DateFormat.is24HourFormat(this));
//        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);
        myHour = hourOfDay;
        myMinute = minute;
        ivTimeClose.setVisibility(View.VISIBLE);

        timeText = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(calendar.getTime());
        etTime.setText(timeText);

        Toast.makeText(this,"Year: " + myYear + "\n" +
                "Month: " + myMonth + "\n" +
                "Day: " + myday + "\n" +
                "Hour: " + myHour + "\n" +
                "Minute: " + myMinute,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_date:
                showDateDialog();
                break;
            case R.id.iv_date:
                showDateDialog();
                break;
            case R.id.iv_date_close:
                clearDateAndTime();
                ivDateClose.setVisibility(View.GONE);
                break;
            case R.id.et_time:
               showTimeDialog();
                break;
            case R.id.iv_time:
                showTimeDialog();
                break;
            case R.id.iv_time_close:
                clearDateAndTime();
                ivTimeClose.setVisibility(View.GONE);
                break;
            case R.id.button_save:
                createdAt = System.currentTimeMillis();
                saveTask();
                break;
        }
    }


}