package com.rhrmaincard.todoapp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rhrmaincard.todoapp.R;
import com.rhrmaincard.todoapp.configs.AppConfig;
import com.rhrmaincard.todoapp.databases.Repositorys.TaskRepo;
import com.rhrmaincard.todoapp.databases.entitys.Task;
import com.rhrmaincard.todoapp.fragments.DatePickerFragment;
import com.rhrmaincard.todoapp.fragments.TimePickerFragment;
import com.rhrmaincard.todoapp.utils.MyAlarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpdateTaskActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private EditText editTextTask, editTextDesc, etDate, etTime;
    ImageView ivDate, ivDateClose, ivTime, ivTimeClose;
    private CheckBox checkBoxFinished;
    TaskRepo taskRepository;
    Calendar calendar;
    DialogFragment datePicker;
    DialogFragment timePicker;
    long createdAt;
    Task taskIF;
    Task task;
    String currentDateString;
    String timeText;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    Task task_alarm_receiver;
    Task deletingTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        editTextTask = findViewById(R.id.editTextTask);
        editTextDesc = findViewById(R.id.editTextDesc);
        etDate = findViewById(R.id.et_date);
        etTime = findViewById(R.id.et_time);
        ivDate = findViewById(R.id.iv_date);
        ivTime = findViewById(R.id.iv_time);
        ivDateClose = findViewById(R.id.iv_date_close);
        ivTimeClose = findViewById(R.id.iv_time_close);
//        editTextFinishBy = findViewById(R.id.editTextFinishBy);

        checkBoxFinished = findViewById(R.id.checkBoxFinished);
        calendar = Calendar.getInstance();

        findViewById(R.id.button_update).setOnClickListener(this);
        findViewById(R.id.button_delete).setOnClickListener(this);
        etDate.setOnClickListener(this);
        ivDate.setOnClickListener(this);
        ivDateClose.setOnClickListener(this);
        etTime.setOnClickListener(this);
        ivTime.setOnClickListener(this);
        ivTimeClose.setOnClickListener(this);

        if( getIntent().getSerializableExtra("task")!=null){
            taskIF = (Task) getIntent().getSerializableExtra("task");
            Log.e("task found for intent filter",taskIF.getTask()+"");
        }else{
            Log.e("no task for intent filter","no task in update Activity");
        }

        taskRepository = new TaskRepo(this);
//        Bundle args = getIntent().getExtras();
//        if(args!=null){
//        String str = args.getString("str");
//        Log.e("string found -> ",str+"");
//        }

    if(getIntent().getBundleExtra("args-obj")!=null){
        Bundle args = getIntent().getBundleExtra("args-obj");
        task_alarm_receiver = (Task) args.getSerializable("task-obj");
        Log.e("task_alarm_receiver in update activity: ",task_alarm_receiver.getTask()+"");
    }
   else{
        Log.e("no action found ","action not working in update activity");
    }


//        task_alarm_receiver = (Task) args.getSerializable("task-obj");

//        if(getIntent().getStringExtra("str")!=null){
//            Log.e("string received -> ",getIntent().getStringExtra("str"));
//            Log.e("object received",getIntent().getSerializableExtra("task-obj")+"");
//        }else{
//            Log.e("obj not found","null obj");
//        }

//        onNewIntent(getIntent());
        //        Task task_alarm_receiver = (Task) getIntent().getSerializableExtra("task-obj");

        if(task_alarm_receiver!=null){
            task = task_alarm_receiver;
            Log.e("task is received","Task  received from alarm receiver");
            loadTask(task);
        }else if(taskIF!=null){
            task = taskIF;
            Log.e("task intent filter received"," task received from intent filter");
            loadTask(task);
        }

        else {
            Log.e("task is null", "no task received");
        }



        String normal   = getIntent().getStringExtra("normal");
        Toast.makeText(this,"value : "+normal,Toast.LENGTH_LONG).show();



    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
