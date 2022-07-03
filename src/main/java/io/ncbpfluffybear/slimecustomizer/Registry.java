package io.ncbpfluffybear.slimecustomizer;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class Registry {

    private final SlimeCustomizer plugin;

    private final HashMap<String, ItemGroup> itemGroupLookup = new HashMap<>();
    private final HashMap<String, NestedItemGroup> nestedItemGroupLookup = new HashMap<>();
    private final HashMap<String, SubItemGroup> subItemGroupLookup = new HashMap<>();

    public Registry(@Nonnull SlimeCustomizer plugin) {
        this.plugin = plugin;
    }

    public void addItemGroup(@Nonnull String key, @Nonnull ItemGroup itemGroup) {
        itemGroupLookup.put(key, itemGroup);
    }

    public void addNestedItemGroup(@Nonnull String key, @Nonnull NestedItemGroup itemGroup) {
        nestedItemGroupLookup.put(key, itemGroup);
    }

    public void addSubItemGroup(@Nonnull String key, @Nonnull SubItemGroup itemGroup) {
        subItemGroupLookup.put(key, itemGroup);
    }

    public boolean hasItemGroup(@Nonnull String key) {
        return itemGroupLookup.containsKey(key);
    }

    public boolean hasNestedItemGroup(@Nonnull String key) {
        return nestedItemGroupLookup.containsKey(key);
    }

    public boolean hasSubItemGroup(@Nonnull String key) {
        return subItemGroupLookup.containsKey(key);
    }

    public boolean isAnyItemGroup(@Nonnull String key) {
        return hasItemGroup(key) || hasNestedItemGroup(key) || hasSubItemGroup(key);
    }

    @Nonnull
    public ItemGroup getItemGroup(@Nonnull String key) {
        return itemGroupLookup.get(key);
    }

    @Nonnull
    public NestedItemGroup getNestedItemGroup(@Nonnull String key) {
        return nestedItemGroupLookup.get(key);
    }

    @Nonnull
    public SubItemGroup getSubItemGroup(@Nonnull String key) {
        return subItemGroupLookup.get(key);
    }

    @Nullable
    public ItemGroup getItemGroupForItem(@Nonnull String key) {
        if (hasItemGroup(key)) {
            return getItemGroup(key);
        } else if (hasSubItemGroup(key)) {
            return getSubItemGroup(key);
        } else {
            return null;
        }
    }
}
