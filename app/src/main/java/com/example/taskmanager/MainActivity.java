package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.database.Task;
import com.example.taskmanager.database.TaskDao;
import com.example.taskmanager.database.TaskDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private TaskAdapter adapter;
    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewTasks);
        adapter = new TaskAdapter(task -> {
            Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
            intent.putExtra("task_id", task.getId());
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Initiate ViewModel
        TaskDao taskDao = TaskDatabase.getInstance(this).taskDao();
        taskViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new TaskViewModel(taskDao);
            }
        }).get(TaskViewModel.class);

        taskViewModel.getAllTasks().observe(this, tasks -> {
            adapter.setTasks(tasks);

            // If task list is empty，insert some test task
            if (tasks == null || tasks.isEmpty()) {
                taskViewModel.insert(new Task("Academic works", "Complete Task4.1", System.currentTimeMillis() + 86400000)); // Tomorrow
                taskViewModel.insert(new Task("Meetings", "Discuss the progress with the project team members", System.currentTimeMillis() + 2 * 86400000));
                taskViewModel.insert(new Task("Study", "Read Room Persistence Document", System.currentTimeMillis() + 3 * 86400000));
            }
        });


        FloatingActionButton fab = findViewById(R.id.fabAddTask);
        fab.setOnClickListener(v -> startActivity(new Intent(this, AddEditTaskActivity.class)));
    }
}