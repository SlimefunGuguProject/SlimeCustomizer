package io.ncbpfluffybear.slimecustomizer.objects;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.ncbpfluffybear.slimecustomizer.SlimeCustomizer;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

abstract class CustomGeoResource extends SlimefunItem implements GEOResource {

    private final NamespacedKey key;
    private final ItemStack item;
    private final String defaultName;
    private final int maxDeviation;

    @ParametersAreNonnullByDefault
    public CustomGeoResource(ItemGroup itemGroup, SlimefunItemStack item, int maxDeviation) {
        this(itemGroup, item, item, maxDeviation);
    }

    @ParametersAreNonnullByDefault
    public CustomGeoResource(ItemGroup itemGroup, SlimefunItemStack item, ItemStack output, int maxDeviation) {
        super(itemGroup, item, RecipeType.GEO_MINER, new ItemStack[0], output);

        this.key = new NamespacedKey(SlimeCustomizer.getInstance(), item.getItemId().toLowerCase());
        this.item = output;
        this.defaultName = item.getDisplayName();
        this.maxDeviation = maxDeviation;
    }

    @Override
    public int getMaxDeviation() {
        return maxDeviation;
    }

    @Nonnull
    @Override
    public String getName() {
        return defaultName;
    }

    @Nonnull
    @Override
    public ItemStack getItem() {
        return item.clone();
    }

    @Override
    public boolean isObtainableFromGEOMiner() {
        return true;
    }

    @Nonnull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    public void registerGeo(@Nonnull SlimefunAddon addon) {
        register();
        register(addon);
    }
}
