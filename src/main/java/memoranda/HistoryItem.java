/**
 * HistoryItem.java
 * Created on 07.03.2003, 18:31:39 Alex
 * Package: net.sf.memoranda
 * 
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package memoranda;

import memoranda.date.CalendarDate;
import memoranda.interfaces.INote;
import memoranda.interfaces.IProject;

/**
 * 
 */
/*$Id: HistoryItem.java,v 1.4 2004/10/06 19:15:43 ivanrise Exp $*/
public class HistoryItem {
    
    private CalendarDate _date;
    private IProject _I_project;
    /**
     * Constructor for HistoryItem.
     */
    public HistoryItem(CalendarDate date, IProject IProject) {
        _date = date;
        _I_project = IProject;
    }
    
    public HistoryItem(INote INote) {
        _date = INote.getDate();
        _I_project = INote.getProject();
    }
    
    public CalendarDate getDate() {
       return _date;
    }
    
    public IProject getProject() {
       return _I_project;
    }
    
    public boolean equals(HistoryItem i) {
       return i.getDate().equals(_date) && i.getProject().getID().equals(_I_project.getID());
    } 

}
