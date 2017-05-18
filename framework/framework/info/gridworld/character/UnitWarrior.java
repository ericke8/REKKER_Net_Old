package framework.info.gridworld.character;

public class UnitWarrior extends CharacterU
{

    public UnitWarrior()
    {
        // TODO Auto-generated constructor stub
        super();
        
        health = 30
        maxHealth = 30
        atk = 20
        def = 10
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
        return new String("Warrior");
    }

}
