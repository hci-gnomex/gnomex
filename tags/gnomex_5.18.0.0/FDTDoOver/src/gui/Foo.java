package gui;

import java.awt.*; // it is necessary to use the Dimension and BorderLayout classes
import javax.swing.*;

public class Foo extends JFrame {

    public Foo() {

        setTitle( "Splits" );
        setDefaultCloseOperation( EXIT_ON_CLOSE );
        setSize( 400, 400 );

        JSplitPane split = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
        split.setDividerLocation( 200 );
        add( split );

        JPanel panel1 = new JPanel();
        panel1.setLayout( new BorderLayout() );
        panel1.add( new JLabel( "top panel" ), BorderLayout.NORTH );

        JPanel myDrawPanel = new JPanel();
        myDrawPanel.setPreferredSize( new Dimension( 100, 100 ) );
        myDrawPanel.add( new JLabel( "draw panel here!" ) );
        panel1.add( new JScrollPane( myDrawPanel ), BorderLayout.CENTER );

        split.setTopComponent( panel1 );


        JPanel panel2 = new JPanel();
        panel2.add( new JLabel( "bottom panel" ) );
        split.setBottomComponent( panel2 );


        setVisible( true );

    }

    public static void main( String[] args ) {
        new Foo();
    }

}