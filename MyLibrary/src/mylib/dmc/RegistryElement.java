package mylib.dmc;

/** RegistryElement is the base class for all Registry Elements.
 * 
 *  Implements IRegistryElement, Comparable.
 *  
 * @author Adam
 *
 */
public abstract class RegistryElement implements IRegistryElement, Comparable<Object> 
{
	@Override
	public abstract String getKey();
	
	/**
	 * Default comparison method for sorting. 
	 */
	@Override
	public int compareTo(Object o) {
		return this.toString().compareToIgnoreCase(o.toString());
	}
}
