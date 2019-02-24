package com.rejaluo.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.rejaluo.todolist.model.MyObjectBox;
import com.rejaluo.todolist.model.Todo;
import com.rejaluo.todolist.model.Todo_;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class MainActivity extends AppCompatActivity {
    public static String TAG = MainActivity.class.getName();

    BoxStore boxStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boxStore = MyObjectBox.builder().androidContext(this).build();
        Box<Todo> todoBox = boxStore.boxFor(Todo.class);
        todoBox.removeAll();

        /**
         * Put new record into todo data object
         */
        Todo todo = new Todo();
        todo.setTask("Create homework");
        todo.setStatus(1);
        todoBox.put(todo);

        /**
         * Bulk operations to put new records
         */
        List<Todo> todoList = new ArrayList<>();
        todoList.add(addTask("Send email", 0));
        todoList.add(addTask("Create proposal", 0));
        todoBox.put(todoList);

        /**
         * Get all record as list
         */
        List<Todo> todos = todoBox.getAll();
        Log.d(TAG, "Todo list size: " + todos.size());

        /**
         * Query with condition
         */
        todos = todoBox.query().equal(Todo_.task, "Create Homework").build().find();
        Log.d(TAG, "Todo query (task equals create homework) : " + todos.size());

        /**
         * Query with multiple condition
         */
        todos = todoBox.query().startsWith(Todo_.task, "Create").equal(Todo_.status, 0).build().find();
        Log.d(TAG, "Todo query (start with create and status equals 0) : " + todos.size());

        /**
         * Edit record
         */
        todos = todoBox.query().contains(Todo_.task, "email").build().find();
        Log.d(TAG, "Todo query (task contains email) : " + todos.get(0).getTask() + ", status: " + todos.get(0).getStatus());

        Todo todoEdit = todos.get(0);
        todoEdit.setTask("Send email to client");
        todoEdit.setStatus(1);
        todoBox.put(todoEdit);

        todos = todoBox.query().contains(Todo_.task, "email").build().find();
        Log.d(TAG, "Todo query (task contains email) : " + todos.get(0).getTask() + ", status: " + todos.get(0).getStatus());
    }

    private Todo addTask(String task, int status) {
        Todo todo = new Todo();
        todo.setTask(task);
        todo.setStatus(status);
        return todo;
    }
}
