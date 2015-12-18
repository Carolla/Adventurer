// Generates a new Hero for the Chronos game
function charGen() {
	// Global constants
	var NBR_TRAITS = 6;
	var RANGE_LOGATE = 8;
	var RANGE_HIGATE = 14;
	// Used for three-part range categories, e.g., weight and height
	var LOW_RANGE = -1;
	var MID_RANGE = 0;
	var HI_RANGE = 1;
	
	// Prime trait indices
	var STR = 0;
	var INT = 1;
	var WIS = 2;
	var DEX = 3;
	var CON = 4;
	var CHR = 5;

	// Attribute constants
	var LITERACY = 0;
	
	// Thief Skill indices
	var _thiefSkillNames = [
			"Find/Open Secret Doors", "Pick Pockets", "Open Locks", "Find/Remove/Make Traps", 
			"Move Silently", "Hide in Shadows", "Listening", "Climb Walls"];
	var OPEN_SECRET_DOORS = 0;
	var PICK_POCKETS = 1;
	var OPEN_LOCKS = 2;
	var FIND_REMOVE_TRAPS = 3;
	var MOVE_SILENTLY = 4;
	var HIDE_IN_SHADOWS = 5;
	var HEAR_NOISE = 6;
	var CLIMB_WALLS = 7;

	// Various occupations with their associated skills	
	var _ocps = [
		"Academic", "Acrobat", "Alchemist", "Apothecary", "Armorer", "Banker", "Bowyer",
		"Carpenter", "Farmer", "Fisher", "Forester", "Freighter", "Gambler", "Hunter",
		"Husbandman", "Innkeeper", "Jeweler", "Leatherworker", "Painter", "Mason",
		"Miner", "Navigator", "Sailor", "Shipwright", "Tailor", "Trader", "Trapper",
		"Weaponsmith", "Weaver", "Woodworker", "Drifter"];


	// Occupational skills and descriptions for defined occupations
	var _ocpSkillTable = [
		"Animal Empathy: Can communicate emotionally with animals",
		"Appraise Jewelry: Estimate selling value of gems and jewelry", 
		"Appraise Tapestries: Estimate selling value of tapestries", 
		"Arcane Knowledge: +1 INT to recognize things, substances, and potions  " 
				+ "<br> --Identify substances (2gp, 1hr)  "
				+ "<br> --Identify potions (2gp 1hr)  "
				+ "<br> --Make acid: half-pint, d4 dmg or dissolve metal (15gp, 1 hr in town only) "
				+ "<br> --Make weak explosive (2d6 dmg (5gp, 1hr) "
				+ "<br> --Make medium explosive (3d8 dmg (20gp, 4hr in town only) ",
		"Balance: +1 DEX for balancing tasks and saves", 
		"Bluff: +2 CHR if the lie is only a matter of luck that the listener believes you",
		"Bowmaking: in the field if proper materials available:"
				+ "<br> --make short bow (-1 to hit) (20 gp, 3 days) "
				+ "<br> --make arrows (-1 dmg), need 1 bird for feathers "
				+	"(1 gp per 3d4+2 per day) ",
		"Cargo Transport: knows tack, harness, and transport equipment"
				+ "<br> --Can repair wagons",
		"Cavern Lore: +1 WIS to guide party through caverns without getting lost"
				+ "<br> --Can avoid natural cavern hazards"
				+ "<br> --Identify most rock ores, +1 INT on rarer ores"
				+ "<br> --Uses picks and shovels as +1, +1 weapons",
		"Climb Walls: as a Level 1 Thief (%)",
		"Concentration: +1 Save vs CON to avoid distraction (and spell interruption)",
		"Diplomacy: +1 CHR for all political negotiating",  
		"Disable Device Skill: same as Remove Traps as Level 1 Thief (%)"
				+ "<br> --Undo or jam wooden devices or traps at +1",
		"Escape Artist: +1 DEX to slip from manacles, ropes, or through tight spaces",
		"Fast Swim: Gains +1 BM when moving in water or underwater (normal water penalty = BM/2) "
				+ "<br> --Gets +4 Save when falling into water due to diving ",	
		"Financial Brokering: +1 CHR when negotiating money deals "
				+ "<br> --No fee banking "
				+ "<br> --Gets 10% discount on all transactions in town ",
		"Find Secrets in Woodwork: +10% chance to find secret doors in wall panels, cabinets, etc.",
		"Find Secrets in Stonework: +10% chance to find openings in stone construction, "
				+ "e.g. cavern walls, stone floors, fireplaces ",
		"Find/Set Traps: for simple traps like snares and deadweights, as a Level 1 Thief (%)",
		"Gather Information: +2 CHR to hear rumor when in an inn or similarly crowded place"
				+ "<br> --+2 CHR to find contact information for a key person",
		"General Knowledge: +1 INT on any general question on specific topic ",
		"Hide in Shadows: as a Level 1 Thief (%)",
		"Hunting: 20% chance of finding wild game",
		"Husbandry: 10% chance of catching live animals"
			+ "<br> --From vet skills, heal d2 HP human dmg or Slow Poison. Needs herbs",
		"Identify Plants: +1 INT on rarer items",
		"Intimidate: +1 CHR to get info from a prison or backdown a bully",
		"Intuit Outdoor Direction: +1 WIS to know direction of travel when outside",
		"Intuit Underground Direction: +1 WIS to know direction when underground",
		"Jump: +2 AP for leaping chasms or reaching lower tree branches",	
		"Leatherworking: Makes leather armor (10gp, 3 days)"
			+ "<br> --Makes boots of gloves (5gp, 1 day)",
		"Listening: as a Level 1 Thief (%)",
		"Luck: Gets +1 on all Saves involving luck and risk taking"
				+ "<br> --Gets +2 on all throws involving gaming luck",
		"Make Raft: Make a sailing raft. Needs hand axe (3 days)",
		"Make Weapons: Can make small metal shield (12gp, 4hr)"
				+ "<br> --Make or repair small melee weapons (all at -1 to Hit, -1 dmg):"
				+ "<br> . . . dagger (5gp, 2hr); battle axe (5gp, 2hr, need hand axe); spear (5gp, 2hr); "
					+	"short sword (no scabbard) (10gp, 1 day)"
				+ "<br> --Make or repair small missile weapons (all at -1 to Hit, -1 dmg): "
					+ "throwing axe (5gp, 2hr, need hand axe); "
				+ "<br> . . . light xbow bolts (10sp, 15 min); heavy bolts (1gp, 30 min); "
				+		"sling bullet or dart (1 gp per 3d6 bullets/4hrs)", 
		"Move Silently: as a Level 1 Thief (%)",
		"Natural Knowledge: +1 WIS for biological or chemical question "
				+ "<br> --Identify substance (3gp, 1hr) "
				+ "<br> --Identify potion (3gp, 1hr) "
				+ "<br> --Detect poison in bottle or by symptoms of person (10gp, 1 hr) "
				+ "<br> --Detect potency and kind of poison after detection (20gp, 1 hr) "
				+ "<br> --Make weak medicinal potions (d4 healing) (10gp, 1 hr) "
				+ "<br> --Make medium medicinal potions (2d6 healing) (20gp, 4 hr in town only) "
				+ "<br> --Make weak poison (L1 poison)( (5gp, 1hr) "
				+ "<br> --Make medium poison (L2 poison)(40gp, 4hr in town only) ",
		"Negotiations: +1 CHR when negotiating money deals",
		"Netmaking: Makes or repairs 10'x10' net that can provide 10d4 fish per day for food. "
				+ " Needs 50' rope (2 days) ",
		"Open Locks: as a Level 1 Thief (%)",
		"Painting: Paints buildings, mixes paint. (yep, that's it).",
		"Pick Pockets: as a Level 1 Thief (%)",
		"Predict Weather: Predict next day weather at +2 WIS",
		"Read Lips: Can understand about 1 minute of speaker's speech if within 30' "
				+ "and knows the speaker's language", 
		"Repair Armor: in the field if proper materials available:"
				+ "<br> --Make shield, small metal (10gp, 1 day) or small wooden (2 gp, 4 hr) "
				+ "<br> --Convert lantern from open (hooded) to bulls-eye lantern (10gp, 4hr) "
				+ "<br> --Make caltrop for 4 spikes (1go, 1hr) ",
		"Sense Motive: +1 WIS to determine if person is lying or bluffing",
		"Sewing: Can make belt (1gp, 1hr), boots (5gp, 1 day), cloak (1gp, 1hr), hat (1gp, 1hr)",
		"Spot Details: +2 WIS to notice details such as ambushing bandits, obscure items in "
				+ "dim room, centipedes in pile of trash", 
		"Train Animals: Can train animals or work teams",		
		"Trapping: Catch animals alive at 20%",
		"Tumble: land softer when falling (reduce dmg by d3) "	
				+ "<br> --Dive tumble over opponents at +2 AC ",
		"Water Lore: +1 WIS to guide party through water areas and avoid natural hazards",
		"Wilderness Lore: Can guide party through badlands and avoid natural hazards "
				+ "<br> --Can navigate outdoor course without getting lost "
				+ "<br> --Can survive off the land",
		"Woodworking: Repair or make mods to wooden items, e.g. repair xbows (not bows),"
				+ "<br> --Add secret compartments to chests"
		];
	
			
	//================================================================================	
	// DEFINITION OF THE HERO OBJECT
	//================================================================================	
	
	// Constructor method for Hero object
	function Hero(name, gender, race, klass) {
		// List of user inputs
		this.name = name;
		this.gender = gender;
		this.race = race;
		this.klass = klass;
		// Prime traits
		this.traits = [];
		// Strength modifiers
		this.toHitStr = 0;
		this.damage = 0;
		this.wtAllow = 0;
		// Intelligence attributes
		this.langs = [];
		this.maxLangs = 1;		// everyone knows Common
		// Wizard Only attributes
		this.MSPperLevel = 0;
		this.MSPs = 0;
		this.percentToKnow = 0;
		this.wizardSpellBook = [];		
		// Wisdom attributes
		this.mam = 0;		// magical attack modifier = magic resist (non-racial)
		// Cleric only attributes
		this.CSPs = 12;
		this.CSPperLevel = 0;
		this.turnUndead = 0;
		this.clericSpellBook = [];
		// HP Mod, a CON attribute, plus racial mods
		this.hpMod = 0;
		this.rmr = 0;		// racial magic resist
		this.rpr = 0;		// racial poison resist
		// Dexterity attributes
		this.toHitDex = 0;
		this.ACMod = 0;
		// Weight in lbs and height in inches
		this.weight = 0;
		this.height = 0;
		// Non-Lethal fighting mods
		this.overbearing = 0;
		this.grappling = 0;
		this.pummeling = 0;
		this.bash = 0;
		
		// Other default starting values
		this.level = 1;
		this.xp = 0;
		this.hp = 0;
		this.ap = 0;
		this.goldInHand = 0;
		this.goldBanked = 0;
		this.AC = 10;
		
		// Table to hold thief skills percentages
		this.thiefSkills = [];

		// Table to hold skills percentages
		this.raceSkills = [];
		
		// Table to hold inventory
		this.inventory = [];
		this.load = 0;

		// Table to hold occupational skills
		this.occupation = "";
		this.ocpSkills = [];

	}	// end of Hero object constructor


	//================================================================================	
	// INSTANTIATE AND POPULATE A NEW HERO 
	//================================================================================	

	// Get the input data (in global scope) from the user
	function getInput() {
	
/*
		// temp values while testing other stuff
		name = "Falsoon";
		gender = "male";
		race = "Human";
		klass = "Fighter";
*/		
		name = prompt("Enter your Hero's name");
		gender = prompt("Are you male or female?");
		race = prompt("Is your Hero Dwarf, Elf, Gnome, Half-Elf, Half-Orc, Hobbit, or Human?");
		klass = prompt("To which Guild would you like to belong: Fighter, Cleric, Thief, or Wizard?");		
	}

	// 1. Get the character profile information for the Hero
	getInput();
	var myHero = new Hero(name, gender, race, klass);
//	echoInput(myHero);

	// 2. Roll the prime traits using 4d6-lowest value method
	myHero.traits = rollAllTraits();
//	displayTraits("Raw traits rolled:", myHero.traits);

	// 3. Adjust raw traits for Klass, Race, and gender
	// -- for Klass
	myHero.traits = adjustTraitsForKlass(myHero.traits);
//	displayTraits("Traits modified for " + myHero.klass, myHero.traits);
	// -- for Race
	myHero.traits = adjustTraitsForRace(myHero.traits);
	if ((race == "Gnome") || (race == "Half-Elf") || (race == "Human")) {
//		document.write("<P> No trait modifications for race " + race);
	} else {
//		displayTraits("<P> Traits modified for " + myHero.race, myHero.traits);
	}
	// -- for gender if female (male is the default)
	if (gender == "female") {
		myHero.traits = adjustTraitsForGender(myHero.traits);
//		displayTraits("<P> Traits modified for female character.", myHero.traits);
	} else {
//		document.write("<P> No trait modifications for male character.");
	}
	// -- verify that final traits are within the race limits
	myHero.traits = traitLimitCheck(myHero);
//	displayTraits("Final adjusted traits: ", myHero.traits);

	// 6. Calculate the STRENGTH attributes
	var strMod = calcStrengthMods(myHero.traits);
	myHero.toHitStr = strMod[0];
	myHero.damage = strMod[1];
	myHero.wtAllow = strMod[2];
//	document.write("<P> to Hit: " + myHero.toHitStr + "; damage mod " + myHero.damage + 
//		"; wt allowed: " + myHero.wtAllow);	

	// 7. Calculate the INTELLIGENCE attributes
	// --Known languages
	myHero.langs = assignRaceLanguages(myHero.race); 
//	displayKnownLanguages(myHero.langs);
	// -- Max number of knowable languages  
	myHero.maxLangs = calcMaxLangs(myHero.traits[INT]);
//	displayMaxLangs(myHero.traits[INT], myHero.maxLangs);
	// -- Literacy factor for the Hero
	myHero.skills = findLiteracy(myHero.traits[INT]);
	if ((myHero.klass == "Wizard") || (myHero.klass == "Cleric")) {
		myHero.skills[LITERACY] = "Can read and write";
	}
//	displayLiteracy(myHero.skills);
	// -- WIZARDS only: get spell attributes: spell points, points/level, % to know
	if (myHero.klass == "Wizard") {
		var wizardMods = calcWizardMods(myHero.traits[INT]);
		myHero.MSPperLevel = wizardMods[0];
		myHero.MSPs = myHero.MSPperLevel;		// for level 1
		myHero.percentToKnow = wizardMods[1];
		myHero.wizardSpellBook = assignWizardSpells(myHero.MSPperLevel);
		myHero.spellsKnown = myHero.wizardSpellBook.length;
//		displayWizardMods(myHero);
//		displayList("Known Spells in Book", myHero.wizardSpellBook, true);
	}

	// 8. Calculate the WISDOM attributes: Magic Attack Resist, and Racial Magic Resist
	myHero.mam = calcMagicAttackMod(myHero.traits[WIS]);
	// -- Certain races get a racial bonus Racial Magic Resist (See CON mods)
//	displayWisdomMods(myHero);
	// -- CLERIC only: get spell attributes: spell points, and points/level, turn undead
	if (myHero.klass == "Cleric") {
		var mods = calcClericMods(myHero.traits[WIS], myHero.mam);
		myHero.turnUndead = mods[0];
		myHero.CSPperLevel = mods[1];
		myHero.CSPs = myHero.CSPperLevel;		// for first level
		myHero.clericSpellBook = assignClericSpells();
		myHero.spellsKnown = myHero.clericSpellBook.length;
//		displayClericMods(myHero);
//		displayList("Clerical Spells known: ", myHero.clericSpellBook, true);
	}

	// 9. Calculate CONSTITUTION attributes: HP Mod, Racial Magic Resist, Racial Poison Resist
	myHero.hpMod = calcRangeMod(myHero.traits[CON]);
//	document.write("<P> " + myHero.name + " gets HP Bonus = " + myHero.hpMod);
	// -- Certain races get a bonus poison resistance
	if ((race == "Dwarf") || (race == "Gnome") || (race == "Hobbit")) {
		myHero.rmr = Math.round(myHero.traits[CON]/3.5) + myHero.mam;
		myHero.rpr = Math.round(myHero.traits[CON]/3.5);
//		document.write("<P> " + myHero.race + " gets magic resist = " + myHero.rmr);
//		document.write("<br/> and bonus poison resist = " + myHero.rpr);
	}	

	// 10. Calculate DEXTERITY attributes: ToHit Mod and AC Mod
	myHero.toHitDex = calcRangeMod(myHero.traits[DEX]);
	myHero.ACMod = myHero.toHitDex;
//	document.write("<P>" + myHero.name + " gets ToHit Dex Mod = AC Mod = " + myHero.ACMod);

	// 11. Calculate the weight and height of the hero
	myHero.weight = calcWeight(myHero.race, myHero.gender);
//	document.write("<P>" + myHero.name + " weighs " + myHero.weight + " lbs.");	
	myHero.height = calcHeight(myHero.race, myHero.gender);
	var height_in_ft = convertInchToFeet(myHero.height);
//	document.write("<br>" + myHero.name + " is " + myHero.height + " in. tall = " 
//		+ height_in_ft[0] + " ft " + height_in_ft[1] + " in.");	

	// 12. Roll for HP
	myHero.hp = rollHP(myHero.klass, myHero.hpMod); 
//	document.write("<P>" + myHero.name + " rolled " + myHero.hp + " HP");
	
	// 13. Set non-lethal fighting stats
	myHero.ap = myHero.traits[STR] + myHero.traits[DEX];
	myHero.overbearing = myHero.ap + Math.floor(myHero.weight/25);
	myHero.grappling = myHero.ap + myHero.damage;
	myHero.pummeling = myHero.ap + myHero.damage + myHero.toHitDex;
	myHero.bash = 0;
//	document.write("<P> Non-lethal stats for AP = " + myHero.ap);
//	document.write("<br> Overbearing = " + myHero.overbearing);
//	document.write(" Grappling = " + myHero.grappling);
//	document.write(" Pummeling = " + myHero.pummeling);
//	document.write(" Shield Bash = " + myHero.bash);
	 
	// 14. Calculate the block movement (speed in 5' increments)
	myHero.speed = calcSpeed(myHero.ap);
//	document.write("<br> " + myHero.ap + " can move " + myHero.speed + " blocks per round");

	// 15. Default armor class (AC)
	myHero.AC += myHero.ACMod;
//	document.write("<br> Without armor, " + myHero.name + " has an AC = " + myHero.AC);

	// 16. Roll for gold (based on klass)
	myHero.goldInHand = rollGold(myHero.klass);
//	document.write("<P> gold in hand for " + myHero.klass + " = " + myHero.goldInHand + 
//		"<br> gold banked = " + myHero.goldBanked);

	// 17. Assign special klass skills and percentages
	if (myHero.klass == "Thief") {
		myHero.thiefSkills = assignThiefSkills(myHero.traits[DEX]);
//		document.write("<P> Before racial adjustment");
		myHero.thiefSkills = adjRacialThiefSkills(myHero.thiefSkills, race);
//		document.write("<P> After racial adjustment");
		myHero.thiefSkills = joinThiefSkills(_thiefSkillNames, myHero.thiefSkills);
//		displayList("Thief skills: ", myHero.thiefSkills, false);
	}	
	
	// 18. Assign racial skills
	if (myHero.race != "Human") {
		myHero.raceSkills = assignRaceSkills();
//		displayList("Special Racial skills: ", myHero.raceSkills, false);
	}	

	// 20. Assign initial inventory
	myHero.inventory = assignInventory(myHero.klass); 
//	displayList("Inventory in backpack: ", myHero.inventory, false);

	// 19. Assign occupation and occupational skills (and possibly occupational Kit)
	myHero.occupation = assignOccupation(_ocps);
//	document.write("<P>" + 	myHero.name + "'s former occupation: " + myHero.occupation);
	myHero.ocpSkills = assignOcpSkills(myHero);
//	displayList(myHero.occupation, myHero.ocpSkills, false);
	// -- Tally weight after all Items assigned
	this.load = tallyInvenWt(myHero.inventory);
//	displayList("Inventory in backpack: ", myHero.inventory, false);
//	document.write("<P> Weight carried = " + this.load + " (gpw)");
	
	
	// 20. Display the final Hero (tada!) in the Character sheet order
	printHero(myHero);
	

	//================================================================================	
	// SUPPORTING FUNCTIONS
	//================================================================================	


	// Display the unformatted character sheet
	function printHero(hero)
	{
		var out = "";
		// Lines 1 & 2: NAME, RACE, and KLASS
		document.write("<P> Name: " + hero.name.toUpperCase());
		// LINE 2: RACE and KLASS
		document.write(" Race: " + hero.race.toUpperCase() + 
			" Class: " + hero.klass.toUpperCase());
		// LINE 3: Level, Current HP, Max HP, AC, AC Magic Adj
		document.write("<br> Level: " + hero.level + "; Current HP = " + hero.hp + 
			"; Max HP = " + hero.hp + "; Armor Class = " + hero.AC + "; AC Magic = " + hero.AC);
		// LINE 4: XP, Speed, Gold in Hand
		document.write("<br> XP = " + hero.xp + "; Speed = " + hero.speed + 
			"; Gold in hand (gp/sp) = " + hero.goldInHand + " / 0");
		// LINE 5: Gender, Gold Banked
		document.write("<br> Gender = " + hero.gender + 
			"; Gold banked (gp/sp) = " + hero.goldBanked + " / 0");
		// LINE 6: STR and mods: ToHit, Damage Mod, Wt Allow, Load Carried
		document.write("<br> STR = " + hero.traits[STR] + "; To Hit (Melee) = " + hero.toHitStr +  
			"; Damage Mod = " + hero.damage + "; Wt allowance (gpw) = " + hero.wtAllow);
		// LINE 7: INT and mods: For wizard: % to Know, Current MSP, Max MSPs, MSPs/Level NbrSpells
		if (hero.klass == "Wizard") {
			document.write("<br> INT = " + hero.traits[INT] + "; % to Know = " + hero.percentToKnow
				+ "; Current Spell Points = " + hero.MSPs + "; Max Spell Points = " + hero.MSPs
				+ "; MSPs/Level = " + hero.MSPperLevel + "; Spells Known = " 
				+ hero.wizardSpellBook.length);   
		} else {
			document.write("<br> INT = " + hero.traits[INT]);
		}
		// LINE 8: WIS and mods: For Cleric: Magic Attack Mod, Current CSP, Max CSPs, 
		//		CSPs/Level, Turn Undead
		if (hero.klass == "Cleric") {
			document.write("<br> WIS = " + hero.traits[WIS] + "; Magic Attack Mod = " 
				+ (hero.mam + hero.rmr)	+ "; Current Spell Points = " + hero.CSPs 
				+ "; Max Spell Points = " + hero.CSPs + "; CSPs/Level = " + hero.CSPperLevel 
				+ "; Turn Undead = " + hero.turnUndead); 
		} else {
			document.write("<br> WIS = " + hero.traits[WIS] + "; Magic Attack Mod = " + hero.mam);
		}
		// LINE 9: CON and mods: HP Mod 
			document.write("<br> CON = " + hero.traits[CON] + "; HP Mod = " + hero.hpMod
				+ "; Poison Resist = " + hero.rpr);
		// LINE 10: DEX and mods: HP Mod, Poison resist 
			document.write("<br> DEX = " + hero.traits[DEX] + "; ToHit (Missile) = " + hero.toHitDex
				+ "; AC Mod = " + hero.ACMod);
		// LINE 11: CHR, weight, and height 
			document.write("<br> CHR = " + hero.traits[CHR] + "; Weight (lbs) = " + hero.weight
				+ "; Height = " + hero.height + " (" + inchesToFeet(hero.height) + ")");
		// LINE 11: AP, overbearing, grappling, pummeling, shield bash 
			document.write("<br> AP = " + hero.ap + "; Overbearing = " + hero.overbearing
				+ "; Grappling = " + hero.grappling + "; Pummeling = " + hero.pummeling
				+ "; Shield Bash = " + hero.bash);
		// LINEs 12 & 13: Max Knowable Languages and list of languages
 			document.write("<br> Languages (can learn " + hero.maxLangs + " more): ");
			document.write("<br>" + displayHorizList(hero.langs)); 

		// Special Abilities page
 			document.write("<P> Special Abilities");
			displayList("Racial Skills", hero.raceSkills, false);
			displayList("Skills for Occupation " + hero.occupation, hero.ocpSkills, false);		
			if (klass == "Thief") {
				displayList("Thief Skills", hero.thiefSkills, false);
			}			
		// Inventory  page
		displayList("<P> Inventory | Weight (gpw)", hero.inventory, true);
					
		
	}	// end of printHero function


	// Display a list horizontally, separated by commas
	function displayHorizList(list) {
		var out = list[0];
		for (var k=1; k < list.length; k++) {
			out += ", " + list[k];
		}
		return out;
	}		

	// Convert inches to ft and inches
	function inchesToFeet(height) {
		var inches = height;
		var feet = Math.floor(inches/12);
		var inch = inches - (feet * 12);
		var htStr = feet + "' " + inch + "\"";
		return htStr;
	}
			

	// Add up all the weight of the items in the Hero's inventory
	function tallyInvenWt(inven) {
		var tally = 0.0;
		for (k in inven) {
			var wtStr = inven[k].toString();
			var ndx = wtStr.indexOf("|");
			var wt = wtStr.substr(ndx+1);
			tally += Math.floor(wt);
		}
//		document.write("<P> Inventory wt (gpw) = " + tally);
		return tally;
	}

	// Return the name and description of each skill associated with the given occupation
	function assignOcpSkills(myHero) {
		var skills = [];
		skills[0] = "";
		var ocpDesc = "";
		// Name (value) | wt (gpw)  ...(8 gp = 1 lb)
		var kits = [
			"Alchemists Kit (100 gp) | 40",		//  5 lb 
			"Leatherworking Kit (50 gp) | 64", 	//  8 lb
			"Metalsmith Kit (50 gp) | 80",  	// 10 lb
			"Sewing Kit (30 gp) | 16", 			//  2 lb
			"Woodworking Kit (50 gp) | 64",		//  8 lb 
			"Thieves Kit (50 gp) | 8"			//  1 lb
			];
	 
		var traits = myHero.traits;
		var occupation = myHero.occupation;
		
		switch(occupation) {
		case("Academic"):
			ocpDesc = "Knows diverse information, court politics and bureaucrats.";
			skills[0] = "Too much book-learning. No practical skills.";	
			if (traits[INT] > 14) {
				skills[0] = extractSkillSet("General Knowledge");
				skills[1] = extractSkillSet("Concentration");	 
				if (traits[CHR] > 14) {
					skills[2] = extractSkillSet("Diplomacy");
				}	
			} 		
			break;
		case("Acrobat"):
			ocpDesc = "Acrobatic and aerial body control.";	
			skills[0] = "You'll break your neck. Don't try it in the dungeon.";
			if (traits[DEX] > 14) {
				skills[0] = extractSkillSet("Climb Walls");
				skills[1] = extractSkillSet("Balance");
				skills[2] = extractSkillSet("Escape Artist");
				skills[3] = extractSkillSet("Jump");
				skills[4] = extractSkillSet("Tumble"); 
			}	
			break;
		case("Alchemist"):	
			ocpDesc = "Knows chemicals and elixirs. Owns Alchemists' Kit."
			addItem(myHero.inventory, kits[0]);
			skills[0] = "You'll blow yourself up. Don't try it even once.";
			if (traits[INT] > 14) {
				skills[0] = extractSkillSet("Arcane Knowledge");
			}	
			break;
		case("Apothecary"):	
			ocpDesc = "Knows herbs, ointments, and medicines. Owns Alchemists' Kit."
			addItem(myHero.inventory, kits[0]);
			skills[0] = "One mistake and you'll poison yourself. Stick to aspirin.";
			if (traits[WIS] > 14) {
				skills[0] = extractSkillSet("Natural Knowledge");
			}	
			break;
		case("Armorer"):	
			ocpDesc = "Makes and repairs metal armor, helmets and shields. Owns Metalsmith Kit."
			addItem(myHero.inventory, kits[2]);
			skills[0] = extractSkillSet("Repair Armor");
			break;
		case("Banker"):	
			ocpDesc = "You were a financial businessman.";
			skills[0] = extractSkillSet("Financial Brokering");
			if (traits[INT] > 15) {
				skills[1] = extractSkillSet("Appraise Jewelry");
			}	
			break;
		case("Bowyer"):	
			ocpDesc = "Can make bows and arrows. Owns Woodworking Kit.";
			addItem(myHero.inventory, kits[4]);
			skills[0] = extractSkillSet("Bowmaking");
			break;
		case("Carpenter"):	
			ocpDesc = "Knows wood and woodworking tools. Owns Woodworking Kit.";
			addItem(myHero.inventory, kits[4]);
			skills[0] = extractSkillSet("Find Secrets in Woodwork");
			break;
		case("Farmer"):	
			ocpDesc = "Knows plants, common herbs, greenery.";
			skills[0] = extractSkillSet("Identify Plants");
			skills[1] = extractSkillSet("Predict Weather");
			break;
		case("Fisher"):	
			ocpDesc = "Knows about bodies of fresh water and lakes. Owns Sewing Kit.";
			addItem(myHero.inventory, kits[3]);
			skills[0] = extractSkillSet("Netmaking");
			if (traits[STR] > 14) {
				skills[1] = extractSkillSet("Fast Swim");
			}	
			break;
		case("Forester"):	
			ocpDesc = "Has natural knowledge in wooded areas.";
			skills[0] = extractSkillSet("Hide in Shadows");
			skills[1] = extractSkillSet("Move Silently");
			skills[2] = extractSkillSet("Wilderness Lore");
			skills[3] = extractSkillSet("Intuit Outdoor Direction");
			skills[4] = extractSkillSet("Spot Details");
			if (traits[STR] > 14) {
				skills[5] = extractSkillSet("Fast Swim");
			}	
			break;
		case("Freighter"):	
			ocpDesc = "Businessman. Ships cargo in wagons. Owns Woodworking Kit.";
			addItem(myHero.inventory, kits[4]);
			skills[0] = extractSkillSet("Negotiations");
			skills[1] = extractSkillSet("Cargo Transport");
			if (traits[WIS] > 14) {
				skills[2] = extractSkillSet("Train Animals");
			}	
			break;
		case("Gambler"):	
			ocpDesc = "Skilled in games of chance.";
			skills[0] = extractSkillSet("Luck");
			skills[1] = extractSkillSet("Pick Pockets");
			skills[2] = extractSkillSet("Open Locks");
			skills[3] = extractSkillSet("Bluff");
			skills[4] = extractSkillSet("Sense Motive");
			break;
		case("Hunter"):	
			ocpDesc = "Tracks and kills wild animals for food";
			skills[0] = extractSkillSet("Hunting");
			skills[1] = extractSkillSet("Find/Set Traps");
			skills[2] = extractSkillSet("Move Silently");
			skills[3] = extractSkillSet("Hide in Shadows");
			skills[4] = extractSkillSet("Spot Details");
			var k=5;
			if (traits[CHR] > 14) {
				skills[k] = extractSkillSet("Intimidate");
				k++;
			}	
			if (traits[CON] > 14) {
				skills[k] = extractSkillSet("Listening");
				k++;
			}	
			break;
		case("Husbandman"):	
			ocpDesc = "Knows livestock of all kinds (horses, sheep, cattle, pigs)";
			skills[0] = extractSkillSet("Husbandry");
			if (traits[WIS] > 14) {
				skills[1] = extractSkillSet("Animal Empathy");
				skills[2] = extractSkillSet("Train Animals");
			}	
			break;
		case("Innkeeper"):	
			ocpDesc = "Businessman. Runs crowded places, people-oriented, business-savvy";
			skills[0] = extractSkillSet("Negotiations");
			skills[1] = extractSkillSet("Sense Motive");
			var k=2;
			if (traits[CHR] > 14) {
				skills[k] = extractSkillSet("Gather Information");
				k++;
			}
			if (traits[INT] > 14) {
				skills[k] = extractSkillSet("Read Lips");
				k++;
			}	
			break;
		case("Jeweler"):	
			ocpDesc = "Recognizes true value of gems, jewelry, etc. "
				+ "Works intricate devices like a watchmaker.";
			skills[0] = extractSkillSet("Appraise Jewelry");
			if (traits[DEX] > 14) {
				skills[1] = extractSkillSet("Open Locks");
			}
			break;
		case("Leatherworker"):	
			ocpDesc = "Tans hides and makes leather items, e.g. leather armor and boots. "
				+ "Owns Leatherworking Kit";
			addItem(myHero.inventory, kits[1]);
			skills[0] = extractSkillSet("Leatherworking");
			break;
		case("Painter"):	
			ocpDesc = "Paints buildings and mixes paint.";
			skills[0] = extractSkillSet("Painting");
			if (traits[CHR] > 14) {
				skills[1] = extractSkillSet("Gather Information");
			}
			break;
		case("Mason"):	
			ocpDesc = "Constructs buildings, works mortar, lays brick; knows stonework.";
			skills[0] = "You're especially good at putting your finger in a dike";
			if (traits[INT] > 14) {
				skills[1] = extractSkillSet("Find Secrets in Stonework");
			}
			break;
		case("Miner"):	
			ocpDesc = "Digs ores from caverns and mines. Know rock and ores";
			skills[0] = extractSkillSet("Intuit Underground Direction");
			skills[1] = extractSkillSet("Cavern Lore");
			if (traits[INT] > 14) {
				skills[2] = extractSkillSet("Find Openings in Stonework");
			}
			break;
		case("Navigator"):	
			ocpDesc = "Knows direction at sea, plots water course without getting lost";
			skills[0] = extractSkillSet("Predict Weather");
			skills[1] = extractSkillSet("Water Lore");
			skills[2] = extractSkillSet("Intuit Outdoor Direction");
			skills[3] = extractSkillSet("Spot Details");
			if (traits[STR] > 14) {
				skills[4] = extractSkillSet("Fast Swim");
			}
			break;
		case("Sailor"):	
			ocpDesc = "Knows ships, has knowledge of bodies of water.";
			skills[0] = extractSkillSet("Make Raft");
			if (traits[STR] > 14) {
				skills[1] = extractSkillSet("Fast Swim");
			}
			break;
		case("Shipwright"):	
			ocpDesc = "Builds ships, knows wood and wood-working tools.";
			skills[0] = extractSkillSet("Make Raft");
			if (traits[STR] > 14) {
				skills[1] = extractSkillSet("Fast Swim");
			}
			break;
		case("Tailor"):	
			ocpDesc = "Makes clothing, knows dyes. Owns Sewing Kit";
			addItem(myHero.inventory, kits[3]);
			skills[0] = extractSkillSet("Sewing");
			if (traits[CHR] > 14) {
				skills[1] = extractSkillSet("Gather Information");
			}
			break;
		case("Trader"):	
			ocpDesc = "Businessman. Familar with transport equipment.";
			skills[0] = extractSkillSet("Financial Brokering");
			skills[1] = extractSkillSet("Sense Motive");
			if (traits[CHR] > 14) {
				skills[2] = extractSkillSet("Diplomacy");
			}
			break;
		case("Trapper"):	
			ocpDesc = "Catches animals for tanning or money.";
			var k=0;
			skills[k++] = extractSkillSet("Trapping");
			skills[k++] = extractSkillSet("Find/Set Traps");
			skills[k++] = extractSkillSet("Move Silently");
			skills[k++] = extractSkillSet("Open Locks");
			skills[k++] = extractSkillSet("Hide in Shadows");
			skills[k++] = extractSkillSet("Spot Details");
			skills[k++] = extractSkillSet("Wilderness Lore");
			if ((traits[DEX] > 14) && (traits[INT] > 14)) {
				skills[k++] = extractSkillSet("Disable Device Skill");
			}
			break;
		case("Weaponsmith"):	
			ocpDesc = "Knows metal weapons of all types and metalworking. Owns Metalsmith Kit.";
			addItem(myHero.inventory, kits[2]);
			skills[0] = extractSkillSet("Make Weapons");
			break;
		case("Weaver"):	
			ocpDesc = "Makes tapestries, rugs, bed clothing. Knows dyes.";
			skills[0] = extractSkillSet("Appraise Tapestries");
			break;
		case("Woodworker"):	
			ocpDesc = "Builds wood furniture, cabinets. Knows wood and wood-working tools. "
				+ "Owns Woodworking Kit."
			addItem(myHero.inventory, kits[4]);
			var k = 0;
			skills[k++] = extractSkillSet("Woodworking");
			skills[k++] = extractSkillSet("Find Secrets in Woodwork");
			if ((traits[DEX] > 14) && (traits[INT] > 14)) {
				skills[k++] = extractSkillSet("Disable Device Skill");
			}
			break;
		case("Drifter"):
			opDesc = "Everyone is running from something. What is your story?";
			skills[0] = "No special skills";
			break;
		default:
			alert("assignOcpSkills(): Can't find the occupation given as " + occupation);
			break;
		}
		// Update occupational description
		myHero.occupation += ": " +  ocpDesc;
		return skills;
	}

	
	// Private: Extract the skills for a given name from the Skills Table
	function extractSkillSet(name) {
		var skillSet = "";
		var out = "<br> name = " + name;
		for (var k in _ocpSkillTable) {
			var fullStr = _ocpSkillTable[k].toString();
			out += "<br> fullstr = " + fullStr;
			var ndx = fullStr.indexOf(":");
			out += "<br> ndx = " + ndx;
			var skillStr = fullStr.substr(0, ndx);		// get occupation line from table
			out += "<br> skillStr = " + skillStr;
			if (skillStr == name) {	
				skillSet = fullStr;			// get matching line including skill name
				break;
			}	 
		}
//		document.write("<P>" + out);
		return skillSet;	
	}
		

	// Assign a random occupation to the Hero
	function assignOccupation(_ocps) {
		var ndx = myRand(_ocps.length+1)-1;	// get number within range [0, length]
		if ((ndx < 0) || (ndx > _ocps.length)) {
			alert("assignOccupation(): incorrect _ocps ndx");
		}	
		return _ocps[ndx];
	}				 

	// Join the thief skill names with the Hero-specific changes and save as thief skill
	function joinThiefSkills(skillNames, skillset) {
		for (var k=0; k< skillset.length; k++) {
			var out = "";			// clear after each skill and chance
			out += skillNames[k];
			out += " | " +  skillset[k];
//			document.write("<br>" + out);
			skillset[k] = out;
		}
		return skillset;
	}

	// Display items in a list
	// msg 	:= message to print above the list
	// list := the list of items to print
	// numbered := if true, will number the items when printing 
	function displayList(msg, list, numbered) {
		var out = msg;
		document.write("<P>" + msg);
		if (list.length == 0) {
			list[0] = "None";
		}	
		for (var k=0; k < list.length; k++) {
			if (numbered == true) {
				document.write("<br>" + (k+1) + ". " + list[k]);
			} else {
				document.write("<br> --" + list[k]);
			}
		}		
	}

	
	// Strip the skills from the occupation 
	function stripSkillFromOcp(ocpSkill) {
		document.write("<br> stripSkillFromOcp(): Trying to get " + ocpSkill + " from skilltable.");
		var index = ocpSkill.indexOf(":");
		var skill = ocpSkill.substring(index+1);
		document.write("<P> Skills retrieved from " + ocpSkill + " = " + skill);
		return skill;
	}						


	// Display the occupational skills for the given occupation
	function displayOcpSkills(ocp, skills) {
		var out = "Former occupation: " + ocp;
		for (var k=0; k< skills.length; k++) { 	
			out += "<br>" + skills[k];
		}
		document.write("<P>" + out);
	}
	
	

/*		
		var select = myRand(ocps.length);
		Hero.occupation = ocps[select];
		switch(Hero.occupation) {
		case("Academic"):	ocps = ocpAcademic(Hero.traits[INT], Hero.traits[CHR]);	break; 
		case("Acrobat"):	ocps = ocpAcrobat(Hero.traits[DEX]);	break; 
		case("Alchemist"):	ocps = ocpAlchemist(Hero.traits[INT]);	break; 
		case("Armorer"):	ocps = ocpArmorer();					break; 
		default: 
			alert("Unrecognized occupation selected: " + Hero.ocp);	 
			break;
		}
*/		


	// Assign the initial wizard spells into the spell book
	function assignWizardSpells(spellsKnown) {
		var book = [];
		// All wizards know 'Read Magic'
		book[0] = "Read Magic";
		// All other spells the wizard gets to select
		for (var k=1; k < spellsKnown; k++) {
			book[k] = "(Spell to be selected)";
		} 
		return book;
	}	

	// Assign initial inventory to Hero (8 gpw = 1 lb)
	function assignInventory(klass) {
		// Basic inventory items: name | weight
		var basics = [
			"backpack | 56",			// 7.00 lb		 
			"tinderbox | 4",			// 0.50 lb
			"torch | 8", 				// 1.00 lb
			"rations | 4", 				// 0.50 lb
			"water skein (empty) | 4", 	// 0.50 lb
			"small belt pouch | 2", 	// 0.25 lb
			"leather boots | 32", 		// 4.00 lb
			"belt | 2", 				// 0.25 lb
			"breeches | 4", 			// 0.50 lb
			"shirt | 4", 				// 0.50 lb
			"cloak | 16"				// 2.00 lb
		]; 
		// total weight = 136 gpw (17.00 lb)
		// Load up on common basics
		var inven = [];
		for (var k=0; k < basics.length; k++) {
			inven[k] = basics[k];						
		}
		// Now klass specific items
		switch(klass) {
		case("Cleric"): 
			addItem(inven, "holy symbol, wooden | 4");		// 0.50 lb
			addItem(inven, "sacred satchel | 2");			// 0.25 lb
			addItem(inven, "quarterstaff | 24");			// 3.00 lb		
			break; 
		case("Wizard"):
			addItem(inven, "magic spell book | 32");		// 4.00 lb
			addItem(inven, "magic bag | 2");				// 0.25 lb
			addItem(inven, "walking stick | 24");			// 3.00 lb
			break; 
		case("Thief"):
			addItem(inven, "thieves' kit | 8");				// 1.00 lb
			addItem(inven, "dagger | 24");					// 3.00 lb
			break; 
		case("Fighter"):
			addItem(inven, "short sword w/scabberd | 80");	// 10 lb
			break;
		default:
			alert("Could not find klass when assigning inventory: " + klass); 
			break;
		}		
		return inven;
	}
	
	// Add an element to an array
	function addItem(inven, item) {
		inven[inven.length] = item;
		return inven;
	}	
	
	// Assign the skill names associated with the occupation
	function ocpArmorer() {
		var ocps = [];
 		ocps[0] = "Makes or repairs metal armor, helmets and shields in the field. Owns Metalsmith Kit";
 		ocps[1] = "Make small metal (10 gp, 1 day) or small wooden (2gp, 4hr) shield";
		ocps[2] = "Convert hooded-lantern to bulls-eye lantern (10 gp, 4hr)"; 		
		ocps[3] = "Make caltrop from spikes (1 gp, 1hr)";
		return ocps;
	}	

	// Assign the skill names associated with the occupation
	function ocpAlchemist(int) {
		var ocps = [];
 		ocps[0] = "Poor alchemical skills. You're likely to blow yourself up!"
		if (int > 14) {	
			ocps[0] = "Arcane Knowledge: knows chemicals and elixirs. Owns Alchemist's Kit"; 
	 		ocps[1] = "+1 INT to identify substance, potions";
	 		ocps[2] = "Can make acids that can dissolve metals (d4 damage on flesh). " +  
	 			"Half pint, 15 gp, 1 hr in town only";
	 		ocps[3] = "Can make weak explosives 2d6 damage (5gp, 1hr)";
	 		ocps[4] = "Can make medium explosives 3d8 damage (20 gp, 4 hr in town only)";
	 	}	
		return ocps;
	}	
	
	// Assign the skill names associated with the occupation
	function ocpAcademic(int, dex) {
		var ocps = [];
 		ocps[0] = "No Skills"
		if (int > 14) 	
			ocps[0] = "General knowledge: knows diverse information, court politics, bureaucrats"; 
	 	if (dex > 14) 	
	 		ocps[1] = "Diplomacy: +1 CHR for political negotiating contexts";
		return ocps;
	}	
	
	// Assign the skill names associated with the occupation
	function ocpAcrobat(dex) {
		var ocps = [];
 		ocps[0] = "Poor acrobatic skills. You're likely to break your neck!"
		if (dex > 14) {
			ocps[0] = "Climb Walls (82%)";
			ocps[1] = "Balance: +1 DEX on Saves requiring balance";
			ocps[2] = "Escape Artist: +1 DEX to slip from tight spaces, ropes, etc.";
			ocps[3] = "Jump: +2 AP for leaping chasms or reaching lower tree branches";
			ocps[4] = "Tumble: reduce damage by d3 when falling; dive tumble over opponents (+2 AC)"; 
		}	
		return ocps;
	}	
	
	
	
	// Assign the skills specific to each race
	function assignRaceSkills() {
		var dwarfList = [
			"Infravision, 60'", "Detect slopes in underground passages (75%)",
			"Detect new construction in tunnel (75%)", 
			"Detect sliding or shifting walls or rooms (66%)",
			"Detect stonework traps (50%)",
			"Determine approximate underground depth (50%)"];			
		var elfList = [
			"Infravision, 60'",
			"Resistance to Sleep and Charm Spells (90%). Second normal Save allowed",
			"Archery: +1 to Hit with bow (not crossbow)",
			"Tingling: Detect secret doors when within 10' (33% passive; 67% active)",
			"Move Silently (26%)" ];
		var gnomeList = [
			"Infravision, 60'",
			"Detect slopes in underground passages (80%)", 
			"Detect unsafe walls, ceilings, floors (70%)",
			"Determine direction of underground travel (50%)",
			"Determine approximate underground depth (60%)"];			
		var halfelfList = [
			"Infravision, 60'",
			"Resistance to Sleep and Charm Spells (30%). Second normal Save allowed",
			"Tingling: Detect secret doors when within 10' (16% passive; 33% active)"];
		var hobbitList = [
			"Infravision, 30'",
			"Resistance to Poison (special save: d20 <= CON/3 + HPMod + MAM)",
			"Resistance to Magic (specials save: d20 <= WIS/3 + MAM)",
			"Detect slopes in underground passages (75%)", 
			"Determine direction of underground travel (50%)"];
		var halforcList = [ "Infravision, 60'"];	 					

		var rlist = [];
		var skills = [];
		// set the race-specific chance list to the generic for processing
		switch(race) {
		case("Dwarf"):		rlist = dwarfList; 	break;	
		case("Elf"):		rlist = elfList; 	break;	
		case("Gnome"):		rlist = gnomeList; 	break;	
		case("Half-Elf"):	rlist = halfelfList;	break;	
		case("Hobbit"):		rlist = hobbitList; 	break;	
		case("Half-Orc"):	rlist = halforcList;	break;	
		default:
			alert("Unrecognized race when adjusting thief skills: " + race);
			break;
		}	
		for (var k=0; k < rlist.length; k++) {
			skills[k] = rlist[k];			
		}
		return skills;
	}



	// Assign the chances for thief skills for level 1 by race
	function adjRacialThiefSkills(skills, race) {
		// Guard for Human thief: no racial adjustments needed 
		if (race == "Human") {
			return skills;
		}
		// Basic chance per skill for level 1 thief; must be in order of global index constants	
		var dwarfMod = 	[15,  0, 10, 15,  0,  0,  0,-10];
		var elfMod = 	[ 0,  5, -5,  0,  5, 10,  5,  0];
		var gnomeMod = 	[10,  0,  5, 10,  5,  5, 10,-15];
		var halfelfMod= [ 0, 10,  0,  0,  0,  5,  0,  0];
		var hobbitMod=	[ 5,  5,  5,  5, 10, 15,  5,-15];
		var halforcMod=	[ 5, -5,  5,  5,  0,  0,  5,  0];
		
		var chanceList = [];
		// set the race-specific chance list to the generic for processing
		switch(race) {
		case("Dwarf"):		chanceList = dwarfMod; 		break;	
		case("Elf"):		chanceList = elfMod; 		break;	
		case("Gnome"):		chanceList = gnomeMod; 		break;	
		case("Half-Elf"):	chanceList = halfelfMod; 	break;	
		case("Hobbit"):		chanceList = hobbitMod; 	break;	
		case("Half-Orc"):	chanceList = halforcMod; 	break;	
		default:
			alert("Unrecognized race when adjusting thief skills: " + race);
			break;
		}	
		if (_thiefSkillNames.length != chanceList.length) {
			var out = "Number of thief skills names = " + _thiefSkillNames.length;
			out += "<br> Chance list size = " + chanceList.length;
			alert(out);
		}
		for (var k=0; k < _thiefSkillNames.length; k++) {
			skills[k] += chanceList[k];			
		}
		return skills;
	}
		 	
	
	// Assign the chances for thief skills for level 1 by dex
	function assignThiefSkills(dex) {
		// Basic chance per skill for level 1 thief; must be in order of global index constants	
		var chanceList = [30, 30, 25, 20, 21, 11, 15, 82];
		
		// Adjustments by dex for DEX [9,18]
		var dex9 	= [0,-15,-10,-10,-20,-10,0,0];
		var dex10 	= [0,-10, -5,-10,-15, -5,0,0];
		var dex11 	= [0, -5,  0, -5,-10,  0,0,0];
		var dex12 	= [0,  0,  0,  0, -5,  0,0,0];
		var dex16 	= [0,  0,  5,  0,  0,  0,0,0];
		var dex17 	= [0,  5, 10,  0,  5,  5,0,0];
		var dex18 	= [0, 10, 15,  5, 10, 10,0,0];
	
		var skills = [];	
		if (_thiefSkillNames.length != chanceList.length) {
			alert("Number of thief skill names and percent chances disagree");
		}
		for (var k=0; k < _thiefSkillNames.length; k++) {
			skills[k] = chanceList[k];			
		}
		// Make adjustments to base chances by DEX value
		if ((dex < 13) || (dex > 15)) {
			switch(dex) {
			case(9):	adjThiefMods(skills,dex9);	break				
			case(10):	adjThiefMods(skills,dex10);	break				
			case(11):	adjThiefMods(skills,dex11);	break				
			case(12):	adjThiefMods(skills,dex12);	break				
			case(16):	adjThiefMods(skills,dex16);	break				
			case(17):	adjThiefMods(skills,dex17);	break				
			case(18):	adjThiefMods(skills,dex18);	break				
			default:
				alert("Unknown DEX found while assigning thief skills: " + dex);
				break;	
			}		
		}	
		return skills;
	}

	// Adjust the thief skills by dex
	function adjThiefMods(skills, adjTable) {
		for (var k=0; k < skills.length; k++) {
			skills[k] += adjTable[k];
		}
		return skills;
	}		
	
		
	
	// Assign 1st level clerical spells
	function assignClericSpells() {
		var spellList = [
			"Bless", "Command", "Create Water", "Cure Light Wounds", "Detect Evil", "Detect Magic",
			"Light", "Protection from Evil", "Purify Food & Drink", "Remove Fear", "Resist Cold", 
			"Sanctuary"];
			
		var spells = [];	
		for (var k=0; k < spellList.length; k++) {
			spells[k] = spellList[k];
		}	
		return spells;
	}	
	
	// Display Wizard mods: MSP/level, Spells known, and percent to know
	function displayWizardMods(myHero) {
		var out = "";
		out += myHero.klass + " " + myHero.name; 
		out += " knows " + myHero.spellsKnown + " spells,";
		out += " gets " + myHero.MSPperLevel + " MSPs per level";
		out += ", and has an " + myHero.percentToKnow + "% chance to learn new spells.";
		document.write("<P>" + out);
	}	


	// Initial gold roll depends on klass * 10 gp
	function rollGold(klass) {
		var gold = 0;
		var expectedMinRange = 0;
		var expectedMaxRange = 0;
		switch(klass) {
		case("Thief"):	  
			expectedMinRange = 20;
			expectedMaxRange = 120; 
			 gold = sumRoll(2,6) * 10; 
			 break; 
		case("Wizard"):   
			expectedMinRange = 20;
			expectedMaxRange = 80; 
			 gold = sumRoll(2,4) * 10; 
			 break; 
		case("Cleric"):
			expectedMinRange = 30;
			expectedMaxRange = 180; 
			 gold = sumRoll(3,6) * 10; 
			 break; 
		case("Fighter"):
			expectedMinRange = 50;
			expectedMaxRange = 200; 
			gold = sumRoll(5,4) * 10; 
			break; 
		default: 
			alert("Hero's Class not found when rolling for gold");
			break;
		}
		if (gold > expectedMaxRange) {
			alert("Gold rolled above expected range. Got " + gold + ", expected " + expectedMaxRange);
		}	
		if (gold < expectedMinRange) {
			alert("Gold rolled below expected range. Got " + gold + ", expected " + expectedMinRange);
		}	
		return gold;	 	
	}


	function calcSpeed(ap) {
		var speed = 0;
		var speedTbl = [15,23,32,99];
		for (var k=0; k < speedTbl.length; k++) {
			if (ap <= speedTbl[k]) {
				speed = k+2;
				break;
			}
		}	
		return speed;
	}	


	// Initial roll gets double HP roll, plus mod. Two rolls are NOT the same as 2 * single roll
	function rollHP(klass, mod) {
		var hp = 0;
		switch(klass) {
		case("Fighter"): hp = myRand(10) + myRand(10) + mod; 	break; 
		case("Cleric"):	 hp = myRand(8) + myRand(8) + mod; 		break; 
		case("Thief"):	 hp = myRand(6) + myRand(6) + mod; 		break; 
		case("Wizard"):  hp = myRand(4) + myRand(4) + mod; 		break; 
		default: 
			alert("Hero's Class not found when rolling HP");
			break;
		}
		return hp;	 	
	}

	// Returns array of ft with inches remainder
	function convertInchToFeet(inches) {
		var result = [];
		result[0] = Math.floor(inches/12);
		result[1] = inches - result[0]*12;
		return result;
	}

	// Roll dice and add them together
	function sumRoll(nbr, sides) {
		var once = 0;
		var sum = 0;
		document.write("<P>");
		for (var k=0; k < nbr; k++) {
			once = myRand(sides);
			sum += once;			
		}
		return sum;
	}

	// Calculate the height of the Hero
	function calcHeight(race, gender) {
		var MALE = 0;
		var FEMALE = 1;
		// Race height tables: [avg male height, avg female ht, low(nbr, sides), high(nbr, sides)] 
		var hobbitHt =  [36, 33, 1,  3, 1,  6];
		var gnomeHt = 	[42, 39, 1,  3, 1,  3];
		var dwarfHt = 	[48, 46, 1,  4, 1,  6];
		var elfHt = 	[60, 54, 1,  4, 1,  6];
		var halfelfHt = [66, 62, 1,  6, 1,  6];
		var humanHt = 	[68, 64, 1, 12, 1, 12];
		var halforcHt = [70, 65, 2,  4, 2,  4];
				
		// Find male or female index
		var index = (gender == "male") ? MALE : FEMALE;
		// Find average, below or above
		var range = findAverageRange();
		// Roll appropriate dice for gender
		var diff = 0;
		switch (race) {
		case("Dwarf"): 		height = lookupHtWt(dwarfHt, index); 	break;
		case("Elf"): 		height = lookupHtWt(elfHt, index); 		break;
		case("Gnome"): 		height = lookupHtWt(gnomeHt, index); 	break;
		case("Half-Elf"): 	height = lookupHtWt(halfelfHt, index); 	break;
		case("Hobbit"): 	height = lookupHtWt(hobbitHt, index); 	break;
		case("Half-Orc"): 	height = lookupHtWt(halforcHt, index); 	break;
		case("Human"): 		height = lookupHtWt(humanHt, index); 	break;
		default: 
			alert("Race not found when calculating height");
			break;
		}
		return height;	 	
	}

	// Calculate the weight of the Hero
	function calcWeight(race, gender) {
		var MALE = 0;
		var FEMALE = 1;
		// Race weight tables: [avg male weight, avg female wt, low(nbr, sides), high(nbr, sides)] 
		var hobbitfWt = [ 60,  50, 2,  4, 2,  6];
		var gnomeWt = 	[ 80,  75, 2,  4, 2,  6];
		var elfWt = 	[100,  80, 1, 10, 1, 20];
		var halfelfWt = [130, 100, 1, 20, 1, 20];
		var dwarfWt = 	[150, 120, 2,  8, 2, 12];
		var humanWt = 	[175, 130, 3, 12, 5, 12];
		var halforcWt = [180, 150, 3,  8, 4, 10];
				
		// Find male or female index
		var index = (gender == "male") ? MALE : FEMALE;
		// Find average, below or above
		var range = findAverageRange();
		// Roll appropriate dice for gender
		var diff = 0;
		switch (race) {
		case("Dwarf"): 		weight = lookupHtWt(dwarfWt, index); 	break;
		case("Elf"): 		weight = lookupHtWt(elfWt,   index); 	break;
		case("Gnome"): 		weight = lookupHtWt(gnomeWt, index); 	break;
		case("Half-Elf"): 	weight = lookupHtWt(halfelfWt, index);  break;
		case("Hobbit"): 	weight = lookupHtWt(hobbitfWt, index);  break;
		case("Half-Orc"): 	weight = lookupHtWt(halforcWt, index);  break;
		case("Human"): 		weight = lookupHtWt(humanWt, index); 	break;
		default: 
			alert("Race not found when calculating weight");
			break;
		}
		return weight;	 	
	}

	function lookupHtWt(dataTable, index) {
		var LONBR = 2;
		var HINBR = 4;
		// Assign the average male or female value as default
		var value = dataTable[index];
//		document.write("<P> Initial value = " + value);
		var range = findAverageRange();
		if (range == LOW_RANGE) {
//			document.write("<br> reduced by " + dataTable[LONBR] + "d" + dataTable[LONBR+1]);
			diff = Math.round(sumRoll(dataTable[LONBR], dataTable[HINBR+1]));
			value -= diff;
		}  
		else if (range == HI_RANGE) {
//			document.write("<br> increased by " + dataTable[HINBR] + "d" + dataTable[HINBR+1]);
			diff = Math.round(sumRoll(dataTable[HINBR], dataTable[HINBR+1]));
			value += diff;
		}
		return value;	
	}
		
	function findAverageRange(){
		var range = MID_RANGE;
		if (myRand(100) <= 30)  range = LOW_RANGE;
		if (myRand(100) > 70)   range = HI_RANGE;
		return range;	
	}


	// Calculate various bonuses/penalties based on trait outside RANGE gates
	function calcRangeMod(trait) {
		var bonus = 0;
		// Basic default 
		if (trait > RANGE_HIGATE) bonus = trait - RANGE_HIGATE; 
		if (trait < RANGE_LOGATE) bonus = trait - RANGE_LOGATE;
		return bonus;
	}

	// Verify that traits do no exceed the limit ranges of the race; else set them back into range
	// No trait for any char can be lower than 7 by DM fiat
	function traitLimitCheck(myHero) {
		// Race-specific tables
		var dwarfMin = 	 [8,  7, 7, 7,12, 7];
		var dwarfMax = 	 [18,18,18,17,19,16];
		var elfMin = 	 [ 7, 8, 7, 7, 7, 8];
		var elfMax = 	 [18,18,18,19,18,18];
		var gnomeMin = 	 [ 7, 7, 7, 7, 8, 7];
		var gnomeMax = 	 [18,18,18,18,18,18];
		var hobbitMin =  [ 7, 7, 7, 8,10, 7];
		var hobbitMax =  [17,18,17,18,19,18];
		var halfelfMin = [ 7, 7, 7, 7, 8, 7];
		var halfelfMax = [18,18,18,18,18,18];
		var halforcMin = [ 9, 7, 7, 7,13, 7];
		var halforcMax = [19,17,14,17,19,12];
		var humanMin =   [ 8, 8, 8, 8, 8, 8];
		var humanMax =   [18,18,18,18,18,18];
		
		var traits  = myHero.traits;		// to save some typing
		switch (myHero.race) {
		case "Dwarf":
			traits = limitRaceTraits(traits, dwarfMin, dwarfMax);
			break;
		case "Elf":
			traits = limitRaceTraits(traits, elfMin, elfMax);
			break;
		case "Gnome":
			traits = limitRaceTraits(traits, gnomeMin, gnomeMax);
			break;
		case "Half-Elf":
			traits = limitRaceTraits(traits, halfelfMin, halfelfMax);
			break;
		case "Human":
			traits = limitRaceTraits(traits, humanMin, humanMax);
			break;
		case "Hobbit":
			traits = limitRaceTraits(traits, hobbitMin, hobbitMax);
			break;
		case "Half-Orc":
			traits = limitRaceTraits(traits, halforcMin, halforcMax);
			break;
		default: 
			alert(myHero.race + ": cannot find in racialLimitCheck()");
			break;
		}
		return traits;
	}
		
	// For any race, force traits within racial limits
	function limitRaceTraits(traits, minTbl, maxTbl) {
		for (var k=0; k < NBR_TRAITS; k++) {
			if (traits[k] < minTbl[k]) traits[k] = minTbl[k];
			if (traits[k] > maxTbl[k]) traits[k] = maxTbl[k];
		}
		return traits;
	}

	// Get the cleric mods, based on his or her wisdom: spell points, points/level, turn undead
	function calcClericMods(wisdom, mam) {
		var mods = [];
		// Turn undead value (without level difference between cleric and undead creature)
		mods[0] = wisdom;	
		// Spells points per level 
		mods[1] = Math.floor(wisdom/2);
		return mods;
	} 
	
	// Display the wisdom mods
	function displayWisdomMods(myHero) {
		out = "<br> Wisdom mods for " + myHero.race + " " + myHero.klass;
		out += "<br> Magic Attack Mod = " + myHero.mam;
		out += "<br> Magic Attack Resist = " + myHero.rmr;
//		document.write("<P> " + out);
	}

	// Display the Cleric mods: turn undead, CSPs, CSPs/level, known spells
	function displayClericMods(myHero) {
		out =  "<br> Cleric attributes for " + myHero.race + " " + myHero.klass;
		out += "<br> Turn Undead value = " + myHero.turnUndead;
		out += "<br> Initial CSP's = " + myHero.CSPs;
		out += "<br> Spell points/level = " + myHero.CSPperLevel;
		out += "<br> Known spells = " + myHero.spellsKnown;
//		document.write("<P> " + out);
	}
	
	// Calculate Magic Attack Mod
	function calcMagicAttackMod(wisdom) {
		var mam = 0;
		// Basic default Magic Attack Mod
		if (wisdom > RANGE_HIGATE) mam = wisdom - RANGE_HIGATE; 
		if (wisdom < RANGE_LOGATE) mam = wisdom - RANGE_LOGATE;
		return mam;
	}

	
	// Get the wizard mods, based on his or her intelligence
	function calcWizardMods(intelligence) {
		var mods = [];
		// spells points per level
		mods[0] = Math.floor(intelligence/2) - 3;
		// percent to learn a new spell
		mods[1] = intelligence * 5;

		return mods;
	} 
	
	
	// Display the Hero's skills
	function displayLiteracy(skills) {
		var out = "<br> Literacy: " + skills;
		document.write("<P>" + out);
	}
	
	// Calculate the literacy of the Hero
	function findLiteracy(intelligence) {
		var literacy = "";
		if (intelligence <= 10) literacy = "illiterate";
		if (intelligence == 11) literacy = "Can read only";
		if (intelligence >= 12) literacy = "Can read and write";
		return literacy;	
	}

	// Calculate the max knowable languages for the Hero
	function calcMaxLangs(intelligence) {
		var maxLang = Math.floor(intelligence/2) - 3;
		maxLang = Math.max(1, maxLang);
		return maxLang;
	}

	// Calculate the max knowable languages for the Hero
	function displayMaxLangs(intelligence, maxLangs) {
		var out = "For Hero's INT of " + intelligence;
		out += ", she or he can learn another " + maxLangs + " languages";
		document.write("<P>" + out);
	}

	// Display the known languages
	function displayKnownLanguages(langs) {
		var out = "Known languages: Common";
		for (var k=1; k < langs.length; k++) {
			out += ", " + langs[k];
		}	
		document.write("<P>" + out);
	}

	// Modify a couple traits to reflect racial bonus and penalties
	function assignRaceLanguages(race) {
		// All Heroes know the Common tongue
		var langs = []; 
		langs[0] = "Common";
		switch(race) {
		case "Dwarf":
			langs[1] = "Groken";
			break;
		case "Elf":
			langs[1] = "Elvish";
			break;
		case "Gnome":
			langs[1] = "Gnomen";
			break;
		case "Half-Elf":
			langs[1] = myRand(100) >= 50 ? "Elvish" : "";
			break;
		case "Hobbit":
			langs[1] = "Tolkeen";
			break;
		case "Half-Orc":
			langs[1] = myRand(100) >= 50 ? "Orcish" : "";
			break;
		case "Human": 
			break;			// no modes for these races
		default:
			alert("Race not found when assigning languages");
		}
		return langs;
	}	
	
	// Set the strength modifiers: ToHit, Damage, and Wt Allowace
	function calcStrengthMods(traits) {
		// Tables range for 8 <= STR <= 19
		var toHitTbl = 	[0,0,0,0,0,0,0,0,0,1,1,2];
		var dmgTbl = 	[0,0,0,0,0,0,0,0,1,1,2,3];
		var wtTbl = 	[280, 280, 400, 400, 560, 560, 800, 800, 1200, 1600, 2800, 4000];
		var mods = [];
		var ndx = traits[STR] - RANGE_LOGATE;
		mods[0] = toHitTbl[ndx];
		mods[1] = dmgTbl[ndx];
		mods[2] = wtTbl[ndx];
		return mods;
	}	
	
	// Adjust traits for gender (if female, male is the default)
	function adjustTraitsForGender(traits) {
		if (gender == "male") return traits;
		traits[STR] -= 1;
		traits[CON] += 1;
		traits[CHR] += 1;
		return traits;
	}

	// Modify a coupe traits to reflect racial bonus and penalties
	function adjustTraitsForRace(traits) {
		switch(race) {
		case "Dwarf":
			traits[CON] += 1;
			traits[CHR] -= 1;
			break;
		case "Elf":
			traits[CON] -= 1;
			traits[DEX] += 1;
			break;
		case "Half-Orc":
			traits[STR] += 1;
			traits[CON] += 1;
			traits[CHR] -= 2;
			break;
		case "Hobbit":
			traits[STR] -= 1;
			traits[DEX] += 1;
			break;
		// no mods for these races
		case "Gnome":
		case "Half-Elf":
		case "Human": 
			break;			
		default:
			alert("Race not found when doing racial modifications");
		}
		return traits;
	}	
	
	
	// Sort the traits so that the klass's prime requisite is always the largest value
	function adjustTraitsForKlass(rawTraits) {
		// Find the largest value
		var largest = 0;
		for (var k=0; k < NBR_TRAITS; k++) {
			largest = Math.max(largest, rawTraits[k]);
		}
		// document.write("<br/> Largest trait = " + largest);
		// Find the index of the largest value
		var largestNdx = -1;
		for (var k=0; k < NBR_TRAITS; k++) {
			if (largest == rawTraits[k]) {
				largestNdx= k;
				break;
			}
		}
		// document.write(" at index " + largestNdx);
		// Swap the largest value with the prime trait value
		switch(klass) {
		case "Fighter":
			swapSingleTrait(rawTraits, STR, largestNdx);
			break;
		case "Cleric":
			swapSingleTrait(rawTraits, WIS, largestNdx);
			break;
		case "Thief":
			swapSingleTrait(rawTraits, DEX, largestNdx);
			break;
		case "Wizard":
			swapSingleTrait(rawTraits, INT, largestNdx);
			break;
		default:
			alert("Klass not found when doing klass swapping");
		}
		return rawTraits;
	}

	
	// Echo back the input values
	function echoInput(myHero) {
		var out = "<br/>" + myHero.name + " is a " + myHero.gender + " " + myHero.race + " " + myHero.klass;
		document.write(out);
	}
	
	// Display the traits with associated explanatory message
	function displayTraits(msg, traits) {
		document.write("<p/> " + msg);
		document.write("<br/> Strength = " 		+ traits[STR]);
		document.write("<br/> Intelligence = "	+ traits[INT]);
		document.write("<br/> Wisdom = " 		+ traits[WIS]);
		document.write("<br/> Dexterity = " 	+ traits[DEX]);
		document.write("<br/> Constitution = "	+ traits[CON]);
		document.write("<br/> Charisma = " 		+ traits[CHR]);
	}

	// Roll 6 prime traits using "4d6 - lowest die" method
	function rollAllTraits() {
		var traits = [];
		for (var k=0; k<NBR_TRAITS; k++) {
			traits[k] = rollOneTrait();
		}
		return traits;
	}
	
	// Roll 4d6 - lowest d6 or set
	function rollOneTrait() {
		var roll = [];
		var trait = 0;
		var NBR_DICE = 4;
		for (var k=0; k<NBR_DICE; k++) {
			roll[k] = myRand(6);
		}
		trait = sumWithoutMin(roll);
		return trait;
	}
	
	// Return the sum of the 4d6 - lowest d6
	function sumWithoutMin(roll) {
		var min = 99;
		var sum = 0;
		var result = 0;
//		document.write("<br/> Inital 4d6 rolls: " + roll + "<br/>");
		// First sum them all
		for (var k=0; k < roll.length; k++) {
			sum += roll[k];
//			document.write("Sum " + k + "= " + sum + "<br/>");
		}
		// Now remove lowest of the set
		for (var k=0; k < roll.length; k++) {
			min = Math.min(min, roll[k]);
//			document.write("Min = " + min + "<br/>");
		}
		result = ((sum - min) < RANGE_LOGATE)? RANGE_LOGATE : sum-min;
//		document.write("Resultant trait = " + result);
		return result;
	}
	
	// SUPPORT UTILITY FUNCTIONS
	// Support: Swap origin trait with dest trait 
	function swapSingleTrait(traits, origin, dest) {
		var tmp = traits[origin];
		traits[origin] = traits[dest];
		traits[dest] = tmp;
		return traits;
	}

	// Generate a random number between 1 and range
	function myRand(range) {
		var x = Math.round(Math.random() * (range-1) + 1);
		return x;
	}


	//================================================================================	
	//	TESTING CODE
	//================================================================================	

/*	
	var done = testMyRand();
	alert("testMyRand() is " + done);

	// Ensure that the end points are hit	
	function  testMyRand() {
		var COUNT = 10000;
		var RANGE = 10;
		var num = -1;
		var zeroCnt = 0;
		var oneCnt = 0;
		var rangeCnt = 0;
		var outOfRange = 0;
		for (var k=0; k < COUNT; k++) {
			num = myRand(RANGE);
			if (num == 0)  		zeroCnt++;
			if (num == 1)  		oneCnt++;
			if (num == RANGE) 	rangeCnt++
			if (num > RANGE) 	outOfRange++;
		}
		var out = "myRand() produced ";
		out += zeroCnt + " 0's, ";
		out += oneCnt + " 1's, ";
		out += rangeCnt + " " + RANGE + "s, and ";
		out += outOfRange + " out of range\n";
		out += "out of " + COUNT + " calls.";
		alert(out);
		return true;
	}		
		
		// test block inside myRand()
		var COUNT = 10000;
		var sum = 0;
		for (var k=0; k < COUNT; k++) {
			sum += Math.round(Math.random() * (range-1) + 1) ;
		}
		var out = "For a range of " + range;
		out += "\n Random generator produced a total value of " + sum;
		out += " and an average value of " + sum/COUNT;
		document.write("<P>" + out);
	

	// Testing random summation rolls
	document.write(testSumRoll(1,6));
	document.write(testSumRoll(2,6));
	document.write(testSumRoll(3,6));
	document.write(testSumRoll(2,8));
	document.write(testSumRoll(1,20));
	document.write(testSumRoll(4,4));
	document.write(testSumRoll(5,6));

	// For testing random summation rolls
	function testSumRoll(nbr, sides) {
		var COUNT = 10000;
		var sum = 0;
		var average = 0.0;
		var expected = 0.0;
		for (var k=0; k < COUNT; k++) {
			sum += sumRoll(nbr, sides);
		}	 
		average = sum/COUNT;
		expected = ((nbr * sides) + nbr)/2;
		document.write("<P> Rolled " +  COUNT + " " + nbr + "d" + sides + " = " + sum); 
		document.write(" Expected " + expected);
		document.write(" Actual = " + average);
	}	 

	var testSkills = testThiefSkillMods();
	testThiefSkills();

	// Test block for racial adjustments to DEX-valued thief skills
	function testThiefSkills() {
		var races = ["Human", "Dwarf", "Elf", "Gnome", "Half-Elf", "Hobbit", "Half-Orc"];
		// Loop through DEX values from 9 to 18
		for (var k=9; k < 19; k++) {
			skills = assignThiefSkills(k);
			// Loop through each race for each dex
			for (var n=0; n<races.length; n++) {
				var out = "DEX = " + k;
				out += " Race = " + races[n];
				document.write("<P>" + out);
				skills = adjRacialThiefSkills(skills, races[n]);
				displayThiefSkills(skills);
			}	
		}	
	}	

	// Test block for racial adjustments to DEX-valued thief skills
	function testAdjRacialThiefSkills(skills, race) {
		var races = ["Dwarf", "Elf", "Gnome", "Half-Elf", "Hobbit", "Half-Orc"];
		for (var k=0; k < races.length; k++) {
			skills += adjRacialThiefSkills(skills,races[k]);
			document.write("<P> Skill percents for DEX = " + k + ", Race = " + races[k]);
			displayThiefSkills(skills);
		}
	}	

	// Test block for thief skill mods
	function testThiefSkillMods() {
		var tstSkills = [];
		for (var k=9; k < 19; k++) {
			skills = assignThiefSkills(k,"Human");
			document.write("<P> Skill percents for DEX = " + k);
			displayThiefSkills(skills);
		}
	}
	
 	 // for testing
	myHero.occupation = "Academic";
	myHero.traits[STR] = 15;
	myHero.traits[INT] = 15;
	myHero.traits[WIS] = 17;
	myHero.traits[CON] = 15;
	myHero.traits[DEX] = 16;
	myHero.traits[CHR] = 17;
	// need traits and occupation for Hero
	myHero.ocpSkills = assignOcpSkills(myHero);
	displayList(myHero.occupation, myHero.ocpSkills, false);

*/	// end of test functions
	
}	// end of charGen function






