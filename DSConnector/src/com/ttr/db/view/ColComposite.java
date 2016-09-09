/**
 * 
 */
package com.ttr.db.view;

import java.text.Collator;
import java.util.Locale;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

 
/**
 * The Class QAOverviewComposite.
 * 
 * @author t_tr
 */
public class ColComposite extends Composite {
	private Table colDescTable;


	/**
	 * @return the colDescTable
	 */
	public Table getColDescTable() {
		return colDescTable;
	}

	/**
	 * @param colDescTable the colDescTable to set
	 */
	public void setColDescTable(Table colDescTable) {
		this.colDescTable = colDescTable;
	}

	/**
	 * Instantiates a new qA overview composite.
	 * 
	 * @param parent
	 *            the parent
	 * @param style
	 *            the style
	 */
	public ColComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		CheckboxTableViewer checkboxTableViewer = CheckboxTableViewer.newCheckList(this, SWT.BORDER | SWT.FULL_SELECTION);
		colDescTable = checkboxTableViewer.getTable();
		colDescTable.setHeaderVisible(true);
		colDescTable.setLinesVisible(true);
		
		TableColumn tblclmnCheckBox = new TableColumn(colDescTable, SWT.NONE);
		tblclmnCheckBox.setWidth(30);

		TableColumn tblclmnOrdinalPos = new TableColumn(colDescTable, SWT.NONE);
		tblclmnOrdinalPos.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("clic");
			}
		});
		tblclmnOrdinalPos.setMoveable(true);
		tblclmnOrdinalPos.setWidth(70);
		tblclmnOrdinalPos.setText("Position");
		
		tblclmnOrdinalPos.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	  System.out.println("clic2");
		    	  // sort column 1
		          TableItem[] items = getColDescTable().getItems();
		          Collator collator = Collator.getInstance(Locale.getDefault());
		          for (int i = 1; i < items.length; i++) {
		            String value1 = items[i].getText(0);
		            for (int j = 0; j < i; j++) {
		              String value2 = items[j].getText(0);
		              if (collator.compare(value1, value2) < 0) {
		                String[] values = { items[i].getText(0),
		                    items[i].getText(1) };
		                items[i].dispose();
		                TableItem item = new TableItem(getColDescTable(), SWT.NONE, j);
		                item.setText(values);
		                items = getColDescTable().getItems();
		                break;
		              }
		            }
		          }
		        }
		      });
		

		TableColumn tblclmnName = new TableColumn(colDescTable, SWT.NONE);
		tblclmnName.setMoveable(true);
		tblclmnName.setWidth(200);
		tblclmnName.setText("Name");
		tblclmnName.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
		    	  System.out.println("clic3");
		          // sort column 1
		          TableItem[] items = getColDescTable().getItems();
		          Collator collator = Collator.getInstance(Locale.getDefault());
		          for (int i = 1; i < items.length; i++) {
		            String value1 = items[i].getText(0);
		            for (int j = 0; j < i; j++) {
		              String value2 = items[j].getText(0);
		              if (collator.compare(value1, value2) < 0) {
		                String[] values = { items[i].getText(0),
		                    items[i].getText(1) };
		                items[i].dispose();
		                TableItem item = new TableItem(getColDescTable(), SWT.NONE, j);
		                item.setText(values);
		                items = getColDescTable().getItems();
		                break;
		              }
		            }
		          }
		        }
		      });

		TableColumn tblclmnType = new TableColumn(colDescTable, SWT.NONE);
		tblclmnType.setMoveable(true);
		tblclmnType.setWidth(150);
		tblclmnType.setText("Type");
		
		TableColumn tblclmnKey = new TableColumn(colDescTable, SWT.NONE);
		tblclmnKey.setMoveable(true);
		tblclmnKey.setWidth(60);
		tblclmnKey.setText("Key");
	}

	/**
	 * Creates the contents.
	 */
	public void createContents() {
		setLayout(new GridLayout(1, false));


	}
}
