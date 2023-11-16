package tfar.heartshopmod;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSelectTradePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tfar.heartshopmod.shop.ShopOffer;
import tfar.heartshopmod.shop.ShopOffers;

public class CustomShopScreen extends AbstractContainerScreen<CustomShopMenu> {
    /**
     * The GUI texture for the villager merchant GUI.
     */
    private static final ResourceLocation VILLAGER_LOCATION = new ResourceLocation(HeartShopMod.MOD_ID,"textures/gui/heart_shop.png");
    private static final int TEXTURE_WIDTH = 512;
    private static final int TEXTURE_HEIGHT = 256;
    private static final int MERCHANT_MENU_PART_X = 99;
    private static final int PROGRESS_BAR_X = 136;
    private static final int PROGRESS_BAR_Y = 16;
    private static final int SELL_ITEM_1_X = 5;
    private static final int SELL_ITEM_2_X = 35;
    private static final int BUY_ITEM_X = 68;
    private static final int LABEL_Y = 6;
    private static final int NUMBER_OF_OFFER_BUTTONS = 7;
    private static final int TRADE_BUTTON_X = 5;
    private static final int TRADE_BUTTON_HEIGHT = 20;
    private static final int TRADE_BUTTON_WIDTH = 88;
    private static final int SCROLLER_HEIGHT = 27;
    private static final int SCROLLER_WIDTH = 6;
    private static final int SCROLL_BAR_HEIGHT = 139;
    private static final int SCROLL_BAR_TOP_POS_Y = 18;
    private static final int SCROLL_BAR_START_X = 94;
    private static final Component TRADES_LABEL = Component.translatable("merchant.trades");
    private static final Component LEVEL_SEPARATOR = Component.literal(" - ");
    private static final Component DEPRECATED_TOOLTIP = Component.translatable("merchant.deprecated");
    /**
     * The integer value corresponding to the currently selected merchant recipe.
     */
    private int shopItem = -1;
    private final TradeOfferButton[] tradeOfferButtons = new TradeOfferButton[7];
    int scrollOff;
    private boolean isDragging;

    public CustomShopScreen(CustomShopMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 276;
        this.inventoryLabelX = 107;
    }

    private void postButtonClick() {
        this.menu.setSelectionHint(this.shopItem);
        this.minecraft.getConnection().send(new ServerboundSelectTradePacket(this.shopItem));
    }

