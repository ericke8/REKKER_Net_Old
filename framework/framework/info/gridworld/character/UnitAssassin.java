package framework.info.gridworld.character;

import framework.info.gridworld.grid.Location;


public class UnitAssassin extends CharacterU
{
    
    public UnitAssassin()
    {
        // TODO Auto-generated constructor stub
        super();
        health = 5;
        maxHealth = 5;
        atk = 30;
        def = 5;
        range = 1;
    }


    @Override
    public void attack()
    {
        // TODO Auto-generated method stub
       

    }


    @Override
    public String getUnitType()
    {
        return new String("Assassin");
    }

}