package leviathan143.polar.api.factions;

import leviathan143.polar.api.PolarAPI;

/**Declares the alignment of an object, such as an item or player. Objects may only have one alignment.**/
public enum FactionAlignment 
{
	RED(0xE34234),
	BLUE(0x007FFF),
	UNALIGNED(0xFFFFFF);

	//The colour for the faction in hexadecimal format
	private final int factionColour;
	//A string key to be used for localising the name of the faction
	private final String langKey;
	
	private FactionAlignment(int factionColour) 
	{
		this.langKey = PolarAPI.PROVIDER_MOD_ID + ".alignment." + this.name();
		this.factionColour = factionColour;
	}

	/**
	 * @return The colour associated with this faction, in hexadecimal format
	 */
	public int getFactionColour() 
	{
		return factionColour;
	}
	
	/**
	 * @return A string key to be used for localising the name of this faction
	 */
	public String getLangKey() 
	{
		return langKey;
	}
}
