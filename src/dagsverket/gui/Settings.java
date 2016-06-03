package dagsverket.gui;

import dagsverket.database.Database;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ArneChristian on 04.04.14.
 */
public class Settings extends JDialog {


    private final String[] SETTINGS = {"Database", "Bruker"};
    private Database database;
    private JList<String> list;
    private JPanel centerPanel;
    private CardLayout cardLayout;
    private ButtonPanel buttonPanel;
    private DatabaseSettings databaseSettings;

    public Settings(Database database, Frame owner){
        super(owner, true);
        setTitle("Innstillinger");

        this.database = database;

        initUI();
        pack();
        setLocationRelativeTo(owner);

    }

    public static void main(String[] args) {
        new Settings(Database.DATABASE, null).setVisible(true);
    }

    public void initUI(){
        rootPane.setLayout(new BorderLayout(12,12));
        rootPane.setPreferredSize(new Dimension(650, 400));

        JPanel listPanel = new JPanel(new GridBagLayout());
        listPanel.setPreferredSize(new Dimension(150, 400));
        listPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.insets.set(12,12,0,12);
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.CENTER;

        JLabel label = new JLabel("Instillinger", JLabel.CENTER);
        //label.setVerticalAlignment();
        listPanel.add(label, c);


        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets.set(12,12,12,12);
        c.weightx = 1.0;
        c.weighty = 1.0;


        list = new JList<String>(SETTINGS);
        //list.setPreferredSize(new Dimension(100, SETTINGS.length * list.setV));
        list.setVisibleRowCount(0);
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                cardLayout.show(centerPanel,SETTINGS[list.getSelectedIndex()]);
            }
        });
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(list);
        scrollPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        listPanel.add(scrollPane, c);
        rootPane.add(listPanel, BorderLayout.WEST);

        databaseSettings = new DatabaseSettings(this);

        cardLayout = new CardLayout();
        centerPanel = new JPanel(cardLayout);
        centerPanel.add(databaseSettings, SETTINGS[0]);
        centerPanel.add(new ChangePassword(database), SETTINGS[1]);

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());


        buttonPanel = new ButtonPanel();
        middlePanel.add(centerPanel, BorderLayout.CENTER);
        middlePanel.add(buttonPanel, BorderLayout.SOUTH);




        rootPane.add(middlePanel, BorderLayout.CENTER);

        list.setSelectionInterval(0,0);

    }

    public void setEnableButtons(boolean b){
        buttonPanel.apply.setEnabled(b);
        buttonPanel.ok.setEnabled(b);
    }

    class ButtonPanel extends JPanel{
        JButton ok;
        JButton apply;

        public ButtonPanel(){
            //setBorder(BorderFactory.createEtchedBorder());
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.gridheight = 1;
            c.gridwidth = 4;
            c.weightx = 1;
            c.weighty = 0;
            c.gridx = 0;
            c.gridy = 0;

            c.anchor = GridBagConstraints.NORTH;
            c.fill = GridBagConstraints.HORIZONTAL;

            add(new JSeparator(JSeparator.HORIZONTAL), c);

            c.weightx = 0;
            c.gridx = 3;
            c.gridy = 1;
            c.gridwidth = 1;
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.EAST;
            c.insets.set(5, 0, 5, 0);

            apply = new JButton("Bruk");
            apply.setEnabled(false);
            apply.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    apply();
                }
            });
            add(apply, c);


            c.gridx = 1;
            ok = new JButton("Ok");
            ok.setEnabled(false);
            ok.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    apply();
                    closeSettings();
                }
            });
            add(ok, c);

            c.gridx = 2;
            JButton cancel = new JButton("Avbryt");
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    closeSettings();
                }
            });
            add(cancel, c);

            c.gridx = 0;
            c.weightx = 1;
            add(new JPanel(), c);


        }

        public void apply(){

            databaseSettings.apply();
            apply.setEnabled(false);
            ok.setEnabled(false);
        }

        public void closeSettings(){

            dispose();
        }
    }






}
