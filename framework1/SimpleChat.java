import javax.swing.JFrame;
import java.awt.BorderLayout;


/**
 * The bootstrap class for this program.  Creates a root pane and adds
 * the chat GUI so it can be used.
 *
 * YOU DO NOT NEED TO EDIT THIS CLASS.
 *
 * $Id: SimpleChat.java
 */
public class SimpleChat
{
    /**
     * Starting method for this program.
     * 
     * @param args Command-line arguments (ignored)
     */
    public static void main( String[] args )
    {
        JFrame frame = new JFrame( "SimpleChat" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        frame.getContentPane().add( new ChatGui(), BorderLayout.CENTER );

        // pack shrinks the window so everything just fits
        frame.pack();
        frame.setVisible( true );

    }

}
