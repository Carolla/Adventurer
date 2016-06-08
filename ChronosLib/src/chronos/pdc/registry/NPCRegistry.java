/**
 * PatronRegistry.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */

package chronos.pdc.registry;

import java.util.ArrayList;
import java.util.List;

import chronos.pdc.Chronos;
import chronos.pdc.NPC;
import mylib.pdc.Registry;

/**
 * Contains the Patrons for the Inn. They interact with the Hero
 * 
 * @author Alan Cline
 * @version Jan 21, 2013 // original <br>
 */
public class NPCRegistry extends Registry<NPC>
{

  /**
   * The default init table containing starting Non-Player Characters 16 NPCs in table
   */
  @SuppressWarnings("serial")
  static private final List<NPC> _patronTable = new ArrayList<NPC>() {
    {
      // Sal the cobbler
      add(new NPC(
          "Sal",
          "cobbler",
          1,
          "A tiny man, wearing stained clothes, carries a huge burlap sack bulging with lumps.",
          "He is perpetually hunched over from his large sack. Looking at your feet, he smiles "
              + "at you pathetically.",
          // rumors
          new ArrayList<String>() {
            {
              add("\"Sal's the name, cobbling's my game!\"");
              add("\"Looks like you could use some good shoe leather.\"");
              add("\"Let me give you some advice: Rats make for lousy shoes.\"");
            }
          },
          // retorts
          new ArrayList<String>() {
            {
              add("\"I peddle shoes not prattle! Beat it!\"");
              add("\"No shoes, no service!\"");
              add("\"Not all the heels of this world are on shoes.\"");
            }
          }));
      // Scruffy the hermit
      add(new NPC(
          "Scruffy",
          "hermit",
          -5,
          "A scruffy-looking old man with a grizzled chin looks around warily.",
          "Close up, this old geezer is a mess. His hair is uncombed, his beard is gnarled, "
              + "several teeth are missing, and he smells as if he hasn't had a bath since spring.",
          // rumors
          new ArrayList<String>() {
            {
              add("\"Sit down and talk a spell.\"");
              add("\"You must be thirsty with all that curiosity built up, eh?\"");
              add("\"The mad hermit is alive and roams the northern forest. He knows that a band of "
                  +
                  "orcs have moved into the dungeon up yonder.\"");
            }
          },
          // retorts
          new ArrayList<String>() {
            {
              add("\"Go bother someone else.\"");
              add("\"I don't know nuttin', and I don't want to talk about it.\"");
              add("\"Innkeeper! Can't a guy enjoy a little peace and quiet!\"");
            }
          }));
      // Boren the blacksmith
      add(new NPC(
          "Boren",
          "blacksmith",
          0,
          "A large man sits astride his bench, sipping on a stein of ale.",
          "Although his face is ruddy and his hands are clean, his leather overalls are covered "
              + "with soot and coal dust. His forearms are as large as your thighs.",
          // rumors
          new ArrayList<String>() {
            {
              add("He exhales alcoholic fumes in satisfaction. Well stranger, sit down and whet your whistle. ");
              add("\"Boren,\" he says curtly. \"Blacksmith. I'm too thirsty to do much talking. You buying?\"");
              add("\"I made a thick metal chest for Zelligar some time ago. He must be stashing huge "
                  + "amounts of gold up there somewhere.\"");
            }
          },
          // retorts
          new ArrayList<String>() {
            {
              add("He stares at you silently.");
              add("He furls his brow. \"Hey buddy! I just came for a cool one, not a conversation.\"");
              add("He flexes his huge arms and hands in front of you as a warning, and stomps out.");
            }
          }));
      // Meladriel the courtesan
      add(new NPC(
          "Meladriel",
          "courtesan",
          5,
          "A beautiful young lady, in a low-cut purple dress, eyes you expectantly and smiles.",
          "The young lady turns slightly in her chair toward you. Her dress rises as she does so, "
              + "revealing a beautiful leg.",
          // rumors
          new ArrayList<String>() {
            {
              add("\"Hello,\" she says in a cool voice, \"My name is Meladriel.\"");
              add("She leans toward you. Her deep brown eyes stares alluringly into yours. +" +
                  "\"See anything you like?\" she says suggestively.");
              add("\"You might have an easier time of dungeon-crashing if you could find the back door "
                  + "to the northwest. The guard is often, ah, sleeping.\"");
            }
          },
          // retorts
          new ArrayList<String>() {
            {
              add("\"You don't look like my type.\"");
              add("She puts on her cloak and covers her face.");
              add("She slaps your face, pushes you roughly, and storms from the Inn.");
            }
          }));
      // Aragon, captain of the guard
      add(new NPC(
          "Aragon",
          "Captain of the Guard",
          -3,
          "A well-to-do gentleman in chainmail armor is busy eating a large dinner.",
          "As you approach, the barrel-chested man notices you from the corner of his eye. " +
              "He shakes his head and smiles grimly to himself, as if to say, " +
              "'Here comes another one.'",
          // rumors
          new ArrayList<String>() {
            {
              add("He smiles slightly and extends his hand. \"Aragon, captain of the guard\" ");
              add("\"I'm always up for a good adventure myself.\"");
              add("\"I must run my patrols tonight, but no one will probably lose any sleep if you routed out "
                  + "the beasts that use Rogahn's castle as their lair. They probably also "
                  + "wouldn't lose any sleep if you kept any loot you found.\"");
            }
          },
          // retorts
          new ArrayList<String>() {
            {
              add("He looks sideways at you and says, \"Begone!\"");
              add("He puts his hand silently on the hilt of his sword.");
              add("\"There are too many would-be adventurers who spill their blood in my liege's castle.\"");
            }
          }));
      // Matilda the matron
      add(new NPC(
          "Matilda",
          "matron",
          2,
          "A plump woman in dusty traveling clothes eats noisily and alone at a table.",
          "She continues to look at you and tears into a cooked chicken, spattering little bits "
              + "of food on the table.",
          // rumors
          new ArrayList<String>() {
            {
              add("\"Have a seat, and grab a leg if you want. No one can say that Matilda is not charitable.\"");
              add("\"I like a person with a healthy appetite. I'm on my way to Blackhawk to the north. You?\"");
              add("\"I've been up and down these roads for many years, but this season is worse than ever--"
                  + "so many bandits on the roads. They must be headquartered near here, I betcha.\"");
            }
          },
          // retorts
          new ArrayList<String>() {
            {
              add("She continues to look at you and chews noisily, spattering little bits of chicken on the table.");
              add("\"Don't know nothing, and I'm busy.\" She pushes more food into her face so that "
                  +
                  " she can't talk.");
              add("\"Why are you pestering me? Can't you see I'm hungry and need to eat, then I must get "
                  +
                  "back on the road again.\" She mutters to herself, \"It's always sumpin'.\"");
            }
          }));
      // Perrin the archer
      add(new NPC(
          "Perrin",
          "archer",
          1,
          "A slim man, with a bow and quiver at his side, chews slowly and eyes you from a distance.",
          "He seems to have traveled many miles, but his worn pack, placed carefully on the " +
              "floor, is well-kept. He has an attitude of competence. He looks down at his " +
              "food as you approach.",
          // rumors
          new ArrayList<String>() {
            {
              add("\"Perrin's the name. I'm the village fletcher. He extends a calloused hand.\" ");
              add("\"If you've a need of anything, my shop is down the street. Can't miss it. " +
                  "I'll give you a good price.\" ");
              add(" \"Stay away from the castle.There are beasts beyond imagination in the lower levels.\" ");
            }
          },
          // retorts
          new ArrayList<String>() {
            {
              add("He turns his back and continues eating.");
              add("\"We don't cotton to strangers in this town. It's a peaceful town, and we like it that way.\"");
              add("\"Don't ask me about the castle. It will only stir up trouble.\"");
            }
          }));
      // Gorbal the thief
      add(new NPC(
          "Gorbal",
          "thief",
          2,
          "A narrow-eyed gnome sits quietly in the corner trying not to appear like he's watching you.",
          "He is nervous, and clutches his weather-beaten bag. His hand drops slowly beneath " +
              "the table as you approach.",
          // rumors
          new ArrayList<String>() {
            {
              add("\"My name's not important. I'm just an honest locksmith looking to make a living. "
                  +
                  " \"Yeah, an honest locksmith.\"");
              add("He settles back, more relaxed. and puts his hand back on the table.");
              add("\"There are rumors of vast amounts of gold in that castle, gold that Zelligar stashed "
                  + "away over the years. He and his partner Rogahn.\"");
            }
          },
          // retorts
          new ArrayList<String>() {
            {
              add("\"I don't trust people who ask too many questions.\"");
              add("He slides a wicked-looking dagger from his vest and shows it to you under the table.");
              add("He throws his dagger at a nearby post, where it sticks with a thud and a quiver. "
                  +
                  "He glares at you and stalks out.");
            }
          }));
      // Balthazar the Cleric, Monastery Building Master
      add(new NPC(
          "Balthazar",
          "Monastery Master",
          3,
          "A tall man in long brown robes is talking quietly to another man at the table.",
          "The tall man stops talking and turns slowly to face you. \"Yes, may I help you?\"",
          // rumors
          new ArrayList<String>() {
            {
              add("\"I am Brother Balthazar, shepherd of the good people of this town.\"");
              add("\"The castle is not a good place these days. It is dangerous, and many people who "
                  +
                  "venture in do not come out alive.\"");
              add("\"If you have the blessings of a good cleric in your party, you may succeed in your quest,\"");
            }
          },
          // retorts
          new ArrayList<String>() {
            {
              add("He turns with a glare and looks at you for a moment. \"Can't you see I'm in the middle "
                  + "of a conversation?\"");
              add("\"I have important business here, stranger, so perhaps we can talk later at my monastery.\"");
              add("Impatiently, the tall man says to his associate, \"Perhaps we should finish our "
                  +
                  "conversation elsewhere.\" The two of them stand and leave.");
            }
          }));

      // Pendergast the Wizard, Arcaneum Building Master
      add(new NPC(
          "Pendergast",
          "Arcaneum Master",
          0,
          "A thin man in dirty maroon robes is talking quietly to another man at the table.",
          "He stops talking and turns to face you. \"Can I help you?\" he says sternly.",
          // rumors
          new ArrayList<String>() {
            {
              add("\"I am Pendergast.\" He answered as if you asked for him by name.");
              add("\"I deal in finding lost items, consulting and advice, and identifying magical items. "
                  +
                  "I do not do love potions, endless purses, parties, or other entertainment.\"");
              add("\"If you have the power of a good wizard in your party, you may succeed in your quest,\"");
            }
          },
          // retorts
          new ArrayList<String>() {
            {
              add("He turns with a glare and looks at you for a moment. \"Can't you see I'm in the middle "
                  + "of a conversation?\"");
              add("\"I have important business here, stranger, so perhaps you can go away. \"Shoo!\"");
              add("Impatiently, the thin man says to his associate, \"Perhaps we should finish our "
                  +
                  "conversation elsewhere.\" The two of them stand and leave.");
            }
          }));
      // Ripper, master thief, Rogues' Den Building Master
      add(new NPC(
          "Ripper",
          "Rogues' Den Master",
          0,
          "A large man with a bulging belly and grease stains on his clothes and beard, is rolling "
              + "dice quietly by himself at a table. ",
          "As you approach, he puts his hand over the dice, \"Up for a friendly game of chance?\" "
              + "he asks with a wicked smile.",
          // rumors
          new ArrayList<String>() {
            {
              add("\"They call me Ripper.\"");
              add("\"I deal in finding lost items...or buying lost items,\" he says with a sly smile.");
              add("He glances around conspiratorily and whispers, \"If you have need to buy or sell "
                  +
                  "something \"special\", then perhaps I can get you a good price for it.");
            }
          },
          // retorts
          new ArrayList<String>() {
            {
              add("He turns with a glare and looks at you for a moment. You notice that the dice always "
                  + "come up twelves");
              add("He eyes you suspiciously. He stares at you as he lifts his vest, revealing a nasty-looking "
                  + "knife in his belt.");
              add("He stands up and says, \"You play a dnagerous game, stranger.\". He looks around "
                  +
                  "furtively, then stomps out of the Inn.");
            }
          }));
      // Loren, fighter, Stadium Building Master
      add(new NPC(
          "Loren",
          "Stadium Master",
          1,
          "A musclular man in chain armor sprawls in a chair that seems too small for him. ",
          "He takes a big swig of frothy ale from his beer mug. \"Aahhh!\" he breathes in obvious "
              + "pleasure.",
          // rumors
          new ArrayList<String>() {
            {
              add("\"Name's Loren.\"  He sticks out a massive hand that you can barely get your hand "
                  +
                  "around to grip it. \"I run the Fighter's Guild.\"");
              add("\"You know what they say, 'There are many bold adventurers, and many old adventurers, "
                  + "but there aren't any old, bold adventurers.'\"");
              add("\"You might want to hone your skills at the local fighters' guild before you go off questing."
                  + "It's at the end of the lane just south of town. Called the Stadium.\"");
            }
          },
          // retorts
          new ArrayList<String>() {
            {
              add("His shoulders sag as he sees you approach. As you begin to speak, he holds up a finger "
                  + "to you to silence you. \"I've had a rough day. I just want to sit and cool off.\"");
              add("He sighs and walks to another table.");
              add("\"Talk to me later at the Stadium,\" he speaks quietly. \"You can find it.\" " +
                  "He drops a coin on the table and leaves.");
            }
          }));
      // Bork, Innkeeper for Ugly Ogre Inn
      add(new NPC(
          "Bork",
          "Innkeeper for Ugly Ogre Inn",
          1,
          "The bulky Innkeeper stands near the bar cleaning mugs. He seems to see everything " +
              "that happens in his place.",
          "Bork is a large wide man. He wears a greasy apron with food splattered all over it. "
              + "His smile is friendly.",
          // rumors
          new ArrayList<String>() {
            {
              add("\"Rogahn and Zelligar own that castle. They are great heroes to our community.\"");
              add("\"Rogahn is a might fighter. It is a shame for it to become the lair of beasties and bandits.\"");
              add("\"Zelligar is a powerful and wise wizard. I'm sure he has booby-traps to protect his place.\"");
              add("\"R & Z have been missing for almost a year. No word, no nuthin'.\"");
              add("\"There is probably thousands of gold pieces of treasure in that place,\" Bork muses.");
              add("\"If you want to help out, I'll let you use my Inn for a safe haven while you're here.\"");
            }
          },
          // retorts
          new ArrayList<String>() {
            {
              add("He stares at you silently, then goes to the other side of the kitchen.");
              add("\"Rogahn and Zelligar probably wouldn't like you poking into their home.\"");
              add("\"A group of adventurers went into that place two days ago. They have not returned.\"");
            }
          }));
      // J.P. Pennypacker, the Bank manager
      add(new NPC(
          "J.P. Pennypacker",
          "Bank manager",
          0,
          "He is obviously not here for entertainment. He is discussing something in low urgent "
              + "whispers.",
          "A slightly-overweight, well-dressed, balding man stares meekly at you. " +
              "A faint flowery scent emanates from the man.",
          // rumors
          new ArrayList<String>() {
            {
              add("He tries at first to ignore you, but since you continue to stand, he turns and faces "
                  + "you with an insincere smile. \"Well, what can I do for you, er, futture customers?\"");
              add("\"A proficient adventurer like yourself will need a safe place to store their loot, er, "
                  + "I mean, wealth. My Bank here in town is the best place to protect your valueables.\"");
              add("\"You can call me Mr. P, and I am always at your service...anytime during normal "
                  +
                  "banking hours. \'");
            }
          },
          // retorts
          new ArrayList<String>() {
            {
              add("He stares at you, annoyed, and askes sternly, \"Do you mind if I conduct my business "
                  + "in private, please?\" and turns back to the Innkeeper.");
              add("The banker turns back to the Innkeeper, and says, \"Is there someplace we can talk "
                  + "in private?\"");
              add("He angrily tells the Innkeeper, \"I'll see you in my office...first thing in the morning.\""
                  + "He strides from the room, looking all business-like self-important.");
            }
          }));
      // Dewey N. Howe, general store owner
      add(new NPC(
          "Dewey N. Howe",
          "General Store Owner",
          3,
          "A paunchy man with thin balding brown hair. He seems meek as a mouse.",
          "He sits alone in the corner, watcing the patrons move about.",
          // rumors
          new ArrayList<String>() {
            {
              add("He smiles shyly and bids you to sit.");
              add("\"I am proprietor Howe of the General Store down the street. If you are looking for quality "
                  + "equipment for your adventure, I might have what you need.");
              add("\"An adventurer should always have enough rope, torches, and rations. ");
            }
          },
          // retorts
          new ArrayList<String>() {
            {
              add("He shifts uncomfortably in his seat. He tries not to notice you standing over him.");
              add("\"Sorry, but I am closed right now. My store, I mean.\"");
              add("\"Come by my store tomorrow and I should have whatever you need.\"");
            }
          }));
      // The Sheriff, keeper of the Jail
      add(new NPC(
          "The Sheriff",
          "Jail Master",
          2,
          "A brawny man with a permanent scowl on his face sits with his back to the wall. He "
              + "eyes everyone suspiciously.",
          "He looks at your apprasingly and says, \"I am the Sheriff of this peaceful town.",
          // rumors
          new ArrayList<String>() {
            {
              add("\"I am the Sheriff of this peaceful town, and if you behave yourself while you're here, "
                  + "you will get into no trouble.\"");
              add("\"If you see something suspicious or dangerous, like bandits or marauding beasts, come "
                  + "tell me about it. Don't try to handle nasty problems like that by yourself. It's too "
                  + "dangerous for amateurs.\"");
              add("\"There are bands of goblins, orcs, and kobolds that have made lairs in Rogahn's castle. "
                  + "They should be wiped out. Berserkers also loot the castle, and they are the "
                  + "most dangerous.\"");
            }
          },
          // retorts
          new ArrayList<String>() {
            {
              add("\"This is not a place for peasants trying to make a name for themselves. We have enough "
                  + "death in this town.\"");
              add("\"One wrong move and you'll end up on the wrong side of my jail cell.\"");
              add("\"I'll be watching you.\"");
            }
          }));
    }
  };


  /*
   * CONSTRUCTOR(S) AND RELATED METHODS
   */
  /**
   * Private ctor because this singleton is called from getInstance(). Registry filename is used for
   * database
   * 
   * @param init flag to initialize registry for default data if true
   */
  public NPCRegistry()
  {
    super(Chronos.NPCRegPath);
  }


  /**
   * Get a particular NPC by name
   * 
   * @param name of the NPC
   * @return the NPC or null if not found
   */
  public NPC getNPC(String name)
  {
    return get(name);
  }

  /*
   * PRIVATE METHODS
   */

  /**
   * Creates the Patron Registry with the static tables given, converting each element to a Patron
   * and saving it in the Patron Registry.
   */
  @Override
  public void initialize()
  {
    _list.addAll(_patronTable);
  }
} // end of NPCRegistry class
