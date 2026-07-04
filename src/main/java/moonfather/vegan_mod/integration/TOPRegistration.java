package moonfather.vegan_mod.integration;

import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

public class TOPRegistration implements Function<ITheOneProbe, Void>
{
    public static Object instance()
    {
        return new TOPRegistration();
    }

    @Override
    public Void apply(ITheOneProbe probe)
    {
        probe.registerProvider(new TOPInfoProvider());
        return null;
    }
}