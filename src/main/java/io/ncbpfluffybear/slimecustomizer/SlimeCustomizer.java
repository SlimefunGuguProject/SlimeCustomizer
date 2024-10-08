package io.ncbpfluffybear.slimecustomizer;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.collections.Pair;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.libraries.paperlib.PaperLib;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.ncbpfluffybear.slimecustomizer.objects.SCMenu;
import io.ncbpfluffybear.slimecustomizer.objects.WindowsExplorerStringComparator;
import io.ncbpfluffybear.slimecustomizer.registration.Capacitors;
import io.ncbpfluffybear.slimecustomizer.registration.Categories;
import io.ncbpfluffybear.slimecustomizer.registration.Generators;
import io.ncbpfluffybear.slimecustomizer.registration.GeoResources;
import io.ncbpfluffybear.slimecustomizer.registration.Items;
import io.ncbpfluffybear.slimecustomizer.registration.Machines;
import io.ncbpfluffybear.slimecustomizer.registration.MaterialGenerators;
import io.ncbpfluffybear.slimecustomizer.registration.MobDrops;
import io.ncbpfluffybear.slimecustomizer.registration.Researches;
import io.ncbpfluffybear.slimecustomizer.registration.SolarGenerators;
import lombok.SneakyThrows;
import net.guizhanss.minecraft.guizhanlib.updater.GuizhanUpdater;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

/**
 * This used to be a smol boi. Now it has grown.
 *
 * @author NCBPFluffyBear
 */
public class SlimeCustomizer extends JavaPlugin implements SlimefunAddon {

    public static SlimeCustomizer instance;
    public static File itemsFolder;

