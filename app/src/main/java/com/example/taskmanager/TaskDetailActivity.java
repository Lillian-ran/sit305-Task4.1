package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.taskmanager.database.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.taskmanager.database.TaskDao;
import com.example.taskmanager.database.TaskDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskDetailActivity extends AppCompatActivity {
    private TextView tvTitle, tvDescription, tvDueDate;
    private Button btnEdit, btnDelete;
    private TaskViewModel taskViewModel;
    private Task currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        int taskId = getIntent().getIntExtra("task_id", -1);
        if (taskId == -1) finish();

        // Initiate ViewModel
        TaskDao taskDao = TaskDatabase.getInstance(this).taskDao();
        taskViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new TaskViewModel(taskDao);
            }
        }).get(TaskViewModel.class);

        //
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvDueDate = findViewById(R.id.tvDueDate);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        // Task detail
        taskViewModel.getTaskById(taskId).observe(this, task -> {
            if (task != null) {
                currentTask = task;
                tvTitle.setText(task.getTitle());
                tvDescription.setText(task.getDescription());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                tvDueDate.setText(sdf.format(new Date(task.getDueDate())));
            }
        });

        // Delete button
        btnDelete.setOnClickListener(v -> {
            if (currentTask != null) {
                taskViewModel.delete(currentTask);
                finish();
            }
        });

        // Edit button
        btnEdit.setOnClickListener(v -> {
            if (currentTask != null) {
                Intent intent = new Intent(this, AddEditTaskActivity.class);
                intent.putExtra("task_id", currentTask.getId());
                startActivity(intent);
            }
        });
    }
}