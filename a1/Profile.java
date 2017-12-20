package cs445.a1;
import java.util.*;
/**
 * ProfileInterface describes a social media profile. It stores a user's name,
 * "about me" blurb, and a set of profiles that the user follows. It can also
 * suggest new profiles to follow based on the "followers of followers"
 * technique.
 */
public class Profile implements ProfileInterface {
	private String profileName;
	private String profileAbout;
	private Set<ProfileInterface> follow = new Set<ProfileInterface>();

	
	
	public Profile()
	{
		profileName = "";
		profileAbout="";

	}
	
	public Profile(String name, String aboutMe)
	{
		profileName = name;
		profileAbout = aboutMe;
	}
	 /**
     * Sets this profile's name.
     *
     * <p> If newName is not null, then setName modifies this profile so that
     * its name is newName. If newName is null, then setName throws
     * IllegalArgumentException without modifying the profile, for example:
     *
     * <p> {@code throw new IllegalArgumentException("Name cannot be null")}
     *
     * @param newName  The new name
     * @throws IllegalArgumentException  If newName is null
     */
    
	public void setName(String newName) throws IllegalArgumentException
	{
		if(newName==null)
		{
			throw new IllegalArgumentException("Name Cannot Be Null");
		}
		
		profileName = newName;
		
	}
	 /**
     * Gets this profile's name.
     *
     * @return  The name
     */
	public String getName()
	{
		return profileName;
	}
	/**
     * Sets this profile's "about me" blurb.
     *
     * <p> If newAbout is not null, then setAbout modifies this profile so that
     * its about blurb is newAbout. If newAbout is null, then setAbout throws
     * IllegalArgumentException without modifying the profile.
     *
     * @param newAbout  The new blurb
     * @throws IllegalArgumentException  If newAbout is null
     */
	public void setAbout(String newAbout) throws IllegalArgumentException
	{	
		if(newAbout == null)
		{
			throw new IllegalArgumentException("About Cannot Be Null");
		}
		profileAbout = newAbout;
		
	}
 	/**
     * Gets this profile's "about me" blurb
     *
     * @return  The blurb
     */
	public String getAbout()
	{
		return profileAbout;
	}
	/**
     * Adds another profile to this profile's following set.
     *
     * <p> If this profile's following set is at capacity, or if other is null,
     * then follow returns false without modifying the profile. Otherwise, other
     * is added to this profile's following set and follow returns true. If this
     * profile already followed other, then follow returns true even though no
     * changes were needed.
     *
     * @param other  The profile to follow
     * @return  True if successful, false otherwise
     */
	public boolean follow (ProfileInterface other)
	{
		boolean follow = true;
		try
		{
			this.follow.add(other);
		}
		catch(IllegalArgumentException e)
		{
			return false;
		}
		catch ( SetFullException e)
		{
			return false;
		}
		return follow;
		
	}
	/**
     * Removes the specified profile from this profile's following set.
     *
     * <p> If this profile's following set does not contain other, or if other
     * is null, then unfollow returns false without modifying the profile.
     * Otherwise, this profile in modified in such a way that other is removed
     * from this profile's following set.
     *
     * @param other  The profile to follow
     * @return  True if successful, false otherwise
     */
	public boolean unfollow(ProfileInterface other)
	{
		boolean unfollow = false;
		try
		{
		 unfollow = this.follow.remove(other);
		}
	
		catch(IllegalArgumentException e)
		{
			
			return false;	
		}
		return unfollow;
	}
	/**
     * Returns a preview of this profile's following set.
     *
     * <p> The howMany parameter is a maximum desired size. The returned array
     * may be less than the requested size if this profile is following fewer
     * than howMany other profiles. Clients of this method must be careful to
     * check the size of the returned array to avoid
     * ArrayIndexOutOfBoundsException.
     *
     * <p> Specifically, following returns an array of size min(howMany, [number
     * of profiles that this profile is following]). This array is populated
     * with arbitrary profiles that this profile follows.
     *
     * @param howMany  The maximum number of profiles to return
     * @return  An array of size &le;howMany, containing profiles that this
     * profile follows
     */
	public ProfileInterface [] following (int howMany)
	{
		
		ProfileInterface [] following;
		if(follow.getCurrentSize()< howMany)
		{
			following = new ProfileInterface [follow.getCurrentSize()];
		}
		else
		{
			following = new ProfileInterface[howMany];
		}
		for(int i = 0; i<following.length; i++)
		{
			following[i] = follow.getElem(i);
		}
		return following;
	}
	/**
     * Recommends a profile for this profile to follow. This returns a profile
     * followed by one of this profile's followed profiles. Should not recommend
     * this profile to follow someone they already follow, and should not
     * recommend to follow oneself.
     *
     * <p> For example, if this profile is Alex, and Alex follows Bart, and Bart
     * follows Crissy, this method might return Crissy.
     *
     * @return  The profile to suggest, or null if no suitable profile is found
     * (only if all of this profile's followees' followees are already followed
     * or this profile itself).
     */
	public ProfileInterface recommend()
	{
		Profile following = null;
		for(int i =0; i<this.follow.getCurrentSize(); i++)
		{
			following = (Profile) follow.getElem(i);
			for(int j=0 ;j<following.follow.getCurrentSize(); j++)
			{
				if(!this.follow.contains(following.follow.getElem(j)) && following.follow.getElem(j)!=this)
				{
					return following.follow.getElem(j);
				}
			}
		}
		return null;
	}

	
}
