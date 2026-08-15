package moonfather.vegan_mod.integration;

import moonfather.vegan_mod.VeganMod;
import moonfather.vegan_mod.blocks.KilnBlockEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.StreamServerDataProvider;

import java.util.ArrayList;
import java.util.List;

public class JadeKilnDataProvider implements StreamServerDataProvider<BlockAccessor, List<Integer>>
{
    private static final JadeKilnDataProvider INSTANCE = new JadeKilnDataProvider();
    private static final Identifier id = Identifier.fromNamespaceAndPath(VeganMod.MODID, "jade_idwtkt_kiln1");

    public static JadeKilnDataProvider getInstance() { return INSTANCE; }



    @Override
    public @Nullable List<Integer> streamData(BlockAccessor accessor)
    {
        KilnBlockEntity kiln = (KilnBlockEntity) accessor.getBlockEntity();
        List<Integer> result = new ArrayList<>(4);
        result.add(0);
        result.add(0);
        result.add(0);
        result.add(0);
        if (kiln != null)
        {
            result.set(0, kiln.getOilVolume());
        }
        return result;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, List<Integer>> streamCodec()
    {
        return ByteBufCodecs.INT.apply(ByteBufCodecs.list(4)).cast();  // will use 1
    }

    @Override
    public Identifier getUid()
    {
        return id;
    }
}
