package io.ncbpfluffybear.slimecustomizer.registration;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.NestedItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.groups.SubItemGroup;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.ncbpfluffybear.slimecustomizer.SlimeCustomizer;
import io.ncbpfluffybear.slimecustomizer.Utils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Pattern;

/**
 * {@link Categories} registers the categories
 * in the categories config file.
 *
 * @author NCBPFluffyBear
 */
public class Categories {

    private static final Pattern VALID_KEY = Pattern.compile("[a-z0-9/._-]+");

    public static boolean register(Config categories) {
        if (categories.getKeys().isEmpty()) {
            Utils.disable("没有任何分类！请在 categories.yml 中至少添加一个分类。");
            return false;
        }

        for (String categoryKey : categories.getKeys()) {
            if (!VALID_KEY.matcher(categoryKey).matches()) {
                Utils.disable("分类 " + categoryKey + " 的ID无效，只能使用[a-z0-9._-]。");
                return false;
            }
            String name = categories.getString(categoryKey + ".category-name");
            String materialString = categories.getString(categoryKey + ".category-item");
            String parent = categories.getString(categoryKey + ".parent");
            Material material = Material.getMaterial(materialString);
            ItemStack item = null;

            /* Item material type */
            if ((material == null && !materialString.startsWith("SKULL"))) {
                Utils.disable(categoryKey + " 的 category-item 设置无效!");
                return false;
            } else if (material != null) {
                item = new ItemStack(material);
            } else if (materialString.startsWith("SKULL")) {
                item = SlimefunUtils.getCustomHead(materialString.replace("SKULL", ""));
            }

            if (SlimeCustomizer.getRegistry().isAnyItemGroup(categoryKey)) {
                Utils.disable("分类 " + categoryKey + " 已被注册! 你是不是用了重复的ID?");
                return false;
            }

            if (parent == null) {
                // ItemGroup
                ItemGroup tempCategory = new ItemGroup(new NamespacedKey(SlimeCustomizer.getInstance(), categoryKey),
                    new CustomItemStack(item, name));
                tempCategory.register(SlimeCustomizer.getInstance());
                SlimeCustomizer.getRegistry().addItemGroup(categoryKey, tempCategory);
                Utils.notify("已注册普通分类 " + categoryKey + "!");
            } else if (parent.equalsIgnoreCase("this")) {
                // NestedItemGroup - parent item group
                NestedItemGroup tempCategory = new NestedItemGroup(new NamespacedKey(SlimeCustomizer.getInstance(), categoryKey), new CustomItemStack(item, name));
                SlimeCustomizer.getRegistry().addNestedItemGroup(categoryKey, tempCategory);
                Utils.notify("已注册父分类 " + categoryKey + "!");
            } else {
                // SubItemGroup - child item group
                if (!SlimeCustomizer.getRegistry().hasNestedItemGroup(parent)) {
                    Utils.disable("分类 " + categoryKey + " 的父分类 " + parent + "不存在!");
                    return false;
                }
                NestedItemGroup parentCategory = SlimeCustomizer.getRegistry().getNestedItemGroup(parent);
                SubItemGroup tempCategory = new SubItemGroup(new NamespacedKey(SlimeCustomizer.getInstance(), categoryKey), parentCategory, new CustomItemStack(item, name));
                tempCategory.register(SlimeCustomizer.getInstance());
                SlimeCustomizer.getRegistry().addSubItemGroup(categoryKey, tempCategory);
                Utils.notify("已注册分类 " + parent + " 的子分类 " + categoryKey + "!");
            }
        }

        return true;
    }

}
