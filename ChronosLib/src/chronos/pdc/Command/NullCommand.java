package chronos.pdc.Command;

import java.util.List;

import mylib.MsgCtrl;

public class NullCommand extends Command
{

    /** Error message if command cannot be found. */
    public static final String ERRMSG_UNKNOWN = "I don't understand what you want to do.";
    private boolean _isInitialized = false;
    
    public NullCommand()
    {
        this("NullCommand", 0, 0, "Null command", "");
    }
    
    public NullCommand(String name, int delay, int duration, String desc, String fmt)
            throws NullPointerException
    {
        super(name, delay, duration, desc, fmt);
//        ChronosLogger logger = MainframeCiv.getLogger();
//        logger.errorOut(ERRMSG_UNKNOWN);
        MsgCtrl.msgln(ERRMSG_UNKNOWN);
    }

    @Override
    public boolean init(List<String> args)
    {
        MsgCtrl.msgln("NullCommand::Init called with " + args);
        _isInitialized  = true;
        return true;
    }

    @Override
    public boolean exec()
    {
       return true;
    }

    public boolean isInitialized()
    {
        return _isInitialized;
    }

}
