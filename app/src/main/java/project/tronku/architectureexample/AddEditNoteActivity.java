package project.tronku.architectureexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddEditNoteActivity extends AppCompatActivity {

    private String noteTitle, noteDesc;
    private int notePriority;
    private Intent intent;

    private EditText title, desc;
    private NumberPicker priority;
    private TextView actionText;

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_DESCRIPTION = "description";
    public static final String EXTRA_PRIORITY = "priority";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        intent = getIntent();

        title = findViewById(R.id.title);
        desc = findViewById(R.id.description);
        priority = findViewById(R.id.priority);
        actionText = findViewById(R.id.action_text);
        FloatingActionButton saveFab = findViewById(R.id.saveNote);

        priority.setMaxValue(10);
        priority.setMinValue(1);
        
        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });

        if(intent.hasExtra(EXTRA_ID)) {
            actionText.setText("Edit note");
            title.setText(intent.getStringExtra(EXTRA_TITLE));
            desc.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            priority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        }
        else {
            actionText.setText("Add note");
        }
    }

    private void saveNote() {
        noteTitle = title.getText().toString();
        noteDesc = desc.getText().toString();
        notePriority = priority.getValue();

        if(noteTitle.length() == 0 || noteDesc.length() == 0) {
            Toast.makeText(this, "Please enter title and description", Toast.LENGTH_LONG).show();
        }
        else {
            Intent data = new Intent();
            data.putExtra(EXTRA_TITLE, noteTitle);
            data.putExtra(EXTRA_DESCRIPTION, noteDesc);
            data.putExtra(EXTRA_PRIORITY, notePriority);

            int id = intent.getIntExtra(EXTRA_ID, -1);
            if (id != -1) {
                data.putExtra(EXTRA_ID, id);
            }

            setResult(RESULT_OK, data);
            finish();
        }
    }
}
