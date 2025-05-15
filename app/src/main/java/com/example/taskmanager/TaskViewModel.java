package com.example.taskmanager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.taskmanager.database.Task;
import com.example.taskmanager.database.TaskDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskViewModel extends ViewModel {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public TaskViewModel(TaskDao taskDao) {
        this.taskDao = taskDao;
        this.allTasks = taskDao.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<Task> getTaskById(int id) {
        return taskDao.getTaskById(id);
    }

    public void insert(Task task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> taskDao.insert(task));
    }

    public void update(Task task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> taskDao.update(task));
    }

    public void delete(Task task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> taskDao.delete(task));
    }
}
