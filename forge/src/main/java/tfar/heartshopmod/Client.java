package tfar.heartshopmod;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
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

import java.util.ArrayList;
import java.util.List;

public class Client {

    public static void renderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Init.HEART_GRENADE_E, ThrownItemRenderer::new);
        event.registerEntityRenderer(Init.HEART_FIREBALL, (context) -> new ThrownItemRenderer<>(context, 1.0F, true));

    }

    private static final float PASS_ONE_ALPHA = 1.0F;
    private static final float PASS_TWO_ALPHA = 0.2647F;// 0.2645 - 0.2649 needs tweaking too much red, too little green/blue
    private static final float PASS_THREE_ALPHA = 0.769F;//exact
    private static final float PASS_FOUR_ALPHA = 0.63F;//< 0.66
    private static final float POTION_ALPHA = 0.85F;
    private static final float PASS_SIX_ALPHA = 0.20F;//< 0.66

    public static void registerBar(RegisterGuiOverlaysEvent e) {
        e.registerBelow(VanillaGuiOverlay.PLAYER_HEALTH.id(), HeartShopMod.MOD_ID, HEALTH);
    }

    public static void disableVanillaBar(RenderGuiOverlayEvent e) {
        if (e.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type() && useCompressedBar(Minecraft.getInstance())) {
            e.setCanceled(true);
        }
    }

    public static final IGuiOverlay HEALTH = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        if (!gui.getMinecraft().options.hideGui && gui.shouldDrawSurvivalElements() && useCompressedBar(gui.getMinecraft())) {
            gui.setupOverlayRenderState(true, false);
            renderHealth(gui, screenWidth, screenHeight, guiGraphics);
        }
    });

    public static void renderHealth(ForgeGui gui, int width, int height, GuiGraphics guiGraphics) {
        gui.getMinecraft().getProfiler().push("health");
        RenderSystem.enableBlend();

        Player player = (Player) gui.getMinecraft().getCameraEntity();
        int health = Mth.ceil(player.getHealth());
        boolean highlight = gui.healthBlinkTime > (long) gui.getGuiTicks() && (gui.healthBlinkTime - (long) gui.getGuiTicks()) / 3L % 2L == 1L;

        if (health < gui.lastHealth && player.invulnerableTime > 0) {
            gui.lastHealthTime = Util.getMillis();
            gui.healthBlinkTime = gui.getGuiTicks() + 20;
        } else if (health > gui.lastHealth && player.invulnerableTime > 0) {
            gui.lastHealthTime = Util.getMillis();
            gui.healthBlinkTime = gui.getGuiTicks() + 10;
        }

        if (Util.getMillis() - gui.lastHealthTime > 1000L) {
            gui.lastHealth = health;
            gui.displayHealth = health;
            gui.lastHealthTime = Util.getMillis();
        }

        gui.lastHealth = health;
        int healthLast = gui.displayHealth;

        AttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        float healthMax = Math.max((float) attrMaxHealth.getValue(), Math.max(healthLast, health));
        int absorb = Mth.ceil(player.getAbsorptionAmount());


        gui.random.setSeed(gui.getGuiTicks() * 312871L);

        int left = width / 2 - 91;
        int top = height - gui.leftHeight;


        int regen = -1;
        if (player.hasEffect(MobEffects.REGENERATION)) {
            regen = gui.getGuiTicks() % Mth.ceil(healthMax + 5.0F);
        }

        renderCustomHearts(gui, guiGraphics, player, left, top, regen, healthMax, health, healthLast, absorb, highlight);

        RenderSystem.disableBlend();
        gui.getMinecraft().getProfiler().pop();
    }

    static int getabsorbRows(int absorb) {
        if (absorb <= 0) {
            return 0;
        } else if (absorb <= 20) {
            return 1;
        } else if (absorb <= 200) {
            return 2;
        } else {
            return 2 + Mth.ceil(absorb / 2000d);
        }
    }

    static int[] getAbsorptionCompression(int absorb) {
        int temp = absorb;

        int ones = absorb % 20;
        temp /=20;

        List<Integer> placed = new ArrayList<>();
        placed.add(ones);
        while (temp > 0) {
            int mod = temp %10;
            temp /=10;
            placed.add(mod);
        }
        return placed.stream().mapToInt(Integer::intValue).toArray();
    }

    protected static void renderCustomHearts(ForgeGui gui, GuiGraphics pGuiGraphics, Player pPlayer, int pX, int pY, int pOffsetHeartIndex, float pMaxHealth, int pCurrentHealth, int pDisplayHealth, int pAbsorptionAmount, boolean pRenderHighlight) {
        HeartType heartType = HeartType.forPlayer(pPlayer);
        int yOffset = 9 * (pPlayer.level().getLevelData().isHardcore() ? 5 : 0);
        int heartContainers = Mth.ceil((double) pMaxHealth / 2.0D);

        //render health first
        for (int index = 0; index < heartContainers; index++) {
            int indexY = index / 10;
            int indexX = index % 10;
            int xPos = pX + indexX * 8;
            int yPos = pY - indexY * 8;
            //render background
            renderHeart(pGuiGraphics, HeartType.CONTAINER, xPos, yPos, 0, pRenderHighlight, false);
            HeartFill heartFill = getFill(pCurrentHealth, index);
            if (heartFill == HeartFill.FULL) {
                renderHeart(pGuiGraphics, heartType, xPos, yPos, 0, pRenderHighlight, false);
            } else if (heartFill == HeartFill.HALF) {
                renderHeart(pGuiGraphics, heartType, xPos, yPos, 0, pRenderHighlight, true);
            }
        }


        for (int index = 0; index < heartContainers; index++) {
            int indexY = index / 10;
            int indexX = index % 10;
            int xPos = pX + indexX * 8;
            int yPos = pY - indexY * 8;
            //render background
            renderHeart(pGuiGraphics, HeartType.CONTAINER, xPos, yPos, 0, pRenderHighlight, false);
            HeartFill heartFill = getFill(pCurrentHealth, index);
            if (heartFill == HeartFill.FULL) {
                renderHeart(pGuiGraphics, heartType, xPos, yPos, 0, pRenderHighlight, false);
            } else if (heartFill == HeartFill.HALF) {
                renderHeart(pGuiGraphics, heartType, xPos, yPos, 0, pRenderHighlight, true);
            }
        }

        int healthRows = Mth.ceil(pMaxHealth / 20);
        int rowHeight = 10;
        gui.leftHeight += healthRows * rowHeight;


        int y2 = pY - healthRows * rowHeight;

        int[] compressedAbsorptionContainers = getAbsorptionCompression(Mth.ceil(pAbsorptionAmount));//0 is 1s, 1 is 10s, 2 is 100s

        int row1Absorb = compressedAbsorptionContainers[0];

        for (int index = 0; index < Mth.ceil(row1Absorb / 2f); index++) {
            int indexY = index / 10;
            int indexX = index % 10;
            int xPos = pX + indexX * 8;
            int yPos = y2 - indexY * 8;
            //render background
            renderHeart(pGuiGraphics, HeartType.CONTAINER, xPos, yPos, 0, pRenderHighlight, false);
            HeartFill heartFill = getFill(row1Absorb, index);
            if (heartFill == HeartFill.FULL) {
                renderTintedHeart(pGuiGraphics, HeartType.ABSORBING, xPos, yPos, 0, pRenderHighlight, false,Color.RED);
            } else if (heartFill == HeartFill.HALF) {
                renderTintedHeart(pGuiGraphics, HeartType.ABSORBING, xPos, yPos, 0, pRenderHighlight, true,Color.RED);
            }
        }

        int row2Absorb = compressedAbsorptionContainers[1];

        for (int index = 0; index < Mth.ceil(row2Absorb / 2f); index++) {
            int indexY = index / 10;
            int indexX = index % 10;
            int xPos = pX + indexX * 8;
            int yPos = y2 - indexY * 8 - 10;
            //render background
          //  renderHeart(pGuiGraphics, HeartType.CONTAINER, xPos, yPos, 0, pRenderHighlight, false);
            HeartFill heartFill = getFill(row2Absorb, index);
            if (heartFill == HeartFill.FULL) {
                renderLargeTintedHeart(pGuiGraphics, HeartType.ABSORBING, xPos, yPos, 0, pRenderHighlight, false,Color.YELLOW,2);
            } else if (heartFill == HeartFill.HALF) {
                renderLargeTintedHeart(pGuiGraphics, HeartType.ABSORBING, xPos, yPos, 0, pRenderHighlight, true,Color.YELLOW,2);
            }
        }

        int row3Absorb = compressedAbsorptionContainers[2];

        for (int index = 0; index < Mth.ceil(row3Absorb / 2f); index++) {
            int indexY = index / 10;
            int indexX = index % 10;
            int xPos = pX + indexX * 8;
            int yPos = y2 - indexY * 8 - 20;
            //render background
            renderHeart(pGuiGraphics, HeartType.CONTAINER, xPos, yPos, 0, pRenderHighlight, false);
            HeartFill heartFill = getFill(row3Absorb, index);
            if (heartFill == HeartFill.FULL) {
                renderTintedHeart(pGuiGraphics, HeartType.ABSORBING, xPos, yPos, 0, pRenderHighlight, false,Color.BLUE);
            } else if (heartFill == HeartFill.HALF) {
                renderTintedHeart(pGuiGraphics, HeartType.ABSORBING, xPos, yPos, 0, pRenderHighlight, true,Color.BLUE);
            }
        }

        int absorbRows = 0;
        if (compressedAbsorptionContainers[2] > 0) {
            absorbRows = Mth.ceil(compressedAbsorptionContainers[2]/20d) + 2;
        } else if (compressedAbsorptionContainers[1]> 0) {
            absorbRows = 2;
        } else if (compressedAbsorptionContainers[0] > 0) {
            absorbRows = 1;
        }

        gui.leftHeight += absorbRows * 10;
    }

    public static HeartFill getFill(int current, int index) {
        if (index * 2 > current) {
            return HeartFill.NONE;
        } else if (current == index * 2 + 1) {
            return HeartFill.HALF;
        } else return HeartFill.FULL;
    }

    enum HeartFill {
        NONE, HALF, FULL;
    }


    private static void renderHeart(GuiGraphics pGuiGraphics, HeartType pHeartType, int pX, int pY, int pYOffset, boolean pRenderHighlight, boolean pHalfHeart) {
        pGuiGraphics.blit(GUI_ICONS_LOCATION, pX, pY, pHeartType.getX(pHalfHeart, pRenderHighlight), pYOffset, 9, 9);
    }

    private static void renderTintedHeart(GuiGraphics pGuiGraphics, HeartType pHeartType, int pX, int pY, int v,
                                          boolean pRenderHighlight, boolean pHalfHeart,Color color) {
        setColor(color.r,color.g,color.b,1);

        int w = pHalfHeart ? 5 : 9;

        //Draw tinted white heart
        setColor(color.r,color.g,color.b, PASS_ONE_ALPHA);
        drawTexturedModalRect(pGuiGraphics, pX, pY, 0, 0, w, 9);

        //Second pass dark highlights
        setColor(color.r,color.g,color.b, PASS_TWO_ALPHA);
        drawTexturedModalRect(pGuiGraphics, pX, pY, 0, 9, w, 9);

        //third pass dot highlight
        setColor(1.0F, 1.0F, 1.0F, PASS_SIX_ALPHA);
        drawTexturedModalRect(pGuiGraphics, pX, pY, 27, 0, w, 9);
        setColor(1,1,1,1);
    }

    private static void renderLargeTintedHeart(GuiGraphics pGuiGraphics, HeartType pHeartType, int pX, int pY, int v,
                                          boolean pRenderHighlight, boolean pHalfHeart,Color color,int scale) {
        setColor(color.r,color.g,color.b,1);

        int w = pHalfHeart ? 5 : 9;

        //Draw tinted white heart
        setColor(color.r,color.g,color.b, PASS_ONE_ALPHA);
        drawLargeTexturedModalRect(pGuiGraphics, pX, pY, 0, 0, w, 9,2);

        //Second pass dark highlights
        setColor(color.r,color.g,color.b, PASS_TWO_ALPHA);
        drawLargeTexturedModalRect(pGuiGraphics, pX, pY, 0, 9, w, 9,2);

        //third pass dot highlight
        setColor(1.0F, 1.0F, 1.0F, PASS_SIX_ALPHA);
        drawLargeTexturedModalRect(pGuiGraphics, pX, pY, 27, 0, w, 9,2);
        setColor(1,1,1,1);
    }

    protected static void renderHearts(ForgeGui gui, GuiGraphics pGuiGraphics, Player pPlayer, int pX, int pY, int pHeight, int pOffsetHeartIndex, float pMaxHealth, int pCurrentHealth, int pDisplayHealth, int pAbsorptionAmount, boolean pRenderHighlight) {
        HeartType heartType = HeartType.forPlayer(pPlayer);
        int i = 9 * (pPlayer.level().getLevelData().isHardcore() ? 5 : 0);
        int heartContainers = Mth.ceil((double) pMaxHealth / 2.0D);
        int absorptionContainers = Mth.ceil((double) pAbsorptionAmount / 2.0D);
        int maxHealth = heartContainers * 2;

        for (int index = heartContainers + absorptionContainers - 1; index >= 0; --index) {
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



    public static int getCompression() {
        Player player = Minecraft.getInstance().player;
        if (player.getMaxHealth() <= 200) return 10;
        return 100;
    }

    protected static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    public record Color(float r, float g, float b) {
        public static final Color RED = new Color(1,0,0);
        public static final Color YELLOW = new Color(1,1,0);
        public static final Color BLUE = new Color(0,0,1);
    }

    private static final Color YELLOW = new Color(1, 1, 0);
    private static final Color BLUE = new Color(0.1f, 0.1f, 1);

    public static void drawTexturedModalRect(GuiGraphics stack, int x, int y, int u, int v, int width, int height) {
        stack.blit(new ResourceLocation(HeartShopMod.MOD_ID, "textures/gui/health.png"), x, y, u, v, width, height);
    }

    //this blit is normally package-private
    public static void drawLargeTexturedModalRect(GuiGraphics stack, int x, int y, int u, int v, int width, int height,int scale) {
        stack.blit(new ResourceLocation(HeartShopMod.MOD_ID, "textures/gui/health.png"), x, y,0, u, v,
                width, height,256,256);
    }

    private static void setColor(float r, float g, float b, float a) {
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
            return player.getAbsorptionAmount() > 0;
        }
        return false;
    }

}
