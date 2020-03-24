package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class NoteEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Initialization
        final DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());

        final EditText titleEditText = findViewById(R.id.titleEditText);
        final EditText contentEditText = findViewById(R.id.contentEditText);
        ImageView saveImageView = findViewById(R.id.saveImageView);

        //Intent(add or update)
        final Intent intent = getIntent();
        final String option = intent.getStringExtra("option");

        if (option.equals("update")) {
            Log.i("Update 1","Called");
            //Set the values
            titleEditText.setText(intent.getStringExtra("title"));
            contentEditText.setText(intent.getStringExtra("content"));
        }


        //Set on click listener for the save button toolbar
        saveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check if either name or message has value
                if (titleEditText.getText().toString().isEmpty() && contentEditText.getText().toString().isEmpty()) {
                    Toast.makeText(NoteEditorActivity.this, "Name and message are empty.", Toast.LENGTH_SHORT).show();
                }
                else {

                    //Check if you create a new Note or Update an existing note and go back to main activity
                    if (option.equals("add")) {
                        dbHandler.addNote(titleEditText.getText().toString(), contentEditText.getText().toString(), R.drawable.ic_pending);
                        Toast.makeText(NoteEditorActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NoteEditorActivity.this, MainActivity.class));
                    } else {
                        int receivedId = intent.getIntExtra("id", -1);
                        dbHandler.updateNote(receivedId, titleEditText.getText().toString(), contentEditText.getText().toString());
                        Toast.makeText(NoteEditorActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NoteEditorActivity.this, MainActivity.class));
                    }
                }
            }
        });


        titleEditText.setSelection(titleEditText.getText().length());
        contentEditText.setSelection(contentEditText.getText().length());

    }
}
