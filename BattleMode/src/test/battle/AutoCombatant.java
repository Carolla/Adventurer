package test.battle;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import battle.Attack;
import battle.Combatant;
import battle.CombatantInterface;
import battle.TargetStrategy;

public class AutoCombatant extends Combatant {
   
	private int _timesAttacked = 0;
	private int _turnCount = 0;

	/**
	 * Private to prevent construction
	 */
	private AutoCombatant(TargetStrategy strategy) { super(strategy); }
	
	public static class CombatantBuilder
	{
		private int withHp = 10;
		private int withInitiative = 10;
		private boolean withEscape = false;
		private CombatantAttack withAttack = CombatantAttack.NORMAL;
		private CombatantWeapon withWeapon = CombatantWeapon.ONE_DAMAGE_WEAPON;
		private CombatantType withType = CombatantType.HERO;
		private int withAttackRoll = 10;
		private int withAc = 10;
		private int withStrength = 10;
		private int withDexterity = 10;
		private Set<CombatantArmor> withArmors = new TreeSet<CombatantArmor>();
		
		public CombatantBuilder() { }
		
		public CombatantBuilder withType(CombatantType type)
		{
			withType = type;
			return this;
		}
		
		public CombatantBuilder withHP(int hp)
		{
			withHp = hp;
			return this;
		}
		
		public CombatantBuilder shouldTryEscaping()
		{
			withEscape = true;
			return this;
		}
		
		public CombatantBuilder withHit(CombatantAttack attack)
		{
			withAttack = attack;
			return this;
		}
		
		public CombatantBuilder withSpecificHit(int attackRoll)
		{
			withAttack = CombatantAttack.CUSTOM;
			withAttackRoll = attackRoll;
			return this;
		}
		
		public CombatantBuilder withWeapon(CombatantWeapon weapon)
		{
			withWeapon = weapon;
			return this;
		}

		public CombatantBuilder withAC(int ac) {
			withAc = ac;
			return this;
		}
		
		public AutoCombatant build()
		{
			AutoCombatant auto = new AutoCombatant(new AutoTargetStrategy());
			auto._type = withType;
			auto._hp = withHp;
			auto._attack = withAttack;
			auto._attackRoll = withAttackRoll;
			auto._ac = withAc;
			auto._shouldTryEscaping = withEscape;
			auto._initiative = withInitiative;
			auto._strength = withStrength;
			auto._dexterity = withDexterity;
			for (CombatantArmor piece : withArmors) {
				auto.equip(piece);
			}
			auto.equip(withWeapon);
			return auto;
		}

		public CombatantBuilder withInitiative(int initiative) {
			withInitiative = initiative;
			return this;
		}

		public CombatantBuilder withArmor(CombatantArmor armor) {
			withArmors.add(armor);
			return this;
		}

		public CombatantBuilder withStrength(int strength) {
			withStrength  = strength;
			return this;
		}

		public CombatantBuilder withDexterity(int dexterity) {
			withDexterity = dexterity;
			return this;
		}

	}
	
	@Override
	public int attack(CombatantInterface target) {
		_turnCount++;
		return super.attack(target);
	}
	
	@Override
	public int attacked(Attack attack) {
		_timesAttacked++;
		return super.attacked(attack);
	}
    public int getTurnCount()
    {
       return _turnCount;
    }
    
    public int getAttackCount()
    {
    	return _timesAttacked;
    }
    
    private class AutoTargetStrategy implements TargetStrategy
    {
	    /* (non-Javadoc)
		 * @see battle.CombatantInterface#takeTurn(java.util.List)
		 */
	    @Override
	    public CombatantInterface selectTarget(List<CombatantInterface> combatants) {
	    	for (CombatantInterface c : combatants) {
	    		if (!c.isType(_type) && !c.isDefeated()) {
	    			return c;
	    		}
	    	}
			return null; //Fail fast when there are no combatants
		}
    }
}
