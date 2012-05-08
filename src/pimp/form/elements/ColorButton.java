package pimp.form.elements;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;

/** Provides a button for popping a color chooser.
The Color can be obtained by calling ColorButton.getBackground().
@author Joel Mason
Modified from original source code by Andrew Thompson
http://stackoverflow.com/questions/6412817/form-layout-to-call-jcolorchooser
 **/

public class ColorButton extends JButton implements ActionListener {

    private JColorChooser chooseColor;

    private String text;

    public ColorButton(String label) {
        super(label);
        text = label;
        chooseColor = new JColorChooser();
        addActionListener( this );
    }

    @Override
    public void setText(String text) {
        super.setText(this.text = text);
    }

    public void actionPerformed(ActionEvent ae) {
        Color col = JColorChooser.showDialog(this, "Choose Color", getBackground());
        if (col!=null) 
        {
            setBackground(col);
        }
    }

    @Override
    public void setBackground(Color bg) {
    	// Call super to change background.
        super.setBackground( bg );
    	
    	// Break down colour components.
    	int r = bg.getRed();
        int g = bg.getGreen();
        int b = bg.getBlue();
        
        // Set text.
        setText("Colour: (" + r + ", " + g + ", " + b + ")");
        
        // Set text colour based on background colour.
        int averageColour = (r+b+g)/3;
        if (averageColour < 120) {
        	setForeground(Color.white);
        }
        else setForeground(Color.black);
    }
}