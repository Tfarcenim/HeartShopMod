package tfar.heartshopmod;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class Client {

    public static void renderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Init.HEART_GRENADE_E,ThrownItemRenderer::new);
    }
}
