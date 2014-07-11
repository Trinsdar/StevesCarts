package vswe.stevesvehicles.block;


import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import vswe.stevesvehicles.old.Helpers.ComponentTypes;
import vswe.stevesvehicles.old.Helpers.RecipeHelper;
import vswe.stevesvehicles.old.Items.ItemBlockDetector;
import vswe.stevesvehicles.old.Items.ItemBlockStorage;
import vswe.stevesvehicles.old.Items.ItemUpgrade;
import vswe.stevesvehicles.old.Items.ModItems;
import vswe.stevesvehicles.tileentity.TileEntityActivator;
import vswe.stevesvehicles.tileentity.TileEntityCargo;
import vswe.stevesvehicles.tileentity.TileEntityCartAssembler;
import vswe.stevesvehicles.tileentity.TileEntityDetector;
import vswe.stevesvehicles.tileentity.TileEntityDistributor;
import vswe.stevesvehicles.tileentity.TileEntityLiquid;
import vswe.stevesvehicles.tileentity.TileEntityUpgrade;

import java.lang.reflect.Constructor;

public enum ModBlocks {
    CARGO_MANAGER("cargo_manager", BlockCargoManager.class, TileEntityCargo.class, "cargo"),
    JUNCTION("junction_rail", BlockRailJunction.class),
    ADVANCED_DETECTOR("advanced_detector_rail", BlockRailAdvancedDetector.class),
    CART_ASSEMBLER("vehicle_assembler", BlockCartAssembler.class, TileEntityCartAssembler.class, "assembler"),
    MODULE_TOGGLER("module_toggler", BlockActivator.class, TileEntityActivator.class, "toggler"),
    EXTERNAL_DISTRIBUTOR("external_distributor", BlockDistributor.class, TileEntityDistributor.class, "distributor"),
    DETECTOR_UNIT("detector_unit", BlockDetector.class, TileEntityDetector.class, "detector", ItemBlockDetector.class),
    UPGRADE("upgrade", BlockUpgrade.class, TileEntityUpgrade.class, "upgrade", ItemUpgrade.class),
    LIQUID_MANAGER("liquid_manager", BlockLiquidManager.class, TileEntityLiquid.class, "liquid"),
    STORAGE("metal_storage", BlockMetalStorage.class, ItemBlockStorage.class);



    private final String unlocalizedName;
    private final Class<? extends IBlockBase> clazz;
    private final Class<? extends TileEntity> tileEntityClazz;
    private final String tileEntityName;
    private final Class<? extends ItemBlock> itemClazz;

    private Block block;

    ModBlocks(String unlocalizedName, Class<? extends IBlockBase> clazz) {
        this(unlocalizedName, clazz, null, null);
    }

    ModBlocks(String unlocalizedName, Class<? extends IBlockBase> clazz, Class<? extends TileEntity> tileEntityClazz, String tileEntityName) {
        this(unlocalizedName, clazz, tileEntityClazz, tileEntityName, ItemBlock.class);
    }

    ModBlocks(String unlocalizedName, Class<? extends IBlockBase> clazz, Class<? extends ItemBlock> itemClazz) {
        this(unlocalizedName, clazz, null, null, itemClazz);
    }

    ModBlocks(String unlocalizedName, Class<? extends IBlockBase> clazz, Class<? extends TileEntity> tileEntityClazz, String tileEntityName, Class<? extends ItemBlock> itemClazz) {
        this.unlocalizedName = unlocalizedName;
        this.clazz = clazz;
        this.tileEntityClazz = tileEntityClazz;
        this.tileEntityName = tileEntityName;
        this.itemClazz = itemClazz;
    }


    public static void init() {
        for (ModBlocks blockInfo : values()) {
            try {
                if (Block.class.isAssignableFrom(blockInfo.clazz)) {
                    Constructor<? extends IBlockBase> blockConstructor = blockInfo.clazz.getConstructor();
                    Object blockInstance = blockConstructor.newInstance();

                    IBlockBase blockBase = (IBlockBase)blockInstance;
                    Block block = (Block)blockInstance;
                    block.setHardness(2F).setStepSound(Block.soundTypeMetal);
                    GameRegistry.registerBlock(block, blockInfo.itemClazz, blockInfo.unlocalizedName);
                    blockBase.setUnlocalizedName("steves_vehicles:tile.common:" + blockInfo.unlocalizedName) ;

                    blockInfo.block = block;

                    if (blockInfo.tileEntityClazz != null) {
                        GameRegistry.registerTileEntity(blockInfo.tileEntityClazz, blockInfo.tileEntityName);
                    }
                }else{
                    System.out.println("This is not a block (" + blockInfo.unlocalizedName + ")");
                }
            }catch(Exception e) {
                System.out.println("Failed to create block (" + blockInfo.unlocalizedName + ")");

                e.printStackTrace();
            }
        }

    }


