package io.github.daomephsta.polar.common;

import java.util.Collection;
import java.util.Locale;

import com.google.common.collect.ImmutableSet;

import net.minecraft.util.StringIdentifiable;

public enum RuneVariant implements StringIdentifiable
{
	//BLUE
	RAL,
	SAI,
	DEL,
	PER,
	SEI,
	NEI,
	//RED
	QER,
	DER,
	NIM,
	MIR,
	JO,
	TIR;
	
	public static final Collection<RuneVariant> 
		BLUE = ImmutableSet.of(RAL, SAI, DEL, PER, SEI, NEI),
		RED = ImmutableSet.of(QER, DER, NIM, MIR, JO, TIR);
	
	@Override
	public String asString()
	{
		return name().toLowerCase(Locale.ROOT);
	}
}