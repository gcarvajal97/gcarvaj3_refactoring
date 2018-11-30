/**
 * CurrentProject.java
 * Created on 13.02.2003, 13:16:52 Alex
 * Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 *
 */
package memoranda;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Vector;

import memoranda.interfaces.*;
import memoranda.ui.AppFrame;
import memoranda.util.Context;
import memoranda.util.CurrentStorage;
import memoranda.interfaces.IStorage;

/**
 *
 */
/*$Id: CurrentProject.java,v 1.6 2005/12/01 08:12:26 alexeya Exp $*/
public class CurrentProject {

    private static IProject _I_project = null;
    private static ITaskList _tasklist = null;
    private static INoteList _notelist = null;
    private static IResourcesList _resources = null;
    private static Vector projectListeners = new Vector();

        
    static {
        String prjId = (String)Context.get("LAST_OPENED_PROJECT_ID");
        if (prjId == null) {
            prjId = "__default";
            Context.put("LAST_OPENED_PROJECT_ID", prjId);
        }
        //ProjectManager.init();
        _I_project = ProjectManager.getProject(prjId);
		
		if (_I_project == null) {
			// alexeya: Fixed bug with NullPointer when LAST_OPENED_PROJECT_ID
			// references to missing project
			_I_project = ProjectManager.getProject("__default");
			if (_I_project == null)
				_I_project = (IProject)ProjectManager.getActiveProjects().get(0);
            Context.put("LAST_OPENED_PROJECT_ID", _I_project.getID());
			
		}		
		
        _tasklist = CurrentStorage.get().openTaskList(_I_project);
        _notelist = CurrentStorage.get().openNoteList(_I_project);
        _resources = CurrentStorage.get().openResourcesList(_I_project);
        AppFrame.addExitListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save();                                               
            }
        });
    }
        

    public static IProject get() {
        return _I_project;
    }

    public static ITaskList getTaskList() {
            return _tasklist;
    }

    public static INoteList getNoteList() {
            return _notelist;
    }
    
    public static IResourcesList getResourcesList() {
            return _resources;
    }

    public static void set(IProject IProject) {
        if (IProject.getID().equals(_I_project.getID())) return;
        ITaskList newtasklist = CurrentStorage.get().openTaskList(IProject);
        INoteList newnotelist = CurrentStorage.get().openNoteList(IProject);
        IResourcesList newresources = CurrentStorage.get().openResourcesList(IProject);
        notifyListenersBefore(IProject, newnotelist, newtasklist, newresources);
        _I_project = IProject;
        _tasklist = newtasklist;
        _notelist = newnotelist;
        _resources = newresources;
        notifyListenersAfter();
        Context.put("LAST_OPENED_PROJECT_ID", IProject.getID());
    }

    public static void addProjectListener(IProjectListener pl) {
        projectListeners.add(pl);
    }

    public static Collection getChangeListeners() {
        return projectListeners;
    }

    private static void notifyListenersBefore(IProject IProject, INoteList nl, ITaskList tl, IResourcesList rl) {
        for (int i = 0; i < projectListeners.size(); i++) {
            ((IProjectListener)projectListeners.get(i)).projectChange(IProject, nl, tl, rl);
            /*DEBUGSystem.out.println(projectListeners.get(i));*/
        }
    }
    
    private static void notifyListenersAfter() {
        for (int i = 0; i < projectListeners.size(); i++) {
            ((IProjectListener)projectListeners.get(i)).projectWasChanged();
        }
    }

    public static void save() {
        IStorage IStorage = CurrentStorage.get();

        IStorage.storeNoteList(_notelist, _I_project);
        IStorage.storeTaskList(_tasklist, _I_project);
        IStorage.storeResourcesList(_resources, _I_project);
        IStorage.storeProjectManager();
    }
    
    public static void free() {
        _I_project = null;
        _tasklist = null;
        _notelist = null;
        _resources = null;
    }
}
