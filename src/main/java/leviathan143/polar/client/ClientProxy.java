package leviathan143.polar.client;

import leviathan143.polar.common.CommonProxy;
import net.minecraft.client.resources.I18n;

public class ClientProxy extends CommonProxy 
{	
	@Override
	public String translate(String key, Object... formatArgs) 
	{
		return I18n.format(key, formatArgs);
	}
}
