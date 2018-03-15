package leviathan143.polar.server;

import leviathan143.polar.common.CommonProxy;
import net.minecraft.util.text.translation.I18n;

@SuppressWarnings("deprecation")
public class ServerProxy extends CommonProxy 
{
	@Override
	public String translate(String key, Object... formatArgs) 
	{
		return I18n.translateToLocalFormatted(key, formatArgs);
	}
}
