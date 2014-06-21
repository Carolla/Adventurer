
package test.mock;

import hic.BuildingRectangle;
import hic.Mainframe;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import mylib.MsgCtrl;
import mylib.pdc.Registry;
import chronos.pdc.Adventure;
import chronos.pdc.NPC;
import chronos.pdc.NullNPC;
import chronos.pdc.buildings.Building;
import chronos.pdc.registry.AdventureRegistry;
import chronos.pdc.registry.BuildingRegistry;
import dmc.PersonReadWriter;

public class AdvObjectMother
{
    public MockAdventureRegistry getMockAdventureRegistry()
    {
        return new MockAdventureRegistry();
    }

    public MockBuilding getMockBuilding()
    {
        return new MockBuilding();
    }

    public MockBuildingRegistry getMockBuildingRegistry()
    {
        return new MockBuildingRegistry();
    }

    public MockMainframe getMockMainframe()
    {
        return new MockMainframe();
    }

    public MockPersonRW getMockPersonRW()
    {
        return new MockPersonRW();
    }

    @SuppressWarnings("serial")
    public class MockMainframe extends Mainframe
    {
        public boolean _displayed;
        public String _lastImage;
        public String _lastDesc;

        @Override
        protected void forceDrawOfObjects()
        {}

        @Override
        protected void createCivs()
        {}

        @Override
        protected void createDisplayObjects()
        {}

        @Override
        public Dimension getImagePanelSize()
        {
            return new Dimension(940, 1020);
        }

        @Override
        public void drawBuilding(BuildingRectangle rect)
        {
            MsgCtrl.msgln("Happily Drawing " + rect);
        }

        @Override
        public void displayTextAndImage(String description, String imagePath)
        {
            _displayed = true;
            _lastDesc = description;
            _lastImage = imagePath;
        }
    }

    public class MockBuilding extends Building
    {
        private static final String emptyString = "non";
        public boolean _extImageDisplayed, _extDescriptionDisplayed;
        public boolean _intImageDisplayed;
        public boolean _intDescriptionDisplayed;

        private MockBuilding()
        {
            this(emptyString, emptyString, emptyString, emptyString, emptyString, emptyString,
                    emptyString);
        }

        protected MockBuilding(String name, String master, String hoverText, String exterior,
                String interior, String extImagePath, String intImagePath)
        {
            super(name, master, hoverText, exterior, interior, extImagePath, intImagePath);
        }

        @Override
        protected NPC findBuildingMaster(String masterName)
        {
            return new NullNPC();
        }

        @Override
        public String getExteriorDescription()
        {
            _extDescriptionDisplayed = true;
            return emptyString;
        }

        @Override
        public String getExtImagePath()
        {
            _extImageDisplayed = true;
            return "ext_" + emptyString;
        }

        @Override
        public String getInteriorDescription()
        {
            _intDescriptionDisplayed = true;
            return emptyString;
        }

        @Override
        public String getIntImagePath()
        {
            _intImageDisplayed = true;
            return "int_" + emptyString;
        }

        @Override
        public String getKey()
        {
            return emptyString;
        }
    }

    public class MockBuildingRegistry extends BuildingRegistry
    {
        public Building _bldg;

//        public MockBuildingRegistry()
//        {
//            super(Registry.TEST_MODE);
//            _isClosed = false;
//        }

        @Override
        public Building getBuilding(String name)
        {
            if (_bldg == null) {
                _bldg = new MockBuilding();
            }
            return _bldg;
        }
    }

    public class MockPersonRW extends PersonReadWriter
    {
        @Override
        public List<String> wakePeople()
        {
            List<String> list = new ArrayList<String>();
            list.add("Falsoon the Gambler");
            return list;
        }
    }

    public class MockAdventureRegistry extends AdventureRegistry
    {
        private String _adventureName;
        public Object loadedAdventure;

//        public MockAdventureRegistry()
//        {
//            super(Registry.TEST_MODE);
//            _isClosed = false;
//        }

        @Override
        public List<Adventure> getAdventureList()
        {
            List<Adventure> list = new ArrayList<Adventure>();
            list.add(new MockAdventure());
            return list;
        }

        @Override
        public Adventure getAdventure(String name)
        {
            _adventureName = name;
            return new MockAdventure();
        }

        @Override
        protected void initialize()
        {}

        public String loadedAdventure()
        {
            return _adventureName;
        }
    }

    public class MockAdventure extends Adventure
    {
        public MockAdventure()
        {
            super(null, null, null, null);
        }
    }
}
