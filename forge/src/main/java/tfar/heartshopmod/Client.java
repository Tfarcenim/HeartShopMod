package tfar.heartshopmod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;

public class Client {

    public static void renderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Init.HEART_GRENADE_E,ThrownItemRenderer::new);
        event.registerEntityRenderer(Init.HEART_FIREBALL, (context) -> new ThrownItemRenderer<>(context, 1.0F, true));

    }

    public static void customHealthBar() {

    }

    public static void registerBar(RegisterGuiOverlaysEvent e) {
        e.registerBelow(VanillaGuiOverlay.PLAYER_HEALTH.id(),HeartShopMod.MOD_ID, HEALTH);
    }

    public static void disableVanillaBar(RenderGuiOverlayEvent e) {
        if (e.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type() && useCompressedBar(Minecraft.getInstance())) {
            e.setCanceled(true);
        }
    }

    public static final IGuiOverlay HEALTH = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
    });

    private static boolean useCompressedBar(Minecraft minecraft) {
        Player player = minecraft.player;
        if (player != null) {
            return player.getMaxHealth() > 40;
        }
        return false;
    }

}
