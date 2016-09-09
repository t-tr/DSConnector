/**
 * 
 */
package com.ttr.db.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

 
/**
 * The Class QAOverviewComposite.
 * 
 * @author t_tr
 */
public class TabComposite extends Composite {

	private ColComposite colComposite;
	
	/**
	 * @return the colComposite
	 */
	public ColComposite getColComposite() {
		return colComposite;
	}

	/**
	 * @param colComposite the colComposite to set
	 */
	public void setColComposite(ColComposite colComposite) {
		this.colComposite = colComposite;
	}

	/**
	 * Instantiates a new qA overview composite.
	 * 
	 * @param parent
	 *            the parent
	 * @param style
	 *            the style
	 */
	public TabComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		TabFolder tabFolder = new TabFolder(this, SWT.NONE);

		TabItem tbtmColumnDescription = new TabItem(tabFolder, SWT.NONE);
		tbtmColumnDescription.setText("Column description");
		
		ColComposite colComposite = new ColComposite(tabFolder, SWT.NONE);
		tbtmColumnDescription.setControl(colComposite);
		colComposite.setLayout(new FillLayout(SWT.HORIZONTAL));
		this.setColComposite(colComposite);
		
		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Table content");
		
		Label lblNewLabel = new Label(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(lblNewLabel);
		lblNewLabel.setText("New Label");
		
	}

	/**
	 * Creates the contents.
	 */
	public void createContents() {
		setLayout(new GridLayout(1, false));


	}
}
