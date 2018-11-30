/**
 * CurrentStorage.java
 * Created on 13.02.2003, 18:30:59 Alex
 * Package: net.sf.memoranda.util
 * 
 * @author Alex V. Alishevskikh, alex@openmechanics.net
 * Copyright (c) 2003 Memoranda Team. http://memoranda.sf.net
 */
package memoranda.util;
import memoranda.interfaces.IStorage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Vector;

/**
 * 
 */
/*$Id: CurrentStorage.java,v 1.3 2004/01/30 12:17:42 alexeya Exp $*/
public class CurrentStorage {
    
    /**
     * @todo: implement storage congiguration
     */
    private static IStorage _I_storage = new FileStorage();
    
    private static Vector actionListeners = new Vector();
    
    public static IStorage get() {
        return _I_storage;
    }
    
    public static void set(IStorage IStorage) {
        _I_storage = IStorage;
        storageChanged();
    }
    
    public static void addChangeListener(ActionListener al) {
        actionListeners.add(al);
    }

    public static Collection getChangeListeners() {
        return actionListeners;
    }
    
    private static void storageChanged() {
        for (int i = 0; i < actionListeners.size(); i++)
            ((ActionListener)actionListeners.get(i)).actionPerformed(new ActionEvent(null,0,"Current storage changed"));
    }
   
}
