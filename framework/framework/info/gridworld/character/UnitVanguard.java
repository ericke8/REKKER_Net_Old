package framework.info.gridworld.character;

public class UnitVanguard extends CharacterU
{

    public UnitVanguard()
    {
        // TODO Auto-generated constructor stub
        super();
        
        health = 50;
        maxHealth = 50;
        atk = 10;
        def = 10;
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
        return new String("Vanguard");
    }

}
