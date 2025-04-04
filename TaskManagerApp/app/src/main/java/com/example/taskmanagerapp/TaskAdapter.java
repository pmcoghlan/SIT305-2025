package com.example.taskmanagerapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class TaskAdapter extends BaseAdapter {

    private Context context;
    private List<Task> taskList;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return taskList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get task data
        Task task = taskList.get(position);

        // Inflate the view (row) for the task item
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.task_list_item, parent, false);
        }

        // Bind task data to the views
        TextView titleTextView = convertView.findViewById(R.id.task_title);
        TextView dueDateTextView = convertView.findViewById(R.id.task_due_date);
        Button editButton = convertView.findViewById(R.id.edit_button);
        Button deleteButton = convertView.findViewById(R.id.delete_button);

        titleTextView.setText(task.getTitle());
        dueDateTextView.setText(task.getDueDate());

        // Set Edit button listener
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddTaskActivity.class);
            intent.putExtra("TASK_ID", task.getId());  // Use "TASK_ID" key to match AddTaskActivity
            context.startActivity(intent);
        });

        // Set Delete button listener
        deleteButton.setOnClickListener(v -> {
            TaskRepository taskRepository = new TaskRepository(context);
            taskRepository.open();
            taskRepository.deleteTask(task.getId());
            taskList.remove(position);  // Remove the task from the list
            notifyDataSetChanged();     // Refresh the list view
        });

        return convertView;
    }
}
