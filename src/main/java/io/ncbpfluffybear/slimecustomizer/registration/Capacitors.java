package io.ncbpfluffybear.slimecustomizer.registration;

import dev.j3fftw.extrautils.utils.LoreBuilderDynamic;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.Capacitor;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import io.ncbpfluffybear.slimecustomizer.SlimeCustomizer;
import io.ncbpfluffybear.slimecustomizer.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Capacitors {

    public static boolean register(Config capacitors) {
        for (String capacitorKey : capacitors.getKeys()) {
            if (capacitorKey.equals("EXAMPLE_CAPACITOR")) {
                SlimeCustomizer.getInstance().getLogger().log(Level.WARNING, "capacitors.yml 仍包含示例电容! " +
                    "你是不是忘记配置了?");
            }

            ItemGroup category = Utils.getCategory(capacitors.getString(capacitorKey + ".category"), capacitorKey);
            if (category == null) {
                return false;
            }

            String blockType = capacitors.getString(capacitorKey + ".block-type");
            SlimefunItemStack tempStack;
            ItemStack block = null;
            int amount = capacitors.getOrSetDefault(capacitorKey + ".item-amount", 1);

            if (blockType == null) {
                Utils.disable(capacitorKey + "未设置 block-type!");
                return false;
            }
            if (amount < 1) {
                Utils.disable(capacitorKey + "的 item-amount 必须为正整数!");
                return false;
            }

            int capacity;
            try {
                capacity = Integer.parseInt(capacitors.getString(capacitorKey + ".capacity"));
            } catch (NumberFormatException ex) {
                Utils.disable(capacitorKey + "的 capacity 必须为正整数!");
                return false;
            }

            if (capacity < 1) {
                Utils.disable(capacitorKey + "的 capacity 必须为正整数!");
                return false;
            }

            Material material = Material.getMaterial(blockType.toUpperCase());

            /* Item material type */
            if ((material == null || !material.isBlock()) && !blockType.equalsIgnoreCase("default")) {
                Utils.disable(capacitorKey + "的 block-type 必须为方块或default!");
                return false;
            } else if (material != null && material.isBlock()) {
                block = new ItemStack(material);
                block.setAmount(amount);
            }

            // Building lore
            List<String> itemLore = Utils.colorList(Stream.concat(
                capacitors.getStringList(capacitorKey + ".capacitor-lore").stream(),
                Stream.of(
                    "",
                    "&e电容",
                    LoreBuilderDynamic.powerBuffer(capacity)
                )
            ).collect(Collectors.toList()));

            if (blockType.equalsIgnoreCase("default")) {
                tempStack = new SlimefunItemStack(capacitorKey, HeadTexture.CAPACITOR_25, capacitors.getString(capacitorKey + ".capacitor-name"));
            } else {
                tempStack = new SlimefunItemStack(capacitorKey, block, capacitors.getString(capacitorKey + ".capacitor-name"));
            }

            // Adding lore
            ItemMeta tempMeta = tempStack.getItemMeta();
            tempMeta.setLore(itemLore);
            tempStack.setItemMeta(tempMeta);

            String recipeTypeString = capacitors.getString(capacitorKey + ".crafting-recipe-type");
            RecipeType recipeType = Utils.getRecipeType(recipeTypeString, capacitorKey);
            if (recipeType == null) {
                Utils.disable(capacitorKey + "的 crafting-recipe-type 无效! 请查阅wiki.");
                return false;
            }

            /* Crafting recipe */
            ItemStack[] recipe = Utils.buildCraftingRecipe(capacitors, capacitorKey, recipeType);
            if (recipe == null) {return false;}

            new Capacitor(category, capacity, tempStack, recipeType, recipe)
                .register(SlimeCustomizer.getInstance());

            Utils.notify("已注册电容 " + capacitorKey + "!");
        }

        return true;
    }
}
