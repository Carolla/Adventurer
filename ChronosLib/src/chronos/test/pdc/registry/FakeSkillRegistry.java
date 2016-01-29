/**
 * FakeRegistry.java
 * Copyright (c) 2009, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */

package chronos.test.pdc.registry;

import java.util.ArrayList;
import java.util.List;

import chronos.pdc.Skill;
import chronos.pdc.registry.SkillRegistry;
import chronos.test.pdc.FakeSkill;

/**
 * Target for testing the RegistryFactory
 * 
 * @author Alan Cline
 * @version <DL>
 *  <DT>Build 1.0 May 18, 2013 // original <DD>
 *  </DL>
 */
public class FakeSkillRegistry extends SkillRegistry
{
    
    /*
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ 
     *         CONSTRUCTOR(S) AND RELATED METHODS 
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    private List<FakeSkill> _list = new ArrayList<FakeSkill>();

    @Override
    protected void init(String filename)
    {
      //Do nothing, don't touch file
    }

    @Override
    public boolean add(Skill obj)
    {
      return _list.add((FakeSkill) obj);
    }
    
    @Override
    public void delete(Skill obj)
    {
      _list.remove(obj);
    }
    
    @Override
    public FakeSkill get(String name)
    {
      for (FakeSkill s : _list) {
        if (name.equals(s.getName())) {
          return s;
        }
      }
      return null;
    }
    
    @Override
    public void initialize()
    {
        for (int k=0; k < SkillRegistry._racialSkillTable.length; k++) {
          add(new FakeSkill(SkillRegistry._racialSkillTable[k][0]));
        }
        for (int k = 0; k < SkillRegistry._occupSkillTable.length; k++) {
          add(new FakeSkill(SkillRegistry._occupSkillTable[k][0]));
        }
    }

}   // end of FakeRegistry class

