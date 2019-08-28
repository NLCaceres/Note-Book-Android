package itp341.caceres.nicholas.a8.noteSaver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import itp341.caceres.nicholas.a8.noteSaver.Model.Note;
import itp341.caceres.nicholas.a8.noteSaver.Model.NoteSingleton;

public class NoteListActivity extends AppCompatActivity {

    private Button addButton;
    private ListView notesLV;
    private NoteSingleton noteModel;
    private notesAdapter adapter;
    private int noteIndex;
    private ActionMode mActionMode;

    public static final int EDIT_ACTIVITY_REQUEST = 0;
    public static final int RESULT_DELETED = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        noteModel = NoteSingleton.getInstance();

        addButton = (Button) findViewById(R.id.addNoteButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), NoteEditActivity.class);
                startActivityForResult(i, 0);
            }
        });

        notesLV = (ListView) findViewById(R.id.noteListView);
        notesLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        adapter = new notesAdapter(this, noteModel.getNotesList());
        notesLV.setAdapter(adapter);
        notesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), NoteEditActivity.class);
                i.putExtra(NoteEditActivity.EXTRA_NOTE_INDEX, position);
                i.putExtra(NoteEditActivity.EXTRA_NOTE_TITLE, noteModel.getNotesList().get(position).getTitle());
                i.putExtra(NoteEditActivity.EXTRA_NOTE_CONTENT, noteModel.getNotesList().get(position).getNoteContent());
                startActivityForResult(i, 0);
            }
        });
        /*notesLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                noteIndex = position;
                if (mActionMode != null) {
                    return true;
                }

                mActionMode = startActionMode(mActionModeCallback);
                //view.setSelected(true);
                // noteModel.getNotesList().remove(position);
                // adapter.notifyDataSetChanged();
                return true;
            }
        }); */
        notesLV.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int selectedCount = notesLV.getCheckedItemCount();
                mode.setTitle(selectedCount + " Selected");
                adapter.toggleSelection(position); // Custom Adapter func
            }
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.contextual_menu, menu);
                return true;
            }
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_icon:
                        SparseBooleanArray selected = adapter.getSelectedNotes(); // Helper Func
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                Note selectedNote = adapter.getItem(selected.keyAt(i));
                                adapter.remove(selectedNote); // Overridden version
                            }
                        }
                        mode.finish(); // Action bar destroy
                        return true;
                    default:
                        return false;
                }
            }
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }
            @Override
            public void onDestroyActionMode(ActionMode mode) { adapter.removeSelection(); }
        });
    }

    public class notesAdapter extends ArrayAdapter<Note> {
        private SparseBooleanArray mSelectedItemsIds;

        public notesAdapter(Context context, ArrayList<Note> notes) {
            super(context, 0, notes);
            mSelectedItemsIds = new SparseBooleanArray();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Note note = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);
            }
            TextView noteName = (TextView) convertView.findViewById(R.id.noteNameTV);
            TextView noteDate = (TextView) convertView.findViewById(R.id.noteDateTV);
            noteName.setText(note.getTitle());
            noteDate.setText(note.getNoteDate());
            return convertView;
        }

        @Override
        public void remove(Note note) {
            noteModel.getNotesList().remove(note);
            notifyDataSetChanged();
        }
        public void toggleSelection(int position) {
            selectView(position, !mSelectedItemsIds.get(position));
        }
        public void removeSelection() {
            mSelectedItemsIds = new SparseBooleanArray();
            notifyDataSetChanged();
        }
        public void selectView(int position, boolean value) {
            if (value) { mSelectedItemsIds.put(position, value); }
            else { mSelectedItemsIds.delete(position); }
            notifyDataSetChanged();
        }
        public SparseBooleanArray getSelectedNotes() {
            return mSelectedItemsIds;
        }
    }

    /*private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.contextual_menu, menu);
            return true;
        }
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete_icon: {
                    noteModel.getNotesList().remove(noteIndex);
                    adapter.notifyDataSetChanged();
                    mode.finish();
                    return true;
                }
                default:
                    return false;
            }
        }
        @Override
        public void onDestroyActionMode(ActionMode mode) { // Called when back button pressed
            Log.d("On Destroy Action Mode", "Hit the destroy button");
            mActionMode = null;
        }
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }
    }; */

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EDIT_ACTIVITY_REQUEST: {
                if (resultCode == Activity.RESULT_OK) {
                    String noteTitle = data.getStringExtra(NoteEditActivity.EXTRA_NOTE_TITLE);
                    String noteContent = data.getStringExtra(NoteEditActivity.EXTRA_NOTE_CONTENT);
                    int noteIndex = data.getIntExtra(NoteEditActivity.EXTRA_NOTE_INDEX, -1);
                    Note newNote = new Note(noteTitle, noteContent);
                    if (noteIndex != -1) {
                        noteModel.getNotesList().remove(noteIndex);
                        //adapter.notifyDataSetChanged();
                        adapter.insert(newNote, noteIndex);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        adapter.add(newNote);
                        adapter.notifyDataSetChanged();
                    }
                }
                else if (resultCode == RESULT_DELETED) {
                    int noteIndex = data.getIntExtra(NoteEditActivity.EXTRA_NOTE_INDEX, -1);
                    if (noteIndex != -1) {
                        noteModel.getNotesList().remove(noteIndex);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        break;
                    }
                }
                else {
                    break;
                }
                break;
            }
        }
    }
}
