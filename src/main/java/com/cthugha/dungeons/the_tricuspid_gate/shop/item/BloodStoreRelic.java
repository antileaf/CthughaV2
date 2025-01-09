//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.cthugha.dungeons.the_tricuspid_gate.shop.item;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cthugha.dungeons.the_tricuspid_gate.shop.BloodShopScreen;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StoreRelic;

import java.util.Iterator;

public class BloodStoreRelic extends StoreRelic {
    private static final float RELIC_GOLD_OFFSET_X = -56.0F * Settings.scale;
    private static final float RELIC_GOLD_OFFSET_Y = -100.0F * Settings.scale;
    private static final float RELIC_PRICE_OFFSET_X = 14.0F * Settings.scale;
    private static final float RELIC_PRICE_OFFSET_Y = -62.0F * Settings.scale;
    private static final float GOLD_IMG_WIDTH = (float)ImageMaster.TP_HP.getWidth() * Settings.scale;

    public BloodStoreRelic(AbstractRelic relic, int slot, ShopScreen screenRef) {
        super(relic, slot, screenRef);
        this.price /= 15;
    }

    @Override
    public void update(float rugY) {
        if (this.relic != null) {
            ShopScreen shopScreen = ReflectionHacks.getPrivate(this, StoreRelic.class, "shopScreen");

            if (!this.isPurchased) {
                int slot = ReflectionHacks.getPrivate(this, StoreRelic.class, "slot");
                this.relic.currentX = 1000.0F * Settings.xScale + 150.0F * slot * Settings.xScale;
                this.relic.currentY = rugY + 400.0F * Settings.yScale;
                this.relic.hb.move(this.relic.currentX, this.relic.currentY);
                this.relic.hb.update();
                if (this.relic.hb.hovered) {
                    shopScreen.moveHand(this.relic.currentX - 190.0F * Settings.xScale,
                            this.relic.currentY - 70.0F * Settings.yScale);
                    if (InputHelper.justClickedLeft) {
                        this.relic.hb.clickStarted = true;
                    }

                    this.relic.scale = Settings.scale * 1.25F;
                } else {
                    this.relic.scale = MathHelper.scaleLerpSnap(this.relic.scale, Settings.scale);
                }

                if (this.relic.hb.hovered && InputHelper.justClickedRight) {
                    CardCrawlGame.relicPopup.open(this.relic);
                }
            }

            if (this.relic.hb.clicked || this.relic.hb.hovered && CInputActionSet.select.isJustPressed()) {
                this.relic.hb.clicked = false;
                if (!Settings.isTouchScreen) {
                    this.purchaseRelic();
                } else if (AbstractDungeon.shopScreen.touchRelic == null) {
                    if (AbstractDungeon.player.maxHealth <= this.price) {
                        shopScreen.playCantBuySfx();
                        shopScreen.createSpeech(BloodShopScreen.getCantBuyMsg());
                    } else {
                        AbstractDungeon.shopScreen.confirmButton.hideInstantly();
                        AbstractDungeon.shopScreen.confirmButton.show();
                        AbstractDungeon.shopScreen.confirmButton.isDisabled = false;
                        AbstractDungeon.shopScreen.confirmButton.hb.clickStarted = false;
                        AbstractDungeon.shopScreen.touchRelic = this;
                    }
                }
            }
        }
    }

    @Override
    public void purchaseRelic() {
        ShopScreen shopScreen = ReflectionHacks.getPrivate(this, StoreRelic.class, "shopScreen");

        if (AbstractDungeon.player.maxHealth > this.price) {
            AbstractDungeon.player.decreaseMaxHealth(this.price);
            CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);
            CardCrawlGame.metricData.addShopPurchaseData(this.relic.relicId);
            AbstractDungeon.getCurrRoom().relics.add(this.relic);
            this.relic.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), true);
            this.relic.flash();
            if (this.relic.relicId.equals(MembershipCard.ID)) {
                shopScreen.applyDiscount(0.5F, true);
            }

            if (this.relic.relicId.equals(SmilingMask.ID)) {
                ShopScreen.actualPurgeCost = 50;
            }

            shopScreen.coloredCards.forEach(c -> this.relic.onPreviewObtainCard(c));
            shopScreen.colorlessCards.forEach(c -> this.relic.onPreviewObtainCard(c));

            shopScreen.playBuySfx();
            shopScreen.createSpeech(ShopScreen.getBuyMsg());
            if (!this.relic.relicId.equals(Courier.ID) && !AbstractDungeon.player.hasRelic(Courier.ID)) {
                this.isPurchased = true;
            } else {
                AbstractRelic tempRelic = AbstractDungeon.returnRandomRelicEnd(ShopScreen.rollRelicTier());
                while (tempRelic instanceof OldCoin ||
                        tempRelic instanceof SmilingMask ||
                        tempRelic instanceof MawBank ||
                        tempRelic instanceof Courier)
                    tempRelic = AbstractDungeon.returnRandomRelicEnd(ShopScreen.rollRelicTier());

                this.relic = tempRelic;
                this.price = this.relic.getPrice() / 15;
                shopScreen.getNewPrice(this);
            }
        } else {
            shopScreen.playCantBuySfx();
            shopScreen.createSpeech(BloodShopScreen.getCantBuyMsg());
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.relic != null) {
            this.relic.renderWithoutAmount(sb, new Color(0.0F, 0.0F, 0.0F, 0.25F));
            sb.setColor(Color.WHITE);
            sb.draw(ImageMaster.TP_HP, this.relic.currentX + RELIC_GOLD_OFFSET_X, this.relic.currentY + RELIC_GOLD_OFFSET_Y, GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);
            Color color = Color.WHITE;
            if (this.price >= AbstractDungeon.player.maxHealth) {
                color = Color.SALMON;
            }

            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, Integer.toString(this.price), this.relic.currentX + RELIC_PRICE_OFFSET_X, this.relic.currentY + RELIC_PRICE_OFFSET_Y, color);
        }

    }
}
