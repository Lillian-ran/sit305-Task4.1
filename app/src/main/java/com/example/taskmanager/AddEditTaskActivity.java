package com.example.taskmanager;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.taskmanager.database.Task;
import com.example.taskmanager.database.TaskDao;
import com.example.taskmanager.database.TaskDatabase;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

public class AddEditTaskActivity extends AppCompatActivity {
    private EditText etTitle, etDescription;
    private Button btnPickDate, btnSave;
    private long dueDateTimestamp = -1;
    private boolean isEditMode = false;
    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        // Initiate ViewModel
        TaskDao taskDao = TaskDatabase.getInstance(this).taskDao();
        taskViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new TaskViewModel(taskDao);
            }
        }).get(TaskViewModel.class);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnSave = findViewById(R.id.btnSave);

        // Date picker
        btnPickDate.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    (view, year, monthOfYear, dayOfMonth) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);
                        dueDateTimestamp = selectedDate.getTimeInMillis();
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.show(getSupportFragmentManager(), "DatePickerDialog");
        });


        // Save logic
        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            if (title.isEmpty()) {
                etTitle.setError("Title can not be empty");
                return;
            }

            if (dueDateTimestamp == -1) {
                Toast.makeText(this, "Please choose due date", Toast.LENGTH_SHORT).show();
                return;
            }

            Task task = new Task(title, description, dueDateTimestamp);
            if (isEditMode) {
                task.setId(getIntent().getIntExtra("task_id", -1));
                taskViewModel.update(task);
            } else {
                taskViewModel.insert(task);
            }
            finish();
        });

        //Edit
        int taskId = getIntent().getIntExtra("task_id", -1);
        if (taskId != -1) {
            isEditMode = true;
            taskViewModel.getAllTasks().observe(this, tasks -> {
                for (Task t : tasks) {
                    if (t.getId() == taskId) {
                        etTitle.setText(t.getTitle());
                        etDescription.setText(t.getDescription());
                        dueDateTimestamp = t.getDueDate();
                        break;
                    }
                }
            });
        }

    }
}