    //TODO update recipes
    public static void addRecipes() {
        String blue = "dyeBlue";
        String orange = "dyeOrange";



        //cargo manager
        RecipeHelper.addRecipe(new ItemStack(CARGO_MANAGER.block, 1), new Object[][]{
                {ComponentTypes.LARGE_IRON_PANE.getItemStack(), ComponentTypes.HUGE_IRON_PANE.getItemStack(), ComponentTypes.LARGE_IRON_PANE.getItemStack()},
                {ComponentTypes.HUGE_IRON_PANE.getItemStack(), ComponentTypes.LARGE_DYNAMIC_PANE.getItemStack(), ComponentTypes.HUGE_IRON_PANE.getItemStack()},
                {ComponentTypes.LARGE_IRON_PANE.getItemStack(), ComponentTypes.HUGE_IRON_PANE.getItemStack(), ComponentTypes.LARGE_IRON_PANE.getItemStack()}
        });


        //activator
        RecipeHelper.addRecipe(new ItemStack(MODULE_TOGGLER.block, 1), new Object[][]{
                {orange, Items.gold_ingot, blue},
                {Blocks.stone, Items.iron_ingot, Blocks.stone},
                {Items.redstone, ComponentTypes.ADVANCED_PCB.getItemStack(), Items.redstone}
        });

        //distributor
        RecipeHelper.addRecipe(new ItemStack(EXTERNAL_DISTRIBUTOR.block, 1), new Object[][]{
                {Blocks.stone, ComponentTypes.SIMPLE_PCB.getItemStack(), Blocks.stone},
                {ComponentTypes.SIMPLE_PCB.getItemStack(), Items.redstone, ComponentTypes.SIMPLE_PCB.getItemStack()},
                {Blocks.stone, ComponentTypes.SIMPLE_PCB.getItemStack(), Blocks.stone}
        });

        //cart assembler
        RecipeHelper.addRecipe(new ItemStack(CART_ASSEMBLER.block, 1), new Object[][]{
                {Items.iron_ingot, Blocks.stone, Items.iron_ingot},
                {Blocks.stone, Items.iron_ingot, Blocks.stone},
                {ComponentTypes.SIMPLE_PCB.getItemStack(), Blocks.stone, ComponentTypes.SIMPLE_PCB.getItemStack()}
        });

        //junction rail
        RecipeHelper.addRecipe(new ItemStack(JUNCTION.block, 1), new Object[][]{
                {null, Items.redstone, null},
                {Items.redstone, Blocks.rail, Items.redstone},
                {null, Items.redstone, null}
        });


        //adv detector rail
        RecipeHelper.addRecipe(new ItemStack(ADVANCED_DETECTOR.block, 2), new Object[][]{
                {Items.iron_ingot, Blocks.stone_pressure_plate, Items.iron_ingot},
                {Items.iron_ingot, Items.redstone, Items.iron_ingot},
                {Items.iron_ingot, Blocks.stone_pressure_plate, Items.iron_ingot}
        });

        /** === detector units === **/
        //detector unit
        ItemStack unit = new ItemStack(DETECTOR_UNIT.block, 1 , 1);
        RecipeHelper.addRecipe(unit, new Object[][]{
                {Blocks.cobblestone, Blocks.stone_pressure_plate, Blocks.cobblestone},
                {Items.iron_ingot, ComponentTypes.SIMPLE_PCB.getItemStack(), Items.iron_ingot},
                {Blocks.cobblestone, Items.redstone, Blocks.cobblestone}
        });
        //detector manager
        RecipeHelper.addRecipe(new ItemStack(DETECTOR_UNIT.block, 1, 0), new Object[][]{
                {ComponentTypes.SIMPLE_PCB.getItemStack()},
                {unit}
        });
        //detector station
        RecipeHelper.addRecipe(new ItemStack(DETECTOR_UNIT.block, 1, 2), new Object[][]{
                {Items.iron_ingot, Items.iron_ingot, Items.iron_ingot},
                {null, unit, null},
                {null, ComponentTypes.SIMPLE_PCB.getItemStack(), null}
        });
        //detector junction
        RecipeHelper.addRecipe(new ItemStack(DETECTOR_UNIT.block, 1, 3), new Object[][]{
                {Blocks.redstone_torch, null, Blocks.redstone_torch},
                {Items.redstone, unit, Items.redstone},
                {null, ComponentTypes.SIMPLE_PCB.getItemStack(), null}
        });
        //detector redstone
        RecipeHelper.addRecipe(new ItemStack(DETECTOR_UNIT.block, 1, 4), new Object[][]{
                {Items.redstone, Items.redstone, Items.redstone},
                {Items.redstone, unit, Items.redstone},
                {Items.redstone, Items.redstone, Items.redstone}
        });
        /** **/

        ItemStack advtank = new ItemStack(ModItems.modules, 1, 66);

        //liquid manager
        RecipeHelper.addRecipe(new ItemStack(LIQUID_MANAGER.block, 1), new Object[][]{
                {advtank, Items.iron_ingot, advtank},
                {Items.iron_ingot, ComponentTypes.TANK_VALVE, Items.iron_ingot},
                {advtank, Items.iron_ingot, advtank}
        });
    }

    public Block getBlock() {
        return block;
    }


}