package io.ncbpfluffybear.slimecustomizer.objects;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

public class SCGeoResource extends CustomGeoResource {

    private final Map<Biome, Integer> biomeMap;
    private final Map<World.Environment, Integer> environmentMap;

    @ParametersAreNonnullByDefault
    public SCGeoResource(ItemGroup itemGroup, SlimefunItemStack item, int maxDeviation, Map<Biome, Integer> biomeMap, Map<World.Environment, Integer> environmentMap) {
        this(itemGroup, item, item, maxDeviation, biomeMap, environmentMap);
    }

    @ParametersAreNonnullByDefault
    public SCGeoResource(ItemGroup itemGroup, SlimefunItemStack item, ItemStack output, int maxDeviation, Map<Biome, Integer> biomeMap, Map<World.Environment, Integer> environmentMap) {
        super(itemGroup, item, output, maxDeviation);

        this.biomeMap = biomeMap;
        this.environmentMap = environmentMap;
    }

    @Override
    public int getDefaultSupply(@Nonnull World.Environment environment, @Nonnull Biome biome) {
        if (biomeMap.containsKey(biome)) {
            return biomeMap.get(biome);
        } else if (environmentMap.containsKey(environment)) {
            return environmentMap.get(environment);
        } else {
            return 0;
        }
    }
}
