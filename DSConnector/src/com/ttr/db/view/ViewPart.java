package com.ttr.db.view;

import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.ttr.db.exception.DBConnectException;
import com.ttr.db.mysql.DbAccess;
import com.ttr.db.mysql.MySQLException;


public class ViewPart extends org.eclipse.ui.part.ViewPart {

    private Composite compositeConnection;
    private Combo sgbdName;
    private Text ipAddress;
    private Text portNumber;
    private Text user;
    private Text password;
    private List listTable;
    private final DbAccess db;
    private Button btnConnection;
    private Text location;
    // private Table tableResult;
    private List listSchema;
    private Label lblTables;
    private SashForm sashForm;
    private SashForm sashFormView;
    private Label lblMessages;
    private Table tableMessage;

    private TabComposite bottomComposite;

    protected final static org.eclipse.swt.graphics.Color red = Display.getDefault().getSystemColor(SWT.COLOR_RED);
    protected final static org.eclipse.swt.graphics.Color blue = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
    protected final static org.eclipse.swt.graphics.Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);

    private boolean isConnected = false;

    /**
     * @return the isConnected
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * @param isConnected the isConnected to set
     */
    public void setConnected(final boolean isConnected) {
        this.isConnected = isConnected;
    }

    /**
     *
     */
    public ViewPart() {
        db = new DbAccess();
    }

    /**
     *
     */
    @Override
    public void createPartControl(final Composite parent) {
        final FillLayout fillLayout = (FillLayout) parent.getLayout();
        fillLayout.type = SWT.VERTICAL;

        sashFormView = new SashForm(parent, SWT.VERTICAL);
        sashFormView.setSashWidth(5);

        final Composite topComposite = new Composite(sashFormView, SWT.BORDER | SWT.NO_BACKGROUND);
        topComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

        sashForm = new SashForm(topComposite, SWT.NONE);
        sashForm.setSashWidth(5);

        compositeConnection = new Composite(sashForm, SWT.BORDER);
        compositeConnection.setLayout(new GridLayout(2, false));

        final Label lblSgbdType = new Label(compositeConnection, SWT.NONE);
        lblSgbdType.setText("SGBD type");

        sgbdName = new Combo(compositeConnection, SWT.READ_ONLY);
        sgbdName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        sgbdName.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                selectSgbd();
            }
        });
        sgbdName.add("MySQL", 0);
        sgbdName.add("DB2", 1);
        sgbdName.add("Oracle", 2);
        sgbdName.add("MS SQL Server", 3);
        sgbdName.setText("MySQL");

        final Label lblIpAddress = new Label(compositeConnection, SWT.NONE);
        lblIpAddress.setText("IP address");

        ipAddress = new Text(compositeConnection, SWT.BORDER);
        ipAddress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        ipAddress.setText("localhost");

        final Label lblPortnNumber = new Label(compositeConnection, SWT.NONE);
        lblPortnNumber.setText("Port number");

        portNumber = new Text(compositeConnection, SWT.BORDER);
        portNumber.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        portNumber.setText("3306");

        final Label lblLocation = new Label(compositeConnection, SWT.NONE);
        lblLocation.setText("Location");

        location = new Text(compositeConnection, SWT.BORDER);
        location.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        location.setEnabled(false);
        location.setVisible(false);

        final Label lblUser = new Label(compositeConnection, SWT.NONE);
        lblUser.setSize(23, 15);
        lblUser.setText("User");

        user = new Text(compositeConnection, SWT.BORDER);
        user.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        user.setText("root");

        final Label lblPassword = new Label(compositeConnection, SWT.NONE);
        lblPassword.setText("Password");
        lblPassword.setBounds(0, 0, 50, 15);

        password = new Text(compositeConnection, SWT.BORDER | SWT.PASSWORD);
        password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        password.setText("root");
        password.setBounds(0, 0, 206, 21);
        new Label(compositeConnection, SWT.NONE);

        btnConnection = new Button(compositeConnection, SWT.NONE);
        btnConnection.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        btnConnection.setText("  Connect  ");
        btnConnection.setEnabled(true);

        btnConnection.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                clickConnectBt();
            }
        });

        lblMessages = new Label(compositeConnection, SWT.NONE);
        lblMessages.setText("Messages");

        tableMessage = new Table(compositeConnection, SWT.BORDER | SWT.FULL_SELECTION);
        tableMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        final Composite compositeSchema = new Composite(sashForm, SWT.BORDER);
        compositeSchema.setLayout(new GridLayout(1, false));

        final Label lblSchema = new Label(compositeSchema, SWT.NONE);
        lblSchema.setText("Schemas");

        listSchema = new List(compositeSchema, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        final GridData gd_listSchema = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_listSchema.heightHint = 83;
        listSchema.setLayoutData(gd_listSchema);

        listSchema.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                // TODO log
                System.out.println("Selected line in listSchema: " + listSchema.getSelectionIndex());
                clickSchema(listSchema.getSelectionIndex());
            }
        });

        lblTables = new Label(compositeSchema, SWT.NONE);
        lblTables.setText("Tables");

        listTable = new List(compositeSchema, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        listTable.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                // TODO log
                System.out.println("Selected line in listTable: " + listTable.getSelectionIndex());
                clickTable(listTable.getSelectionIndex());
            }
        });

        listTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        sashForm.setWeights(new int[] {1, 1});

        bottomComposite = new TabComposite(sashFormView, SWT.NONE);
        bottomComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

        // Composite bottomComposite = new Composite(sashForm_1, SWT.NONE);
        // bottomComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

        // tableResult = new Table(bottomComposite, SWT.BORDER | SWT.FULL_SELECTION );
        // tableResult.setHeaderVisible(true);
        // tableResult.setLinesVisible(true);

        sashFormView.setWeights(new int[] {1, 1});

    }

    @Override
    public void setFocus() {
        // TODO Auto-generated method stub
    }

    /**
     * Connection to the sub system
     */
    private void clickConnectBt() {

        if (!this.isConnected()) {
            this.connect();
        } else {
            this.disconnect();
        }
    }

    /**
     * Connection to the sub system
     */
    private void connect() {

        try {
            compositeConnection.setEnabled(false);

            checkParameterConnection();

            db.getConnection(sgbdName.getText(), ipAddress.getText(), portNumber.getText(), user.getText(), password.getText(),
                location.getText());

            if (null == db.getConn()) {
                this.addMessage("Connection failed", 3);
            } else {
                this.setConnected(true);
                btnConnection.setText("Disconnect");

                listSchema.removeAll();
                listTable.removeAll();
                // tableResult.removeAll();

                /*
                 * while (tableResult.getColumnCount()>0){
                 * tableResult.getColumn(0).dispose();
                 * }
                 */

                // --- get the list of schemas
                final java.util.List<String> result = db.getSchemas(sgbdName.getText());

                if (result.isEmpty()) {
                    this.addMessage("No schema found", 3);
                    disconnect();
                } else {
                    final Iterator<String> itr = result.iterator();

                    while (itr.hasNext()) {
                        final String element = itr.next();
                        listSchema.add(element);
                    }
                }

                this.addMessage("Connection successful", 1);
            }
        } catch (final DBConnectException e) {
            this.addMessage(e.getMessage(), 3);
        } catch (final MySQLException e) {
            this.addMessage(e.getMessage(), 3);
        } finally {
            compositeConnection.setEnabled(true);
        }
    }

    /**
     * Check parameters for the connection
     */
    /**
     * @throws DBConnectException
     */
    private void checkParameterConnection() throws DBConnectException {

        if (null == sgbdName.getText() || null == ipAddress.getText() || null == portNumber.getText() || null == user.getText()
            || null == password.getText()) {
            throw new DBConnectException("A parameter is null");
        }

        if (sgbdName.getText().trim().equalsIgnoreCase("") || ipAddress.getText().trim().equalsIgnoreCase("")
            || portNumber.getText().trim().equalsIgnoreCase("")
            || user.getText().trim().equalsIgnoreCase("") /*
                                                           * ||
                                                           * -- password is optional
                                                           * password.getText().trim().equalsIgnoreCase("")
                                                           */) {
            throw new DBConnectException("A parameter is empty");
        }

        try {
            Integer.parseInt(portNumber.getText());
        } catch (final NumberFormatException e) {
            throw new DBConnectException("The port number is invalid");
        }
    }

    /**
     * clickBrowseBt
     */
    private void clickSchema(final int schema) {

        try {
            listTable.removeAll();

            final java.util.List<String> result = db.getTables(sgbdName.getText(), listSchema.getItem(schema));

            if (result.isEmpty()) {
                this.addMessage("The table is empty", 2);
            } else {
                final Iterator<String> itr = result.iterator();

                while (itr.hasNext()) {
                    final String element = itr.next();
                    listTable.add(element);
                }
            }
        } catch (final MySQLException e) {
            this.addMessage(e.getMessage(), 3);
        }
    }

    /**
     *
     * @param table
     */
    private void clickTable(final int table) {

        try {

            // --- get the column description

            final java.util.List<String[]> descriptions = db
                .getColumnDescription(listSchema.getItem(listSchema.getSelectionIndex()), listTable.getItem(table));

            /*
             * tableResult.removeAll();
             * while (tableResult.getColumnCount()>0){
             * tableResult.getColumn(0).dispose();
             * }
             */

            if (descriptions.isEmpty()) {
                this.addMessage("The table " + listTable.getItem(table) + " is empty", 2);
            } else {
                // System.out.println("Number of rows:" + rows.size());
                this.addMessage(descriptions.size() + " rows retrieved", 1);
                final Iterator<String[]> itr = descriptions.iterator();

                for (int i = 0; itr.hasNext(); i++) {
                    final String[] element = itr.next();

                    // TODO log
                    // System.out.println("element [" +i+"]=" + element.toString());

                    final TableItem item = new TableItem(bottomComposite.getColComposite().getColDescTable(), SWT.NONE, i);

                    item.setText(element);
                }
            }
        } catch (final MySQLException e) {
            this.addMessage(e.getMessage(), 3);
        }
    }

    /**
     *
     * @param table
     */
    @SuppressWarnings ("unused")
    private void browseTable(final int table) {

        int nbCols = 0;

        try {

            // --- get the column description

            final java.util.List<String> columns = db.getColumns(sgbdName.getText(),
                listSchema.getItem(listSchema.getSelectionIndex()), listTable.getItem(table));

            /*
             * tableResult.removeAll();
             * while (tableResult.getColumnCount()>0){
             * tableResult.getColumn(0).dispose();
             * }
             */

            if (columns.isEmpty()) {
                // TODO log
                this.addMessage("The table " + listTable.getItem(table) + " is empty", 2);
            } else {
                final Iterator<String> itr = columns.iterator();

                for (nbCols = 0; itr.hasNext(); nbCols++) {
                    final String element = itr.next();
                    // TODO log
                    System.out.println("column:" + element);

                    /*
                     * TableColumn column = new TableColumn(tableResult, SWT.NONE, nbCols);
                     * column.setMoveable(true);
                     * column.setText("Col " + (nbCols + 1) + " = " + element);
                     * column.pack();
                     */
                }
            }

            // --- fill the table

            final java.util.List<String[]> rows = db.browseTable(listSchema.getItem(listSchema.getSelectionIndex()),
                listTable.getItem(table), nbCols);

            if (rows.isEmpty()) {
                this.addMessage("The table " + listTable.getItem(table) + " is empty", 2);
            } else {
                // System.out.println("Number of rows:" + rows.size());
                this.addMessage(rows.size() + " rows retrieved", 1);
                final Iterator<String[]> itr = rows.iterator();

                for (; itr.hasNext();) {
                    itr.next();

                    // TODO log
                    // System.out.println("element [" +i+"]=" + element.toString());

                    // TableItem item = new TableItem(tableResult, SWT.NONE, i);
                    // item.setText(element);
                }
            }
        } catch (final MySQLException e) {
            this.addMessage(e.getMessage(), 3);
        }
    }

    private void selectSgbd() {

        disconnect();

        if (sgbdName.getText().equals("DB2")) {
            ipAddress.setEnabled(true);
            portNumber.setEnabled(true);
            user.setEnabled(true);
            listSchema.setEnabled(true);
            password.setEnabled(true);
            location.setEnabled(true);
            listTable.setEnabled(true);
            btnConnection.setText("Connection");
            btnConnection.setEnabled(true);

            // --- S0W1, DZBA
            ipAddress.setText("192.168.41.59");
            portNumber.setText("452");
            user.setText("");
            listSchema.removeAll();
            password.setText("");
            location.setText("DZAALOC1");

            // --- S0W1, DZBA
            ipAddress.setText("192.168.41.59");
            portNumber.setText("454");
            user.setText("");
            listSchema.removeAll();
            password.setText("");
            location.setText("DZBALOC1");

            // --- INSOFT HOST, DB8G
            /*
             * ipAddress.setText("192.168.9.63");
             * portNumber.setText("5024");
             * user.setText("");
             * listSchema.removeAll();
             * password.setText("");
             * location.setText("INSOFT8");
             */

            // --- ZPS1
            /*
             * ipAddress.setText("192.168.41.123");
             * portNumber.setText("452");
             * user.setText("");
             * listSchema.removeAll();
             * password.setText("");
             * location.setText("DZAALOC1");
             */

            // --- INSOFT HOST, DB8G, info11
            /*
             * ipAddress.setText("192.168.9.63");
             * portNumber.setText("5024");
             * user.setText("");
             * listSchema.removeAll();
             * password.setText("");
             * location.setText("INSOFT8");
             */
        }

        if (sgbdName.getText().equals("MySQL")) {
            ipAddress.setEnabled(true);
            portNumber.setEnabled(true);
            user.setEnabled(true);
            listSchema.setEnabled(true);
            password.setEnabled(true);
            listTable.setEnabled(true);
            btnConnection.setText("Connection");
            btnConnection.setEnabled(true);
            location.setEnabled(false);

            ipAddress.setText("sen57");
            portNumber.setText("3306");
            user.setText("root");
            listSchema.removeAll();
            password.setText("root");
            location.setText("");
        }

        if (sgbdName.getText().equals("Oracle") || sgbdName.getText().equals("MS SQL Server")) {
            ipAddress.setEnabled(false);
            portNumber.setEnabled(false);
            user.setEnabled(false);
            listSchema.setEnabled(false);
            password.setEnabled(false);
            listTable.setEnabled(false);
            btnConnection.setEnabled(false);
            location.setEnabled(false);
        }
    }

    /**
     * disconnect from the database
     */
    private void disconnect() {

        try {
            compositeConnection.setEnabled(false);

            listSchema.removeAll();
            listTable.removeAll();
            // tableResult.removeAll();

            /*
             * while (tableResult.getColumnCount()>0){
             * tableResult.getColumn(0).dispose();
             * }
             * tableResult.redraw();
             */

            db.closeConnection();
        } catch (final MySQLException e) {
            this.addMessage(e.getMessage(), 3);
        } finally {
            db.setConn(null);
            this.setConnected(false);
            this.addMessage("Connection closed", 1);
            btnConnection.setText("Connection");
            compositeConnection.setEnabled(true);
        }
    }

    /**
     * add a message in the list
     *
     */
    private void addMessage(final String message, final int level) {

        final TableItem msg = new TableItem(tableMessage, SWT.NONE, 0);
        msg.setText(message);

        if (level == 3) {
            msg.setForeground(red);
        } else {
            if (level == 2) {
                msg.setForeground(blue);
            } else {
                msg.setForeground(black);
            }
        }
    }
}
