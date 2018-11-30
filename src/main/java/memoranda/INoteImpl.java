/**
 * INoteImpl.java
 * Created on 13.02.2003, 15:36:55 Alex
 * Package: net.sf.memoranda
 * 
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package memoranda;

import memoranda.date.CalendarDate;
import memoranda.interfaces.INote;
import memoranda.interfaces.IProject;
import nu.xom.Attribute;
import nu.xom.Element;

/**
 * 
 */
/*$Id: INoteImpl.java,v 1.6 2004/10/06 19:15:44 ivanrise Exp $*/
public class INoteImpl implements INote, Comparable {
    
    private Element _el = null; 
    private IProject _I_project;
    
    /**
     * Constructor for INoteImpl.
     */
    public INoteImpl(Element el, IProject IProject) {
        _el = el;
        _I_project = IProject;
    }

    /**
     * @see INote#getDate()
     */
    public CalendarDate getDate() {
		Element day = (Element)_el.getParent();
		Element month = (Element)day.getParent();
		Element year = (Element)month.getParent();

     //   return new CalendarDate(day.getAttribute("date").getValue());
		
		return new CalendarDate(new Integer(day.getAttribute("day").getValue()).intValue(), 
								new Integer(month.getAttribute("month").getValue()).intValue(),
								new Integer(year.getAttribute("year").getValue()).intValue());

    }
    
    public IProject getProject() {
        return _I_project;
    }
    /**
     * @see INote#getTitle()
     */
    public String getTitle() {
        Attribute ta = _el.getAttribute("title");
        if (ta == null) return "";
        return _el.getAttribute("title").getValue();
    }
    /**
     * @see INote#setTitle(java.lang.String)
     */
    public void setTitle(String s) {
        Attribute ta = _el.getAttribute("title");
        if (ta == null) _el.addAttribute(new Attribute("title", s));
        else 
            ta.setValue(s);
    }
	
	/**
     * @see INote#getId
     */
	
	public String getId() {
		Attribute id = _el.getAttribute("refid");
		if (id==null) return "";
		return _el.getAttribute("refid").getValue();
	}
	
	/**
     * @see INote#setId(java.lang.String)
     */
	 
	public void setId(String s) {
		Attribute id = _el.getAttribute("refid");
		if(id==null) _el.addAttribute(new Attribute("refid", s));
	}
    /**
     * @see INote#isMarked()
     */
    public boolean isMarked() {
        return _el.getAttribute("bookmark") != null;        
    }
    /**
     * @see INote#setMark(boolean)
     */
    public void setMark(boolean mark) {
        Attribute ma = _el.getAttribute("bookmark");        
        if (ma == null) {
            if (mark)
                _el.addAttribute(new Attribute("bookmark", "yes"));
            return;
        }
        else if (!mark)
            _el.removeAttribute(ma);
    }
	
	/*
	 * Comparable interface
	 */
	public int compareTo(Object o) {
		INote INote = (INote) o;
		boolean greaterThan = (getDate().getDate().getTime() > INote.getDate()
                .getDate().getTime());
		boolean lessThan = (getDate().getDate().getTime() < INote.getDate()
                .getDate()
                .getTime());
		if (greaterThan | lessThan) {
		    return (greaterThan) ? 1 : -1;
        }
        return 0;
	}
    
}
