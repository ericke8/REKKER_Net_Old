/*
 * AP(r) Computer Science GridWorld Case Study: Copyright(c) 2002-2006 College
 * Entrance Examination Board (http://www.collegeboard.com).
 *
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * @author Julie Zelenski
 * 
 * @author Cay Horstmann
 */

package framework.info.gridworld.gui;

import framework.info.gridworld.character.CharacterU;
import framework.info.gridworld.grid.*;
import framework.info.gridworld.world.World;
import framework.info.gridworld.grid.Grid;
import framework.info.gridworld.grid.Location;
import framework.info.gridworld.gui.DisplayMap;
import framework.info.gridworld.gui.GridPanel;
import framework.info.gridworld.gui.MenuMaker;
import framework.info.gridworld.gui.WorldFrame;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.*;


/**
 * The GUIController controls the behavior in a WorldFrame. <br />
 * This code is not tested on the AP CS A and AB exams. It contains GUI
 * implementation details that are not intended to be understood by AP CS
 * students.
 */

public class GUIController
{
    public static final int INDEFINITE = 0, FIXED_STEPS = 1, PROMPT_STEPS = 2;

    private static final int MIN_DELAY_MSECS = 10, MAX_DELAY_MSECS = 1000;

    private static final int INITIAL_DELAY = MIN_DELAY_MSECS + ( MAX_DELAY_MSECS - MIN_DELAY_MSECS ) / 2;

    private Timer timer;

    private JButton moveButton, attackerButton, targetButton, cancelButton;

    private JButton newGameButton;

    private JComponent controlPanel;

    private GridPanel display;

    private WorldFrame parentFrame;

    private int numStepsToRun, numStepsSoFar;

    private ResourceBundle resources;

    private DisplayMap displayMap;

    private CharacterU attacker;

    private CharacterU target;

    private CharacterU mover;

    private Location base;
    // private boolean running;

    private Set<Class> occupantClasses;


    /**
     * Creates a new controller tied to the specified display and gui frame.
     * 
     * @param parent
     *            the frame for the world window
     * @param disp
     *            the panel that displays the grid
     * @param displayMap
     *            the map for occupant displays
     * @param res
     *            the resource bundle for message display
     */
    public GUIController( WorldFrame parent, GridPanel disp, DisplayMap displayMap, ResourceBundle res )
    {
        resources = res;
        display = disp;
        parentFrame = parent;
        this.displayMap = displayMap;
        makeControls();

        occupantClasses = new TreeSet<Class>( new Comparator<Class>()
        {
            public int compare( Class a, Class b )
            {
                return a.getName().compareTo( b.getName() );
            }
        } );

        World world = parentFrame.getWorld();
        Grid gr = world.getGrid();
        for ( Location loc : gr.getOccupiedLocations() )
            addOccupant( gr.get( loc ) );
        for ( String name : world.getOccupantClasses() )
            try
            {
                occupantClasses.add( Class.forName( name ) );
            }
            catch ( Exception ex )
            {
                ex.printStackTrace();
            }

        timer = new Timer( INITIAL_DELAY, new ActionListener()
        {
            public void actionPerformed( ActionEvent evt )
            {
                attacker();
            }
        } );

        display.addMouseListener( new MouseAdapter()// TODO
        {
            public void mousePressed( MouseEvent evt )
            {
                Grid gr = parentFrame.getWorld().getGrid();
                Location loc = display.locationForPoint( evt.getPoint() );
                if ( loc != null && gr.isValid( loc ) )
                {
                    display.setCurrentLocation( loc );
                    locationClicked();// TODO
                }
            }
        } );
        // stop();
    }


    /**
     * Advances the world one attacker.
     */
    public void attacker()// TODO corresponds with attacker
    {
        parentFrame.getWorld().step();
        Location l = display.getCurrentLocation();
        if ( parentFrame.getWorld().getGrid().get( l ) != null )
        {
            targetButton.setEnabled( true );
            attacker = parentFrame.getWorld().getGrid().get( l );
            parentFrame.getWorld().setMessage( "Attacker Selected: Unit at " + l.toString() );

            // parentFrame.getWorld().setMessage( "Attacker Selected: Unit at "
            // );
        }

        // parentFrame.getWorld().attacker();
        // parentFrame.repaint();
        // if ( ++numStepsSoFar == numStepsToRun )
        // stop();
        // Grid gr = parentFrame.getWorld().getGrid();
        //
        // for ( Location loc : gr.getOccupiedLocations() )
        // addOccupant( gr.get( loc ) );
    }