    protected void init() {
        super.init();
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        int k = j + 16 + 2;

        for (int l = 0; l < 7; ++l) {
            this.tradeOfferButtons[l] = this.addRenderableWidget(new TradeOfferButton(i + 5, k, l, (button) -> {
                if (button instanceof TradeOfferButton tradeOfferButton) {
                    this.shopItem = tradeOfferButton.getIndex() + this.scrollOff;
                    this.postButtonClick();
                }

            }));
            k += 20;
        }

    }

    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, this.title, 49 + this.imageWidth / 2 - this.font.width(this.title) / 2, 6, 4210752, false);

        pGuiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
        int l = this.font.width(TRADES_LABEL);
        pGuiGraphics.drawString(this.font, TRADES_LABEL, 5 - l / 2 + 48, 6, 4210752, false);
        ShopOffer shopOffer = shopItem >=0 && shopItem < menu.getOffers().size() ? menu.getOffers().get(shopItem) : null;
        if (shopOffer != null) {
            renderHeartTypeWithNumber(pGuiGraphics,pMouseX,pMouseY,138,41,shopOffer.getCost(),HeartType.REGULAR, 0xffffff);
        }
        renderHeartTypeWithNumber(pGuiGraphics,pMouseX,pMouseY,138,20,menu.getBalance(),HeartType.REGULAR,0x404040);
    }

    protected void renderHeartTypeWithNumber(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, int pX, int pY, int amount, HeartType heartType, int color) {
        Component component = Component.literal(amount +"");
        pGuiGraphics.drawString(this.font, component, pX, pY, color, false);
        int x = heartType.x;
        int y = heartType.y;
        pGuiGraphics.blit(heartType.location,pX + font.width(component),pY,x,y,0,0,x,y,x,y);
    }

    public enum HeartType{
        REGULAR(9, 9, new ResourceLocation(HeartShopMod.MOD_ID,"textures/item/red_heart.png")),
        SUPER(11, 11, new ResourceLocation(HeartShopMod.MOD_ID,"textures/item/red_heart.png")),
        ULTRA(13, 13, new ResourceLocation(HeartShopMod.MOD_ID,"textures/item/red_heart.png"));

        private final int x,y;
        private final ResourceLocation location;
        HeartType(int x, int y, ResourceLocation location) {
            this.x = x;
            this.y = y;

            this.location = location;
        }

    }

    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        pGuiGraphics.blit(VILLAGER_LOCATION, i, j, 0, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 512, 256);
    }

    private void renderScroller(GuiGraphics pGuiGraphics, int pPosX, int pPosY, ShopOffers pMerchantOffers) {
        int i = pMerchantOffers.size() + 1 - 7;
        if (i > 1) {
            int j = 139 - (27 + (i - 1) * 139 / i);
            int k = 1 + j / i + 139 / i;
            int l = 113;
            int i1 = Math.min(113, this.scrollOff * k);
            if (this.scrollOff == i - 1) {
                i1 = 113;
            }

            pGuiGraphics.blit(VILLAGER_LOCATION, pPosX + 94, pPosY + 18 + i1, 0, 0.0F, 199.0F, 6, 27, 512, 256);
        } else {
            pGuiGraphics.blit(VILLAGER_LOCATION, pPosX + 94, pPosY + 18, 0, 6.0F, 199.0F, 6, 27, 512, 256);
        }

    }

    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        ShopOffers merchantoffers = this.menu.getOffers();
        if (!merchantoffers.isEmpty()) {
            int i = (this.width - this.imageWidth) / 2;
            int j = (this.height - this.imageHeight) / 2;
            int k = j + 16 + 1;
            int l = i + 5 + 5;
            this.renderScroller(pGuiGraphics, i, j, merchantoffers);
            int i1 = 0;

            for (ShopOffer merchantoffer : merchantoffers) {
                if (!this.canScroll(merchantoffers.size()) || i1 >= this.scrollOff && i1 < 7 + this.scrollOff) {
                    int itemstack = merchantoffer.getCost();
                    ItemStack itemstack3 = merchantoffer.getResult();
                    pGuiGraphics.pose().pushPose();
                    pGuiGraphics.pose().translate(0.0F, 0.0F, 100.0F);
                    int j1 = k + 2;
                    this.renderShopCost(pGuiGraphics,itemstack, l, j1);

                    this.renderButtonArrows(pGuiGraphics, i, j1);
                    pGuiGraphics.renderFakeItem(itemstack3, i + 5 + 68, j1);
                    pGuiGraphics.renderItemDecorations(this.font, itemstack3, i + 5 + 68, j1);
                    pGuiGraphics.pose().popPose();
                    k += 20;
                }
                ++i1;
            }

            for (TradeOfferButton merchantscreen$tradeofferbutton : this.tradeOfferButtons) {
                if (merchantscreen$tradeofferbutton.isHoveredOrFocused()) {
                    merchantscreen$tradeofferbutton.renderToolTip(pGuiGraphics, pMouseX, pMouseY);
                }

                merchantscreen$tradeofferbutton.visible = merchantscreen$tradeofferbutton.index < this.menu.getOffers().size();
            }

            RenderSystem.enableDepthTest();
        }

        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    private void renderButtonArrows(GuiGraphics pGuiGraphics, int pPosX, int pPosY) {
        RenderSystem.enableBlend();
        pGuiGraphics.blit(VILLAGER_LOCATION, pPosX + 5 + 35 + 20, pPosY + 3, 0, 15.0F, 171.0F, 10, 9, 512, 256);
    }

    private void renderShopCost(GuiGraphics pGuiGraphics, int cost, int pX, int pY) {
        pGuiGraphics.drawString(font,cost +"",pX+3,pY+3,0xffffff);
     //   pGuiGraphics.renderFakeItem(cost, pX, pY);
      //  pGuiGraphics.renderItemDecorations(this.font, cost, pX, pY);

    }

    private boolean canScroll(int pNumOffers) {
        return pNumOffers > 7;
    }

    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        int i = this.menu.getOffers().size();
        if (this.canScroll(i)) {
            int j = i - 7;
            this.scrollOff = Mth.clamp((int) ((double) this.scrollOff - pDelta), 0, j);
        }

        return true;
    }

    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        int i = this.menu.getOffers().size();
        if (this.isDragging) {
            int j = this.topPos + 18;
            int k = j + 139;
            int l = i - 7;
            float f = ((float) pMouseY - (float) j - 13.5F) / ((float) (k - j) - 27.0F);
            f = f * (float) l + 0.5F;
            this.scrollOff = Mth.clamp((int) f, 0, l);
            return true;
        } else {
            return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        }
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        this.isDragging = false;
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        if (this.canScroll(this.menu.getOffers().size()) && pMouseX > (double) (i + 94) && pMouseX < (double) (i + 94 + 6) && pMouseY > (double) (j + 18) && pMouseY <= (double) (j + 18 + 139 + 1)) {
            this.isDragging = true;
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @OnlyIn(Dist.CLIENT)
    class TradeOfferButton extends Button {
        final int index;

        public TradeOfferButton(int pX, int pY, int pIndex, Button.OnPress pOnPress) {
            super(pX, pY, 88, 20, CommonComponents.EMPTY, pOnPress, DEFAULT_NARRATION);
            this.index = pIndex;
            this.visible = false;
        }

        public int getIndex() {
            return this.index;
        }

        public void renderToolTip(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
            if (this.isHovered && CustomShopScreen.this.menu.getOffers().size() > this.index + CustomShopScreen.this.scrollOff) {
                if (pMouseX < this.getX() + 20) {
                    int cost = CustomShopScreen.this.menu.getOffers().get(this.index + CustomShopScreen.this.scrollOff).getCost();
                    pGuiGraphics.renderTooltip(CustomShopScreen.this.font, Component.literal(cost+ " Hearts"), pMouseX, pMouseY);
                } else if (pMouseX < this.getX() + 50 && pMouseX > this.getX() + 30) {
                } else if (pMouseX > this.getX() + 65) {
                    ItemStack itemstack1 = CustomShopScreen.this.menu.getOffers().get(this.index + CustomShopScreen.this.scrollOff).getResult();
                    pGuiGraphics.renderTooltip(CustomShopScreen.this.font, itemstack1, pMouseX, pMouseY);
                }
            }
        }
    }
}

