package com.example.taskmanagerapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText, dueDateEditText;
    private Button saveButton;
    private TaskRepository taskRepository;
    private long taskId = -1; // Default for new tasks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        titleEditText = findViewById(R.id.title_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        dueDateEditText = findViewById(R.id.due_date_edit_text);
        saveButton = findViewById(R.id.save_button);

        taskRepository = new TaskRepository(this);
        taskRepository.open();

        // Check if we're editing an existing task
        Intent intent = getIntent();
        if (intent.hasExtra("TASK_ID")) {
            taskId = intent.getLongExtra("TASK_ID", -1);
            String title = intent.getStringExtra("TASK_TITLE");
            String description = intent.getStringExtra("TASK_DESCRIPTION");
            String dueDate = intent.getStringExtra("TASK_DUE_DATE");

            titleEditText.setText(title);
            descriptionEditText.setText(description);
            dueDateEditText.setText(dueDate);

            saveButton.setText("Update Task");
        }

        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String dueDate = dueDateEditText.getText().toString();

            if (taskId == -1) {
                // Add new task
                taskRepository.addTask(title, description, dueDate);
                Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show();
            } else {
                // Update existing task
                taskRepository.updateTask(taskId, title, description, dueDate);
                Toast.makeText(this, "Task Updated", Toast.LENGTH_SHORT).show();
            }
            finish(); // Go back to the previous activity
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        taskRepository.close();
    }
}
