package framework.info.gridworld.character;

public class UnitHealer extends CharacterU
{

    public UnitHealer()
    {
        // TODO Auto-generated constructor stub
        super();
        health = 15;
        maxHealth = 15;
        atk = 7;
        def = 3;
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
        return new String("Healer");
    }

}