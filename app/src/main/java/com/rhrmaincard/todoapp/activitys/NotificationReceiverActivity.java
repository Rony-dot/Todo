package com.rhrmaincard.todoapp.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rhrmaincard.todoapp.R;
import com.rhrmaincard.todoapp.databases.Repositorys.TaskRepo;
import com.rhrmaincard.todoapp.databases.entitys.Task;

import java.util.Calendar;

public class NotificationReceiverActivity extends AppCompatActivity {
    private TextView tvTaskName, tvTaskDesc, tvTaskDate, tvTaskTime;
    private CheckBox checkBoxFinishedOrNot;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_receiver);
    }


    @Override
    protected void onStart() {
        super.onStart();
        tvTaskName = findViewById(R.id.tv_task_name);
        tvTaskDesc = findViewById(R.id.tv_task_desc);
        tvTaskDate = findViewById(R.id.tv_date_due);
        tvTaskTime = findViewById(R.id.tv_time_due);
        checkBoxFinishedOrNot = findViewById(R.id.checkBoxFinishedOrNot);
        Bundle args = getIntent().getBundleExtra("args-obj2");
        Task task = (Task) args.getSerializable("task-obj2");
        Log.e("task received inside notification receiver activity class ->",task.getTask()+"");
//        String string = args.getString("string");
//        Log.e("string recived ->",string+"");
        loadTaskView(task);
    }

    private void loadTaskView(Task task) {
        tvTaskName.setText(task.getTask());
        tvTaskDesc.setText(task.getDesc());
//        editTextFinishBy.setText(task.getFinishBy());
        checkBoxFinishedOrNot.setChecked(task.isFinished());

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(task.getDue_time());
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            calendar.set(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE));
        }

        currentDateString = java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL).format(calendar.getTime());
        tvTaskDate.setText("");
        tvTaskDate.setText(currentDateString);

        timeText = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(calendar.getTime());
        tvTaskTime.setText("");
        tvTaskTime.setText(timeText);
        Log.e("your task view",task.getTask()+"");
        Log.e("your task view",task.getDesc()+"");
        Log.e("your task view",currentDateString+"");
        Log.e("your task view",task.isFinished()+"");
    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Bundle args = intent.getBundleExtra("args-obj2");
//        Task task = (Task) args.getSerializable("task-obj2");
//        Log.e("task received inside notification receiver activity class ->",task.getTask()+"");
//        loadTaskView(task);
//    }
}