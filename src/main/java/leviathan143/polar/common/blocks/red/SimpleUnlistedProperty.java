package leviathan143.polar.common.blocks.red;

import java.util.function.Function;
import java.util.function.Predicate;

import com.google.common.base.Predicates;

import net.minecraftforge.common.property.IUnlistedProperty;

public class SimpleUnlistedProperty<V> implements IUnlistedProperty<V>
{
	private final String name;
	private final Class<V> type;
	private final Predicate<V> validator;
	private final Function<V, String> stringifier;
	
	public SimpleUnlistedProperty(String name, Class<V> type, Predicate<V> validator, Function<V, String> stringifier)
	{
		this.name = name;
		this.type = type;
		this.validator = validator;
		this.stringifier = stringifier;
	}
	
	public SimpleUnlistedProperty(String name, Class<V> type, Predicate<V> validator)
	{
		this(name, type, validator, Object::toString);
	}
	
	public SimpleUnlistedProperty(String name, Class<V> type, Function<V, String> stringifier)
	{
		this(name, type, Predicates.alwaysTrue(), stringifier);
	}
	
	public SimpleUnlistedProperty(String name, Class<V> type)
	{
		this(name, type, Predicates.alwaysTrue(), Object::toString);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public boolean isValid(V value)
	{
		return validator.test(value);
	}

	@Override
	public Class<V> getType()
	{
		return type;
	}

	@Override
	public String valueToString(V value)
	{
		return stringifier.apply(value);
	}
}
