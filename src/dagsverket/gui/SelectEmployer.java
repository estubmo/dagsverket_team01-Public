package dagsverket.gui;

import dagsverket.database.Database;
import dagsverket.gui.mainWindow.TaskInfoPanel;
import dagsverket.gui.mainWindow.TaskView;
import dagsverket.system.Employer;
import dagsverket.system.Task;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Created by ArneChristian on 10.04.14.
 */
public class SelectEmployer extends JDialog {

    private Task task;
    private Database database;
    private JTextField kundeTextField;

    private ArrayList<Employer> employers, searchedEmployers;

    private JTextFieldWithPlaceholder searchField;
    private JList<Employer> employerJList;
    private JButton select;

    private Employer selectedEmp;

    public SelectEmployer(Database database, JComponent location){
        super((Dialog) null, "", true);
        this.database = database;
        setUndecorated(true);
        setResizable(false);


        initGUI();
        pack();
        setLocationRelativeTo(location);
    }

    private void initGUI(){
        rootPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();


        searchField = new JTextFieldWithPlaceholder(12, "Søk", (char) 0);

        searchField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (employers.isEmpty()) {
                    } else if (searchField.getText().equals("")) {
                        searchedEmployers.clear();
                        for (int i = 0; i < employers.size(); i++) {
                            searchedEmployers.add(employers.get(i));
                        }
                    } else if (!searchField.getText().equals("")) {
                        setSearchedEmployersList(searchField.getText());
                    }
                    employerJList.updateUI();
                    int size = employerJList.getModel().getSize();
                    if (size > 0){
                        employerJList.setSelectionInterval(0,0);
                    } else {
                        employerJList.clearSelection();
                    }
                }

            }

        });

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.weighty = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        c.insets.set(6, 4, 4, 4);

        rootPane.add(searchField, c);


        employers = database.getEmployers();

        searchedEmployers = new ArrayList<>();


        for (int i = 0; i < employers.size(); i++) {
            searchedEmployers.add(employers.get(i));
        }

        employerJList = new JList<>();
        employerJList.setModel(new ListModel<Employer>() {
            @Override
            public int getSize() {
                return searchedEmployers.size();
            }

            @Override
            public Employer getElementAt(int index) {
                return searchedEmployers.get(index);
            }

            @Override
            public void addListDataListener(ListDataListener l) {

            }

            @Override
            public void removeListDataListener(ListDataListener l) {

            }
        });
        employerJList.setBackground(Color.WHITE);


        employerJList.setPreferredSize(new Dimension(250, 200));
        employerJList.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        employerJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = employerJList.getSelectedIndex();
                if (selectedRow >= 0 && selectedRow < searchedEmployers.size()){
                    selectedEmp = searchedEmployers.get(selectedRow);
                    select.setEnabled(true);
                } else {
                    selectedEmp = null;
                    select.setEnabled(false);
                }
            }
        });

        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.weighty = 1;
        c.insets.set(0, 4, 4, 4);


        rootPane.add(employerJList, c);

        //Glue
        c.weightx = 1;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        rootPane.add(new JPanel(), c);



        select = new JButton("Velg");
        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setEmployerToTask();
            }
        });
        select.setEnabled(false);

        c.gridx = 1;
        c.weightx = 0;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.NONE;
        c.insets.set(0, 6, 6, 6);

        rootPane.add((select), c);



        JButton ok = new JButton("Lukk");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });

        c.gridx = 2;
        rootPane.add(ok, c);







    }

    public void setSearchedEmployersList(String search) {
        search = search.toUpperCase();
        if (employers.size() != 0) {
            searchedEmployers.clear();
            for (int i = 0; i < employers.size(); i++) {
                if (employers.get(i).getEmpName().toUpperCase().contains(search)) {
                    searchedEmployers.add(employers.get(i));
                }
            }
        }





    }

    private void setEmployerToTask(){

        selectedEmp = searchedEmployers.get(employerJList.getSelectedIndex());
        dispose();
    }

    private void close(){
        selectedEmp = null;
        dispose();
    }






    public Employer getEmployer(){
        setVisible(true);
        return selectedEmp;
    }






}