    private void addOccupant( CharacterU occupant )
    {
        Class cl = occupant.getClass();
        do
        {
            if ( ( cl.getModifiers() & Modifier.ABSTRACT ) == 0 )
                occupantClasses.add( cl );
            cl = cl.getSuperclass();
        } while ( cl != Object.class );
    }


    /**
     * Starts a timer to repeatedly carry out steps at the speed currently
     * indicated by the speed slider up Depending on the run option, it will
     * either carry out steps for some fixed number or indefinitely until
     * stopped.
     */
    public void target()// TODO corresponds with target
    {
        Location l = display.getCurrentLocation();
        if ( parentFrame.getWorld().getGrid().get( l ) != null )
        {
            target = parentFrame.getWorld().getGrid().get( l );
            parentFrame.getWorld().setMessage( "Target Selected: Unit at " + l.toString() );
            executeAttack();
        }
        // display.setToolTipsEnabled( false ); // hide tool tips while running
        // parentFrame.setRunMenuItemsEnabled( false );
        // cancelButton.setEnabled( true );
        // attackerButton.setEnabled( false );
        // newGameButton.setEnabled( true );
        // targetButton.setEnabled( false );
        // numStepsSoFar = 0;
        // timer.start();
    }


    public void executeAttack()
    {
        if ( target != null && attacker != null )
        {
            if ( ( Math.abs( target.getLocation().getRow() - attacker.getLocation().getRow() ) <= attacker
                .attackRange() )

                && ( Math.abs( target.getLocation().getCol() - attacker.getLocation().getCol() ) <= attacker
                    .attackRange() ) )
            {
                if ( attacker.getUnitType().equals( new String( "Healer" ) ) )
                {
                    target.healthAdjust( attacker.getAtk() );
                    parentFrame.getWorld().setMessage(
                        "Healer healed the " + target.getUnitType() + " for " + attacker.getAtk() + " health." );
                }
                else if ( target.getDef() - attacker.getAtk() < 0 )
                {
                    target.healthAdjust( target.getDef() - attacker.getAtk() );
                    parentFrame.getWorld().setMessage( attacker.getUnitType() + " damaged the " + target.getUnitType()
                        + " for " + ( attacker.getAtk() - target.getDef() ) + " damage." );
                }
                else
                {
                    parentFrame.getWorld().setMessage(
                        attacker.getUnitType() + " damaged the " + target.getUnitType() + " for 0 damage." );
                }
                attacker = null;
                target = null;
                targetButton.setEnabled( false );
            }
            else
            {
                attacker = null;
                target = null;
                targetButton.setEnabled( false );
                parentFrame.getWorld().setMessage( "Out of Range." );
            }
        }
        else
        {
            attacker = null;
            target = null;
            targetButton.setEnabled( false );
            parentFrame.getWorld().setMessage( "Selection invalid." );
            throw new IllegalArgumentException( "Selection invalid." );
        }
    }


    /**
     * Stops any existing timer currently carrying out steps.
     */
    public void cancel()// TODO corresponds with end turn
    {
        attacker = null;
        target = null;
        targetButton.setEnabled( false );
        base = null;
        parentFrame.getWorld().setMessage( new String( "Actions cancelled." ) );

        // display.setToolTipsEnabled( true );
        // parentFrame.setRunMenuItemsEnabled( true );
        // timer.stop();
        // cancelButton.setEnabled( false );
        // newGameButton.setEnabled( true );
        // targetButton.setEnabled( false );
        // attackerButton.setEnabled( false );
    }


