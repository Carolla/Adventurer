package battle;

import java.util.Scanner;

import battle.CombatantInterface.CombatantArmor;
import battle.CombatantInterface.CombatantWeapon;

public class ActionSelector
{
    public enum CombatAction {ATTACK, FLEE, EQUIP_WEAPON, ATTACK_SAME, EQUIP_ARMOR};
    private final Scanner in = new Scanner(System.in);
	private CombatAction _lastAction = null;
    
	public CombatAction getNextAction() {
		boolean goodInput = false;
		CombatAction action = CombatAction.ATTACK;
		while (!goodInput) {
			if (_lastAction == CombatAction.ATTACK || _lastAction == CombatAction.ATTACK_SAME) {
				System.out.println("What would you like to do? [s]ame, [a]ttack, [f]lee, [e]quip weapon, equip a[r]mor");
			} else {
				System.out.println("What would you like to do? [a]ttack, [f]lee, [e]quip weapon, equip a[r]mor");
			}
			String line = in.nextLine().trim();
			
			if (line.equalsIgnoreCase("a")) {
				action = CombatAction.ATTACK;
				goodInput = true;
			} else if (line.equalsIgnoreCase("f")) {
				action = CombatAction.FLEE;
				goodInput = true;
			} else if (line.equalsIgnoreCase("s")) {
				action = CombatAction.ATTACK_SAME;
				goodInput = true;
			} else if (line.equalsIgnoreCase("e")) {
				action = CombatAction.EQUIP_WEAPON;
				goodInput = true;				
			} else if (line.equalsIgnoreCase("r")) {
				action = CombatAction.EQUIP_ARMOR;
				goodInput = true;				
			}
		}
		_lastAction = action;
		return action;
	}

	public CombatantWeapon selectWeapon() {
		boolean goodInput = false;
		CombatantWeapon weapon = CombatantWeapon.ONE_DAMAGE_WEAPON;
		while (!goodInput) {
			System.out.println("What would you like to equip? [f]ist, [d]agger, [m]orning star");

			String line = in.nextLine().trim();
			
			if (line.equalsIgnoreCase("f")) {
				weapon = CombatantWeapon.ONE_DAMAGE_WEAPON;
				goodInput = true;
			} else if (line.equalsIgnoreCase("d")) {
				weapon = CombatantWeapon.DAGGER;
				goodInput = true;
			} else if (line.equalsIgnoreCase("m")) {
				weapon = CombatantWeapon.MORNING_STAR;
				goodInput = true;
			} 
		}

		return weapon;
	}
	
	public CombatantArmor selectArmor() {
		boolean goodInput = false;
		CombatantArmor armor = CombatantArmor.SHIELD;
		while (!goodInput) {
			System.out.println("What would you like to equip? [s]hield, [h]elmet, chain [m]ail");

			String line = in.nextLine().trim();
			
			if (line.equalsIgnoreCase("s")) {
				armor = CombatantArmor.SHIELD;
				goodInput = true;
			} else if (line.equalsIgnoreCase("h")) {
				armor = CombatantArmor.HELMET;
				goodInput = true;
			} else if (line.equalsIgnoreCase("m")) {
				armor = CombatantArmor.CHAIN_MAIL;
				goodInput = true;
			} 
		}

		return armor;
	}
}
