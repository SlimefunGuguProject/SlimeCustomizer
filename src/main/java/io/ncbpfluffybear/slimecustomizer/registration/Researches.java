package io.ncbpfluffybear.slimecustomizer.registration;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import io.ncbpfluffybear.slimecustomizer.SlimeCustomizer;
import io.ncbpfluffybear.slimecustomizer.Utils;
import org.bukkit.NamespacedKey;

import java.util.List;

public class Researches {
    public static boolean register(Config researches) {
        if (researches.getKeys().isEmpty()) {
            return true;
        }

        for (String researchKey : researches.getKeys()) {
            int researchId = researches.getInt(researchKey + ".id");
            String name = researches.getString(researchKey + ".name");
            int cost = researches.getInt(researchKey + ".cost");
            List<String> items = researches.getStringList(researchKey + ".items");
            if (researchId <= 0) {
                Utils.disable("研究 " + researchKey + "的 id 必须大于0!");
                return false;
            }
            if (cost <= 0) {
                Utils.disable("研究 " + researchKey + "的 cost 必须大于0!");
                return false;
            }
            if (name == null) {
                Utils.disable("研究 " + researchKey + "的 name 不能为空!");
                return false;
            }

            Research research = new Research(new NamespacedKey(SlimeCustomizer.getInstance(), researchKey),
                researchId, name, cost);
            for (String itemId : items) {
                SlimefunItem sfItem = SlimefunItem.getById(itemId);
                if (sfItem == null) {
                    Utils.disable("研究 " + researchKey + " 的物品 " + itemId + " 不是粘液科技物品!");
                    return false;
                }
                research.addItems(sfItem);
            }

            research.register();

            Utils.notify("已注册研究 " + researchKey + "!");

        }

        return true;
    }
}
