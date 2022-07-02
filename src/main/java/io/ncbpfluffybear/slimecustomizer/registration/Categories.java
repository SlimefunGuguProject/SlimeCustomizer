package io.ncbpfluffybear.slimecustomizer.registration;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.ncbpfluffybear.slimecustomizer.SlimeCustomizer;
import io.ncbpfluffybear.slimecustomizer.Utils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@link Categories} registers the categories
 * in the categories config file.
 *
 * @author NCBPFluffyBear
 */
public class Categories {

    public static boolean register(Config categories) {
        if (categories.getKeys().isEmpty()) {
            Utils.disable("没有任何分类！请在 categories.yml 中至少添加一个分类。");
            return false;
        }

        for (String categoryKey : categories.getKeys()) {
            String name = categories.getString(categoryKey + ".category-name");
            String materialString = categories.getString(categoryKey + ".category-item");
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

            ItemGroup tempCategory = new ItemGroup(new NamespacedKey(SlimeCustomizer.getInstance(), categoryKey),
                new CustomItemStack(item, name));

            AtomicBoolean disable = new AtomicBoolean(false);
            SlimeCustomizer.allCategories.forEach((key, storedCategory) -> {
                if (key.equalsIgnoreCase(categoryKey)) {
                    Utils.disable("分类 " + categoryKey + " 已被注册！你似乎设置了重复的分类ID？");
                    disable.set(true);
                }
            });
            if (disable.get()) {
                return false;
            }

            SlimeCustomizer.allCategories.put(categoryKey, tempCategory);
            Utils.notify("已注册分类 " + categoryKey + "!");

        }

        return true;
    }

}
