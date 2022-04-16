# SlimeCustomizer（自定义粘液附属）
这是一个Slimefun4附属，它可以让您自定义属于自己的附属，操作简单易懂!
<p align="center">
  <a href="https://thebusybiscuit.github.io/builds/NCBPFluffyBear/SlimeCustomizer/master/">
    <img src="https://thebusybiscuit.github.io/builds/NCBPFluffyBear/SlimeCustomizer/master/badge.svg" alt="Build Server"/>
  </a>
</p>

### 展示
*Want to see what SlimeCustomizer can do? Visit `play.royale-mc.com` and take a look at their custom items! All showcase image credits go to Azakaturan.*

![Resources](https://user-images.githubusercontent.com/31554056/110004177-c124dd00-7cdc-11eb-8031-3c1feeec228e.png)\
![Category](https://user-images.githubusercontent.com/31554056/110005314-f2ea7380-7cdd-11eb-9090-36017111cbf1.png)
![Electric Ingot Factory IV](https://user-images.githubusercontent.com/31554056/110005311-f251dd00-7cdd-11eb-905a-55d4a86ff5d0.png)
![Heated Redstone](https://user-images.githubusercontent.com/31554056/110005901-9cca0000-7cde-11eb-846c-2a1bd5b2c900.png)
![Charcoal Kiln](https://user-images.githubusercontent.com/31554056/110005316-f2ea7380-7cdd-11eb-8bd3-d95d50b25be6.png)
![Heated Carbon Press](https://user-images.githubusercontent.com/31554056/110005318-f3830a00-7cdd-11eb-871b-6b9fa733231a.png)

#### 汇报bug
汉化版自定义附属请在此汇报bug [issue](https://github.com/SlimefunGuguProject/SlimeCustomizer).

## 如何使用SlimeCustomizer

##### 安装插件
1. 下载SlimeCustomizer[Slimefun repo server](https://thebusybiscuit.github.io/builds/NCBPFluffyBear/SlimeCustomizer/master/)
2. 拖动jar文件至你服务器的plugins文件,定位至`\<YOUR_SERVER_LOCATION>\plugins`
3. 启动服务器以生成正确的配置文件
4. 再次关闭服务器就可以在配置文件里编辑属于你自己的附属了

##### 添加你的分类
1. 打开 `categories.yml`文件, 定位至 `\<YOUR_SERVER_LOCATION>\plugins\SlimeCustomizer`
下表展示了每块的内容

```yaml
slime_customizer:
  category-name: "&cSlimeCustomizer"
  category-item: REDSTONE_LAMP
```

| 内容 | 描述 |
| -------- | -------- |
| slime_customizer | 这是分类的ID. 如果你要添加不同的分类，你需要更改/额外添加此ID! |
| category-name | 这是显示在粘液科技书里的分类名 |
| category-item | 这里需要填入原版物品ID或者头颅（格式Skull+URL） |

如果你要添加头颅材质并获取头颅的URL
请访问https://minecraft-heads.com/custom-heads


##### 添加你的物品材料
1. 打开`items.yml`文件, 定位至 `\<YOUR_SERVER_LOCATION>\plugins\SlimeCustomizer`
下表展示了每块的内容


```yaml
EXAMPLE_ITEM:
  category: slime_customizer
  item-type: CUSTOM
  item-name: "&bExample Item"
  item-lore:
  - "&7This is an example item!"
  - "&cSlimeCustomizer now supports multiline lore!"
  item-id: STICK
  item-amount: 1
  placeable: false
  crafting-recipe-type: ENHANCED_CRAFTING_TABLE
  crafting-recipe:
    1:
      type: VANILLA
      id: STICK
      amount: 1
    2:
      type: NONE
      id: N/A
      amount: 1
    3:
      type: NONE
      id: N/A
      amount: 1
    4:
      type: VANILLA
      id: STICK
      amount: 1
    5:
      type: NONE
      id: N/A
      amount: 1
    6:
      type: NONE
      id: N/A
      amount: 1
    7:
      type: NONE
      id: N/A
      amount: 1
    8:
      type: NONE
      id: N/A
      amount: 1
    9:
      type: NONE
      id: N/A
      amount: 1
```

| 内容 | 描述 | 有效输入 |
| --- | ----------- | ----------------- |
| EXAMPLE_ITEM | 这是物品的ID.如果你要添加不同的物品，你需要更改/额外添加此ID! |
| category | 此项输入物品所在分类的ID，即你之前创建的分类ID |
| item-type | 这是你物品注册的方式 | CUSTOM (填入此时，你可以自定义物品名称、描述、种类), SAVEDITEM (从saveditems文件里加载物品，下文会讲) |
| item-name | 这是物品的名称 |
| item-lore | 这是物品的描述 |
| item-id | 这里需要填入原版物品ID或者头颅（格式Skull+URL） |
| item-amount | 一次合成该物品时所输出的数量 |
| placeable | 物品是否可放置。不要放置工具等本来就无法放置的物品！ |
| crafting-recipe-type | 制作此物品所用的多块机器 | ENHANCED_CRAFTING_TABLE（强化工作台）, MAGIC_WORKBENCH（魔法工作台）, ARMOR_FORGE（盔甲锻造台）, COMPRESSOR（压缩机）, PRESSURE_CHAMBER（压力舱）, SMELTERY（冶炼炉）, ORE_CRUSHER（矿石粉碎机）, GRIND_STONE（磨石）, NONE (无法被合成) |
| crafting-recipe.#.type | 物品的种类 | NONE (无物品), SLIMEFUN, SAVEDITEM |
| crafting-recipe.#.id | 原版/保存物品的物品ID（保存物品ID为saveditem文件夹里对应已保存物品的文件名（默认为数字）） |
| crafting-recipe.#.amount | 物品的数量 |

*这些物品也可用于您的自定义机器/发电机！*
*所有已注册的自定义物品都可以通过crafting-recipe.#.type为SLIMEFUN下填入你自定义物品的id来引用该自定义物品（可作为配方材料，输入输出等）。 如果你想使用items.yml中的一个物品作为另一个物品的制作材料，那么这个制作材料必须事先注册。(注意材料注册顺序，处在越靠前的材料最先注册，如果一个物品处在另一个物品的上方，但上方的物品需要用到下方的物品作为配方，那么上方的物品需要在游戏里手持该物品，输入/sfc saveitem来保存该物品)*

*Slimefun物品在其元数据中会标记有关键字，以便Slimefun可以识别它们。为了确保您的SAVEDITEM在需要被其他插件识别时不会有冲突，这个标签被移除了。但是自定义物品仍有此标签。因此，在SAVEDITEM上使用`/sf give '可能会干扰其他插件，因为它将被其它插件标记。若要获取该物品的未标记版本，请改用`/sc give'。*

##### 添加你的机器
1. 打开`machines.yml`文件, 定位至`\<YOUR_SERVER_LOCATION>\plugins\SlimeCustomizer`
下表展示了每块的内容

```yaml
EXAMPLE_MACHINE:
  category: slime_customizer
  machine-name: "&bExample Machine"
  machine-lore:
  - "&7This is an example machine!"
  block-type: FURNACE
  progress-bar-item: FLINT_AND_STEEL
  stats:
    energy-consumption: 16
    energy-buffer: 64
  crafting-recipe-type: ENHANCED_CRAFTING_TABLE
  crafting-recipe:
    1:
      type: VANILLA
      id: IRON_BLOCK
      amount: 1
    2:
      type: NONE
      id: N/A
      amount: 1
    3:
      type: VANILLA
      id: IRON_BLOCK
      amount: 1
    4:
      type: VANILLA
      id: IRON_BLOCK
      amount: 1
    5:
      type: SLIMEFUN
      id: SMALL_CAPACITOR
      amount: 1
    6:
      type: VANILLA
      id: IRON_BLOCK
      amount: 1
    7:
      type: VANILLA
      id: IRON_BLOCK
      amount: 1
    8:
      type: VANILLA
      id: IRON_BLOCK
      amount: 1
    9:
      type: VANILLA
      id: IRON_BLOCK
      amount: 1
  recipes:
    1:
      speed-in-seconds: 5
      input:
        1:
          type: VANILLA
          id: IRON_INGOT
          amount: 9
        2:
          type: NONE
          id: N/A
          amount: 1
      output:
        1:
          type: VANILLA
          id: IRON_BLOCK
          amount: 1
        2:
          type: NONE
          id: N/A
          amount: 1
    2:
      speed-in-seconds: 5
      input:
        1:
          type: SLIMEFUN
          id: GOLD_24K
          amount: 9
        2:
          type: NONE
          id: N/A
          amount: 1
      output:
        1:
          type: SLIMEFUN
          id: GOLD_24K_BLOCK
          amount: 1
        2:
          type: NONE
          id: N/A
          amount: 1
```
| 内容 | 描述 | 有效输入 |
| --- | ----------- | ----------------- |
| EXAMPLE_MACHINE | 这是机器的ID.如果你要添加不同的机器，你需要更改/额外添加此ID! |
| category | 此项输入机器所在分类的ID，即你之前创建的分类ID |
| machine-name | 这是机器的名称 |
| machine-lore | 这是机器的描述 |
| block-type | 这里需要填入原版物品ID或者头颅（格式Skull+URL） | 
| progress-bar-item | 这里需要填入原版物品ID，这决定了机器的进度栏物品 |
| stats.energy-consumption | 这台机器每粘液刻消耗的能量，最大为2147483647 |
| stats.energy-buffer | 这台机器可储存的能量，最大为2147483647 |
| crafting-recipe-type | 制作此物品所用的多块机器 | ENHANCED_CRAFTING_TABLE（强化工作台）, MAGIC_WORKBENCH（魔法工作台）, ARMOR_FORGE（盔甲锻造台）, COMPRESSOR（压缩机）, PRESSURE_CHAMBER（压力舱）, SMELTERY（冶炼炉）, ORE_CRUSHER（矿石粉碎机）, GRIND_STONE（磨石）, NONE (无法被合成) |
| crafting-recipe.#.type | 物品种类 | NONE (无物品), VANILLA, SLIMEFUN, SAVEDITEM |
| crafting-recipe.#.id | 原版/粘液科技/保存物品的物品ID |
| crafting-recipe.#.amount | 合成该机器所需物品的数量，高级工作台所需物品数量仅能为1. |
| recipes.#.speed-in-seconds | 输出物品所需时间，最大为2147483647 |
| recipes.#.input/output.#.type | 物品的种类 | NONE (无物品), VANILLA, SLIMEFUN, SAVEDITEM |
| recipes.#.input/output.#.id | 原版/粘液科技/保存物品的物品ID |
| recipes.#.input/output.#.amount | 输入/输出物品数量 |

*提示:查看粘液科技物品ID，可在游戏内手持该粘液科技物品，输入/sf id以查看*
*每台机器只能有两种物品的输入/输出*

##### 添加你的发电机
1. 打开 `generators.yml` 文件,定位至`\<YOUR_SERVER_LOCATION>\plugins\SlimeCustomizer`
下表展示了每块的内容

```yaml
EXAMPLE_GENERATOR:
  category: slime_customizer
  generator-name: "&bExample Generator"
  generator-lore:
  - "&7This is an example generator!"
  block-type: SKULLe707c7f6c3a056a377d4120028405fdd09acfcd5ae804bfde0f653be866afe39
  progress-bar-item: FLINT_AND_STEEL
  stats:
    energy-production: 16
    energy-buffer: 64
  crafting-recipe-type: ENHANCED_CRAFTING_TABLE
  crafting-recipe:
    1:
      type: VANILLA
      id: IRON_BLOCK
      amount: 1
    2:
      type: NONE
      id: N/A
      amount: 1
    3:
      type: VANILLA
      id: IRON_BLOCK
      amount: 1
    4:
      type: VANILLA
      id: IRON_BLOCK
      amount: 1
    5:
      type: SLIMEFUN
      id: COAL_GENERATOR
      amount: 1
    6:
      type: VANILLA
      id: IRON_BLOCK
      amount: 1
    7:
      type: VANILLA
      id: IRON_BLOCK
      amount: 1
    8:
      type: VANILLA
      id: IRON_BLOCK
      amount: 1
    9:
      type: VANILLA
      id: IRON_BLOCK
      amount: 1
  recipes:
    1:
      time-in-seconds: 5
      input:
        type: VANILLA
        id: SPRUCE_SIGN
        amount: 1
      output:
        type: NONE
        id: N/A
        amount: N/A
    2:
      time-in-seconds: 10
      input:
        type: VANILLA
        id: BEDROCK
        amount: 1
      output:
        type: VANILLA
        id: BIRCH_PLANKS
        amount: 1
```
| 内容 | 描述 | 有效输入 |
| --- | ----------- | ----------------- |
| EXAMPLE_GENERATOR | 这是机器的ID.如果你要添加不同的机器，你需要更改/额外添加此ID! |
| category | The key of the category that this item will be under in the Slimefun guide.
| generator-name | The name of the generator. |
| generator-lore | The lore of the generator. |
| block-type | The vanilla ID or skull hash of the material this item will use. | 
| progress-bar-item | The vanilla ID of the progress bar item. |
| stats.energy-production | The amount of energy produced by this generator per Slimefun tick. |
| stats.energy-buffer | The amount of energy that can be stored in this machine. |
| crafting-recipe-type | The multiblock machine that this item will be crafted in. | ENHANCED_CRAFTING_TABLE, MAGIC_WORKBENCH, ARMOR_FORGE, COMPRESSOR, PRESSURE_CHAMBER, SMELTERY, ORE_CRUSHER, GRIND_STONE, NONE (Can not be crafted with multiblocks) |
| crafting-recipe.#.type | The type of item. | NONE (Empty spot, all other fields will be ignored), VANILLA, SLIMEFUN, SAVEDITEM |
| crafting-recipe.#.id | The id of the item based on the type. |
| crafting-recipe.#.amount | The amount of the item to use in the recipe. Enhanced Crafting Table only accepts 1. |
| recipes.#.time-in-seconds | The time it takes for the recipe to complete. |
| recipes.#.input/output.type | The type of item. | NONE (Empty spot, all other fields will be ignored), VANILLA, SLIMEFUN, SAVEDITEM |
| recipes.#.input/output.id | The id of the item based on the type. |
| recipes.#.input/output.amount | The amount of items. |

##### Adding your solar generator
1. Open the `solar-generators.yml` file, located at `\<YOUR_SERVER_LOCATION>\plugins\SlimeCustomizer`
The table below explains what each key does.

```yaml
EXAMPLE_SOLAR_GENERATOR:
  category: slime_customizer
  generator-name: "&bExample Solar Generator"
  generator-lore:
  - "&7This is an example solar generator!"
  block-type: DAYLIGHT_DETECTOR
  stats:
    energy-production:
      day: 256
      night: 128
  crafting-recipe-type: ENHANCED_CRAFTING_TABLE
  crafting-recipe:
    1:
      type: VANILLA
      id: BEDROCK
      amount: 1
    2:
      type: NONE
      id: N/A
      amount: 1
    3:
      type: VANILLA
      id: BEDROCK
      amount: 1
    4:
      type: VANILLA
      id: IRON_BLOCK
      amount: 1
    5:
      type: SLIMEFUN
      id: COAL_GENERATOR
      amount: 1
    6:
      type: VANILLA
      id: IRON_BLOCK
      amount: 1
    7:
      type: VANILLA
      id: IRON_BLOCK
      amount: 1
    8:
      type: VANILLA
      id: IRON_BLOCK
      amount: 1
    9:
      type: VANILLA
      id: IRON_BLOCK
      amount: 1
```
| Key | Description | Acceptable Inputs |
| --- | ----------- | ----------------- |
| EXAMPLE_SOLAR_GENERATOR | The ID of the generator. You can change this key! |
| category | The key of the category that this item will be under in the Slimefun guide.
| generator-name | The name of the generator. |
| generator-lore | The lore of the generator. |
| block-type | The vanilla ID or skull hash of the material this item will use. | 
| stats.energy-production.day | The amount of energy produced by this generator per Slimefun tick during daytime. |
| stats.energy-production.day | The amount of energy produced by this generator per Slimefun tick during nighttime. |
| crafting-recipe-type | The multiblock machine that this item will be crafted in. | ENHANCED_CRAFTING_TABLE, MAGIC_WORKBENCH, ARMOR_FORGE, COMPRESSOR, PRESSURE_CHAMBER, SMELTERY, ORE_CRUSHER, GRIND_STONE, NONE (Can not be crafted with multiblocks) |
| crafting-recipe.#.type | The type of item. | NONE (Empty spot, all other fields will be ignored), VANILLA, SLIMEFUN, SAVEDITEM |
| crafting-recipe.#.id | The id of the item based on the type. |
| crafting-recipe.#.amount | The amount of the item to use in the recipe. Enhanced Crafting Table only accepts 1. |

##### Adding your mob drops
1. Open the `mob-drops.yml` file, located at `\<YOUR_SERVER_LOCATION>\plugins\SlimeCustomizer`
   The table below explains what each key does.

```yaml
#READ THE WIKI BEFORE CREATING AN ITEM! https://github.com/NCBPFluffyBear/SlimeCustomizer/blob/master/README.md
EXAMPLE_DROP:
  category: slime_customizer
  item-type: CUSTOM
  item-name: "&bExample Drop"
  item-lore:
    - "&7This is an example mob-drop!"
    - "&cExample drops are not obtainable"
  item-id: STICK
  item-amount: 1
  mob: GHAST
  chance: 0
  recipe-display-item: GHAST_SPAWN_EGG
```
| Key | Description | Acceptable Inputs |
| --- | ----------- | ----------------- |
| EXAMPLE_DROP | The ID of the mob drop. You can change this key! |
| category | The key of the category that this drop will appear under in the Slimefun guide.
| item-type | The type of item that you are registering. | CUSTOM (You define the name, lore, and type), SAVEDITEM (Load key from saveditems folder) |
| item-name | The name of the item. (Custom item types only) |
| item-lore | The lore of the item. (Custom item types only) |
| item-id | The vanilla ID or skull hash of the material this item will use. |
| item-amount | The amount of this item dropped. |
| mob | The type of mob that drops this item |
| chance | The chance that the specified mob drops the item (0 - 100) |
| recipe-display-item | The item that appears in the Slimefun guide's instructions on how to obtain this drop |

#### Using skull textures
Want to use a skull texture instead of a block? Replace `block-type` with `SKULL<hash>`. Example provided in the generators config.
How to create a skull hash: https://bukkit.org/threads/create-your-own-custom-head-texture.424286/

#### Using custom items
SlimeCustomizer supports custom items! These can be from other plugins or even renamed/relored items!

###### I will be "setting up" an example item along the way.
##### Saving an item
1. Hold the item you want to use in your hand
2. Type `/sc saveitem` (You must have the proper permissions to use this command)

The location of where your item is saved will appear in chat. You can rename this file to anything WITHOUT SPACES. This name will be used in your configs.

###### Example: Hold dirt, type /sc saveitem. Navigate to `\plugins\SlimeCustomizer\saveditems` and rename `0.yml` to `DIRT.yml` 
##### Using your saved item
Your saved item can be used in crafting recipes, machine inputs/outputs, and generator inputs/outputs.
For `type`, use `SAVEDITEM` and for `id`, use the file name.

###### Example:
```yaml
1:
  type: SAVEDITEM
  id: DIRT    
```
#### Important notes
- Shaped multiblock machines (ENHANCED_CRAFTING_TABLE, MAGIC_WORKBENCH, ARMOR_FORGE, PRESSURE_CHAMBER) will only accept recipe inputs with a stack size of 1.
- The speed/time and energy production/consumption that you configure may not line up exactly in game depending in your Slimefun tick delay. The lore is adaptive to show you to correct values according to real time.
- When the `type` for the crafting recipes or machine inputs/outputs is set to NONE, all of the fields below it can be omitted.
- If you are updating SlimeCustomizer and new keys have been added, they may appear in different spots than in the examples provided above. Feel free to move them around.

## Permissions
| **Permission** | **Description** |
| --------------------- | ---------------------------------------- |
| slimecustomizer.admin | Access to SlimeCustomizer admin commands |

## Commands
| **Command** | **Permission** | **Parameters** | **Description** |
| ----------- | -------------- | -------------- | --------------- |
| saveitem | slimecustomizer.admin | | Saves the item in your hand to a yml file. Read #saving-an-item for more info. |
| give | slimecustomizer.admin | \<player_name\> \<item_id\> \<amount\> | Used to give an item to a player. |
| getsaveditem | slimecustomizer.admin | gui / \<item_id\> \<player_name\> \<amount\> | Used to get/give a saveditem. |

## Compatability with other Slimefun addons
To be compatible with items from other addons, SlimeCustomizer softdepends the following:
  - ChestTerminal
  - ColoredEnderChests
  - DyedBackpacks
  - EcoPower
  - ElectricSpawners
  - ExoticGarden
  - ExtraGear
  - ExtraHeads
  - HotbarPets
  - luckyblocks-sf
  - PrivateStorage
  - SlimefunOreChunks
  - SlimyTreeTaps
  - SoulJars
  - CommandOverride
  - CS-CoreLib
  - EmeraldEnchants2
  - QuickMarket
  - QuickSell
  - RankPrefixPlus
  - LiteXpansion
  - MobCapturer
  - SoundMuffler
  - ExtraTools
  - TranscEndence
  - Liquid
  - SFCalc
  - SlimefunWarfare
  - Slimy-Power-Suits
  - FluffyMachines
  - SlimyRepair
  - InfinityExpansion
  - FoxyMachines
  - GlobalWarming
  - DynaTech
  - GeneticChickengineering
  - HeadLimiter
  - SlimeXpansion
  - Barrels
  - ClayTech
  - FNAmplifications
  - SMG
  - EMC2
  - Simple-Storage
  - AlchimiaVitae
  - SlimeTinker
  - PotionExpansion
  - FlowerPower
  - Galactifun
  - Element-Manipulation
  - CrystamaeHistoria
  - DankTech2
  - Networks
  - VillagerUtil
  - MissileWarfare
  - SensibleToolbox

It is highly unlikely that new addons will be added to this list. If you are making a new addon or own a private addon and wish to it in SlimeCustomizer, add the following to your `plugin.yml`
```yaml
loadbefore:
    - SlimeCustomizer
```

## Changelog
- DEV 1:
    - Release of SlimeCustomizer!
    - Build your own [custom machines](#adding-your-machine) and [custom generators](#adding-your-generator)
- DEV 2:
    - Updated to no longer require CS-CoreLib
- DEV 3:
    - Added multi-line lore support
    - Added support for other multiblocks (If you have a preexisting config, this key will be smashed onto the bottom. Feel free to reorganize!)
    - Added support for machines to have 2 inputs and/or outputs
    - Added [custom items](#adding-your-item)
    - Added [custom categories](#adding-your-category)
    - Added new commands
- DEV 4:
    - Machine bug fix
- DEV 5:
    - Added [solar generators](#adding-your-solar-generator)

Have any questions? Join the Slimefun discord at https://discord.gg/slimefun/