    @Override
    public void onEnable() {

        instance = this;
        itemsFolder = new File(this.getDataFolder(), "saveditems");

        if (!getServer().getPluginManager().isPluginEnabled("GuizhanLibPlugin")) {
            getLogger().log(Level.SEVERE, "本插件需要 鬼斩前置库插件(GuizhanLibPlugin) 才能运行!");
            getLogger().log(Level.SEVERE, "从此处下载: https://50l.cc/gzlib");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Read something from your config.yml
        Config cfg = new Config(this);

        if (cfg.getBoolean("options.auto-update") && getDescription().getVersion().startsWith("Build")) {
            GuizhanUpdater.start(this, getFile(), "SlimefunGuguProject", "SlimeCustomizer", "master");
        }

        final Metrics metrics = new Metrics(this, 9841);

        /* File generation */
        final File scAddonFile = new File(getInstance().getDataFolder(), "sc-addon.yml");
        copyFile(scAddonFile, "sc-addon");

        final File categoriesFile = new File(getInstance().getDataFolder(), "categories.yml");
        copyFile(categoriesFile, "categories");

        final File mobDropsFile = new File(getInstance().getDataFolder(), "mob-drops.yml");
        copyFile(mobDropsFile, "mob-drops");

        final File geoFile = new File(getInstance().getDataFolder(), "geo-resources.yml");
        copyFile(geoFile, "geo-resources");

        final File itemsFile = new File(getInstance().getDataFolder(), "items.yml");
        copyFile(itemsFile, "items");

        final File capacitorsFile = new File(getInstance().getDataFolder(), "capacitors.yml");
        copyFile(capacitorsFile, "capacitors");

        final File machinesFile = new File(getInstance().getDataFolder(), "machines.yml");
        copyFile(machinesFile, "machines");

        final File generatorsFile = new File(getInstance().getDataFolder(), "generators.yml");
        copyFile(generatorsFile, "generators");

        final File solarGeneratorsFile = new File(getInstance().getDataFolder(), "solar-generators.yml");
        copyFile(solarGeneratorsFile, "solar-generators");

        final File materialGeneratorsFile = new File(getInstance().getDataFolder(), "material-generators.yml");
        copyFile(materialGeneratorsFile, "material-generators");

        final File researchesFile = new File(getInstance().getDataFolder(), "researches.yml");
        copyFile(researchesFile, "researches");

        /*
        final File passiveMachinesFile = new File(getInstance().getDataFolder(), "passive-machines.yml");
        if (!passiveMachinesFile.exists()) {
            try {
                Files.copy(this.getClass().getResourceAsStream("/passive-machines.yml"), passiveMachinesFile.toPath());
            } catch (IOException e) {
                getInstance().getLogger().log(Level.SEVERE, "Failed to copy default passive-machines.yml file", e);
            }
        }

         */

        if (!itemsFolder.exists()) {
            try {
                Files.createDirectory(itemsFolder.toPath());
            } catch (IOException e) {
                getInstance().getLogger().log(Level.SEVERE, "无法创建文件夹: saveditems", e);
            }
        }

        Config scAddon = new Config(this, "sc-addon.yml");
        Config categories = new Config(this, "categories.yml");
        Config mobDrops = new Config(this, "mob-drops.yml");
        Config geoResources = new Config(this, "geo-resources.yml");
        Config items = new Config(this, "items.yml");
        Config capacitors = new Config(this, "capacitors.yml");
        Config machines = new Config(this, "machines.yml");
        Config generators = new Config(this, "generators.yml");
        Config solarGenerators = new Config(this, "solar-generators.yml");
        Config passiveMachines = new Config(this, "passive-machines.yml");
        Config materialGenerators = new Config(this, "material-generators.yml");
        Config researches = new Config(this, "researches.yml");

        this.getCommand("slimecustomizer").setTabCompleter(new SCTabCompleter());

        Bukkit.getConsoleSender().sendMessage("[自定义粘液附属] " + ChatColor.BLUE + "正在初始化自定义粘液附属...");

        int requiredVersion = scAddon.getInt("version.minecraft");
        if (requiredVersion > 0 && PaperLib.getMinecraftVersion() < requiredVersion) {
            Bukkit.getConsoleSender().sendMessage("[自定义粘液附属] " + ChatColor.RED + "你的服务器版本过低! " +
                "该自定义粘液附属配置需要至少 " + ChatColor.YELLOW + "Minecraft 1." + requiredVersion + ChatColor.RED + "!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        boolean dependsValid = true;
        List<String> depends = new ArrayList<>();
        for (String depend : scAddon.getStringList("depend")) {
            if (Bukkit.getPluginManager().isPluginEnabled(depend)) {
                depends.add(ChatColor.GREEN + depend);
            } else {
                dependsValid = false;
                depends.add(ChatColor.RED + depend);
            }
        }
        if (!dependsValid) {
            Bukkit.getConsoleSender().sendMessage("[自定义粘液附属] " + ChatColor.RED + "依赖项检查失败! " +
                "需要以下所有插件启用后才能运行：");
            Bukkit.getConsoleSender().sendMessage("[自定义粘液附属] " + ChatColor.RED + String.join(ChatColor.WHITE + ", ",
                depends));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        if (!Categories.register(categories)) {return;}
        if (!MobDrops.register(mobDrops)) {return;}
        if (!GeoResources.register(geoResources)) {return;}
        if (!Items.register(items)) {return;}
        if (!Capacitors.register(capacitors)) {return;}
        if (!Machines.register(machines)) {return;}
        if (!Generators.register(generators)) {return;}
        if (!SolarGenerators.register(solarGenerators)) {return;}
        if (!MaterialGenerators.register(materialGenerators)) {return;}
        if (!Researches.register(researches)) {return;}

        Bukkit.getPluginManager().registerEvents(new Events(), instance);
    }

    /**
     * 检测所有依赖项是否已启用
     * @param scAddon 配置文件 sc-addon.yml
     * @return 所有依赖项是否已启用
     */
    private boolean checkDepends(Config scAddon) {
        if (!scAddon.contains("depend")) {
            return true;
        }
        for (String pluginName : scAddon.getStringList("depend")) {
            if (!Bukkit.getPluginManager().isPluginEnabled(pluginName)) {
                return false;
            }
        }
        return true;
    }

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args[0].equals("saveitem")) {
            Player p = (Player) sender;
            if (!Utils.checkPermission(p, "slimecustomizer.admin")) {
                return true;
            }
            int id = 0;
            File itemFile = new File(getInstance().getDataFolder().getPath() + "/saveditems", id + ".yml");
            while (itemFile.exists()) {
                id++;
                itemFile = new File(getInstance().getDataFolder().getPath() + "/saveditems", id + ".yml");
            }

            if (!itemFile.createNewFile()) {
                getInstance().getLogger().log(Level.SEVERE, "无法创建该物品的配置: " + id);
            }

            Config itemFileConfig = new Config(this, "saveditems/" + id + ".yml");
            itemFileConfig.setValue("item", p.getInventory().getItemInMainHand());
            itemFileConfig.save();
            Utils.send(p, "&e你的物品已保存于 " + itemFile.getPath() + ". 请参考 " +
                "&9" + Links.USING_CUSTOM_ITEMS);

        } else if (args[0].equals("give") && args.length > 2) {
            if (sender instanceof Player && !Utils.checkPermission((Player) sender, "slimecustomizer.admin")) {
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                Utils.send(sender, "&c玩家未在线!");
                return true;
            }

            SlimefunItem sfItem = SlimefunItem.getById(args[2].toUpperCase());
            if (sfItem == null) {
                Utils.send(sender, "&c该Slimefun物品无效!");
                return true;
            }

            int amount;

            if (args[3] != null) {

                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException ignored) {
                    amount = 1;
                }
            } else {
                amount = 1;
            }

            giveItems(sender, target, sfItem, amount);

        } else if (args[0].equals("getsaveditem") && args.length > 1) {

            if (sender instanceof Player && !Utils.checkPermission((Player) sender, "slimecustomizer.admin")) {
                return true;
            }

            if (args[1].equals("gui")) {

                if (!(sender instanceof Player)) {
                    Utils.send(sender, "&4这个命令只能在游戏中执行");
                    return true;
                }

                Player p = (Player) sender;
                List<Pair<String, ItemStack>> items = new ArrayList<>();
                items.add(new Pair<>(null, null));

                String[] fileNames = itemsFolder.list();
                if (fileNames != null) {
                    for (int i = 0; i < fileNames.length; i++) {
                        fileNames[i] = fileNames[i].replace(".yml", "");
                    }

                    Arrays.sort(fileNames, new WindowsExplorerStringComparator());

                    for (String id : fileNames) {
                        items.add(new Pair<>(id, Utils.retrieveSavedItem(id, 1, false)));
                    }

                    int page = 1;
                    SCMenu menu = new SCMenu("&a&l已保存物品");
                    menu.setSize(54);
                    populateMenu(menu, items, page, p);
                    menu.setPlayerInventoryClickable(false);
                    menu.setBackgroundNonClickable(false);
                    menu.open(p);

                }

            } else {
                if (args.length < 4) {
                    Utils.send(sender, "&c/sc getsaveditem gui | <item_id> <player_name> <amount>");
                    return true;
                }

                String savedID = args[1];

                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) {
                    Utils.send(sender, "&c玩家未在线!");
                    return true;
                }

                int amount;

                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException ignored) {
                    amount = 1;
                }
                
                ItemStack item = Utils.retrieveSavedItem(savedID, amount, false);
                if (item != null) {
                    HashMap<Integer, ItemStack> leftovers = target.getInventory().addItem(item);
                    for (ItemStack leftover : leftovers.values()) {
                        target.getWorld().dropItem(target.getLocation(), leftover);
                    }

                    Utils.send(sender, "&b你已经给予 " + target.getName() + " &a" + amount + " &b个 &7\"&a" +
                            savedID + "&7\"");
                } else {
                    Utils.send(sender, "&c保存物品未找到!");
                }
            }
        } else if (sender instanceof Player && args[0].equals("categories")) {
            Player p = (Player) sender;
            if (!Utils.checkPermission(p, "slimecustomizer.admin")) {
                return true;
            }

            SCMenu menu = new SCMenu("&6分类与ID列表");
            menu.setSize(54);

            populateCategoryMenu(menu, Slimefun.getRegistry().getAllItemGroups(), 1, p);

            menu.setPlayerInventoryClickable(false);
            menu.setBackgroundNonClickable(true);
            menu.open(p);
        } else {
            Utils.send(sender, "&e所有指令可在此查看 &9" + Links.COMMANDS);
        }

