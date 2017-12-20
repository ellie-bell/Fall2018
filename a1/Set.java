package cs445.a1;

import java.io.Serializable;
import java.util.Arrays;


public class Set<E> implements SetInterface<E> 
{
	private E[] array;
	private int size;
    private static final int DEFAULT_CAPACITY = 25;
//    Creates an empty bag whose initial capacity is 25.

	public Set(int capacity)
	{
	    @SuppressWarnings("unchecked")
 		E[] tempArray = (E[])new Object[capacity];
        array = tempArray;
        size = 0;	
    }
	public Set(){
		this(DEFAULT_CAPACITY);
	}

	public Set(E[] entries)
	{
	    array = Arrays.copyOf(entries, entries.length);
        size = entries.length;
    }
	
	
	
	/**
     * Determines the current number of entries in this set.
     *
     * @return  The integer number of entries currently in this set
     */

	public int getCurrentSize()
	{
		return size;
	}
	
	
	/**
     * Determines whether this set is empty.
     *
     * @return  true if this set is empty; false if not
     */

	public boolean isEmpty()
	{
		return size == 0;
	}
			
	
	/**
     * Adds a new entry to this set, avoiding duplicates.
     *
     * <p> If newEntry is not null, this set does not contain newEntry, and this
     * set has available capacity (if fixed), then add modifies the set so that
     * it contains newEntry. All other entries remain unmodified. Duplicates are
     * determined using the .equals() method.
     *
     * <p> If newEntry is null, then add throws IllegalArgumentException without
     * modifying the set. If this set already contains newEntry, then add
     * returns false without modifying the set. If this set has a capacity
     * limit, and does not have available capacity, then add throws
     * SetFullException without modifying the set.
     *
     * @param newEntry  The object to be added as a new entry
     * @return  true if the addition is successful; false if the item already is
     * in this set
     * @throws SetFullException  If this set has a fixed capacity and does not
     * have the capacity to store an additional entry
     * @throws IllegalArgumentException  If newEntry is null
     */
    
	public boolean add(E newEntry) throws SetFullException, IllegalArgumentException
	{
		if(DEFAULT_CAPACITY >= size || newEntry != null || !array.equals(newEntry))
		{
			array[size] = newEntry;
        	size++;
        	return true;
        }
        else if( array.equals(newEntry))
        {
        	return false;
        }
        else if( newEntry == null)
        {
        	throw new IllegalArgumentException("Cannot Be Null");
        }
        else if ( DEFAULT_CAPACITY <size)
        {
        	throw new SetFullException();
        }
		return false;
	}
	/**
     * Removes a specific entry from this set, if possible.
     *
     * <p> If this set contains the entry, remove will modify the set so that it
     * no longer contains entry. All other entries remain unmodified.
     * Identifying this entry is accomplished using the .equals() method.
     *
     * <p> If this set does not contain entry, remove will return false without
     * modifying the set. If entry is null, then remove throws
     * IllegalArgumentException without modifying the set.
     *
     * @param entry  The entry to be removed
     * @return  true if the removal was successful; false if not
     * @throws IllegalArgumentException  If entry is null
     */		

	public boolean remove(E newEntry) throws IllegalArgumentException
	{
		
			if ( newEntry == null)
			{
				 throw new IllegalArgumentException("Cant Be Null");
			} 
			int index = getIndexOf(newEntry);
        	E result = removeEntry(index);
        	size--;
       		return newEntry != null && newEntry.equals(result);
       		
    }
	
	
	 /**
     * Removes an arbitrary entry from this set, if possible.
     *
     * <p> If this set contains at least one entry, remove will modify the set
     * so that it no longer contains one of its entries. All other entries
     * remain unmodified. The removed entry will be returned.
     *
     * <p> If this set is empty, remove will return null without modifying the
     * set. Because null cannot be added, a return value of null will never
     * indicate a successful removal.
     *
     * @return  The removed entry if the removal was successful; null otherwise
     */
    public E remove()
    {
		E result = removeEntry(size - 1);
        size--;
        return result;
       
    }
    
    private E removeEntry(int index)
    {
        E result = null;

        if (!isEmpty() && (index >= 0)) 
        {
            result = array[index];
            int lastIndex = size - 1;
            array[index] = array[lastIndex];
            array[lastIndex] = null;
        }
        return result;
    }
	
	
	
	

	 /**
     * Removes all entries from this set.
     *
     * <p> If this set is already empty, clear will not modify the set.
     * Otherwise, the set will be modified so that it contains no entries.
     */
    public void clear()
    {
	 	while (!isEmpty())
	 	{
            remove();
        }
        size = 0;
    }
	 /**
     * Tests whether this set contains a given entry. Equality is determined
     * using the .equals() method.
     *
     * <p> If this set contains entry, then contains returns true. Otherwise
     * (including if this set is empty), contains returns false. If entry is
     * null, then remove throws IllegalArgumentException. The method never
     * modifies this set.
     *
     * @param entry  The entry to locate
     * @return  true if this set contains entry; false if not
     * @throws IllegalArgumentException  If entry is null
     */
	public boolean contains(E newEntry) 
	{
		return getIndexOf(newEntry) > -1;
	}
 	
 	
 	/* for method contains*/
 	public int getIndexOf(E newEntry)
 	{
        int location = -1;
        int index = 0;
        boolean found = false;

        while (!found && (index < size)) 
        {
            if (newEntry != null && newEntry.equals(array[index])) 
            {
                found = true;
                location = index;
            }
            index++;
        }
    	return location;
    }

	E getElem(int indexElem)
	{
		return array[indexElem];
	}
	  /**
     * Retrieves all entries that are in this set.
     *
     * <p> An array is returned that contains a reference to each of the entries
     * in this set. The returned array's length will be equal to the number of
     * elements in this set, and thus the array will contain no null values.
     *
     * <p> If the implementation of set is array-backed, toArray will not return
     * the private backing array. Instead, a new array will be allocated with
     * the appropriate capacity.
     *
     * @return  A newly-allocated array of all the entries in this set
     */
	public E[] toArray()
	{
		@SuppressWarnings("unchecked")
        E[] newArray = (E[])new Object[size]; // Unchecked cast
        for (int index = 0; index < size; index++)
        {
            newArray[index] = array[index];
        }
        return newArray;
     }
}
