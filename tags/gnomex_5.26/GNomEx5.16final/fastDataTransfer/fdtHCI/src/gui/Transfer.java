/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import javax.swing.JFileChooser;

/**
 * Dummy class used to decide which fileCHooser to start depending on whether its an upload or download.
 *
 * @author Shobhit
 */
public abstract class Transfer extends javax.swing.JFrame{

    protected JFileChooser localFileChooser;

    protected static FdtGUI guiInstance = null;

    public Transfer()
    {
        localFileChooser = new JFileChooser();
        guiInstance = FdtGUI.getFdtGUIInstance();
    }

    public abstract void showFileChooser();

}
