/**
 * IRegistryElement.java Copyright (c) 2013, Carolla Development, Inc. All Rights Reserved
 * 
 * Permission to make digital or hard copies of all or parts of this work for commercial use is
 * prohibited. To republish, to post on servers, to reuse, or to redistribute to lists, requires
 * prior specific permission and/or a fee. Request permission to use from Carolla Development, Inc.
 * by email: acline@carolla.com
 */


package mylib.dmc;


/**
 * All elements that are stored in a {@code Registry} must implement this interface so that the
 * query engine will have the proper callbacks to process transactions. Each
 * {@code IRegistryElement} derived class must implement the {@code equals} method (for object
 * equality) and the {@code getKey} method (for field compares) for common Registry services.
 * <P>
 * Warning: Do not rely on the {@code Object.equals} default method because it compares
 * instantiation values, which will not work for db4o. Any element that needs more sophisticated
 * matching should provide an implementation method to the {@code getPredicate} method. All three
 * methods are used as part of db4o's {@code query} method, which in turn calls
 * {@code Predicate.match} method.
 * <P>
 * The {@code Predicate} class requires a {@code match} method, which is called by the query. In the
 * example below, {@code equals} method is used, but some other comparison method can be supplied in
 * the {@code Predicate} instead.
 * <P>
 * 
 * <Pre>
 *    List&ltIRegistryElement&gt obSet = _db.query(new Predicate&ltIRegistryElement&gt() { 
 *      public boolean match(IRegistryElement candidate)                                        
 *      {                                                                                                                                
 *        return candidate.equals(target);                                                                           
 *      }                                                                                                                                 
 *     });
 * </Pre>
 * 
 * @author Alan Cline
 * @version Mar 14, 2013 // original <br>
 */
public interface IRegistryElement
{
  abstract public String getKey();
}

