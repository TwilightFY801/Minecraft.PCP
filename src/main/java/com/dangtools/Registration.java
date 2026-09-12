package com.dangtools;

import com.dangtools.block.FlagBlock;
import com.dangtools.block.FlagBlockEntity;
import com.dangtools.block.PartyPowerBlock;
import com.dangtools.block.PartyPowerBlockEntity;
import com.dangtools.block.PartyWeightBlock;
import com.dangtools.block.PartyWeightBlockEntity;
import com.dangtools.item.FlightLicenseItem;
import com.dangtools.item.HammerItem;
import com.dangtools.item.PartyToolItem;
import com.dangtools.item.SickleItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Map;

public class Registration {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DangTools.MODID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DangTools.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, DangTools.MODID);
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, DangTools.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DangTools.MODID);

    // ---------- 核心工具（仅铁锭 + 木棍，仅铁制层级） ----------
    public static final DeferredItem<HammerItem> HAMMER =
            ITEMS.registerItem("hammer", HammerItem::new, toolProps(HammerItem.DURABILITY, 6.0));
    public static final DeferredItem<SickleItem> SICKLE =
            ITEMS.registerItem("sickle", SickleItem::new, toolProps(SickleItem.DURABILITY, 6.0));
    public static final DeferredItem<PartyToolItem> PARTY_TOOL =
            ITEMS.registerItem("party_sickle_hammer", PartyToolItem::new, toolProps(999999, 9998.0));

    // ---------- 飞行执照（国旗贴图；副手=不死图腾；胸甲=保护+荆棘+鞘翅；全透明不显示） ----------
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> FLIGHT_LICENSE_MATERIAL =
            ARMOR_MATERIALS.register("flight_license", () -> new ArmorMaterial(
                    Map.of(ArmorItem.Type.CHESTPLATE, 9),
                    30,
                    SoundEvents.ARMOR_EQUIP_NETHERITE,
                    () -> Ingredient.EMPTY,
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(DangTools.MODID, "flight_license"))),
                    4.0F,
                    0.1F));
    public static final DeferredItem<FlightLicenseItem> FLIGHT_LICENSE =
            ITEMS.registerItem("flight_license",
                    props -> new FlightLicenseItem(FLIGHT_LICENSE_MATERIAL, props),
                    new Item.Properties().stacksTo(1));

    // ---------- 党的动力（创造马达的直接复制，方块实体复用创造马达的 MOTOR 类型） ----------
    public static final DeferredBlock<PartyPowerBlock> PARTY_POWER =
            BLOCKS.register("party_power",
                    () -> new PartyPowerBlock(BlockBehaviour.Properties.of().strength(3.0F, 6.0F).noOcclusion()));
    public static final DeferredItem<BlockItem> PARTY_POWER_ITEM =
            ITEMS.registerItem("party_power", props -> new BlockItem(PARTY_POWER.get(), props), new Item.Properties());
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PartyPowerBlockEntity>> PARTY_POWER_BE =
            BLOCK_ENTITIES.register("party_power",
                    () -> BlockEntityType.Builder.<PartyPowerBlockEntity>of(
                                    (pos, state) -> new PartyPowerBlockEntity(blockEntityType("party_power"), pos, state),
                                    PARTY_POWER.get())
                            .build(null));

    // ---------- 党的分量（创造马达外观、黄色机壳、无传动杆；功能为“可调节重力”，单通道） ----------
    public static final DeferredBlock<PartyWeightBlock> PARTY_WEIGHT =
            BLOCKS.register("party_weight",
                    () -> new PartyWeightBlock(BlockBehaviour.Properties.of().strength(3.0F, 6.0F)));
    public static final DeferredItem<BlockItem> PARTY_WEIGHT_ITEM =
            ITEMS.registerItem("party_weight", props -> new BlockItem(PARTY_WEIGHT.get(), props), new Item.Properties());
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PartyWeightBlockEntity>> PARTY_WEIGHT_BE =
            BLOCK_ENTITIES.register("party_weight",
                    () -> BlockEntityType.Builder.<PartyWeightBlockEntity>of(
                                    (pos, state) -> new PartyWeightBlockEntity(blockEntityType("party_weight"), pos, state),
                                    PARTY_WEIGHT.get())
                            .build(null));

    // ---------- 国旗（放在竖直传动杆上，范围内满级信标增益 64×64×64） ----------
    public static final DeferredBlock<FlagBlock> FLAG =
            BLOCKS.register("flag",
                    () -> new FlagBlock(BlockBehaviour.Properties.of().instabreak().noOcclusion().noCollission()));
    public static final DeferredItem<BlockItem> FLAG_ITEM =
            ITEMS.registerItem("flag", props -> new BlockItem(FLAG.get(), props), new Item.Properties());
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FlagBlockEntity>> FLAG_BE =
            BLOCK_ENTITIES.register("flag",
                    () -> BlockEntityType.Builder.<FlagBlockEntity>of(
                                    (pos, state) -> new FlagBlockEntity(blockEntityType("flag"), pos, state),
                                    FLAG.get())
                            .build(null));

    // ---------- 创造模式物品栏：“党” ----------
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PARTY_TAB =
            CREATIVE_TABS.register("party", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + DangTools.MODID))
                    .icon(() -> new ItemStack(FLIGHT_LICENSE.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(HAMMER.get());
                        output.accept(SICKLE.get());
                        output.accept(PARTY_TOOL.get());
                        output.accept(FLIGHT_LICENSE.get());
                        output.accept(PARTY_POWER_ITEM.get());
                        output.accept(PARTY_WEIGHT_ITEM.get());
                        output.accept(FLAG_ITEM.get());
                    })
                    .build());

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        ARMOR_MATERIALS.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
    }

    /** 方块实体创建时从注册表取得本类型（此时类型已注册完成）。 */
    @SuppressWarnings("unchecked")
    private static <T extends net.minecraft.world.level.block.entity.BlockEntity> BlockEntityType<T> blockEntityType(String id) {
        return (BlockEntityType<T>) (BlockEntityType<?>) BuiltInRegistries.BLOCK_ENTITY_TYPE
                .get(ResourceLocation.fromNamespaceAndPath(DangTools.MODID, id));
    }

    /**
     * 通用工具属性：耐久 + 攻击伤害。
     * 攻击伤害作为“基础 + 加成”的一部分：base 玩家攻击为 1，因此传入 6.0 = 总 7（比铁剑 4 高 3），
     * 传入 9998.0 = 总 9999。
     */
    private static Item.Properties toolProps(int durability, double attackDamage) {
        return new Item.Properties()
                .durability(durability)
                .attributes(ItemAttributeModifiers.builder()
                        .add(Attributes.ATTACK_DAMAGE,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(DangTools.MODID, "attack_damage"),
                                        attackDamage, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND)
                        .add(Attributes.ATTACK_SPEED,
                                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(DangTools.MODID, "attack_speed"),
                                        -2.0, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND)
                        .build());
    }
}
