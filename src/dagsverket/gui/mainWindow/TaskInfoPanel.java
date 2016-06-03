package dagsverket.gui.mainWindow;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.toedter.calendar.JDateChooser;
import dagsverket.database.Database;
import dagsverket.gui.SelectEmployer;
import dagsverket.system.Employer;
import dagsverket.system.Supervisor;
import dagsverket.system.Task;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by ArneChristian on 28.03.14.
 */
public class TaskInfoPanel extends JPanel {

    private JPanel rootPane;
    private JTextField textFieldNavn;
    private JComboBox comboBoxArbLeder;
    private JCheckBox betaltCheckBox;
    private JTextField textFieldAdresse;
    private JFormattedTextField formattedTextField1;
    private JTextField kundeTextField;
    private JDateChooser date;
    private JTextField textFieldPostnummer;
    private JTextField textFieldPoststed;
    private JTextField textFieldBefaring;
    private JCheckBox uteCheck;
    private JCheckBox utførtCheck;
    private JTextField regAvTextField;
    private JButton selectEmpButton;


    private Task task;
    private ArrayList<Supervisor> supervisors;
    private Database database;
    private TaskView taskView;


    public TaskInfoPanel(Database database, TaskView taskView) {

        super();
        this.database = database;
        this.taskView = taskView;
        supervisors = database.getSupervisors();
        supervisors.add(0, null);


        $$$setupUI$$$();

        add(rootPane);


        setInheritsPopupMenu(true);


        createListeners();
        setTask(null);
        selectEmpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectEmployer();
            }
        });
    }

    private void selectEmployer() {
        Employer emp = new SelectEmployer(database, selectEmpButton).getEmployer();

        if (emp == null) return;

            task.setEmployer(emp);
            kundeTextField.setText(emp.getEmpName());
            updateTask();
    }

    private void createListeners() {

        FocusListener focusListener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                updateTask();
            }
        };

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTask();
            }
        };

        KeyListener keyListener = new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Robot robot = null;
                    try {
                        robot = new Robot();
                    } catch (AWTException e1) {
                        e1.printStackTrace();
                    }
                    robot.keyPress(KeyEvent.VK_TAB);
                }
            }
        };


        textFieldNavn.addFocusListener(focusListener);
        comboBoxArbLeder.addFocusListener(focusListener);
        date.addFocusListener(focusListener);
        kundeTextField.addFocusListener(focusListener);
        textFieldPostnummer.addFocusListener(focusListener);
        textFieldAdresse.addFocusListener(focusListener);
        textFieldBefaring.addFocusListener(focusListener);
        textFieldPoststed.addFocusListener(focusListener);
        formattedTextField1.addFocusListener(focusListener);
        uteCheck.addActionListener(actionListener);
        utførtCheck.addActionListener(actionListener);
        betaltCheckBox.addActionListener(actionListener);
       /* date.addPropertyChangeListener(
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent e) {
                        if ("date".equals(e.getPropertyName())) {
                            updateTask();
                        }
                    }
                }
        );  // */


        textFieldNavn.addKeyListener(keyListener);
        comboBoxArbLeder.addKeyListener(keyListener);
        date.addKeyListener(keyListener);
        kundeTextField.addKeyListener(keyListener);
        textFieldPostnummer.addKeyListener(keyListener);
        textFieldAdresse.addKeyListener(keyListener);
        textFieldBefaring.addKeyListener(keyListener);
        textFieldPoststed.addKeyListener(keyListener);
        formattedTextField1.addKeyListener(keyListener);
        uteCheck.addKeyListener(keyListener);
        utførtCheck.addKeyListener(keyListener);
        betaltCheckBox.addKeyListener(keyListener);


    }

    public void update() {
        supervisors = database.getSupervisors();
        supervisors.add(0, null);

    }

    private void updateTask() {


        if (task == null) return;

        int index = comboBoxArbLeder.getSelectedIndex();
        if (index >= 0 && index < supervisors.size())
            task.setSupervisor(supervisors.get(index));
        task.setTaskName(textFieldNavn.getText());
        task.setAddress(textFieldAdresse.getText());
        task.setZipcode(textFieldPostnummer.getText());
        task.setBefaring(textFieldBefaring.getText());
        task.setPost(textFieldPoststed.getText());
        task.setCompleted(utførtCheck.isSelected());
        task.setIsOutside(uteCheck.isSelected());


        Number num = (Number) formattedTextField1.getValue();
        if (num != null) {
            task.setCost(num.doubleValue());
        } else {
            task.setCost(0);
        }
        task.setPaid(betaltCheckBox.isSelected());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyMMdd");
        task.setStartDate(sdf.format(date.getDate()));

        database.updateTask(task);
        taskView.updateUI();

    }


    public void setTask(Task task) {
        updateTask();

        this.task = task;
        if (task != null) {
            textFieldNavn.setText(task.getTaskName());


            int i = 0;
            if (task.getSupervisor() != null) {
                int arbleder = task.getSupervisor().getPersnr();


                for (i = 1; i < supervisors.size(); i++) {
                    if (arbleder == supervisors.get(i).getPersnr()) {
                        break;
                    }
                }
            }

            if (i < supervisors.size())
                comboBoxArbLeder.setSelectedIndex(i);
            else
                comboBoxArbLeder.setSelectedIndex(0);


            date.setDate(task.getStartDate().getTime());
            textFieldPostnummer.setText(task.getZipcode());
            textFieldAdresse.setText(task.getAddress());
            textFieldBefaring.setText(task.getBefaring());
            textFieldPoststed.setText(task.getPost());
            formattedTextField1.setText("" + task.getCost());
            uteCheck.setSelected(task.isIsOutside());
            utførtCheck.setSelected(task.getCompleted());
            betaltCheckBox.setSelected(task.isPaid());
            regAvTextField.setText(task.getUsername() + ", " + task.getRegDateString());
            kundeTextField.setText(task.getEmployer().getEmpName());


            textFieldNavn.setEnabled(true);
            comboBoxArbLeder.setEnabled(true);
            date.setEnabled(true);
            kundeTextField.setEnabled(true);
            textFieldPostnummer.setEnabled(true);
            textFieldAdresse.setEnabled(true);
            textFieldBefaring.setEnabled(true);
            textFieldPoststed.setEnabled(true);
            formattedTextField1.setEnabled(true);
            uteCheck.setEnabled(true);
            utførtCheck.setEnabled(true);
            betaltCheckBox.setEnabled(true);
            regAvTextField.setEnabled(true);
            selectEmpButton.setEnabled(true);


        } else {
            textFieldNavn.setText("");
            comboBoxArbLeder.setSelectedIndex(0);
            date.setDate(null);
            kundeTextField.setText("");
            textFieldPostnummer.setText("");
            textFieldAdresse.setText("");
            textFieldBefaring.setText("");
            textFieldPoststed.setText("");
            formattedTextField1.setText("");
            uteCheck.setSelected(false);
            utførtCheck.setSelected(false);
            betaltCheckBox.setSelected(false);
            regAvTextField.setText("");

            textFieldNavn.setEnabled(false);
            comboBoxArbLeder.setEnabled(false);
            date.setEnabled(false);
            kundeTextField.setEnabled(false);
            textFieldPostnummer.setEnabled(false);
            textFieldAdresse.setEnabled(false);
            textFieldBefaring.setEnabled(false);
            textFieldPoststed.setEnabled(false);
            formattedTextField1.setEnabled(false);
            uteCheck.setEnabled(false);
            utførtCheck.setEnabled(false);
            betaltCheckBox.setEnabled(false);
            regAvTextField.setEnabled(false);
            selectEmpButton.setEnabled(false);
        }
    }


    private void createUIComponents() {
        formattedTextField1 = new JFormattedTextField(NumberFormat.getNumberInstance());
        comboBoxArbLeder = new JComboBox();
        comboBoxArbLeder.setModel(new DefaultComboBoxModel() {




            @Override
            public int getSize() {
                return supervisors.size();
            }

            @Override
            public Object getElementAt(int index) {
                return supervisors.get(index);
            }

            @Override
            public void addListDataListener(ListDataListener l) {

            }

            @Override
            public void removeListDataListener(ListDataListener l) {

            }
        });





        selectEmpButton = new JButton(new ImageIcon(getClass().getResource("/resource/select_emp.png")));


    }


    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        rootPane = new JPanel();
        rootPane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        rootPane.setInheritsPopupMenu(true);
        rootPane.setMinimumSize(new Dimension(300, 0));
        rootPane.setOpaque(true);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(15, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setAlignmentY(0.0f);
        panel1.setInheritsPopupMenu(true);
        rootPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, -1));
        panel2.setAlignmentY(0.0f);
        panel2.setInheritsPopupMenu(true);
        panel1.add(panel2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(320, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(4);
        label1.setHorizontalTextPosition(4);
        label1.setMaximumSize(new Dimension(100, 16));
        label1.setMinimumSize(new Dimension(120, 16));
        label1.setPreferredSize(new Dimension(120, 16));
        label1.setText("Dato:  ");
        label1.setVerticalTextPosition(1);
        panel2.add(label1, BorderLayout.WEST);
        date = new JDateChooser();
        date.setAlignmentX(0.5f);
        date.setAlignmentY(0.5f);
        date.setMinimumSize(new Dimension(42, 18));
        date.setPreferredSize(new Dimension(123, 20));
        panel2.add(date, BorderLayout.CENTER);
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        panel3.setAlignmentY(0.0f);
        panel3.setInheritsPopupMenu(true);
        panel1.add(panel3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(320, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setHorizontalAlignment(4);
        label2.setHorizontalTextPosition(4);
        label2.setMaximumSize(new Dimension(100, -1));
        label2.setMinimumSize(new Dimension(120, 16));
        label2.setPreferredSize(new Dimension(120, 16));
        label2.setText("Adresse:  ");
        label2.setVerticalAlignment(0);
        label2.setVerticalTextPosition(0);
        panel3.add(label2, BorderLayout.WEST);
        textFieldAdresse = new JTextField();
        textFieldAdresse.setAlignmentX(0.5f);
        textFieldAdresse.setAlignmentY(0.5f);
        textFieldAdresse.setMinimumSize(new Dimension(14, 18));
        textFieldAdresse.setPreferredSize(new Dimension(14, 20));
        panel3.add(textFieldAdresse, BorderLayout.CENTER);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new BorderLayout(0, 0));
        panel4.setAlignmentY(0.0f);
        panel4.setInheritsPopupMenu(true);
        panel1.add(panel4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(320, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setHorizontalAlignment(4);
        label3.setHorizontalTextPosition(4);
        label3.setMaximumSize(new Dimension(100, 16));
        label3.setMinimumSize(new Dimension(120, 16));
        label3.setPreferredSize(new Dimension(120, 16));
        label3.setText("Kunde:  ");
        label3.setVerticalAlignment(0);
        label3.setVerticalTextPosition(0);
        panel4.add(label3, BorderLayout.WEST);
        kundeTextField = new JTextField();
        kundeTextField.setAlignmentX(0.5f);
        kundeTextField.setAlignmentY(0.5f);
        kundeTextField.setEditable(false);
        kundeTextField.setMinimumSize(new Dimension(14, 18));
        kundeTextField.setPreferredSize(new Dimension(53, 20));
        kundeTextField.setText("Kunde");
        panel4.add(kundeTextField, BorderLayout.CENTER);
        selectEmpButton.setBorderPainted(false);
        selectEmpButton.setEnabled(false);
        selectEmpButton.setFocusPainted(true);
        selectEmpButton.setMaximumSize(new Dimension(75, 20));
        selectEmpButton.setMinimumSize(new Dimension(75, 20));
        selectEmpButton.setOpaque(true);
        selectEmpButton.setPreferredSize(new Dimension(28, 20));
        selectEmpButton.setText("");
        panel4.add(selectEmpButton, BorderLayout.EAST);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new BorderLayout(0, 0));
        panel5.setAlignmentY(0.0f);
        panel5.setInheritsPopupMenu(true);
        panel1.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(320, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setHorizontalAlignment(4);
        label4.setHorizontalTextPosition(4);
        label4.setMaximumSize(new Dimension(100, 16));
        label4.setMinimumSize(new Dimension(120, 16));
        label4.setPreferredSize(new Dimension(120, 16));
        label4.setText("Navn:  ");
        label4.setVerticalTextPosition(1);
        panel5.add(label4, BorderLayout.WEST);
        textFieldNavn = new JTextField();
        textFieldNavn.setAlignmentX(0.5f);
        textFieldNavn.setAlignmentY(0.5f);
        textFieldNavn.setMinimumSize(new Dimension(14, 18));
        textFieldNavn.setPreferredSize(new Dimension(14, 20));
        panel5.add(textFieldNavn, BorderLayout.CENTER);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new BorderLayout(0, 0));
        panel6.setAlignmentY(0.0f);
        panel6.setInheritsPopupMenu(true);
        panel1.add(panel6, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(320, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setHorizontalAlignment(4);
        label5.setHorizontalTextPosition(4);
        label5.setMaximumSize(new Dimension(100, 16));
        label5.setMinimumSize(new Dimension(120, 16));
        label5.setPreferredSize(new Dimension(120, 16));
        label5.setText("Befaring:  ");
        label5.setVerticalTextPosition(1);
        panel6.add(label5, BorderLayout.WEST);
        textFieldBefaring = new JTextField();
        textFieldBefaring.setAlignmentX(0.5f);
        textFieldBefaring.setAlignmentY(0.5f);
        textFieldBefaring.setMinimumSize(new Dimension(14, 18));
        textFieldBefaring.setPreferredSize(new Dimension(14, 20));
        panel6.add(textFieldBefaring, BorderLayout.CENTER);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new BorderLayout(0, 0));
        panel7.setAlignmentY(0.0f);
        panel7.setInheritsPopupMenu(true);
        panel1.add(panel7, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(320, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setHorizontalAlignment(4);
        label6.setHorizontalTextPosition(4);
        label6.setMaximumSize(new Dimension(100, 16));
        label6.setMinimumSize(new Dimension(120, 16));
        label6.setPreferredSize(new Dimension(120, 16));
        label6.setText("");
        label6.setVerticalTextPosition(1);
        panel7.add(label6, BorderLayout.WEST);
        uteCheck = new JCheckBox();
        uteCheck.setAlignmentX(0.5f);
        uteCheck.setAlignmentY(0.5f);
        uteCheck.setMinimumSize(new Dimension(89, 18));
        uteCheck.setPreferredSize(new Dimension(89, 20));
        uteCheck.setText("Utendørs");
        panel7.add(uteCheck, BorderLayout.CENTER);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new BorderLayout(0, 0));
        panel8.setAlignmentY(0.0f);
        panel8.setInheritsPopupMenu(true);
        panel1.add(panel8, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(320, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setHorizontalAlignment(4);
        label7.setHorizontalTextPosition(4);
        label7.setMaximumSize(new Dimension(100, -1));
        label7.setMinimumSize(new Dimension(120, 16));
        label7.setPreferredSize(new Dimension(120, 16));
        label7.setText("Registrert av:  ");
        label7.setVerticalAlignment(1);
        label7.setVerticalTextPosition(1);
        panel8.add(label7, BorderLayout.WEST);
        regAvTextField = new JTextField();
        regAvTextField.setAlignmentX(0.5f);
        regAvTextField.setAlignmentY(0.5f);
        regAvTextField.setEditable(false);
        regAvTextField.setMinimumSize(new Dimension(14, 18));
        regAvTextField.setPreferredSize(new Dimension(53, 20));
        regAvTextField.setText("Kunde");
        panel8.add(regAvTextField, BorderLayout.CENTER);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new BorderLayout(0, 0));
        panel9.setAlignmentY(0.0f);
        panel9.setInheritsPopupMenu(true);
        panel1.add(panel9, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(320, -1), null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setHorizontalAlignment(4);
        label8.setHorizontalTextPosition(4);
        label8.setMaximumSize(new Dimension(100, 16));
        label8.setMinimumSize(new Dimension(120, 16));
        label8.setPreferredSize(new Dimension(120, 16));
        label8.setText("Poststed:  ");
        label8.setVerticalTextPosition(1);
        panel9.add(label8, BorderLayout.WEST);
        textFieldPoststed = new JTextField();
        textFieldPoststed.setAlignmentX(0.5f);
        textFieldPoststed.setAlignmentY(0.5f);
        textFieldPoststed.setMinimumSize(new Dimension(14, 18));
        textFieldPoststed.setPreferredSize(new Dimension(14, 20));
        panel9.add(textFieldPoststed, BorderLayout.CENTER);
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new BorderLayout(0, 0));
        panel10.setAlignmentY(0.0f);
        panel10.setInheritsPopupMenu(true);
        panel1.add(panel10, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(320, -1), null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setHorizontalAlignment(4);
        label9.setHorizontalTextPosition(4);
        label9.setMaximumSize(new Dimension(100, 16));
        label9.setMinimumSize(new Dimension(120, 16));
        label9.setPreferredSize(new Dimension(120, 16));
        label9.setText("");
        label9.setVerticalAlignment(1);
        label9.setVerticalTextPosition(1);
        panel10.add(label9, BorderLayout.WEST);
        betaltCheckBox = new JCheckBox();
        betaltCheckBox.setAlignmentX(0.5f);
        betaltCheckBox.setAlignmentY(0.5f);
        betaltCheckBox.setMinimumSize(new Dimension(67, 18));
        betaltCheckBox.setPreferredSize(new Dimension(67, 20));
        betaltCheckBox.setText("Betalt");
        panel10.add(betaltCheckBox, BorderLayout.CENTER);
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new BorderLayout(0, 0));
        panel11.setAlignmentY(0.0f);
        panel11.setInheritsPopupMenu(true);
        panel1.add(panel11, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(320, -1), null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setHorizontalAlignment(4);
        label10.setHorizontalTextPosition(4);
        label10.setMaximumSize(new Dimension(100, 16));
        label10.setMinimumSize(new Dimension(120, 16));
        label10.setPreferredSize(new Dimension(120, 16));
        label10.setText("Postnummer:  ");
        label10.setVerticalTextPosition(1);
        panel11.add(label10, BorderLayout.WEST);
        textFieldPostnummer = new JTextField();
        textFieldPostnummer.setAlignmentX(0.5f);
        textFieldPostnummer.setAlignmentY(0.5f);
        textFieldPostnummer.setMinimumSize(new Dimension(14, 18));
        textFieldPostnummer.setPreferredSize(new Dimension(14, 20));
        panel11.add(textFieldPostnummer, BorderLayout.CENTER);
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new BorderLayout(0, 0));
        panel12.setAlignmentY(0.0f);
        panel12.setInheritsPopupMenu(true);
        panel1.add(panel12, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(320, -1), null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setHorizontalAlignment(4);
        label11.setHorizontalTextPosition(4);
        label11.setMaximumSize(new Dimension(100, 16));
        label11.setMinimumSize(new Dimension(120, 16));
        label11.setPreferredSize(new Dimension(120, 16));
        label11.setText("Arbeidsleder:  ");
        label11.setVerticalTextPosition(1);
        panel12.add(label11, BorderLayout.WEST);
        comboBoxArbLeder.setAlignmentX(0.5f);
        comboBoxArbLeder.setAlignmentY(0.5f);
        comboBoxArbLeder.setMinimumSize(new Dimension(52, 18));
        comboBoxArbLeder.setPreferredSize(new Dimension(52, 22));
        panel12.add(comboBoxArbLeder, BorderLayout.CENTER);
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new BorderLayout(0, 0));
        panel13.setAlignmentY(0.0f);
        panel13.setInheritsPopupMenu(true);
        panel1.add(panel13, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(320, -1), null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setHorizontalAlignment(4);
        label12.setHorizontalTextPosition(4);
        label12.setMaximumSize(new Dimension(100, 16));
        label12.setMinimumSize(new Dimension(120, 16));
        label12.setPreferredSize(new Dimension(120, 16));
        label12.setText("Pris:  ");
        label12.setVerticalTextPosition(1);
        panel13.add(label12, BorderLayout.WEST);
        formattedTextField1.setAlignmentX(0.5f);
        formattedTextField1.setAlignmentY(0.5f);
        formattedTextField1.setMinimumSize(new Dimension(14, 18));
        formattedTextField1.setPreferredSize(new Dimension(14, 20));
        panel13.add(formattedTextField1, BorderLayout.CENTER);
        final JLabel label13 = new JLabel();
        label13.setText(" NOK ");
        panel13.add(label13, BorderLayout.EAST);
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new BorderLayout(0, 0));
        panel14.setAlignmentY(0.0f);
        panel14.setInheritsPopupMenu(true);
        panel1.add(panel14, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(320, -1), null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setHorizontalAlignment(4);
        label14.setHorizontalTextPosition(4);
        label14.setMaximumSize(new Dimension(100, 16));
        label14.setMinimumSize(new Dimension(120, 16));
        label14.setPreferredSize(new Dimension(120, 16));
        label14.setText("");
        label14.setVerticalTextPosition(1);
        panel14.add(label14, BorderLayout.WEST);
        utførtCheck = new JCheckBox();
        utførtCheck.setAlignmentX(0.5f);
        utførtCheck.setAlignmentY(0.5f);
        utførtCheck.setMinimumSize(new Dimension(69, 18));
        utførtCheck.setPreferredSize(new Dimension(69, 20));
        utførtCheck.setText("Utført");
        panel14.add(utførtCheck, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPane;
    }
}
