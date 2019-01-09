package project.tronku.architectureexample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    private static final int ADD_NOTE_REQUEST = 101;
    private static final int EDIT_NOTE_REQUEST = 201;

    private TextView noNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView noteRecyclerView = findViewById(R.id.noteRecyclerView);
        final NoteAdapter adapter = new NoteAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        noteRecyclerView.setAdapter(adapter);
        noteRecyclerView.setLayoutManager(manager);

        noNotes = findViewById(R.id.no_notes_found);

        FloatingActionButton addFab = findViewById(R.id.addNote);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, AddEditNoteActivity.class), ADD_NOTE_REQUEST);
            }
        });

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if(notes.size() != 0) {
                    noNotes.setVisibility(View.INVISIBLE);
                    noteRecyclerView.setVisibility(View.VISIBLE);
                }
                else {
                    noNotes.setVisibility(View.VISIBLE);
                    noteRecyclerView.setVisibility(View.INVISIBLE);
                }
                adapter.submitList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(noteRecyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Note note) {
                Intent edit = new Intent(MainActivity.this, AddEditNoteActivity.class);
                edit.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
                edit.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                edit.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                edit.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());
                startActivityForResult(edit, EDIT_NOTE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == ADD_NOTE_REQUEST) {

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(desc, title, priority);
            noteViewModel.insert(note);

            Toast.makeText(MainActivity.this, "Note saved!", Toast.LENGTH_SHORT).show();
        }

        else if (resultCode == RESULT_OK && requestCode == EDIT_NOTE_REQUEST) {

            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(MainActivity.this, "Note can't be updated!", Toast.LENGTH_SHORT).show();
            }

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(desc, title, priority);
            note.setId(id);
            noteViewModel.update(note);

            Toast.makeText(MainActivity.this, "Note updated!", Toast.LENGTH_SHORT).show();
        }

        else
            Toast.makeText(MainActivity.this, "Note not saved!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAll:
                noteViewModel.deleteAll();
                Toast.makeText(MainActivity.this, "Deleted all notes!", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
