/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Just used by every class to display a popup message.
 *
 * @author Shobhit
 */
public class ShowMessageDialog {

    public static void showErrorDialog(String message, String title)
    {
        if(title.equals("") || title == null)
            title = "Error";

       JOptionPane.showMessageDialog(new JFrame(), message, title ,JOptionPane.ERROR_MESSAGE);
    }

    public static void showInformationDialog(String message, String title)
    {
        if(title.equals("") || title == null)
            title = "Information";

        JOptionPane.showMessageDialog(new JFrame(), message, title, JOptionPane.INFORMATION_MESSAGE);
    }

}
