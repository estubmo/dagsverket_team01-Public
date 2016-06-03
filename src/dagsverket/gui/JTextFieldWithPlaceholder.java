package dagsverket.gui;

/**
 * Created by ArneChristian on 04.04.14.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class JTextFieldWithPlaceholder extends JPasswordField implements FocusListener {

    private String placeholder;
    private boolean utfylt;
    private char echoChar;


    public JTextFieldWithPlaceholder(int cols, String placeholder, char echoChar){
        super(cols);
        this.placeholder = placeholder;
        this.echoChar = echoChar;
        utfylt = false;

        addFocusListener(this);
        setText(placeholder);
        setFont(new Font("Lucida Grande", Font.ITALIC, 13));
        setEchoChar((char) 0);
        setForeground(Color.GRAY);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (!utfylt){
            setEchoChar(echoChar);
            setText("");
            setForeground(Color.BLACK);
            setFont(new Font("Lucida Grande", Font.PLAIN, 13));
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if(new String(getPassword()).isEmpty()){
            setEchoChar((char) 0);
            setText(placeholder);
            setForeground(Color.GRAY);
            setFont(new Font("Lucida Grande", Font.ITALIC, 13));
            utfylt = false;
        } else {
            utfylt = true;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getText(){
        return new String(getPassword());
    }



    public void clear(){
        setEchoChar((char) 0);
        setText(placeholder);
        setForeground(Color.GRAY);
        setFont(new Font("Lucida Grande", Font.ITALIC, 13));
        utfylt = false;
    }


}
