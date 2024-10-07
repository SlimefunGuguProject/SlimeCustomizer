package io.ncbpfluffybear.slimecustomizer.registration;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.Pair;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.ncbpfluffybear.slimecustomizer.SlimeCustomizer;
import io.ncbpfluffybear.slimecustomizer.Utils;
import io.ncbpfluffybear.slimecustomizer.objects.CustomMachine;
import io.ncbpfluffybear.slimecustomizer.objects.SCMachine;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.logging.Level;

/**
 * {@link Machines} registers the machines
 * in the machines config file.
 *
 * @author NCBPFluffyBear
 */
public class Machines {

    public static boolean register(Config machines) {
        for (String machineKey : machines.getKeys()) {
            if (machineKey.equals("EXAMPLE_MACHINE")) {
                SlimeCustomizer.getInstance().getLogger().log(Level.WARNING, "machines.yml 中仍包含示例机器! " +
                    "你是不是忘记配置了?");
            }

            SCMachine machine = new SCMachine(machines, machineKey, "machine");
            if (!machine.isValid()) {return false;}

            ItemGroup category = Utils.getCategory(machines.getString(machineKey + ".category"), machineKey);
            if (category == null) {return false;}

            // Update to new dual input/output system
            // Utils.updateInputAndOutputFormat(machines, machineKey);

            LinkedHashMap<Pair<ItemStack[], ItemStack[]>, Integer> customRecipe = new LinkedHashMap<>();

            /* Machine recipes */
            for (String recipeKey : machines.getKeys(machineKey + ".recipes")) {
                String path = machineKey + ".recipes." + recipeKey;
                int speed;
                ItemStack input1 = null;
                ItemStack input2 = null;
                ItemStack output1 = null;
                ItemStack output2 = null;

                /* Speed */
                try {
                    speed = Integer.parseInt(machines.getString(path + ".speed-in-seconds"));
                } catch (NumberFormatException e) {
                    Utils.disable(machineKey + "的机器配方 " + recipeKey + " 的 speed-in-seconds 必须为正整数!");
                    return false;
                }

                if (speed < 0) {
                    Utils.disable(machineKey + "的机器配方 " + recipeKey + " 的 speed-in-seconds 必须为正整数!");
                    return false;
                }

                for (int i = 0; i < 2; i++) {

                    // Run this 2 times for input/output
                    String slot;
                    if (i == 0) {
                        slot = "input";
                    } else {
                        slot = "output";
                    }

                    for (int transIndex = 1; transIndex < 3; transIndex++) {

                        String type = machines.getString(path + "." + slot + "." + transIndex + ".type");
                        String material = machines.getString(path + "." + slot + "." + transIndex + ".id");

                        if (type == null) {
                            Utils.disable(machineKey + "的配方 " + recipeKey + " 的第" + transIndex
                                + "个" + slot + "物品的类型未填写!");
                            return false;
                        }
                        if (material == null) {
                            Utils.disable(machineKey + "的配方 " + recipeKey + " 的第" + transIndex
                                + "个" + slot + "物品的ID未填写!");
                            return false;
                        } else {
                            material = material.toUpperCase();
                        }

                        int amount;

                        /* Validate amount */
                        try {
                            amount = Integer.parseInt(machines.getString(path + "." + slot + "." + transIndex + ".amount"));
                        } catch (NumberFormatException e) {
                            Utils.disable(machineKey + "的配方 " + recipeKey + " 的第" + transIndex
                                + "个" + slot + "物品的数量必须为正整数!");
                            return false;
                        }

                        if (amount < 0) {
                            Utils.disable(machineKey + "的配方 " + recipeKey + " 的第" + transIndex
                                + "个" + slot + "物品的数量必须为正整数!");
                            return false;
                        }

                        if (type.equalsIgnoreCase("VANILLA")) {
                            Material vanillaMat = Material.getMaterial(material);
                            if (vanillaMat == null) {
                                Utils.disable(machineKey + "的配方 " + recipeKey + " 的第" + transIndex
                                    + "个" + slot + "物品不是有效的原版物品!");
                                return false;
                            } else {
                                if (i == 0) {
                                    if (transIndex == 1) {
                                        input1 = new ItemStack(vanillaMat);
                                        input1.setAmount(amount);
                                        if (!Utils.checkFitsStackSize(input1, slot, machineKey, recipeKey)) {return false;}
                                    } else {
                                        input2 = new ItemStack(vanillaMat);
                                        input2.setAmount(amount);
                                        if (!Utils.checkFitsStackSize(input2, slot, machineKey, recipeKey)) {return false;}
                                    }

                                } else {
                                    if (transIndex == 1) {
                                        output1 = new ItemStack(vanillaMat);
                                        output1.setAmount(amount);
                                        if (!Utils.checkFitsStackSize(output1, slot, machineKey, recipeKey)) {return false;}
                                    } else {
                                        output2 = new ItemStack(vanillaMat);
                                        output2.setAmount(amount);
                                        if (!Utils.checkFitsStackSize(output2, slot, machineKey, recipeKey)) {return false;}
                                    }
                                }
                            }
                        } else if (type.equalsIgnoreCase("SLIMEFUN")) {
                            SlimefunItem sfMat = SlimefunItem.getById(material);
                            if (sfMat == null) {
                                Utils.disable(machineKey + "的配方 " + recipeKey + " 的第" + transIndex
                                    + "个" + slot + "物品不是有效的粘液科技物品!");
                            } else {
                                if (i == 0) {
                                    if (transIndex == 1) {
                                        input1 = sfMat.getItem().clone();
                                        input1.setAmount(amount);
                                        if (!Utils.checkFitsStackSize(input1, slot, machineKey, recipeKey)) {return false;}
                                    } else {
                                        input2 = sfMat.getItem().clone();
                                        input2.setAmount(amount);
                                        if (!Utils.checkFitsStackSize(input2, slot, machineKey, recipeKey)) {return false;}
                                    }

                                } else {
                                    if (transIndex == 1) {
                                        output1 = sfMat.getItem().clone();
                                        output1.setAmount(amount);
                                        if (!Utils.checkFitsStackSize(output1, slot, machineKey, recipeKey)) {return false;}
                                    } else {
                                        output2 = sfMat.getItem().clone();
                                        output2.setAmount(amount);
                                        if (!Utils.checkFitsStackSize(output2, slot, machineKey, recipeKey)) {return false;}
                                    }
                                }
                            }
                        } else if (type.equalsIgnoreCase("SAVEDITEM")) {
                            ItemStack savedItem = Utils.retrieveSavedItem(material, amount, true);
                            if (savedItem == null) {return false;}
                            if (i == 0) {
                                if (transIndex == 1) {
                                    input1 = savedItem.clone();
                                    if (!Utils.checkFitsStackSize(input1, slot, machineKey, recipeKey)) {return false;}
                                } else {
                                    input2 = savedItem.clone();
                                    if (!Utils.checkFitsStackSize(input2, slot, machineKey, recipeKey)) {return false;}
                                }

                            } else {
                                if (transIndex == 1) {
                                    output1 = savedItem.clone();
                                    output1.setAmount(amount);
                                    if (!Utils.checkFitsStackSize(output1, slot, machineKey, recipeKey)) {return false;}
                                } else {
                                    output2 = savedItem.clone();
                                    output2.setAmount(amount);
                                    if (!Utils.checkFitsStackSize(output2, slot, machineKey, recipeKey)) {return false;}
                                }
                            }
                        } else if (!(type.equalsIgnoreCase("NONE") && transIndex == 2)) {
                            Utils.disable(machineKey + "的配方 " + recipeKey + " 的第一个" + slot + "物品类型只能为 VANILLA, SLIMEFUN, 或 SAVEDITEM, 第二个" + slot + "物品类型只能为 VANILLA, SLIMEFUN, SAVEDITEM 或 NONE!");
                        }
                    }
                }

                // Machines can not process null as a recipe

                ItemStack[] inputs;
                ItemStack[] outputs;

                if (input2 == null) {
                    inputs = new ItemStack[] {input1};
                } else {
                    inputs = new ItemStack[] {input1, input2};
                }

                if (output2 == null) {
                    outputs = new ItemStack[] {output1};
                } else {
                    outputs = new ItemStack[] {output1, output2};
                }
                customRecipe.put(new Pair<>(inputs, outputs), speed);

            }

            new CustomMachine(category, machine.getMachineStack(),
                machine.getRecipeType(),
                machine.getRecipe(),
                machineKey, machine.getProgressItem(), machine.getEnergyConsumption(), machine.getEnergyBuffer(),
                customRecipe).register(SlimeCustomizer.getInstance());

            Utils.notify("已注册机器 " + machineKey + "!");

        }

        return true;
    }

}
