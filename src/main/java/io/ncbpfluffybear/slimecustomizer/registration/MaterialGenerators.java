package io.ncbpfluffybear.slimecustomizer.registration;

import dev.j3fftw.extrautils.utils.LoreBuilderDynamic;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.Capacitor;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import io.ncbpfluffybear.slimecustomizer.SlimeCustomizer;
import io.ncbpfluffybear.slimecustomizer.Utils;
import io.ncbpfluffybear.slimecustomizer.objects.CustomMaterialGenerator;
import net.guizhanss.slimecustomizer.utils.NumberUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MaterialGenerators {

    public static boolean register(Config materialGenerators) {
        for (String genKey : materialGenerators.getKeys()) {
            if (genKey.equals("EXAMPLE_MATERIAL_GENERATOR")) {
                SlimeCustomizer.getInstance().getLogger().log(Level.WARNING, "material-generators.yml 仍包含示例材料生成器! " +
                    "你是不是忘记配置了?");
            }

            ItemGroup category = Utils.getCategory(materialGenerators.getString(genKey + ".category"), genKey);
            if (category == null) {
                return false;
            }

            SlimefunItemStack tempStack;
            ItemStack block = null;
            int amount = materialGenerators.getOrSetDefault(genKey + ".item-amount", 1);

            if (amount < 1) {
                Utils.disable(genKey + "的 item-amount 必须为正整数!");
                return false;
            }

            int energyConsumption, energyBuffer, tickRate;
            var cEnergyConsumption = NumberUtils.getConfigInt(
                materialGenerators.getString(genKey + ".stats.energy-consumption"),
                val -> val >= 0
            );
            var cEnergyBuffer = NumberUtils.getConfigInt(
                materialGenerators.getString(genKey + ".stats.energy-buffer"),
                val -> val >= 0
            );
            var cTickRate = NumberUtils.getConfigInt(
                materialGenerators.getString(genKey + ".output.tick-rate"),
                val -> val > 0
            );

            if (cEnergyConsumption.isEmpty()) {
                Utils.disable(genKey + "的 stats.energy-consumption 必须为整数且大于等于0!");
                return false;
            } else {
                energyConsumption = cEnergyConsumption.get();
            }
            if (cEnergyBuffer.isEmpty()) {
                Utils.disable(genKey + "的 stats.energy-buffer 必须为整数且大于等于0!");
                return false;
            } else {
                energyBuffer = cEnergyBuffer.get();
            }
            if (cTickRate.isEmpty()) {
                Utils.disable(genKey + "的 output.tick-rate 必须为正整数!");
                return false;
            } else {
                tickRate = cTickRate.get();
            }

            block = Utils.getBlockFromConfig(genKey, materialGenerators.getString(genKey + ".block-type"));
            if (block == null) {return false;}

            // Building lore
            List<String> itemLore = Utils.colorList(Stream.concat(
                materialGenerators.getStringList(genKey + ".item-lore").stream(),
                Stream.of(
                    "",
                    "&e材料生成器",
                    "&8⇨ &7速度: &b每 " + tickRate + " 粘液刻生成一次",
                    LoreBuilderDynamic.powerBuffer(energyBuffer),
                    LoreBuilderDynamic.powerPerSecond(energyConsumption)
                )
            ).collect(Collectors.toList()));

            tempStack = new SlimefunItemStack(genKey, block, materialGenerators.getString(genKey + ".item-name"));

            // Adding lore
            ItemMeta tempMeta = tempStack.getItemMeta();
            tempMeta.setLore(itemLore);
            tempStack.setItemMeta(tempMeta);

            String recipeTypeString = materialGenerators.getString(genKey + ".crafting-recipe-type");
            RecipeType recipeType = Utils.getRecipeType(recipeTypeString, genKey);
            if (recipeType == null) {
                Utils.disable(genKey + "的 crafting-recipe-type 无效! 请查阅wiki.");
                return false;
            }

            /* Crafting recipe */
            ItemStack[] recipe = Utils.buildCraftingRecipe(materialGenerators, genKey, recipeType);
            if (recipe == null) {return false;}

            int outputAmount = materialGenerators.getOrSetDefault(genKey + ".output.amount", 1);

            if (outputAmount < 1) {
                Utils.disable(genKey + "的 output.amount 必须为正整数!");
                return false;
            }

            ItemStack output;
            String outputType = materialGenerators.getString(genKey + ".output.type");
            String outputMaterial = materialGenerators.getString(genKey + ".output.id");
            if (outputType.equalsIgnoreCase("VANILLA")) {
                Material vanillaMat = Material.getMaterial(outputMaterial);
                if (vanillaMat == null) {
                    Utils.disable(genKey + "的输出物品的 id 不是有效的原版物品ID!");
                    return false;
                } else {
                    output = new ItemStack(vanillaMat, amount);
                }
            } else if (outputType.equalsIgnoreCase("SLIMEFUN")) {
                SlimefunItem sfMat = SlimefunItem.getById(outputMaterial);
                if (sfMat == null) {
                    Utils.disable(genKey + "的输出物品的 id 不是有效的粘液科技物品ID!");
                    return false;
                } else {
                    output = new CustomItemStack(sfMat.getItem().clone(), amount);
                }
            } else if (outputType.equalsIgnoreCase("SAVEDITEM")) {
                output = Utils.retrieveSavedItem(outputMaterial, amount, true);
            } else {
                Utils.disable(genKey + "的输出物品的类型只能为: VANILLA, SLIMEFUN, 或 SAVEDITEM!");
                return false;
            }

            CustomMaterialGenerator matGen = new CustomMaterialGenerator(category, tempStack, recipeType, recipe,
                tickRate, output);
            matGen.setEnergyPerTick(energyConsumption);
            matGen.setEnergyCapacity(energyBuffer);
            matGen.register(SlimeCustomizer.getInstance());

            Utils.notify("已注册材料生成器 " + genKey + "!");
        }

        return true;
    }
}
