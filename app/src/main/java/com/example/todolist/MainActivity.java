package com.example.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ExampleItem> notesList;

    private RecyclerView notesRecyclerView;
    private ExampleAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    boolean taskIsComplete = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton fabDelete = findViewById(R.id.fabDelete);

        notesList = new ArrayList<>();
        /*
        notesList.add(new ExampleItem("Title 1",R.drawable.ic_pending));
        notesList.add(new ExampleItem("Title 2",R.drawable.ic_pending));
        notesList.add(new ExampleItem("Title 3",R.drawable.ic_pending));
        notesList.add(new ExampleItem("Title 4",R.drawable.ic_complete));
        notesList.add(new ExampleItem("Title 5",R.drawable.ic_pending));
         */

        final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        notesList = db.getAllNotes();

        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ExampleAdapter(this, notesList);

        notesRecyclerView.setLayoutManager(layoutManager);
        notesRecyclerView.setAdapter(adapter);

//      Updating Existing Note
        adapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            //Update note
            @Override
            public void onItemClick(int position) {
                Intent updateIntent = new Intent(MainActivity.this, NoteEditorActivity.class);
                updateIntent.putExtra("option","update");
                updateIntent.putExtra("id",notesList.get(position).getId());
                updateIntent.putExtra("title",notesList.get(position).getTitleText());
                updateIntent.putExtra("content",notesList.get(position).getContentText());
                updateIntent.putExtra("status",notesList.get(position).getStatusImage());
                startActivity(updateIntent);
            }

            //Delete note
            @Override
            public void onItemLongClick(final int position) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are You Sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteNote(notesList.get(position).getId());
                                notesList.remove(position);
                                adapter.notifyDataSetChanged();
                                //Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                                Snackbar.make(findViewById(R.id.coordinatorLayout), "Note Deleted", Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }

            //update status
            @Override
            public void onChangeClick(int position) {
                if(taskIsComplete){      //After clicking task is complete
                    notesList.get(position).changeImage(R.drawable.ic_complete);
                    adapter.notifyItemChanged(position);

                    db.updateTask(notesList.get(position).getId(),R.drawable.ic_complete);  //updating value in the database
                    taskIsComplete = false;
                    Toast.makeText(MainActivity.this, "Task Completed", Toast.LENGTH_SHORT).show();
                }
                else if(!taskIsComplete){
                    notesList.get(position).changeImage(R.drawable.ic_pending);
                    adapter.notifyItemChanged(position);

                    db.updateTask(notesList.get(position).getId(),R.drawable.ic_pending);
                    taskIsComplete = true;
                    Toast.makeText(MainActivity.this, "Task is Pending", Toast.LENGTH_SHORT).show();
                }
            }
        });

//      Adding New Note
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteEditorActivity.class);
                intent.putExtra("option","add");
                startActivity(intent);
            }
        });

        Log.i("Notes List size", String.valueOf(notesList.size()));


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                db.deleteNote(adapter.getNoteAt(viewHolder.getAdapterPosition()).getId());
//                notesList.remove(position);
//                adapter.notifyDataSetChanged();       //notifyDataSetChanged NOT NEEDED
                Snackbar.make(findViewById(R.id.coordinatorLayout), "Note Deleted", Snackbar.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(notesRecyclerView);

//      Delete all note
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notesList.size()!=0) {
                    db.deleteAllNote();
                    notesList.removeAll(notesList);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "All notes Deleted", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this, "List is empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
