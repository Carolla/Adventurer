package battle;

public class Attack
{
    private final int _damageRoll;
    private final int _hitRoll;

    public Attack(int hitRoll, int damageRoll)
    {
        _hitRoll = hitRoll;
        _damageRoll = damageRoll;
    }

    public int hitRoll()
    {
        return _hitRoll;
    }
    
    public int damageRoll()
    {
        return _damageRoll;
    }
}
