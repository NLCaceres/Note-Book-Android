package itp341.caceres.nicholas.a8.noteSaver.Model;

import java.util.ArrayList;

/**
 * Created by NLCaceres on 4/5/2016.
 */
public class NoteSingleton {

    private static NoteSingleton singleton;

    private ArrayList<Note> notesList;

    private NoteSingleton() {
        notesList = new ArrayList<Note>();
        Note introNote = new Note();
        introNote.setTitle("Introduction Note");
        introNote.setNoteContent("To set up a new note, hit the add button and follow the prompts in the next screen");
        Note editingNote = new Note();
        editingNote.setTitle("Editing Note");
        editingNote.setNoteContent("To edit a note you made just click it and you'll get back to the screen you used to make it");
        notesList.add(introNote);
        notesList.add(editingNote);
    }

    public void insertNote(Note newNote) {
        notesList.add(newNote);
    }

    public Note getNote(int index) {
        return notesList.get(index);
    }

    public ArrayList<Note> getNotesList() {
        return notesList;
    }

    public static NoteSingleton getInstance() {
        if (singleton == null) {
            singleton = new NoteSingleton();
        }
        return singleton;
    }

}
