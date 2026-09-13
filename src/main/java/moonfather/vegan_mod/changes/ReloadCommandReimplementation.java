package moonfather.vegan_mod.changes;

import com.google.common.collect.Lists;
import moonfather.vegan_mod.VeganMod;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.WorldData;

import java.util.Collection;

public class ReloadCommandReimplementation
{
    public static void slashReload(MinecraftServer server)
    {
        PackRepository packRepository = server.getPackRepository();
        WorldData worldData = server.getWorldData();
        Collection<String> currentPacks = packRepository.getSelectedIds();
        Collection<String> newSelectedPacks = discoverNewPacks(packRepository, worldData, currentPacks);
        reloadPacks(newSelectedPacks, server);
    }
    private static void reloadPacks(final Collection<String> selectedPacks, MinecraftServer server)
    {
        server.reloadResources(selectedPacks).exceptionally((throwable) -> {
            VeganMod.LOGGER.warn("Failed to execute reload", throwable);
            VeganMod.LOGGER.error("Reload failed, keeping old data. VM FTW.");
            return null;
        });
    }

    private static Collection<String> discoverNewPacks(final PackRepository packRepository, final WorldData worldData, final Collection<String> currentPacks)
    {
        packRepository.reload();
        Collection<String> selected = Lists.newArrayList(currentPacks);
        Collection<String> disabled = worldData.getDataConfiguration().dataPacks().getDisabled();
        for(String pack : packRepository.getAvailableIds())
        {
            if (!disabled.contains(pack) && !selected.contains(pack))
            {
                selected.add(pack);
            }
        }
        return selected;
    }

}
