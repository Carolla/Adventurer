package battle;


public class BattleAction
{
    public interface AttackAction
    {
        void getAttackResult();
    }

    public enum BattleActionType
    {
        HIT, MISS, WAIT
    }

    private final BattleActionType _type;

    public BattleAction(BattleActionType type)
    {
        _type = type;
    }

    /**
     * Complete the action, constituting one round of battle
     * 
     * @param opponent the target for this round
     * @return the damage done
     */
    public int Execute(Combatant opponent)
    {
        switch (_type)
        {
            case HIT:
                return opponent.attack(20);
            case MISS:
            case WAIT:
            default:
                return 0;
        }
    }
}