////        Bundle args = getIntent().getBundleExtra("args-obj");
////        task_alarm_receiver = (Task) args.getSerializable("task-obj");
//
//    }



    private void loadTask(Task task) {
        editTextTask.setText(task.getTask());
        editTextDesc.setText(task.getDesc());
//        editTextFinishBy.setText(task.getFinishBy());
        checkBoxFinished.setChecked(task.isFinished());


        calendar.setTimeInMillis(task.getDue_time());
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            calendar.set(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE));
        }


        currentDateString = java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL).format(calendar.getTime());
        etDate.setText("");
        etDate.setText(currentDateString);

        timeText = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(calendar.getTime());
        etTime.setText("");
        etTime.setText(timeText);
    }

    private void updateTask(final Task task) {
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
//
//        if (sFinishBy.isEmpty()) {
//            editTextFinishBy.setError("Finish by required");
//            editTextFinishBy.requestFocus();
//            return;
//        }

        //setting the repeating alarm that will be fired every day
        Long time = calendar.getTimeInMillis();

        // broadcast sending
//        Intent intent = new Intent("Task-Send-Receiver") ;
//        intent.putExtra("Task-Receive",task);
//        getApplicationContext().sendBroadcast(intent);

        task.setTask(sTask);
        task.setDesc(sDesc);
//        task.setFinishBy(sFinishBy);
        task.setFinished(checkBoxFinished.isChecked());
        task.setDue_time(time);
        taskRepository.updateTask(task);


        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, MyAlarm.class);
        Bundle args = new Bundle();
        args.putSerializable("task-obj",(Serializable) task);

        i.putExtra("args-obj",args);
        //creating a pending intent using the intent
        final int id = (int) System.currentTimeMillis();
        PendingIntent pi = PendingIntent.getBroadcast(this, id, i, PendingIntent.FLAG_ONE_SHOT);

//        am.setInexactRepeating(AlarmManager.RTC_WAKEUP,time, AlarmManager.INTERVAL_DAY, pi);
        am.set(AlarmManager.RTC_WAKEUP,time, pi);
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();

        Log.e("the date i got",currentDateString);
        Log.e("the time i got",timeText);

        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(UpdateTaskActivity.this, NavMainActivity.class));
    }


    private void deleteTask(final Task task) {
        taskRepository.deleteTask(task);
        Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
        finish();
        Log.e("Deleted from delete task fucntion of update activity","task deleted");
        startActivity(new Intent(UpdateTaskActivity.this, NavMainActivity.class));

    }


    private void showDateDialog() {

        calendar.setTimeInMillis(task.getDue_time());
//        if (android.os.Build.VERSION.SDK_INT >= 23) {
//            calendar.set(calendar.get(Calendar.YEAR),
//                    calendar.get(Calendar.MONTH),
//                    calendar.get(Calendar.DAY_OF_MONTH),
//                    calendar.get(Calendar.HOUR_OF_DAY),
//                    calendar.get(Calendar.MINUTE));
//        }

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateTaskActivity.this,UpdateTaskActivity.this,year, month,day);
        datePickerDialog.show();

//        if(datePicker!=null){
//            datePicker.show(getSupportFragmentManager(), "date picker");
//        }else{
//            datePicker = new DatePickerFragment();
//            datePicker.show(getSupportFragmentManager(), "date picker");
//        }
    }

    private void showTimeDialog() {

        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateTaskActivity.this, UpdateTaskActivity.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();

//        if(timePicker==null){
//            timePicker = new TimePickerFragment();
//            timePicker.show(getSupportFragmentManager(), "time picker");
//        }else{
//            timePicker.show(getSupportFragmentManager(), "time picker");
//        }
    }



    void clearDateAndTime(){
        calendar.setTimeInMillis(task.getDue_time());
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

    private void deleteAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateTaskActivity.this);
        builder.setTitle("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteTask(task);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog ad = builder.create();
        ad.show();
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
            case R.id.button_update:
                //createdAt = System.currentTimeMillis();
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
                updateTask(task);
                break;
            case R.id.button_delete:
                deleteAlert();
                break;
        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        myYear = year;
        myday = dayOfMonth;
        myMonth = month;
        ivDateClose.setVisibility(View.VISIBLE);
        currentDateString = java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL).format(calendar.getTime());
        etDate.setText(currentDateString);
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
}