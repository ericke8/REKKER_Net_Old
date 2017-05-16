package info.gridworld.character;

import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;


public abstract class Character
{
    protected Grid<Character> grid;

    protected int atk;

    protected int def;

    protected int health;

    protected boolean ranged;

    protected Location location;


    public abstract void attack();


    public Character()
    {
        grid = null;
        location = null;
    }


    /**
     * Gets the grid in which this character is located.
     * 
     * @return the grid of this character, or <code>null</code> if this
     *         character is not contained in a grid
     */
    public Grid<Character> getGrid()
    {
        return grid;
    }


    /**
     * Puts this character into a grid. If there is another character at the
     * given location, it is removed. <br />
     * Precondition: (1) This character is not contained in a grid (2)
     * <code>loc</code> is valid in <code>gr</code>
     * 
     * @param gr
     *            the grid into which this character should be placed
     * @param loc
     *            the location into which the character should be placed
     */
    public void putSelfInGrid( Grid<Character> gr, Location loc )
    {
        if ( grid != null )
            throw new IllegalStateException( "This character is already contained in a grid. " );

        Character character = gr.get( loc );
        if ( character != null )
            character.removeSelfFromGrid();
        gr.put( loc, this );
        grid = gr;
        location = loc;
    }


    /**
     * Moves this character to a new location. If there is another character at
     * the given location, it is removed. <br />
     * Precondition: (1) This character is contained in a grid (2)
     * <code>newLocation</code> is valid in the grid of this character
     * 
     * @param newLocation
     *            the new location
     * @return newLocation new location
     */
    public Location moveTo( Location newLocation )
    {
        if ( grid == null )
            throw new IllegalStateException( "This character is not in a grid." );
        if ( grid.get( location ) != this )
            throw new IllegalStateException( "The grid contains a different character at location " + location + "." );
        if ( !grid.isValid( newLocation ) )
            throw new IllegalArgumentException( "Location " + newLocation + " is not valid." );

        if ( newLocation.equals( location ) )
            return newLocation;
        grid.remove( location );
        Character other = grid.get( newLocation );
        if ( other != null )
            other.removeSelfFromGrid();
        location = newLocation;
        grid.put( location, this );
        return newLocation;
    }


    /**
     * Removes this character from its grid. <br />
     * Precondition: This character is contained in a grid
     */
    public void removeSelfFromGrid()
    {
        if ( grid == null )
            throw new IllegalStateException( "This character is not contained in a grid. (NULL GRID ERROR)" );
        if ( grid.get( location ) != this )
            throw new IllegalStateException( "The grid contains a different character at location " + location + "." );

        grid.remove( location );
        grid = null;
        location = null;
    }


    public Location getLocation()
    {
        return location;
    }


    public boolean isRanged()
    {
        return ranged;
    }


    public int getAtk()
    {
        return atk;
    }


    public int getDef()
    {
        return def;
    }


    public int getHealth()
    {
        return health;
    }


    public int healthAdjust( int adjust )
    {
        health += adjust;
        return health;
    }


    /**
     * Creates a string that describes this character.
     * 
     * @return a string with the location of this character
     */
    public String toString()
    {
        return getClass().getName() + "[location=" + location + "]";
    }

}