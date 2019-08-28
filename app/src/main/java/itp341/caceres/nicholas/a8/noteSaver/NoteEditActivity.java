package itp341.caceres.nicholas.a8.noteSaver;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NoteEditActivity extends AppCompatActivity {

    private EditText titleET;
    private String noteTitle;
    private EditText contentET;
    private String noteContent;
    private int noteIndex;

    private Button saveNoteButton;
    private Button deleteNoteButton;
    private ButtonListener sDListener;

    public static final String EXTRA_NOTE_TITLE = "itp341.caceres.nicholas.a8.note_title";
    public static final String EXTRA_NOTE_CONTENT = "itp341.caceres.nicholas.a8.note_content";
    public static final String EXTRA_NOTE_INDEX = "itp341.caceres.nicholas.a8.note_index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        Intent i = getIntent();

        titleET = (EditText) findViewById(R.id.titleEditText);
        contentET = (EditText) findViewById(R.id.contentEditText);
        noteIndex = i.getIntExtra(EXTRA_NOTE_INDEX, -1);
        if (noteIndex != -1) {
            noteTitle = i.getStringExtra(EXTRA_NOTE_TITLE);
            titleET.setText(noteTitle);
            noteContent = i.getStringExtra(EXTRA_NOTE_CONTENT);
            contentET.setText(noteContent);
        }
        /*titleET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                noteTitle = titleET.getText().toString();
                return true;
            }
        });*/
        titleET.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { noteTitle = titleET.getText().toString(); }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        /*contentET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                noteContent = contentET.getText().toString();
                return true;
            }
        });*/
        contentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { noteContent = contentET.getText().toString(); }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        sDListener = new ButtonListener();
        saveNoteButton = (Button) findViewById(R.id.saveButton);
        saveNoteButton.setOnClickListener(sDListener);
        deleteNoteButton = (Button) findViewById(R.id.deleteButton);
        deleteNoteButton.setOnClickListener(sDListener);
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.saveButton: {
                    Intent i = getIntent();
                    i.putExtra(EXTRA_NOTE_TITLE, noteTitle);
                    i.putExtra(EXTRA_NOTE_CONTENT, noteContent);
                    i.putExtra(EXTRA_NOTE_INDEX, noteIndex);
                    // Date handled on construction (or reconstruction) of new Note
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }
                case R.id.deleteButton: {
                    Intent i = getIntent();
                    i.putExtra(EXTRA_NOTE_INDEX, noteIndex);
                    setResult(NoteListActivity.RESULT_DELETED, i);
                    finish();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
