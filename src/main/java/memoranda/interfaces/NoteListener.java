package memoranda.interfaces;

import memoranda.interfaces.Note;

public interface NoteListener {
  void noteChange(Note note, boolean toSaveCurrentNote);
}
