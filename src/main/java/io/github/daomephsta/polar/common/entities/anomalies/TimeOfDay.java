package io.github.daomephsta.polar.common.entities.anomalies;

public enum TimeOfDay
{
    SUNRISE(0, 0),
    NOON(6000, 1),
    SUNSET(12000, 2),
    MIDNIGHT(18000, 3);

    private static final TimeOfDay[] BY_INDEX;
    static
    {
        BY_INDEX = new TimeOfDay[values().length];
        for (TimeOfDay time : values())
        {
            if (BY_INDEX[time.getIndex()] != null)
                throw new IllegalArgumentException(String.format("Index collision between constants %s and %s",
                    BY_INDEX[time.getIndex()], time));
            BY_INDEX[time.getIndex()] = time;
        }
    }
    private final int ticks;
    private final int index;

    private TimeOfDay(int ticks, int index)
    {
        this.ticks = ticks;
        this.index = index;
    }

    public static TimeOfDay byIndex(int index)
    {
        if (index < values().length) return BY_INDEX[index];
        throw new IllegalArgumentException("No constant exists with the index " + index);
    }

    public int getTicks()
    {
        return ticks;
    }

    public int getIndex()
    {
        return index;
    }
}