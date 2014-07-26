package test.other;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chronos.pdc.Occupation;
import chronos.pdc.Skill;
import chronos.pdc.registry.OccupationRegistry;
import chronos.pdc.registry.RegistryFactory;
import chronos.pdc.registry.RegistryFactory.RegKey;
import chronos.pdc.registry.SkillRegistry;

/**
 * Display the Skills and Items from the SkillRegistry and ItemRegistry as a
 * test
 * 
 * @author Tim and Dave
 * 
 */
public class RegistryReadingTest {

    private SkillRegistry sr;
    private OccupationRegistry or;

    @Before
    public void setUp() throws Exception {
        sr = (SkillRegistry) RegistryFactory.getInstance().getRegistry(RegKey.SKILL);
        or = (OccupationRegistry) RegistryFactory.getInstance().getRegistry(RegKey.OCP);
        assertTrue(sr != null);
        assertNotNull(or);

    }

    @After
    public void tearDown() throws Exception {
        sr.closeRegistry();
        or.closeRegistry();
    }

    @Test
    public void testReadSkills() {
        List<String> skills = new ArrayList<String>();
        for (Skill s : sr.getSkillList()) {
            skills.add(s.getName());
        }
        assertNotNull(skills);
        assertTrue(skills.size() > 0);

        PriorityQueue<String> orderedSkills = new PriorityQueue<String>();

        // Put all the skills in a priority queue so we can print in
        // alphabetical order
        for (String s : skills) {
            orderedSkills.add(s);
        }

        // Now print them in order
        int i = orderedSkills.size();
        for (int j = 0; j < i; j++) {
            System.out.print((j + 1) + ". ");
            System.out.println(orderedSkills.remove());
        }
    }

    @Test
    public void testReadOccupations() {
        List<String> occupations = new ArrayList<String>();
        for (Occupation o : or.getOccupationList()) {
            occupations.add(o.getName());
        }
        assertNotNull(occupations);
        assertTrue(occupations.size() > 0);

        // print occupations
        for (int j = 0; j < occupations.size(); j++) {
            System.out.print((j + 1) + ". ");
            System.out.println(occupations.get(j));
        }

    }
}
