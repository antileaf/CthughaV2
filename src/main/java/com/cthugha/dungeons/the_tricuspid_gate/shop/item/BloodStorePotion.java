//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.cthugha.dungeons.the_tricuspid_gate.shop.item;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cthugha.dungeons.the_tricuspid_gate.shop.BloodShopScreen;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.Courier;
import com.megacrit.cardcrawl.relics.Sozu;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;

public class BloodStorePotion extends StorePotion {
	private static final float RELIC_GOLD_OFFSET_X = -56.0F * Settings.scale;
	private static final float RELIC_GOLD_OFFSET_Y = -100.0F * Settings.scale;
	private static final float RELIC_PRICE_OFFSET_X = 14.0F * Settings.scale;
	private static final float RELIC_PRICE_OFFSET_Y = -62.0F * Settings.scale;
	private static final float GOLD_IMG_WIDTH = (float)ImageMaster.UI_GOLD.getWidth() * Settings.scale;

	public BloodStorePotion(AbstractPotion potion, int slot, ShopScreen screenRef) {
		super(potion, slot, screenRef);
		this.price /= 15;
	}

	@Override
	public void update(float rugY) {
		ShopScreen shopScreen = ReflectionHacks.getPrivate(this, StorePotion.class, "shopScreen");

		if (this.potion != null) {
			if (!this.isPurchased) {
				int slot = ReflectionHacks.getPrivate(this, StorePotion.class, "slot");
				this.potion.posX = 1000.0F * Settings.xScale + 150.0F * slot * Settings.xScale;
				this.potion.posY = rugY + 200.0F * Settings.yScale;
				this.potion.hb.move(this.potion.posX, this.potion.posY);
				this.potion.hb.update();
				if (this.potion.hb.hovered) {
					shopScreen.moveHand(this.potion.posX - 190.0F * Settings.scale,
									this.potion.posY - 70.0F * Settings.scale);
					if (InputHelper.justClickedLeft) {
						this.potion.hb.clickStarted = true;
					}
				}
			}

			if (this.potion.hb.clicked || this.potion.hb.hovered && CInputActionSet.select.isJustPressed()) {
				this.potion.hb.clicked = false;
				if (!Settings.isTouchScreen) {
					this.purchasePotion();
				} else if (AbstractDungeon.shopScreen.touchPotion == null) {
					if (AbstractDungeon.player.maxHealth <= this.price) {
						shopScreen.playCantBuySfx();
						shopScreen.createSpeech(BloodShopScreen.getCantBuyMsg());
					} else {
						AbstractDungeon.shopScreen.confirmButton.hideInstantly();
						AbstractDungeon.shopScreen.confirmButton.show();
						AbstractDungeon.shopScreen.confirmButton.isDisabled = false;
						AbstractDungeon.shopScreen.confirmButton.hb.clickStarted = false;
						AbstractDungeon.shopScreen.touchPotion = this;
					}
				}
			}
		}
	}

	@Override
	public void purchasePotion() {
		if (AbstractDungeon.player.hasRelic(Sozu.ID)) {
			AbstractDungeon.player.getRelic(Sozu.ID).flash();
		} else {
			ShopScreen shopScreen = ReflectionHacks.getPrivate(this, StorePotion.class, "shopScreen");

			if (AbstractDungeon.player.maxHealth > this.price) {
				if (AbstractDungeon.player.obtainPotion(this.potion)) {
					AbstractDungeon.player.decreaseMaxHealth(this.price);
					CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);
					CardCrawlGame.metricData.addShopPurchaseData(this.potion.ID);
					shopScreen.playBuySfx();
					shopScreen.createSpeech(ShopScreen.getBuyMsg());
					if (AbstractDungeon.player.hasRelic(Courier.ID)) {
						this.potion = AbstractDungeon.returnRandomPotion();
						this.price = this.potion.getPrice() / 15;
						shopScreen.getNewPrice(this);
					} else {
						this.isPurchased = true;
					}

					return;
				}

				shopScreen.createSpeech(TEXT[0]);
				AbstractDungeon.topPanel.flashRed();
			} else {
				shopScreen.playCantBuySfx();
				shopScreen.createSpeech(BloodShopScreen.getCantBuyMsg());
			}

		}
	}

	@Override
	public void render(SpriteBatch sb) {
		if (this.potion != null) {
			this.potion.shopRender(sb);
			sb.setColor(Color.WHITE);
			sb.draw(ImageMaster.TP_HP, this.potion.posX + RELIC_GOLD_OFFSET_X, this.potion.posY + RELIC_GOLD_OFFSET_Y, GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);
			Color color = Color.WHITE;
			if (this.price >= AbstractDungeon.player.maxHealth)
				color = Color.SALMON;

			FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, Integer.toString(this.price), this.potion.posX + RELIC_PRICE_OFFSET_X, this.potion.posY + RELIC_PRICE_OFFSET_Y, color);
		}

	}
}
