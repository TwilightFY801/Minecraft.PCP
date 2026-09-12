package com.dangtools.client;

import com.dangtools.DangTools;
import com.dangtools.Registration;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.kinetics.motor.CreativeMotorRenderer;
import dev.engine_room.flywheel.api.visualization.BlockEntityVisualizer;
import dev.engine_room.flywheel.api.visualization.VisualizerRegistry;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * 客户端渲染接线：让“党的动力”的传动杆/旋转与创造马达完全一致。
 * <p>
 * 关键点：创造马达那根转动的传动杆是 <b>Flywheel 可视化器(Visualizer)</b> 渲染的，
 * 它是按“方块实体类型”注册的。我们自己新建了方块实体类型，所以要把创造马达类型的
 * 可视化器复制到我们的类型上，传动杆才会出现并旋转。
 * <p>
 * 本类只在客户端加载（由 DangTools 构造器里的 Dist 判断保护）。
 */
public class ClientSetup {

    /** 后备：注册创造马达的普通渲染器。 */
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(Registration.PARTY_POWER_BE.get(), CreativeMotorRenderer::new);
    }

    /** 把创造马达方块实体类型上的 Flywheel 可视化器复制到“党的动力”的类型上。 */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void copyMotorVisual(FMLClientSetupEvent event) {
        try {
            BlockEntityVisualizer<?> visualizer =
                    VisualizerRegistry.getVisualizer((BlockEntityType) AllBlockEntityTypes.MOTOR.get());
            if (visualizer != null) {
                VisualizerRegistry.setVisualizer(Registration.PARTY_POWER_BE.get(),
                        (BlockEntityVisualizer) visualizer);
                DangTools.LOGGER.info("[dangtools] 已把创造马达的 Flywheel 可视化器复制给党的动力。");
            } else {
                DangTools.LOGGER.warn("[dangtools] 创造马达没有可复制的 Flywheel 可视化器。");
            }
        } catch (Throwable t) {
            DangTools.LOGGER.warn("[dangtools] 复制 Flywheel 可视化器失败: {}", t.toString());
        }
    }
}
