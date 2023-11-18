package tfar.heartshopmod;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;

public class Client {

    public static void renderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Init.HEART_GRENADE_E,ThrownItemRenderer::new);
        event.registerEntityRenderer(Init.HEART_FIREBALL, (context) -> new ThrownItemRenderer<>(context, 1.0F, true));

    }

    private static final float PASS_ONE_ALPHA = 1.0F;
    private static final float PASS_TWO_ALPHA = 0.2647F;// 0.2645 - 0.2649 needs tweaking too much red, too little green/blue
    private static final float PASS_THREE_ALPHA = 0.769F;//exact
    private static final float PASS_FOUR_ALPHA = 0.63F;//< 0.66
    private static final float POTION_ALPHA = 0.85F;
    private static final float PASS_SIX_ALPHA = 0.20F;//< 0.66

    public static void registerBar(RegisterGuiOverlaysEvent e) {
        e.registerBelow(VanillaGuiOverlay.PLAYER_HEALTH.id(),HeartShopMod.MOD_ID, HEALTH);
    }

    public static void disableVanillaBar(RenderGuiOverlayEvent e) {
        if (e.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type() && useCompressedBar(Minecraft.getInstance())) {
            e.setCanceled(true);
        }
    }

    public static final IGuiOverlay HEALTH = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        if (!gui.getMinecraft().options.hideGui && gui.shouldDrawSurvivalElements() && useCompressedBar(gui.getMinecraft()))
        {
            gui.setupOverlayRenderState(true, false);
            renderHealth(gui,screenWidth, screenHeight, guiGraphics);
        }
    });

    public static void renderHealth(ForgeGui gui,int width, int height, GuiGraphics guiGraphics)
    {
        gui.getMinecraft().getProfiler().push("health");
        RenderSystem.enableBlend();

        Player player = (Player) gui.getMinecraft().getCameraEntity();
        int health = Mth.ceil(player.getHealth());
        boolean highlight = gui.healthBlinkTime > (long) gui.getGuiTicks() && (gui.healthBlinkTime - (long) gui.getGuiTicks()) / 3L % 2L == 1L;

        if (health < gui.lastHealth && player.invulnerableTime > 0)
        {
            gui.lastHealthTime = Util.getMillis();
            gui.healthBlinkTime = gui.getGuiTicks() + 20;
        }
        else if (health > gui.lastHealth && player.invulnerableTime > 0)
        {
            gui.lastHealthTime = Util.getMillis();
            gui.healthBlinkTime = gui.getGuiTicks() + 10;
        }

        if (Util.getMillis() - gui.lastHealthTime > 1000L)
        {
            gui.lastHealth = health;
            gui.displayHealth = health;
            gui.lastHealthTime = Util.getMillis();
        }

        gui.lastHealth = health;
        int healthLast = gui.displayHealth;

        AttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        float healthMax = Math.max((float) attrMaxHealth.getValue(), Math.max(healthLast, health));
        int absorb = Mth.ceil(player.getAbsorptionAmount());

        int healthRows = Mth.ceil((healthMax + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);

        gui.random.setSeed(gui.getGuiTicks() * 312871L);

        int left = width / 2 - 91;
        int top = height - gui.leftHeight;
        gui.leftHeight += (healthRows * rowHeight);
        if (rowHeight != 10) gui.leftHeight += 10 - rowHeight;

        int regen = -1;
        if (player.hasEffect(MobEffects.REGENERATION))
        {
            regen = gui.getGuiTicks() % Mth.ceil(healthMax + 5.0F);
        }

        renderHearts(gui,guiGraphics, player, left, top, rowHeight, regen, healthMax, health, healthLast, absorb, highlight);

        RenderSystem.disableBlend();
        gui.getMinecraft().getProfiler().pop();
    }

    protected static void renderHearts(ForgeGui gui,GuiGraphics pGuiGraphics, Player pPlayer, int pX, int pY, int pHeight, int pOffsetHeartIndex, float pMaxHealth, int pCurrentHealth, int pDisplayHealth, int pAbsorptionAmount, boolean pRenderHighlight) {
        HeartType heartType = HeartType.forPlayer(pPlayer);
        int i = 9 * (pPlayer.level().getLevelData().isHardcore() ? 5 : 0);
        int heartContainers = Mth.ceil((double)pMaxHealth / 2.0D);
        int absorptionContainers = Mth.ceil((double)pAbsorptionAmount / 2.0D);
        int maxHealth = heartContainers * 2;

        for(int index = heartContainers + absorptionContainers - 1; index >= 0; --index) {
            int heartY = index / 10;
            int heartX = index % 10;
            int l1 = pX + heartX * 8;
            int i2 = pY - heartY * pHeight;
            if (pCurrentHealth + pAbsorptionAmount <= 4) {
                i2 += gui.random.nextInt(2);
            }

            if (index < heartContainers && index == pOffsetHeartIndex) {
                i2 -= 2;
            }

            renderHeart(pGuiGraphics, HeartType.CONTAINER, l1, i2, i, pRenderHighlight, false);
            int j2 = index * 2;
            boolean flag = index >= heartContainers;
            if (flag) {
                int k2 = j2 - maxHealth;
                if (k2 < pAbsorptionAmount) {
                    boolean halfHeart = k2 + 1 == pAbsorptionAmount;
                    renderHeart(pGuiGraphics, heartType == HeartType.WITHERED ? heartType : HeartType.ABSORBING, l1, i2, i, false, halfHeart);
                }
            }

            if (pRenderHighlight && j2 < pDisplayHealth) {
                boolean halfHeart = j2 + 1 == pDisplayHealth;
                renderHeart(pGuiGraphics, heartType, l1, i2, i, true, halfHeart);
            }

            if (j2 < pCurrentHealth) {
                boolean halfHeart = j2 + 1 == pCurrentHealth;
                renderHeart(pGuiGraphics, heartType, l1, i2, i, false, halfHeart);
            }
        }

    }

    private static void renderHeart(GuiGraphics pGuiGraphics, HeartType pHeartType, int pX, int pY, int pYOffset, boolean pRenderHighlight, boolean pHalfHeart) {
        pGuiGraphics.blit(GUI_ICONS_LOCATION, pX, pY, pHeartType.getX(pHalfHeart, pRenderHighlight), pYOffset, 9, 9);
    }



    public static int getCompression() {
        Player player = Minecraft.getInstance().player;
        if (player.getMaxHealth() <= 200) return 10;
        return 100;
    }

    protected static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    public record Color(float r,float g,float b) {}

    private static final Color YELLOW = new Color(1,1,0);
    private static final Color BLUE = new Color(0.1f,0.1f,1);

    public static void drawTexturedModalRect(GuiGraphics stack,int x, int y, int textureX, int textureY, int width, int height) {
        stack.blit(new ResourceLocation(HeartShopMod.MOD_ID,"textures/gui/health.png"),x, y, textureX, textureY, width, height);
    }

    private static void setColor(float r,float g,float b,float a) {
        RenderSystem.setShaderColor(r, g, b, a);
    }


    enum HeartType {
        CONTAINER(0, false),
        NORMAL(2, true),
        POISIONED(4, true),
        WITHERED(6, true),
        ABSORBING(8, false),
        FROZEN(9, false);

        private final int index;
        private final boolean canBlink;

        HeartType(int pIndex, boolean pCanBlink) {
            this.index = pIndex;
            this.canBlink = pCanBlink;
        }

        public int getX(boolean pHalfHeart, boolean pRenderHighlight) {
            int i;
            if (this == CONTAINER) {
                i = pRenderHighlight ? 1 : 0;
            } else {
                int j = pHalfHeart ? 1 : 0;
                int k = this.canBlink && pRenderHighlight ? 2 : 0;
                i = j + k;
            }

            return 16 + (this.index * 2 + i) * 9;
        }

        static HeartType forPlayer(Player pPlayer) {
            HeartType gui$hearttype;
            if (pPlayer.hasEffect(MobEffects.POISON)) {
                gui$hearttype = POISIONED;
            } else if (pPlayer.hasEffect(MobEffects.WITHER)) {
                gui$hearttype = WITHERED;
            } else if (pPlayer.isFullyFrozen()) {
                gui$hearttype = FROZEN;
            } else {
                gui$hearttype = NORMAL;
            }

            return gui$hearttype;
        }
    }


    private static boolean useCompressedBar(Minecraft minecraft) {
        Player player = minecraft.player;
        if (player != null) {
            return player.getMaxHealth() > 40;
        }
        return false;
    }

}
