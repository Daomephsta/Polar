package io.github.daomephsta.polar.api.factions;

import io.github.daomephsta.polar.api.PolarApi;

/**Declares the alignment of a player or NPC. A player or NPC should only have one alignment.**/
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
        this.langKey = PolarApi.PROVIDER_MOD_ID + ".alignment." + this.name();
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
