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

    public void Execute(Combatant opponent)
    {
        switch (_type)
        {
            case HIT:
                opponent.attack(1);
                break;
            case MISS:
            case WAIT:
            default:
                break;
        }
    }
}
