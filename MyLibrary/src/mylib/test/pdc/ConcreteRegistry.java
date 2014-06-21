/**
 * ConcreteRegistry.java
 * Copyright (c) 2010, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package mylib.test.pdc;

import java.util.List;

import com.db4o.query.Predicate;

import mylib.ApplicationException;
import mylib.dmc.DbReadWriter.MockDBRW;
import mylib.dmc.IRegistryElement;
import mylib.pdc.Registry;
import mylib.test.dmc.SomeObject;


/**
 * A concrete Registry object used for testing.  
 * It is named FauxRegistry to indicate that is merely used for testing, and to
 * distinguish it from the inner class MockRegistry to access private methods. 
 * <p>
 * The concrete class derived from this abstact class works with a data management
 * component class <code>RegistryReadWriter</code> to handle the actual database read
 * and write operations. 
 * 
 * @author Alan Cline
 * @version <DL>
 * <DT> Build 1.0		May 8, 2010   // original <DD>
 * <DT> Build 1.1		January 22, 2011 // working to implement registry <DD>
 * <DT> Bulid 1.2		May 15 2011		// TAA: cleared out errors <DD>
 * <DT> Build 1.3       May 23 2011     // TAA: added getKey method <DD>
 * <DT> Build 2.0		Jun 13 2011		// TAA: updated/deprecated methods <DD>
 * </DL>
 */
public class ConcreteRegistry extends Registry 
{
    
	/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
	 * 			CONSTRUCTOR(S) AND RELATED METHODS
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

	/** Default constructor
	 * @param  name of the file to act as db repository
	 * @throws ApplicationException if the constructor fails
	 */
  public ConcreteRegistry(String filename) throws ApplicationException
  {
      super(filename);
  }
	
  
/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
 *      Required implementations of abstract methods
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
  
  /*    
   * Sets some predetermined data into the registry to act as default
   */
    @Override
    protected void initialize()
    {
        // Create three objects to initialize database
        SomeObject obj1 = new SomeObject(1.0, "one");
        SomeObject obj2 = new SomeObject(2.0, "two");
        SomeObject obj3 = new SomeObject(3.0, "three");
        add(obj1);
        add(obj2);
        add(obj3);
    }

//    /** Close db, destroy the dbReadWriter and set this registry to null
//     * @param eraseFile     if true, erase registry file; else not
//     */
//    public void closeRegistry()
//    {
//        super.close();
//    }
//
//    /** Delete db, destroy the dbReadWriter and set this registry to null
//     * @param eraseFile     if true, erase registry file; else not
//     */
//    public void deleteRegistry()
//    {
//        super.delete();
//    }
    
    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++  
    *        INNER CLASS MockRegistry
     * ++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

/** Accesses private data in test target */    
public class MockRegistry
{
    /** Testing mock for the DBReadWriter component */
    MockDBRW _dbMock = null;

    
    /** Creates this testing mock, and a testing mock of the DbReadWriter to have access to
     * its private methods
     */
    public MockRegistry() 
    { 
        _dbMock = _regRW.new MockDBRW();
    }
    
    /** Clear all elements in the database but leave it open */
    public void clearElements()
    {
        // Get all the elements first
        List<IRegistryElement> list = get(new Predicate<IRegistryElement>() {
            public boolean match(IRegistryElement candidate) 
            {
                return  true;
            }
    });
        // Now delete everything found from the registry
        for (int k=0; k < list.size(); k++) {
            delete(list.get(k));
        }
    }


    /** Exposes the db erase method for testing purposes*/
    public void eraseDB()
    {
        _dbMock.dbErase();
    }
    
    
    /** Get all the elements in the Registry */
    public List<IRegistryElement> getAll() 
    {
        // Run the query using the equals method
        List<IRegistryElement> obSet = _regRW.dbQuery(new Predicate<IRegistryElement>() {
                public boolean match(IRegistryElement candidate) 
                {
                    return  true;
                }
        });
        return obSet;
    }

    
    /** Load the registry with the initialized elements */
    public void init()
    {
        ConcreteRegistry.this.initialize();
    }
    
}       // end of MockRegistry inner class     
    
    
}	// end of ConcreteRegistry outer class

