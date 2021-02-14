package com.rhrmaincard.todoapp.adapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.rhrmaincard.todoapp.R;
import com.rhrmaincard.todoapp.activitys.UpdateTaskActivity;
import com.rhrmaincard.todoapp.databases.Repositorys.TaskRepo;
import com.rhrmaincard.todoapp.databases.entitys.Task;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.logging.LogRecord;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TasksViewHolder> {

    private Context mCtx;
    private List<Task> taskList;
    TaskRepo taskRepository;
    Handler handler;


    private RequestQueue requestQueue;
    private String TAG  = "Simple-Request";
    private String Image_Tag  = "Image-Request";

    public TaskAdapter(Context mCtx, List<Task> taskList) {
        this.mCtx = mCtx;
        this.taskList = taskList;
        taskRepository = new TaskRepo(mCtx);
        handler = new Handler();
        requestQueue = Volley.newRequestQueue(mCtx);
        notifyDataSetChanged();
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.adapter_recyclerview_tasks, parent, false);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        Task t = taskList.get(position);

        String currentDateString = java.text.DateFormat.getDateInstance(java.text.DateFormat.FULL).format(t.getDue_time());
        String timeText = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(t.getDue_time());

        holder.textViewTask.setText(t.getTask());
        holder.textViewDesc.setText(t.getDesc());

        holder.tvDueDate.setText("Due Date: "+currentDateString);
        holder.tvDueTime.setText("Due Time: "+timeText);

//        holder.textViewFinishBy.setText(t.getFinishBy());

        if (t.isFinished())
            holder.textViewStatus.setText("Completed");
        else
            holder.textViewStatus.setText("Not Completed");
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    /**
     *     ViewHolder class is below
     */
    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewStatus, textViewTask, textViewDesc, tvDueDate, tvDueTime;
        ImageView delTask, taskImg;

        public TasksViewHolder(View itemView) {
            super(itemView);

            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewTask = itemView.findViewById(R.id.textViewTask);
            textViewDesc = itemView.findViewById(R.id.textViewDesc);
            tvDueDate = itemView.findViewById(R.id.tv_due_date);
            tvDueTime = itemView.findViewById(R.id.tv_due_time);
            taskImg = itemView.findViewById(R.id.task_img);
            delTask = itemView.findViewById(R.id.del_task);

            delTask.setOnClickListener(this);
            itemView.setOnClickListener(this);

            requestQueue.getCache().clear();
            requestQueue.cancelAll(Image_Tag);
            Picasso.get().load(String.format("https://picsum.photos/200/300?random=1"))
                    .into(taskImg);

//            loadImageOnImageView();
            handler.postAtTime(loadImage,5000);

        }
        Runnable loadImage =  new Runnable() {
            @Override
            public void run() {
//                requestQueue.getCache().clear();
//                requestQueue.cancelAll(Image_Tag);
//                loadImageOnImageView();
//                handler.postDelayed(loadImage,5000);
                Picasso.get().load(String.format("https://picsum.photos/200/300?random=1"))
                        .into(taskImg);

            }
        };

        @Override
        public void onClick(View view) {
            Task task = taskList.get(getAdapterPosition());

            if(view.getId()==R.id.del_task){
                taskRepository.deleteTask(task);
                taskList.clear();
                taskList.addAll(taskRepository.getAll());
                notifyDataSetChanged();

            }else {
                Intent intent = new Intent(mCtx, UpdateTaskActivity.class);
//            Bundle arg = new Bundle();
//            arg.putSerializable("task", (Serializable) task);
                intent.putExtra("task", task);
                intent.putExtra("normal", "val from recycler");
                mCtx.startActivity(intent);
            }
        }

        private void loadImageOnImageView() {
            String imgUrl = "https://picsum.photos/200/300?random=1";
            /**
             * image request
             */
            ImageRequest imageRequest = new ImageRequest(imgUrl, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    taskImg.setImageBitmap(response);
                }
            }, 150,
                    150,
                    ImageView.ScaleType.CENTER_CROP,
                    Bitmap.Config.RGB_565,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Snackbar.make(itemView,"Image cant set", Snackbar.LENGTH_LONG).show();
                        }
                    });
            imageRequest.setTag(Image_Tag);
            requestQueue.add(imageRequest);
        }

    }

}