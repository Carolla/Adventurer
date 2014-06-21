/**
 * IRegistryElement.java
 * Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 *
 * Permission to make digital or hard copies of all or parts of this work for
 * commercial use is prohibited. To republish, to post on servers, to reuse,
 * or to redistribute to lists, requires prior specific permission and/or a
 * fee. Request permission to use from Carolla Development, Inc. 
 * by email: acline@carolla.com
 */


package mylib.dmc;



/**
 *   All elements that are stored in a Registry must implement this interface
 *   so that the query engine will have the proper callbacks to process transactions. 
 *   Each IRegistryElement derived class must implement the <code>equals()</code> method 
 *   (for object equality) and the <code>getKey()</code> method (for field compares) for 
 *   common Registry services. <br>
 *   Warning: Do not rely on the <code>Object.equals()</code> default method because it compares
 *   instantiation values, which will not work for db4o. 
 *   
 *   Any element that needs more sophisticated matching should
 *   provide an implementation method to <code>getPredicate()</code>. 
 *   All three methods are used as part of db4o's <code>query()</code> method, which in turn
 *   calls <code>Predicate.match()</code>method.
 * <P>
 * The <code>Predicate</code> class requires a <code>match()</code> method,
  * which is called by the query. In the example below, <code>equals()</code> is
  * used, but some other comparison method can be supplied in the <code>Predicate</code> instead.
  * <P>
  * <Pre> 
  *  List&ltIRegistryElement&gt obSet = _db.query(new Predicate&ltIRegistryElement&gt() { 
  *           public boolean match(IRegistryElement candidate)                                        
  *           {                                                                                                                                
  *               return candidate.equals(target);                                                                           
  *           }                                                                                                                                 
  *   });                                                                                                                                       
  * </Pre>
  *
  * @author Alan Cline
  * @version <DL>
  * <DT> Build 1.0		Mar 14, 2013   // original <DD>
  * </DL>
  */
public interface IRegistryElement
{
    /** Compare one or more of the fields of this obect with the target
     * @param target    to check for equality
     * @return true if the the 'this' object and the target are equal; else false  */
    @Override
    abstract public boolean equals(Object target);

    /** Return a unique field of the element to act as a search key. This method allows a retrieval
     * by name of the object.
     * @return the field key of the element
     */
    abstract public String getKey();

}           // end of IRegisterElement interface
