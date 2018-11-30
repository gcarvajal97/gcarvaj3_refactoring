package memoranda.ui;

import javax.swing.*;
import javax.swing.table.*;

import memoranda.*;
import memoranda.date.*;
import memoranda.interfaces.IProject;
import memoranda.interfaces.ITask;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

public class TaskTableSorterIA extends TaskTableModelIA {
	
	// -1 == no sorting
	int sorting_column = -1;
	
	// sort opposite direction
	boolean opposite = false;
	
	Comparator comparator = new Comparator(){
		public int compare(Object o1, Object o2){
			if(sorting_column == -1) return 0;
			if( (o1 instanceof ITask) == false) return 0;
			if( (o2 instanceof ITask) == false ) return 0;
			
			
			ITask ITask1 = (ITask) o1;
			ITask ITask2 = (ITask) o2;
			
			// based on TaskTableModelIA.columnNames
			switch(sorting_column){
				case 1: return ITask1.getText().compareTo(ITask2.getText());
				case 2: return ITask1.getStartDate().getDate().compareTo(ITask2.getStartDate().getDate());
				case 3: return ITask1.getEndDate().getDate().compareTo(ITask2.getEndDate().getDate());
				case 0: // ITask priority, same as 4
				case 4: return ITask1.getPriority() - ITask2.getPriority();
				case 5: return ITask1.getStatus( CurrentDate.get() ) - ITask2.getStatus( CurrentDate.get() );
				case 6: return ITask1.getProgress() - ITask2.getProgress();
			}
			
			return 0;
		}
	};
	
	public TaskTableSorterIA(TaskTable table ){
		JTableHeader tableHeader = table.getTableHeader();
		tableHeader.addMouseListener( new MouseHandler() );
		tableHeader.setDefaultRenderer( new SortableHeaderRenderer());
	}
	
	public Object getChild(Object parent, int index) {
		Collection c = null;
		
		if (parent instanceof IProject){
			if( activeOnly() ) c = CurrentProject.getTaskList().getActiveSubTasks(null, CurrentDate.get());
			else c = CurrentProject.getTaskList().getTopLevelTasks();
		}
		else{
			ITask t = (ITask) parent;
			if(activeOnly()) c = CurrentProject.getTaskList().getActiveSubTasks(t.getID(), CurrentDate.get());
			else c = t.getSubTasks();
		}
		
		Object array[] = c.toArray();
		Arrays.sort(array, comparator);
		if(opposite){
			return array[ array.length - index - 1];
		}
		return array[index];
	}

	
    
    private class MouseHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            JTableHeader h = (JTableHeader) e.getSource();
            TableColumnModel columnModel = h.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(e.getX());
            int column = columnModel.getColumn(viewColumn).getModelIndex();
            if (column != -1) {
		sorting_column = column;
		
		// 0 == priority icon column
		// 4 == priority text column
		if(column == 0) sorting_column = 4;
		
		if(e.isControlDown()) sorting_column = -1;
		else opposite = !opposite;
		
		TaskTable treetable = ( (TaskTable) h.getTable());
		
		//java.util.Collection expanded = treetable.getExpandedTreeNodes();
		
		treetable.tableChanged();
		//treetable.setExpandedTreeNodes(expanded);
		//h.updateUI();
		h.resizeAndRepaint();
            }
        }
    }
    
	/**
	* Render sorting header differently
	*/
	private class SortableHeaderRenderer implements TableCellRenderer {
	    
	    
	    
		public Component getTableCellRendererComponent(JTable table, 
							       Object value,
							       boolean isSelected, 
							       boolean hasFocus,
							       int row, 
							       int column) {
			JComponent c = new JLabel(value.toString());
			if(column == sorting_column){
				c.setFont(c.getFont().deriveFont(Font.BOLD));
				//c.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.BLACK));
			}
			else c.setFont(c.getFont().deriveFont(Font.PLAIN));
			return c;
		}
	}
	
}
