package pdc.command;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import chronos.pdc.NPC;
import chronos.pdc.buildings.Bank;
import chronos.pdc.buildings.Building;
import chronos.pdc.command.Command;
import chronos.pdc.registry.BuildingRegistry;
import chronos.pdc.registry.RegistryFactory;
import civ.BuildingDisplayCiv;

public class CmdInspect extends Command
{
    // THESE CONSTANTS MUST BE STATIC BECAUSE THEY ARE CALLED IN THE CONSTRUCTOR
    /** The name of the command, mostly for error messages */
    static private final String CMD_NAME = "INSPECT";
    /** The description of what the command does, used in the {@code help()} method. */
    static private final String CMD_DESCRIPTION = "Get the near description of an npc.";
    /** Format for this command */
    static private final String CMDFMT = "INSPECT <NPC Name>";
    /** This command starts immediately, requiring no delay. */
    static private final int DELAY = 0;
    /** This command takes this number of seconds on the game clock. */
    static private final int DURATION = 5;
    
    /** Parm passed into init, can be multi-word. */
    private String _npcName;
    /** Needed to get current building. */
    private BuildingDisplayCiv _bdCiv;

    public CmdInspect(BuildingDisplayCiv bdc)
            throws NullPointerException
    {
        super(CMD_NAME, DELAY, DURATION, CMD_DESCRIPTION, CMDFMT);
        _bdCiv = bdc;
    }

    /** Takes passed in NPC name tokens, combining them into a single token
     *
     *@return single-word String NPC name
     */
    @Override
    public boolean init(List<String> args)
    {
        boolean retVal = false;
        if (args.size() == 0) {
            retVal = false;
        } else {
            _npcName = "";
            for (String token : args) {
                _npcName += token + " ";
            }
            _npcName = _npcName.trim();
            retVal = true;
        }
        return retVal;
    }

    @Override
    public boolean exec()
    {
        // need to force there to be a current building
        // enterBuilding?
        Building cBuild = _bdCiv.getCurrBuilding();
        NPC npc = null;
        List<NPC> npcList = (List<NPC>) cBuild.getPatrons();
        for (NPC anNpc : npcList) {
            if (_npcName.equals(anNpc.getName())) {
               npc = anNpc; 
            }
        }
        System.out.println("\t" + npc.getName() + " = " + npc.getNearDescription());
        return false;
    }
    

}
