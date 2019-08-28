package itp341.caceres.nicholas.a8.noteSaver.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by NLCaceres on 4/5/2016.
 */
public class Note {
    private String Title;
    private String NoteContent;
    private Date DateB4Format;

    public Note() {
        Title = "";
        NoteContent = "";
        DateB4Format = new Date();
    }

    public Note(String title, String noteContent) {
        Title = title;
        NoteContent = noteContent;
        DateB4Format = new Date();
    }

    public String getTitle() {
        return Title;
    }

    public String getNoteContent() {
        return NoteContent;
    }

    public String getNoteDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("M/d/yy");
        String dateFormatted = formatter.format(DateB4Format);
        return dateFormatted;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setNoteContent(String noteContent) {
        NoteContent = noteContent;
    }

    public void setNoteDate() {
        DateB4Format = new Date();
    }


}
