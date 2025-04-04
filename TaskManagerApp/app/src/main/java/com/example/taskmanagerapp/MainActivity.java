package com.example.taskmanagerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView taskListView;
    private Button addTaskButton;
    private TaskRepository taskRepository;
    private TaskAdapter taskAdapter;
    private List<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        taskListView = findViewById(R.id.task_list);  // Reference to the ListView
        addTaskButton = findViewById(R.id.add_task_button);  // Reference to the Button

        // Initialize the repository and adapter
        taskRepository = new TaskRepository(this);
        taskRepository.open();
        tasks = taskRepository.getAllTasks();
        taskAdapter = new TaskAdapter(this, tasks);
        taskListView.setAdapter(taskAdapter);

        // Set up the add task button click listener
        addTaskButton.setOnClickListener(v -> {
            // Start AddTaskActivity to add a new task
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload tasks from the database when the activity is resumed
        tasks.clear();
        tasks.addAll(taskRepository.getAllTasks());
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        taskRepository.close();
    }
}
