package io.ncbpfluffybear.slimecustomizer.registration;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.ncbpfluffybear.slimecustomizer.SlimeCustomizer;
import io.ncbpfluffybear.slimecustomizer.Utils;
import io.ncbpfluffybear.slimecustomizer.objects.SCGeoResource;
import io.ncbpfluffybear.slimecustomizer.objects.SCMobDrop;
import net.guizhanss.guizhanlib.minecraft.helper.entity.EntityTypeHelper;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * {@link GeoResources} registers the geo resources.
 *
 * @author ybw0014
 */
public class GeoResources {

    public static boolean register(Config geoResources) {
        for (String geoKey : geoResources.getKeys()) {
            if (geoKey.equals("EXAMPLE_GEO")) {
                SlimeCustomizer.getInstance().getLogger().log(Level.WARNING, "geo-resources.yml 文件中仍包含示例配置! " +
                    "你是不是忘记配置了?");
            }

            String itemType = geoResources.getString(geoKey + ".item-type");

            ItemGroup category = Utils.getCategory(geoResources.getString(geoKey + ".category"), geoKey);
            if (category == null) {return false;}

            String itemId = geoResources.getString(geoKey + ".item-id");
            SlimefunItemStack tempStack;
            ItemStack item = null;
            int maxDeviation = geoResources.getOrSetDefault(geoKey + ".max-deviation", 0);

            if (itemType == null) {
                Utils.disable(geoKey + "未设置 item-type!");
                return false;
            }
            if (itemId == null) {
                Utils.disable(geoKey + "未设置 item-id!");
                return false;
            }
            if (maxDeviation < 0) {
                Utils.disable(geoKey + "的 max-deviation 不能小于0!");
                return false;
            }

            if (itemType.equalsIgnoreCase("CUSTOM")) {

                Material material = Material.getMaterial(itemId);

                /* Item material type */
                if (material == null && !itemId.startsWith("SKULL")) {
                    Utils.disable(geoKey + "的 item-id 无效!");
                    return false;
                } else if (material != null) {
                    item = new ItemStack(material);
                } else if (itemId.startsWith("SKULL")) {
                    item = SlimefunUtils.getCustomHead(itemId.replace("SKULL", ""));
                }

                // Building lore
                List<String> itemLore = Utils.colorList(geoResources.getStringList(geoKey + ".item-lore"));

                tempStack = new SlimefunItemStack(geoKey, item, geoResources.getString(geoKey + ".item-name"));

                // Adding lore
                ItemMeta tempMeta = tempStack.getItemMeta();
                tempMeta.setLore(itemLore);
                tempStack.setItemMeta(tempMeta);
            } else if (itemType.equalsIgnoreCase("SAVEDITEM")) {
                item = Utils.retrieveSavedItem(itemId, 1, true);
                if (item == null) {return false;}

                tempStack = new SlimefunItemStack(geoKey, item);
            } else {
                Utils.disable(geoKey + "的 item-type 只能为 CUSTOM 或 SAVEDITEM!");
                return false;
            }

            // Load biome map (not really)
            Map<Biome, Integer> biomeMap = new HashMap<>();
            Map<World.Environment, Integer> environmentMap = new HashMap<>();
            ConfigurationSection biomes = geoResources.getConfiguration().getConfigurationSection(geoKey + ".biome");
            ConfigurationSection environments = geoResources.getConfiguration().getConfigurationSection(geoKey + ".environment");
            if (biomes == null && environments == null) {
                Utils.disable(geoKey + "未进行生物群系或世界类型配置!");
                return false;
            }

            if (biomes != null) {
                for (String biomeKey : biomes.getKeys(false)) {
                    Biome biome;
                    int amount;

                    try {
                        biome = Biome.valueOf(biomeKey);
                    } catch (IllegalArgumentException ex) {
                        Utils.disable(geoKey + "的生物群系 " + biomeKey + " 不是有效的生物群系!");
                        return false;
                    }

                    try {
                        amount = Integer.parseInt(biomes.getString(biomeKey));
                    } catch (NumberFormatException ex) {
                        Utils.disable(geoKey + "的生物群系 " + biomeKey + " 的数量无效!");
                        return false;
                    }

                    if (amount < 0) {
                        Utils.disable(geoKey + "的生物群系 " + biomeKey + " 的数量不能是负数!");
                        return false;
                    }

                    biomeMap.put(biome, amount);
                }
            }

            if (environments != null) {
                for (String environmentKey : environments.getKeys(false)) {
                    World.Environment environment;
                    int amount;

                    try {
                        environment = World.Environment.valueOf(environmentKey);
                    } catch (IllegalArgumentException ex) {
                        Utils.disable(geoKey + "的世界类型 " + environmentKey + " 不是有效的世界类型!");
                        return false;
                    }

                    try {
                        amount = Integer.parseInt(environments.getString(environmentKey));
                    } catch (NumberFormatException ex) {
                        Utils.disable(geoKey + "的世界类型 " + environmentKey + " 的数量无效!");
                        return false;
                    }

                    if (amount < 0) {
                        Utils.disable(geoKey + "的世界类型 " + environmentKey + " 的数量不能是负数!");
                        return false;
                    }

                    environmentMap.put(environment, amount);
                }
            }

            if (itemType.equalsIgnoreCase("CUSTOM")) {
                new SCGeoResource(category, tempStack, maxDeviation, biomeMap, environmentMap
                ).registerGeo(SlimeCustomizer.getInstance());
            } else {
                new SCGeoResource(category, tempStack, item, maxDeviation, biomeMap, environmentMap
                ).registerGeo(SlimeCustomizer.getInstance());
            }

            Utils.notify("已注册GEO资源 " + geoKey + "!");
        }

        return true;
    }

}
