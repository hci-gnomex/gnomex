package gui;

import java.awt.Component;
import java.awt.Dimension;

import com.alee.extended.filechooser.WebFileDrop;
import com.alee.extended.window.TestFrame;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.text.WebTextArea;

public class Test
{
    public static void main ( String[] args )
    {
        WebLookAndFeel.install ();

        WebSplitPane splitPane = new WebSplitPane ( WebSplitPane.VERTICAL_SPLIT, createFileDropArea (), createLog () );
        splitPane.setOneTouchExpandable ( true );
        splitPane.setPreferredSize ( new Dimension ( 400, 200 ) );
        splitPane.setDividerLocation ( 100 );
        splitPane.setContinuousLayout ( false );
        splitPane.setResizeWeight ( 0.5 );

        new TestFrame ( splitPane, 5 ).setVisible(true);;
    }

    private static Component createFileDropArea ()
    {
        return new WebScrollPane ( new WebFileDrop () );
    }

    private static Component createLog ()
    {
        return new WebScrollPane ( new WebTextArea ( "Checking port...\nChecking fdt.jar..." ) );
    }
}