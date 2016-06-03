package dagsverket.gui.mainWindow;

import dagsverket.database.Database;
import dagsverket.system.Task;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by ArneChristian on 10.04.14.
 */
public class TaskDescription extends JPanel {

    private Task task;
    private JTextArea textArea;
    private ButtonPanel buttonPanel;

    private Database database;

    public TaskDescription(Database database){

        this.database = database;
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEnabled(false);
        textArea.setLineWrap(true);
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                buttonPanel.apply.setEnabled(true);
            }
        });


        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        buttonPanel = new ButtonPanel();

        add(buttonPanel, BorderLayout.SOUTH);





    }

    public void setTask(Task task) {
        this.task = task;
        if (task != null){
            textArea.setEnabled(true);
            textArea.setText(task.getDescription());
            buttonPanel.apply.setEnabled(true);
        } else {
            textArea.setText("");
            textArea.setEnabled(false);
        }
        buttonPanel.apply.setEnabled(false);

    }

    public void updateTask(){
        task.setDescription(textArea.getText());
        database.updateTask(task);
    }



    class ButtonPanel extends JPanel{
        JButton ok;
        JButton apply;

        public ButtonPanel(){
            //setBorder(BorderFactory.createEtchedBorder());
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            c.gridheight = 1;


            c.weightx = 0;
            c.gridx = 1;
            c.gridy = 0;
            c.gridwidth = 1;
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.EAST;
            c.insets.set(5, 5, 5, 5);

            apply = new JButton("Lagre");
            apply.setEnabled(false);
            apply.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateTask();
                    apply.setEnabled(false);
                }
            });
            add(apply, c);


            c.gridx = 0;
            c.weightx = 1;
            add(new JPanel(), c);


        }

    }
}