    public void move()
    {
        Location l = display.getCurrentLocation();
        if ( base != null )
        {
            if ( parentFrame.getWorld().getGrid().get( l ) == null )
            {
                if ( ( Math.abs( base.getRow() - l.getRow() ) <= 2 )

                    && Math.abs( base.getCol() - l.getCol() ) <= 2 )
                {
                    CharacterU c = parentFrame.getWorld().getGrid().get( base );
                    c.removeSelfFromGrid();
                    c.putSelfInGrid( parentFrame.getWorld().getGrid(), l );
                    parentFrame.repaint();
                    base = null;
                }
                else
                {
                    parentFrame.getWorld().setMessage( new String( "Selection outside of movement range." ) );
                    base = null;
                }
            }
            else
            {
                parentFrame.getWorld().setMessage( new String( "Destination already occupied." ) );
                base = null;
            }
        }
        else if ( parentFrame.getWorld().getGrid().get( l ) != null )
        {
            base = l;
        }
        // mover = parentFrame.getWorld().getGrid().get( l );
        //
        // if ( parentFrame.getWorld().getGrid().get( l ) != null )
        // {
        // // mover = parentFrame.getWorld().getGrid().get( l );
        // display.addMouseListener( new MouseAdapter()
        // {
        // public void mousePressed( MouseEvent evt )
        // {
        // Location l = display.getCurrentLocation();
        // Grid gr = parentFrame.getWorld().getGrid();
        // Location loc = display.locationForPoint( evt.getPoint() );
        // display.setCurrentLocation( loc );
        // dest = loc;
        // if ( loc != null && gr.isValid( loc ) )
        // {
        // display.setCurrentLocation( loc );
        // Location l2 = display.getCurrentLocation();
        // locationClicked();
        // if ( Math.abs( l2.getRow() - l.getRow() ) <= 2 )
        // {
        // if ( Math.abs( l2.getCol() - l.getCol() ) <= 2 )
        // {
        // if ( parentFrame.getWorld().getGrid().get( l2 ) == null )
        // {
        // CharacterU c = parentFrame.getWorld().getGrid().get( l );
        // //c.moveTo( l2 );// null pointer exception:
        // // Exception in thread
        // // "AWT-EventQueue-0"
        // // parentFrame.getWorld().setMessage( new
        // // String( l2 + "" + mover.getLocation() )
        // // );
        // }
        // }
        // }
        // }
        // }
        // } );
        // if ( gr == null )
        // throw new IllegalStateException( "This character is not in a grid."
        // );
        // if ( gr.get( location ) != this )
        // throw new IllegalStateException( "The grid contains a different
        // character at location " + location + "." );
        // if ( !gr.isValid( newLocation ) )
        // throw new IllegalArgumentException( "Location " + newLocation + " is
        // not valid." );
        //
        // if ( newLocation.equals( location ) )
        // return newLocation;
        // grid.remove( location );
        // if ( grid.get( newLocation ) != null )
        // {
        // throw new IllegalArgumentException( "Location " + newLocation + " is
        // not valid." );
        // // CharacterU other = grid.get( newLocation );
        // // if ( other != null )
        // // other.removeSelfFromGrid();
        // }
        // location = newLocation;
        // grid.put( location, this );
        // parentFrame.getWorld().setMessage( new String( dest + "" ));
        // }

    }


    public void newGame()
    {
        // TODO
    }


    // public boolean isRunning()
    // {
    // return running;
    // }

