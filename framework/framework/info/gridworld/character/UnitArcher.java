package framework.info.gridworld.character;

public class UnitArcher extends CharacterU
{

    public UnitArcher()
    {
        // TODO Auto-generated constructor stub
        super();
        health = 15;
        maxHealth = 15;
        atk = 20;
        def = 5;
        range = 2;
    }


    @Override
    public void attack()
    {
        // TODO Auto-generated method stub

    }


    @Override
    public String getUnitType()
    {
        return new String("Archer");
    }

}