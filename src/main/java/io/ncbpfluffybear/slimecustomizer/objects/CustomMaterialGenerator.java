package io.ncbpfluffybear.slimecustomizer.objects;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import net.guizhanss.guizhanlib.slimefun.machines.AbstractMachineBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class CustomMaterialGenerator extends AbstractMachineBlock implements RecipeDisplayItem {
    private static final int INFO_SLOT = 0;
    private static final int[] OUTPUT_BORDER = {1, 8};
    private static final int[] OUTPUT_SLOTS = {2, 3, 4, 5, 6, 7};

    private final int tickRate;
    private final ItemStack output;

    public CustomMaterialGenerator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType,
                                   ItemStack[] recipe, int tickRate, ItemStack output) {
        super(itemGroup, item, recipeType, recipe);
        this.tickRate = tickRate;
        this.output = output;
    }

    @Override
    protected int getStatusSlot() {
        return INFO_SLOT;
    }

    @Override
    protected ItemStack getNoEnergyItem() {
        return new CustomItemStack(
            Material.RED_STAINED_GLASS_PANE,
            "&c电量不足"
        );
    }

    @Override
    protected void setup(BlockMenuPreset blockMenuPreset) {
        blockMenuPreset.drawBackground(ChestMenuUtils.getOutputSlotTexture(), OUTPUT_BORDER);

        blockMenuPreset.addItem(INFO_SLOT, ChestMenuUtils.getBackground());
        blockMenuPreset.addMenuClickHandler(INFO_SLOT, ChestMenuUtils.getEmptyClickHandler());
    }

    @Override
    protected int[] getInputSlots() {
        return new int[0];
    }

    @Override
    protected int[] getOutputSlots() {
        return OUTPUT_SLOTS;
    }

    @Override
    protected boolean process(Block block, BlockMenu blockMenu) {
        int progress = getProgress(block);

        if (progress >= tickRate) {
            if (blockMenu.fits(output, OUTPUT_SLOTS)) {
                blockMenu.pushItem(output.clone(), OUTPUT_SLOTS);
                progress = 1;
            } else {
                if (blockMenu.hasViewer()) {
                    blockMenu.replaceExistingItem(getStatusSlot(), new CustomItemStack(
                        Material.RED_STAINED_GLASS_PANE,
                        "&c空间不足"
                    ));
                }
                return false;
            }
        } else {
            progress++;
        }

        setProgress(block, progress);
        if (blockMenu.hasViewer()) {
            blockMenu.replaceExistingItem(getStatusSlot(), new CustomItemStack(
                Material.LIME_STAINED_GLASS_PANE,
                "&a生产中"
            ));
        }
        return true;
    }

    private static void setProgress(Block b, int progress) {
        BlockStorage.addBlockInfo(b, "progress", String.valueOf(progress));
    }

    private static int getProgress(Block b) {
        int progress;
        try {
            progress = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "progress"));
        } catch (NumberFormatException ex) {
            progress = 1;
        }
        return progress;
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        final List<ItemStack> items = new ArrayList<>();

        items.add(null);
        items.add(output);

        return items;
    }
}