    /**
     * Builds the panel with the various controls (buttons and slider).
     */
    private void makeControls()
    {
        controlPanel = new JPanel();
        moveButton = new JButton( new String( "Move" ) );
        attackerButton = new JButton( new String( "Attacker" ) );
        targetButton = new JButton( new String( "Target" ) );
        cancelButton = new JButton( new String( "Cancel" ) );
        newGameButton = new JButton( new String( "New Game" ) );

        controlPanel.setLayout( new BoxLayout( controlPanel, BoxLayout.X_AXIS ) );
        controlPanel.setBorder( BorderFactory.createEtchedBorder() );

        Dimension spacer = new Dimension( 10, attackerButton.getPreferredSize().height + 10 );
        Dimension largeSpacer = new Dimension( 200, attackerButton.getPreferredSize().height + 10 );

        controlPanel.add( Box.createRigidArea( spacer ) );

        controlPanel.add( moveButton );
        controlPanel.add( Box.createRigidArea( spacer ) );
        controlPanel.add( attackerButton );
        controlPanel.add( Box.createRigidArea( spacer ) );
        controlPanel.add( targetButton );
        controlPanel.add( Box.createRigidArea( spacer ) );
        controlPanel.add( cancelButton );
        controlPanel.add( Box.createRigidArea( largeSpacer ) );
        controlPanel.add( newGameButton );
        moveButton.setEnabled( true );
        newGameButton.setEnabled( false );
        targetButton.setEnabled( false );
        attackerButton.setEnabled( true );
        cancelButton.setEnabled( true );

        // controlPanel.add(Box.createRigidArea(spacer));
        // controlPanel.add(new JLabel(resources.getString("slider.gui.slow")));
        // JSlider speedSlider = new JSlider(MIN_DELAY_MSECS, MAX_DELAY_MSECS,
        // INITIAL_DELAY);
        // speedSlider.setInverted(true);
        // speedSlider.setPreferredSize(new Dimension(100, speedSlider
        // .getPreferredSize().height));
        // speedSlider.setMaximumSize(speedSlider.getPreferredSize());
        //
        // // remove control PAGE_UP, PAGE_DOWN from slider--they should be used
        // // for zoom
        // InputMap map = speedSlider.getInputMap();
        // while (map != null)
        // {
        // map.remove(KeyStroke.getKeyStroke("control PAGE_UP"));
        // map.remove(KeyStroke.getKeyStroke("control PAGE_DOWN"));
        // map = map.getParent();
        // }
        //
        // controlPanel.add(speedSlider);
        // controlPanel.add(new JLabel(resources.getString("slider.gui.fast")));
        // controlPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        moveButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                move();// TODO unit selection and stuff
            }
        } );
        attackerButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                attacker();// TODO unit selection and stuff
            }
        } );
        targetButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                target();// TODO
            }
        } );
        cancelButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {

                cancel();// TODO
            }
        } );
        newGameButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                newGame();// TODO
            }
        } );
        // speedSlider.addChangeListener(new ChangeListener()
        // {
        // public void stateChanged(ChangeEvent evt)
        // {
        // timer.setDelay(((JSlider) evt.getSource()).getValue());
        // }
        // });
    }


    /**
     * Returns the panel containing the controls.
     * 
     * @return the control panel
     */
    public JComponent controlPanel()
    {
        return controlPanel;
    }


    /**
     * Callback on mousePressed when editing a grid.
     */
    private void locationClicked()
    {
        World world = parentFrame.getWorld();
        Location loc = display.getCurrentLocation();
        if ( loc != null && !world.locationClicked( loc ) )
            editLocation();
        parentFrame.repaint();
    }


    /**
     * Edits the contents of the current location, by displaying the constructor
     * or method menu.
     */
    public void editLocation()
    {
        World world = parentFrame.getWorld();

        Location loc = display.getCurrentLocation();
        if ( loc != null )
        {
            CharacterU occupant = world.getGrid().get( loc );
            if ( occupant == null )
            {
                // MenuMaker maker = new MenuMaker( parentFrame, resources,
                // displayMap );
                // JPopupMenu popup = maker.makeConstructorMenu(
                // occupantClasses, loc );
                Point p = display.pointForLocation( loc );
                // popup.show( display, p.x, p.y );
            }
            else
            {
                MenuMaker maker = new MenuMaker( parentFrame, resources, displayMap );
                JPopupMenu popup = maker.makeMethodMenu( occupant, loc );
                Point p = display.pointForLocation( loc );
                popup.show( display, p.x, p.y );
            }
        }
        parentFrame.repaint();
    }


    /**
     * Edits the contents of the current location, by displaying the constructor
     * or method menu.
     */
    public void deleteLocation()
    {
        World world = parentFrame.getWorld();
        Location loc = display.getCurrentLocation();
        if ( loc != null )
        {
            world.remove( loc );
            parentFrame.repaint();
        }
    }
}
