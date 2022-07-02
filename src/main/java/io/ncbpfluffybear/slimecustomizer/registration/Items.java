package io.ncbpfluffybear.slimecustomizer.registration;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.ncbpfluffybear.slimecustomizer.SlimeCustomizer;
import io.ncbpfluffybear.slimecustomizer.Utils;
import io.ncbpfluffybear.slimecustomizer.objects.CustomSCItem;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.ncbpfluffybear.slimecustomizer.objects.NPCustomSCItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

/**
 * {@link Items} registers the items
 * in the items config file.
 *
 * @author NCBPFluffyBear
 */
public class Items {

    public static boolean register(Config items) {
        for (String itemKey : items.getKeys()) {
            if (itemKey.equals("EXAMPLE_ITEM")) {
                SlimeCustomizer.getInstance().getLogger().log(Level.WARNING, "items.yml 仍包含示例物品! " +
                    "你是不是忘记配置了?");
            }

            // Update config for new "placeable" option
            Utils.updatePlaceableOption(items, itemKey);

            ItemGroup category = Utils.getCategory(items.getString(itemKey + ".category"), itemKey);
            if (category == null) {
                return false;
            }

            String itemType = items.getString(itemKey + ".item-type");
            String materialString = items.getString(itemKey + ".item-id");
            SlimefunItemStack tempStack;
            ItemStack item = null;
            int amount = items.getOrSetDefault(itemKey + ".item-amount", 1);
            boolean placeable = items.getBoolean(itemKey + ".placeable");

            if (itemType == null) {
                Utils.disable(itemKey + "未设置 item-type!");
                return false;
            }
            if (materialString == null) {
                Utils.disable(itemKey + "未设置 item-id!");
                return false;
            }
            if (amount < 1) {
                Utils.disable(itemKey + "的 item-amount 必须为正整数!");
                return false;
            }
            materialString = materialString.toUpperCase(Locale.ROOT);

            if (itemType.equalsIgnoreCase("CUSTOM")) {
                Material material = Material.getMaterial(materialString);

                /* Item material type */
                if (material == null && !materialString.startsWith("SKULL")) {
                    Utils.disable(itemKey + "的 item-id 无效!");
                    return false;
                } else if (material != null) {
                    item = new ItemStack(material);
                } else if (materialString.startsWith("SKULL")) {
                    item = SlimefunUtils.getCustomHead(materialString.replace("SKULL", ""));
                }

                item.setAmount(amount);

                // Building lore
                List<String> itemLore = Utils.colorList(items.getStringList(itemKey + ".item-lore"));

                tempStack = new SlimefunItemStack(itemKey, item, items.getString(itemKey + ".item-name"));

                // Adding lore
                ItemMeta tempMeta = tempStack.getItemMeta();
                tempMeta.setLore(itemLore);
                tempStack.setItemMeta(tempMeta);
            } else if (itemType.equalsIgnoreCase("SAVEDITEM")) {
                item = Utils.retrieveSavedItem(materialString, amount, true);
                if (item == null) {return false;}

                tempStack = new SlimefunItemStack(itemKey, item);
            } else {
                Utils.disable(itemKey + "的 item-type 只能为 CUSTOM 或 SAVEDITEM!");
                return false;
            }

            String recipeTypeString = items.getString(itemKey + ".crafting-recipe-type");
            RecipeType recipeType = Utils.getRecipeType(recipeTypeString, itemKey);
            if (recipeType == null) {
                Utils.disable(itemKey + "的 crafting-recipe-type 无效! 请查阅wiki.");
                return false;
            }

            /* Crafting recipe */
            ItemStack[] recipe = Utils.buildCraftingRecipe(items, itemKey, recipeType);
            if (recipe == null) {return false;}

            if (placeable) {
                if (itemType.equalsIgnoreCase("CUSTOM")) {
                    new CustomSCItem(category, tempStack, recipeType, recipe
                    ).register(SlimeCustomizer.getInstance());
                } else {
                    new CustomSCItem(category, tempStack, recipeType, recipe, item
                    ).register(SlimeCustomizer.getInstance());
                }
            } else {
                if (itemType.equalsIgnoreCase("CUSTOM")) {
                    new NPCustomSCItem(category, tempStack, recipeType, recipe
                    ).register(SlimeCustomizer.getInstance());
                } else {
                    new NPCustomSCItem(category, tempStack, recipeType, recipe, item
                    ).register(SlimeCustomizer.getInstance());
                }
            }

            Utils.notify("已注册物品 " + itemKey + "!");
        }

        return true;
    }

}