        return true;
    }

    /**
     * Populates the saveditem gui. 45 items per page.
     * @param menu the SCMenu to populate
     * @param items the List of items
     * @param page the page number
     * @param p the player that will be viewing this menu
     */
    private void populateMenu(SCMenu menu, List<Pair<String, ItemStack>> items, int page, Player p) {
        for (int i = 45; i < 54; i++) {
            menu.replaceExistingItem(i, ChestMenuUtils.getBackground());
        }

        menu.wipe(0, 44, true);

        for (int i = 0; i < 45; i++) {
            int itemIndex = i + 1 + (page - 1) * 45;
            ItemStack item = getItemOrNull(items, itemIndex);
            if (item != null) {
                ItemMeta im = item.getItemMeta();
                if (im == null) {
                    Utils.notify("这个物品没有元数据! 貌似出了点问题? " + items.get(itemIndex).getFirstValue());
                    continue;
                }
                List<String> lore = im.getLore();

                if (lore == null) {
                    lore = new ArrayList<>(Arrays.asList("", Utils.color("&bID: " + items.get(itemIndex).getFirstValue()),
                        Utils.color("&a> 单击获取该物品")));
                } else {
                    lore.addAll(new ArrayList<>(Arrays.asList("", Utils.color("&bID: " + items.get(itemIndex).getFirstValue()),
                        Utils.color("&a> 单击获取该物品"))));
                }

                im.setLore(lore);
                item.setItemMeta(im);
                menu.replaceExistingItem(i, item);
                menu.addMenuClickHandler(i, (pl, s, is, action) -> {
                    HashMap<Integer, ItemStack> leftovers = p.getInventory().addItem(getItemOrNull(items, itemIndex));
                    for (ItemStack leftover : leftovers.values()) {
                        p.getWorld().dropItem(p.getLocation(), leftover);
                    }
                    return false;
                });
            }
        }

        if (page != 1) {
            menu.replaceExistingItem(46, new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "&a上一页"));
            menu.addMenuClickHandler(46, (pl, s, is, action) -> {
                populateMenu(menu, items, page - 1, p);
                return false;
            });
        }

        if (getItemOrNull(items, 45 * page) != null) {
            menu.replaceExistingItem(52, new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "&a下一页"));
            menu.addMenuClickHandler(52, (pl, s, is, action) -> {
                populateMenu(menu, items, page + 1, p);
                return false;
            });
        }

    }

    /**
     * Populates the category gui. 45 items per page.
     * @param menu the SCMenu to populate
     * @param groups the List of itemgroups
     * @param page the page number
     * @param p the player that will be viewing this menu
     */
    private void populateCategoryMenu(SCMenu menu, List<ItemGroup> groups, int page, Player p) {
        for (int i = 45; i < 54; i++) {
            menu.replaceExistingItem(i, ChestMenuUtils.getBackground());
        }

        menu.wipe(0, 44, true);

        for (int i = 0; i < 45; i++) {
            int groupIndex = i + 1 + (page - 1) * 45;
            ItemGroup group = getItemGroupOrNull(groups, groupIndex);
            if (group != null) {
                ItemStack catItem = group.getItem(p).clone();
                ItemMeta catMeta = catItem.getItemMeta();
                List<String> catLore = catMeta.getLore();

                catLore.set(catLore.size() - 1, Utils.color(
                    "&6ID: " + group.getKey().getNamespace() + ":" + group.getKey().getKey())
                ); // Replaces the "Click to Open" line
                catMeta.setLore(catLore);
                catItem.setItemMeta(catMeta);
                menu.replaceExistingItem(i, catItem);
                menu.addMenuClickHandler(i, ChestMenuUtils.getEmptyClickHandler());
            }
        }

        if (page != 1) {
            menu.replaceExistingItem(46, new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "&a上一页"));
            menu.addMenuClickHandler(46, (pl, s, is, action) -> {
                populateCategoryMenu(menu, groups, page - 1, p);
                return false;
            });
        }

        if (getItemGroupOrNull(groups, 45 * page) != null) {
            menu.replaceExistingItem(52, new CustomItemStack(Material.LIME_STAINED_GLASS_PANE, "&a下一页"));
            menu.addMenuClickHandler(52, (pl, s, is, action) -> {
                populateCategoryMenu(menu, groups, page + 1, p);
                return false;
            });
        }

    }

    private ItemStack getItemOrNull(List<Pair<String, ItemStack>> items, int index) {
        ItemStack item;
        try {
            item = items.get(index).getSecondValue().clone();
        } catch (IndexOutOfBoundsException e) {
            item = null;
        }
        return item;
    }

    private ItemGroup getItemGroupOrNull(List<ItemGroup> groups, int index) {
        ItemGroup group = null;
        try {
            group = groups.get(index);
        } catch (IndexOutOfBoundsException ignored) {}
        return group;
    }

    private void giveItems(CommandSender s, Player p, SlimefunItem sfItem, int amount) {
        p.getInventory().addItem(new CustomItemStack(sfItem.getRecipeOutput(), amount));
        Utils.send(s, "&b你已给予 " + p.getName() + " &a" + amount + "&b个 &7\"&b" + sfItem.getItemName() + "&7\"");
    }

    private void copyFile(File file, String name) {
        if (!file.exists()) {
            try {
                Files.copy(this.getClass().getResourceAsStream("/"+ name + ".yml"), file.toPath());
            } catch (IOException e) {
                getInstance().getLogger().log(Level.SEVERE, "无法加载默认配置文件 " + name + ".yml", e);
            }
        }
    }

    @Override
    public void onDisable() {
        // Logic for disabling the plugin...
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/SlimefunGuguProject/SlimeCustomizer/issues";
    }

    @Override
    @Nonnull
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    public static SlimeCustomizer getInstance() {
        return instance;
    }

}
