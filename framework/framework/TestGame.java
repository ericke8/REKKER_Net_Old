package framework;


/*
 * AP(r) Computer Science GridWorld Case Study: Copyright(c) 2005-2006 Cay S.
 * Horstmann (http://horstmann.com)
 *
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * @author Cay Horstmann
 */

import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import info.gridworld.world.World;

import info.gridworld.character.*;
import info.gridworld.gui.*;

import java.awt.Color;
import java.util.*;

import javax.swing.*;

import framework.info.gridworld.character.CharacterU;


public class TestGame extends World implements ChatDisplay
{
    // private Tile firstTile;
    //
    // private Tile secondTile;
    //
    // private boolean first;

    /** Default port to connect to on remote hosts */
    public static final int DEFAULT_PORT = 1337;

    private int port = DEFAULT_PORT;

    /** Object that performs all networking and IO */
    protected ChatConnectionHandler networker;

    /** Data model for connections list */
    protected DefaultListModel connModel;

    /** List of active connections */
    protected JList connections;


    public TestGame()
    {
        // CharacterWorld world = new ();
        // world.add( new Location( 1, 1 ), new UnitAssassin() );
        //
        // setSeed(15);
        Scanner scan = new Scanner( System.in );
        connModel = new DefaultListModel();
        connections = new JList( connModel );
        // //add( new Location( 1, 1 ), new UnitAssassin() );
        // Color[] colors = { Color.RED, Color.BLUE, Color.GREEN, Color.CYAN,
        // Color.PINK, Color.ORANGE, Color.GRAY, Color.MAGENTA, Color.YELLOW };
        // for ( Color color : colors )
        // {
        // add( getRandomEmptyLocation(), new Tile( color ) );
        // add( getRandomEmptyLocation(), new Tile( color ) );
        // }
        // setMessage( "Click on the first tile" );
        // first = true;
        add( new Location( 1, 1 ), new UnitAssassin() );
        add( new Location( 3, 1 ), new UnitHealer() );
        add( new Location( 1, 3 ), new UnitVanguard() );
        add( new Location( 1, 5 ), new UnitArcher() );
        add( new Location( 3, 5 ), new UnitWarrior() );
        System.out.print( "Enter listen port: " );
        port = scan.nextInt();

        // create a chat networking object to peform I/O
        networker = new ChatConnectionHandler( this, port );

        System.out.print( "Enter talk port: " );
        port = scan.nextInt();
        connect();
    }


    public void chatMessage( SocketName name, String message )
    {
        setMessage( name + ": " + message );
        int space = message.indexOf( "," );
        int r = Integer.parseInt( message.substring( 0, space ) );
        int c = Integer.parseInt( message.substring( space + 1 ) );
        locationClicked( new Location( r, c ), false );
    }


    public boolean locationClicked( Location loc )
    {
        locationClicked( loc, true );
        return true;
    }


    public void locationClicked( Location loc, boolean local )
    {

        if ( local )
        {
            // send message
            networker.send( loc.getRow() + "," + loc.getCol() );
        }
        Grid gr = getGrid();
        CharacterU t = gr.get( loc );
        if ( t != null )
        {
            setMessage( t.getUnitType() + " at " + loc.toString() + "     HP: " + t.getHealth() + "     Atk: " + t.getAtk()
                + "     Def: " + t.getDef() + "     Range: " + t.attackRange() );
            // if ( first )
            // {
            // if ( firstTile != null )
            // {
            // firstTile.flip();
            // secondTile.flip();
            // }
            // firstTile = t;
            // setMessage( "Click on the second tile" );
            // first = false;
        }
        else
        {
            // if ( firstTile.getColor().equals( t.getColor() ) )
            // {
            // firstTile = secondTile = null;
            // }
            // else
            // secondTile = t;
            // setMessage( "Click on the first tile" );
            // first = true;
            // }
        }
    }


    /**
     * Helper method to read inputs from GUI components and create a new socket
     * connection.
     */
    protected void connect()
    {
        try
        {
            SocketName sock = new SocketName( "127.0.0.1", port + "", "port_" + port );

            if ( connModel.contains( sock ) )
            {
                statusMessage( "Cannot connect to " + sock + ": already connected" );
            }
            else
            {
                networker.connect( sock );
                statusMessage( "Connected to " + sock );
            }
        }
        catch ( IllegalArgumentException iae )
        {
            statusMessage( "Cannot connect: " + iae.getMessage() );
        }

    }


    /**
     * Helper method to read inputs from GUI components and destroy an existing
     * socket connection.
     */
    protected void disconnect()
    {
        int index = connections.getSelectedIndex();
        if ( index > -1 )
        {
            SocketName dead = (SocketName)( connModel.elementAt( index ) );

            networker.disconnect( dead );
        }
    }


    /**
     * @see ChatDisplay#createSocket
     */
    public synchronized void createSocket( SocketName name )
    {
        connModel.addElement( name );
    }


    public void statusMessage( String message )
    {
        setMessage( message );
    }


    /**
     * @see ChatDisplay#destroySocket
     */
    public void destroySocket( SocketName name )
    {
        if ( connModel.contains( name ) )
        {
            connModel.removeElement( name );
        }
    }


    public static void main( String[] args )
    {
        // CharacterWorld world = new CharacterWorld();
        // world.add( new Location( 1, 1 ), new UnitAssassin() );
        // world.show();

        new TestGame().show();
    }
}
