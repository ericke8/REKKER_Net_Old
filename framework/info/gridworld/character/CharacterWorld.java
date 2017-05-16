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

package info.gridworld.character;

import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import info.gridworld.world.World;

import java.util.ArrayList;


/**
 * An <code>characterWorld</code> is occupied by characters. <br />
 * This class is not tested on the AP CS A and AB exams.
 */

public class CharacterWorld extends World<Character>
{
    private static final String DEFAULT_MESSAGE = "Click on a grid location to manipulate a character.";


    /**
     * Constructs an character world with a default grid.
     */
    public CharacterWorld()
    {
    }


    /**
     * Constructs an character world with a given grid.
     * 
     * @param grid
     *            the grid for this world.
     */
    public CharacterWorld( Grid<Character> grid )
    {
        super( grid );
    }


    public void show()
    {
        if ( getMessage() == null )
            setMessage( DEFAULT_MESSAGE );
        super.show();
    }


    // public void step()//TODO
    // {
    // Grid<Character> gr = getGrid();
    // ArrayList<Character> characters = new ArrayList<Character>();
    // for ( Location loc : gr.getOccupiedLocations() )
    // characters.add( gr.get( loc ) );
    //
    // for ( Character a : characters )
    // {
    // // only act if another character hasn't removed a
    // if ( a.getGrid() == gr )
    // a.act();
    // }
    // }

    /**
     * Adds an character to this world at a given location.
     * 
     * @param loc
     *            the location at which to add the character
     * @param occupant
     *            the character to add
     */
    public void add( Location loc, Character occupant )
    {
        occupant.putSelfInGrid( getGrid(), loc );
    }


    /**
     * Adds an occupant at a random empty location.
     * 
     * @param occupant
     *            the occupant to add
     */
    public void add( Character occupant )
    {
        Location loc = getRandomEmptyLocation();
        if ( loc != null )
            add( loc, occupant );
    }


    /**
     * Removes an character from this world.
     * 
     * @param loc
     *            the location from which to remove an character
     * @return the removed character, or null if there was no character at the
     *         given location.
     */
    public Character remove( Location loc )
    {
        Character occupant = getGrid().get( loc );
        if ( occupant == null )
            return null;
        occupant.removeSelfFromGrid();
        return occupant;
    }
}