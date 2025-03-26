package com.cthugha.dungeons.the_tricuspid_gate.shop;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.cthugha.dungeons.the_tricuspid_gate.shop.item.BloodStorePotion;
import com.cthugha.dungeons.the_tricuspid_gate.shop.item.BloodStoreRelic;
import com.cthugha.utils.ConfigHelper;
import com.cthugha.utils.CthughaHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.Courier;
import com.megacrit.cardcrawl.relics.Orrery;
import com.megacrit.cardcrawl.relics.TinyHouse;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.shop.Merchant;
import com.megacrit.cardcrawl.shop.OnSaleTag;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;
import com.megacrit.cardcrawl.vfx.FloatyEffect;

import basemod.ReflectionHacks;


public class BloodShopScreen extends ShopScreen {
	private static final Logger logger = LogManager.getLogger(BloodShopScreen.class.getName());

	private static final float DRAW_START_X = (float)Settings.WIDTH * 0.16F;
	private static final float TOP_ROW_Y = 760.0F * Settings.yScale;
	private static final float BOTTOM_ROW_Y = 337.0F * Settings.yScale;
	private static final float SPEECH_TEXT_R_X = 164.0F * Settings.scale;
	private static final float SPEECH_TEXT_L_X = -166.0F * Settings.scale;
	private static final float SPEECH_TEXT_Y = 126.0F * Settings.scale;
	private static final float GOLD_IMG_WIDTH = (float)ImageMaster.UI_GOLD.getWidth() * Settings.scale;
	private static final float GOLD_IMG_OFFSET_X = -50.0F * Settings.scale;
	private static final float GOLD_IMG_OFFSET_Y = -215.0F * Settings.scale;
	private static final float PRICE_TEXT_OFFSET_X = 16.0F * Settings.scale;
	private static final float PRICE_TEXT_OFFSET_Y = -180.0F * Settings.scale;

	private static final UIStrings CANT_BUY_MSG = CardCrawlGame.languagePack
			.getUIString(CthughaHelper.makeID("CantBuyMessage"));

	public static final int PAGE_COUNT = 3;
	private static final int MAX_PURGE_COUNT = 3;

	public ArrayList<ArrayList<AbstractCard>> coloredCardsPages = new ArrayList<>();
	public ArrayList<ArrayList<AbstractCard>> colorlessCardsPages = new ArrayList<>();
	private ArrayList<OnSaleTag> saleTagPages = new ArrayList<>();
	private ArrayList<ArrayList<StoreRelic>> relicPages = new ArrayList<>();
	private ArrayList<ArrayList<StorePotion>> potionPages = new ArrayList<>();

	private static boolean justPurged = false;
	private int purgeCount = 0;

	public int currentPage = 0;
	private Hitbox prevHb, nextHb;

	public BloodShopScreen() {
		super();

		this.prevHb = new Hitbox(160.0F * Settings.scale, 160.0F * Settings.scale);
		this.nextHb = new Hitbox(160.0F * Settings.scale, 160.0F * Settings.scale);
	}

	@Override
	public void init(ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
		super.init(coloredCards, colorlessCards);

		this.coloredCardsPages.add(new ArrayList<>());
		this.coloredCardsPages.get(this.currentPage).addAll(coloredCards);

		this.colorlessCardsPages.add(new ArrayList<>());
		this.colorlessCardsPages.get(this.currentPage).addAll(colorlessCards);

		this.saleTagPages.add(ReflectionHacks.getPrivate(this, ShopScreen.class, "saleTag"));

		this.relicPages.add(new ArrayList<>());
		this.relicPages.get(this.currentPage).addAll(ReflectionHacks.getPrivate(
				this, ShopScreen.class, "relics"));

		this.potionPages.add(new ArrayList<>());
		this.potionPages.get(this.currentPage).addAll(ReflectionHacks.getPrivate(
				this, ShopScreen.class, "potions"));

		this.currentPage++;

		if (this.currentPage == PAGE_COUNT) {
			this.currentPage = 0;

			setPage(this.currentPage);

			ArrayList<String> idleMessages = ReflectionHacks.getPrivate(
					this, ShopScreen.class, "idleMessages");
			idleMessages.clear();
			Collections.addAll(idleMessages, CardCrawlGame.languagePack
					.getUIString(CthughaHelper.makeID("Merchant")).TEXT);

			justPurged = false;
			this.purgeCount = 0;
		}

		logger.info("Colored cards: {}", this.coloredCards);
		logger.info("Colorless cards: {}", this.colorlessCards);
	}

	@SpireOverride
	protected void initCards() {
		SpireSuper.call(this);

		this.coloredCards.forEach(c -> c.price = MathUtils.round(c.price / 20.0F));
		this.colorlessCards.forEach(c -> c.price = MathUtils.round(c.price / 20.0F));
	}

	private void setPage(int newPage) {
		this.coloredCards = this.coloredCardsPages.get(newPage);
		this.colorlessCards = this.colorlessCardsPages.get(newPage);
		ReflectionHacks.setPrivate(this, ShopScreen.class, "saleTag",
				this.saleTagPages.get(newPage));
		ReflectionHacks.setPrivate(this, ShopScreen.class, "relics",
				this.relicPages.get(newPage));
		ReflectionHacks.setPrivate(this, ShopScreen.class, "potions",
				this.potionPages.get(newPage));

		this.currentPage = newPage;
	}

	// Overriding ShopScreen's method
	// Called in ShopScreenPatch
	public static void purgeCard() {
		AbstractDungeon.player.decreaseMaxHealth(MathUtils.round(actualPurgeCost / 12.5F));

		CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);
		ShopScreen.purgeCost += 25;
		ShopScreen.actualPurgeCost += 25;
		if (AbstractDungeon.player.hasRelic("Smiling Mask")) {
			ShopScreen.actualPurgeCost = 50;
			AbstractDungeon.player.getRelic("Smiling Mask").stopPulse();
		} else if (AbstractDungeon.player.hasRelic("The Courier") &&
				AbstractDungeon.player.hasRelic("Membership Card")) {
			ShopScreen.actualPurgeCost = MathUtils.round((float)purgeCost * 0.8F * 0.5F);
		} else if (AbstractDungeon.player.hasRelic("The Courier")) {
			ShopScreen.actualPurgeCost = MathUtils.round((float)purgeCost * 0.8F);
		} else if (AbstractDungeon.player.hasRelic("Membership Card")) {
			ShopScreen.actualPurgeCost = MathUtils.round((float)purgeCost * 0.5F);
		}

		justPurged = true;
	}

	@Override
	public void updatePurge() {
		super.updatePurge();

		if (justPurged) {
			justPurged = false;
			this.purgeCount++;
			this.purgeAvailable = this.purgeCount < MAX_PURGE_COUNT;
		}
	}

	public static String getCantBuyMsg() {
		if (MathUtils.randomBoolean(0.01F))
			return CANT_BUY_MSG.EXTRA_TEXT[0];

		ArrayList<String> list = new ArrayList<>();
		Collections.addAll(list, CANT_BUY_MSG.TEXT);
		return list.get(MathUtils.random(list.size() - 1));
	}

	@Override
	public void applyDiscount(float multiplier, boolean affectPurge) {
		super.applyDiscount(multiplier, affectPurge);

		if (this.relicPages != null && this.relicPages.size() == PAGE_COUNT) {
			for (int page = 0; page < PAGE_COUNT; page++)
				if (page != this.currentPage) {
					for (StoreRelic r : this.relicPages.get(page))
						r.price = Math.max(1, MathUtils.round((float) r.price * multiplier));

					for (StorePotion p : this.potionPages.get(page))
						p.price = Math.max(1, MathUtils.round((float) p.price * multiplier));

					for (AbstractCard c : this.coloredCardsPages.get(page))
						c.price = Math.max(1, MathUtils.round((float) c.price * multiplier));

					for (AbstractCard c : this.colorlessCardsPages.get(page))
						c.price = Math.max(1, MathUtils.round((float) c.price * multiplier));
				}
		}
	}

	@SpireOverride
	protected void initRelics() {
		SpireSuper.call(this);

		ArrayList<StoreRelic> relics = ReflectionHacks.getPrivate(this, ShopScreen.class, "relics");
		ArrayList<BloodStoreRelic> newRelics = relics.stream()
				.map(r -> new BloodStoreRelic(r.relic,
						ReflectionHacks.getPrivate(r, StoreRelic.class, "slot"),
						this))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		relics.clear();
		relics.addAll(newRelics);
	}

	@SpireOverride
	protected void initPotions() {
		SpireSuper.call(this);

		ArrayList<StorePotion> potions = ReflectionHacks.getPrivate(this, ShopScreen.class, "potions");
		ArrayList<BloodStorePotion> newPotions = potions.stream()
				.map(p -> new BloodStorePotion(p.potion,
						ReflectionHacks.getPrivate(p, StorePotion.class, "slot"),
						this))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
		potions.clear();
		potions.addAll(newPotions);
	}

	@Override
	public void open() {
		super.open();

		if (ConfigHelper.showShopTutorial()) {
			TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString(
					CthughaHelper.makeID("Shop"));

			AbstractDungeon.ftue = new FtueTip(tutorialStrings.LABEL[0], tutorialStrings.TEXT[0],
					Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, FtueTip.TipType.CARD_REWARD);

			ConfigHelper.setShowShopTutorial(false);
			ConfigHelper.save();
		}
	}

	@Override
	public void update() {
		if (Settings.isTouchScreen) {
			this.confirmButton.update();
			if (InputHelper.justClickedLeft && this.confirmButton.hb.hovered) {
				this.confirmButton.hb.clickStarted = true;
			}

			if (InputHelper.justReleasedClickLeft && !this.confirmButton.hb.hovered) {
				ReflectionHacks.privateMethod(ShopScreen.class, "resetTouchscreenVars")
						.invoke(this);
			} else if (this.confirmButton.hb.clicked) {
				this.confirmButton.hb.clicked = false;

				AbstractCard touchCard = ReflectionHacks.getPrivate(this,
						ShopScreen.class, "touchCard");

				if (this.touchRelic != null) {
					this.touchRelic.purchaseRelic();
				} else if (touchCard != null) {
					ReflectionHacks.privateMethod(ShopScreen.class, "purchaseCard",
							AbstractCard.class).invoke(this, touchCard);
				} else if (this.touchPotion != null) {
					this.touchPotion.purchasePotion();
				} else if ((boolean) ReflectionHacks.getPrivate(this, ShopScreen.class, "touchPurge")) {
					ReflectionHacks.privateMethod(ShopScreen.class, "purchasePurge")
							.invoke(this);
				}

				ReflectionHacks.privateMethod(ShopScreen.class, "resetTouchscreenVars")
						.invoke(this);
			}
		}

		float handTimer = ReflectionHacks.getPrivate(this, ShopScreen.class, "handTimer");
		if (handTimer != 0.0F) {
			handTimer -= Gdx.graphics.getDeltaTime();
			if (handTimer < 0.0F)
				handTimer = 0.0F;
		}
		ReflectionHacks.setPrivate(this, ShopScreen.class, "handTimer", handTimer);

		FloatyEffect f_effect = ReflectionHacks.getPrivate(this, ShopScreen.class, "f_effect");
		f_effect.update();
		ReflectionHacks.setPrivate(this, ShopScreen.class, "somethingHovered", false);
		ReflectionHacks.privateMethod(ShopScreen.class, "updateControllerInput").invoke(this);
		this.updatePurgeCard();
		this.updatePurge();
		ReflectionHacks.privateMethod(ShopScreen.class, "updateRelics").invoke(this);
		ReflectionHacks.privateMethod(ShopScreen.class, "updatePotions").invoke(this);
		ReflectionHacks.privateMethod(ShopScreen.class, "updateRug").invoke(this);
		ReflectionHacks.privateMethod(ShopScreen.class, "updateSpeech").invoke(this);
		ReflectionHacks.privateMethod(ShopScreen.class, "updateCards").invoke(this);
		ReflectionHacks.privateMethod(ShopScreen.class, "updateHand").invoke(this);
		AbstractCard hoveredCard = null;

		for (AbstractCard c : this.coloredCards) {
			if (c.hb.hovered) {
				hoveredCard = c;
				ReflectionHacks.setPrivate(this, ShopScreen.class, "somethingHovered", true);
				this.moveHand(c.current_x - AbstractCard.IMG_WIDTH / 2.0F, c.current_y);
				break;
			}
		}

		for (AbstractCard c : this.colorlessCards) {
			if (c.hb.hovered) {
				hoveredCard = c;
				ReflectionHacks.setPrivate(this, ShopScreen.class, "somethingHovered", true);
				this.moveHand(c.current_x - AbstractCard.IMG_WIDTH / 2.0F, c.current_y);
				break;
			}
		}

		if (!(boolean)ReflectionHacks.getPrivate(this, ShopScreen.class, "somethingHovered")) {
			float notHoveredTimer = ReflectionHacks.getPrivate(this, ShopScreen.class, "notHoveredTimer");
			notHoveredTimer += Gdx.graphics.getDeltaTime();
			ReflectionHacks.setPrivate(this, ShopScreen.class, "notHoveredTimer", notHoveredTimer);

			if (notHoveredTimer > 1.0F) {
				ReflectionHacks.setPrivate(this, ShopScreen.class,
						"handTargetY", (float)Settings.HEIGHT);
			}
		} else {
			ReflectionHacks.setPrivate(this, ShopScreen.class, "notHoveredTimer", 0.0F);
		}

		if (hoveredCard != null && InputHelper.justClickedLeft) {
			hoveredCard.hb.clickStarted = true;
		}

		if (hoveredCard != null && (InputHelper.justClickedRight || CInputActionSet.proceed.isJustPressed())) {
			InputHelper.justClickedRight = false;
			CardCrawlGame.cardPopup.open(hoveredCard);
		}

		if (hoveredCard != null && (hoveredCard.hb.clicked || CInputActionSet.select.isJustPressed())) {
			hoveredCard.hb.clicked = false;
			if (!Settings.isTouchScreen) {
				ReflectionHacks.privateMethod(ShopScreen.class, "purchaseCard",
						AbstractCard.class).invoke(this, hoveredCard);
			} else if (ReflectionHacks.getPrivate(this, ShopScreen.class, "touchCard") == null) {
				if (AbstractDungeon.player.maxHealth <= hoveredCard.price) {
					ReflectionHacks.setPrivate(this, ShopScreen.class, "speechTimer",
							MathUtils.random(40.0F, 60.0F));
					this.playCantBuySfx();
					this.createSpeech(BloodShopScreen.getCantBuyMsg());
				} else {
					this.confirmButton.hideInstantly();
					this.confirmButton.show();
					this.confirmButton.isDisabled = false;
					this.confirmButton.hb.clickStarted = false;
					ReflectionHacks.setPrivate(this, ShopScreen.class,
							"touchCard", hoveredCard);
				}
			}
		}

		if (this.currentPage > 0)
			this.prevHb.move(Settings.WIDTH * 0.08F, Settings.HEIGHT / 2.0F);
		else
			this.prevHb.move(-1000.0F, -1000.0F);

		if (this.currentPage < PAGE_COUNT - 1)
			this.nextHb.move(Settings.WIDTH * 0.92F, Settings.HEIGHT / 2.0F);
		else
			this.nextHb.move(-1000.0F, -1000.0F);

		this.prevHb.update();
		this.nextHb.update();

		if (this.prevHb.clicked) {
			this.prevHb.clicked = false;
			CardCrawlGame.sound.play("UI_CLICK_1");

			if (this.currentPage > 0)
				setPage(this.currentPage - 1);
			else
				logger.warn("Trying to go to previous page when already at first page");
		}

		if (this.nextHb.clicked) {
			this.nextHb.clicked = false;
			CardCrawlGame.sound.play("UI_CLICK_1");

			if (this.currentPage < PAGE_COUNT - 1)
				setPage(this.currentPage + 1);
			else
				logger.warn("Trying to go to next page when already at last page");
		}

		if (InputHelper.justClickedLeft) {
			if (this.prevHb.hovered)
				this.prevHb.clickStarted = true;
			if (this.nextHb.hovered)
				this.nextHb.clickStarted = true;
		}
	}

	@SpireOverride
	protected void setPrice(AbstractCard c) {
		SpireSuper.call(c);
		c.price = MathUtils.round(c.price / 20.0F);
	}

	@SpireOverride
	protected void purchaseCard(AbstractCard hoveredCard) {
		if (AbstractDungeon.player.maxHealth > hoveredCard.price) {
			CardCrawlGame.metricData.addShopPurchaseData(hoveredCard.getMetricID());
			AbstractDungeon.topLevelEffects.add(new FastCardObtainEffect(hoveredCard, hoveredCard.current_x, hoveredCard.current_y));
			AbstractDungeon.player.decreaseMaxHealth(hoveredCard.price);
			CardCrawlGame.sound.play("SHOP_PURCHASE", 0.1F);

			if (!AbstractDungeon.player.hasRelic(Courier.ID)) {
				this.coloredCards.remove(hoveredCard);
				this.colorlessCards.remove(hoveredCard);
			} else if (hoveredCard.color == AbstractCard.CardColor.COLORLESS) {
				AbstractCard.CardRarity tempRarity = AbstractCard.CardRarity.UNCOMMON;
				if (AbstractDungeon.merchantRng.random() < AbstractDungeon.colorlessRareChance) {
					tempRarity = AbstractCard.CardRarity.RARE;
				}

				AbstractCard c = AbstractDungeon.getColorlessCardFromPool(tempRarity).makeCopy();
				AbstractDungeon.player.relics.forEach(r -> r.onPreviewObtainCard(c));

				c.current_x = hoveredCard.current_x;
				c.current_y = hoveredCard.current_y;
				c.target_x = c.current_x;
				c.target_y = c.current_y;
				this.setPrice(c);
				this.colorlessCards.set(this.colorlessCards.indexOf(hoveredCard), c);
			} else {
				AbstractCard c;
				for(c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), hoveredCard.type, false).makeCopy(); c.color == AbstractCard.CardColor.COLORLESS; c = AbstractDungeon.getCardFromPool(AbstractDungeon.rollRarity(), hoveredCard.type, false).makeCopy()) {
				}

				AbstractCard finalC = c;
				AbstractDungeon.player.relics.forEach(r -> r.onPreviewObtainCard(finalC));

				c.current_x = hoveredCard.current_x;
				c.current_y = hoveredCard.current_y;
				c.target_x = c.current_x;
				c.target_y = c.current_y;
				this.setPrice(c);
				this.coloredCards.set(this.coloredCards.indexOf(hoveredCard), c);
			}

			hoveredCard = null;
			InputHelper.justClickedLeft = false;
			ReflectionHacks.setPrivate(this, ShopScreen.class, "notHoveredTimer", 1.0F);
			ReflectionHacks.setPrivate(this, ShopScreen.class,
					"speechTimer", MathUtils.random(40.0F, 60.0F));
			this.playBuySfx();
			this.createSpeech(getBuyMsg());
		} else {
			ReflectionHacks.setPrivate(this, ShopScreen.class,
					"speechTimer", MathUtils.random(40.0F, 60.0F));
			this.playCantBuySfx();
			this.createSpeech(getCantBuyMsg());
		}

	}

	@SpireOverride
	protected void updatePurgeCard() {
		ReflectionHacks.setPrivate(this, ShopScreen.class,
				"purgeCardX", 1554.0F * Settings.scale);
		ReflectionHacks.setPrivate(this, ShopScreen.class,
				"purgeCardY",
				(float)ReflectionHacks.getPrivate(this,
						ShopScreen.class, "rugY") +
						BOTTOM_ROW_Y);

		if (this.purgeAvailable) {
			float CARD_W = 110.0F * Settings.scale;
			float CARD_H = 150.0F * Settings.scale;

			float purgeCardX = ReflectionHacks.getPrivate(this, ShopScreen.class, "purgeCardX");
			float purgeCardY = ReflectionHacks.getPrivate(this, ShopScreen.class, "purgeCardY");

			if (InputHelper.mX > purgeCardX - CARD_W && InputHelper.mX < purgeCardX + CARD_W &&
					InputHelper.mY > purgeCardY - CARD_H && InputHelper.mY < purgeCardY + CARD_H) {
				ReflectionHacks.setPrivate(this, ShopScreen.class,
						"purgeHovered", true);
				this.moveHand(purgeCardX - AbstractCard.IMG_WIDTH / 2.0F, purgeCardY);
				ReflectionHacks.setPrivate(this, ShopScreen.class,
						"somethingHovered", true);
				ReflectionHacks.setPrivate(this, ShopScreen.class,
						"purgeCardScale", Settings.scale);
			} else {
				ReflectionHacks.setPrivate(this, ShopScreen.class,
						"purgeHovered", false);
			}

			if (!(boolean)ReflectionHacks.getPrivate(this, ShopScreen.class, "purgeHovered")) {
				float purgeCardScale = ReflectionHacks.getPrivate(this,
						ShopScreen.class, "purgeCardScale");
				purgeCardScale = MathHelper.cardScaleLerpSnap(purgeCardScale, 0.75F * Settings.scale);
				ReflectionHacks.setPrivate(this, ShopScreen.class,
						"purgeCardScale", purgeCardScale);
			} else {
				if ((InputHelper.justClickedLeft) || (CInputActionSet.select.isJustPressed())) {
					CInputActionSet.select.unpress();
					ReflectionHacks.setPrivate(this, ShopScreen.class,
							"purgeHovered", false);
					if (AbstractDungeon.player.maxHealth > MathUtils.round(actualPurgeCost / 12.5F)) {
						AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.SHOP;

						AbstractDungeon.gridSelectScreen.open(
								CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck
										.getPurgeableCards()), 1, NAMES[13], false, false, true, true);
						if (AbstractDungeon.player.hasRelic(TinyHouse.ID) || AbstractDungeon.player.hasRelic(Orrery.ID)) {
							this.purgeAvailable = false;
						}
					} else {
						this.playCantBuySfx();
						this.createSpeech(getCantBuyMsg());
					}
				}
				TipHelper.renderGenericTip(InputHelper.mX - 360.0F * Settings.scale, InputHelper.mY - 70.0F * Settings.scale, LABEL[0], MSG[0] + 25 + MSG[1]);
			}


		} else {
			float purgeCardScale = ReflectionHacks.getPrivate(this,
					ShopScreen.class, "purgeCardScale");
			purgeCardScale = MathHelper.cardScaleLerpSnap(purgeCardScale, 0.75F * Settings.scale);
			ReflectionHacks.setPrivate(this, ShopScreen.class,
					"purgeCardScale", purgeCardScale);
		}
	}

	@SpireOverride
	protected String getIdleMsg() {
		if (MathUtils.randomBoolean(0.01F))
			return CardCrawlGame.languagePack
					.getUIString(CthughaHelper.makeID("Merchant")).EXTRA_TEXT[0];

		return SpireSuper.call();
	}

	@Override
	public void render(SpriteBatch sb) {
		super.render(sb);

		sb.setColor(Color.WHITE);

		if (this.currentPage > 0) {
			sb.draw(ImageMaster.POPUP_ARROW,
					this.prevHb.cX - 128.0F,
					this.prevHb.cY - 128.0F,
					128.0F, 128.0F,
					256.0F, 256.0F,
					Settings.scale, Settings.scale,
					0.0F,
					0, 0, 256, 256,
					false, false);

			if (this.prevHb.hovered) {
				sb.setBlendFunction(770, 1);
				sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.5F));
				sb.draw(ImageMaster.POPUP_ARROW, this.prevHb.cX - 128.0F, this.prevHb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);
				sb.setColor(Color.WHITE);
				sb.setBlendFunction(770, 771);
			}
		}

		if (this.currentPage < MAX_PURGE_COUNT - 1) {
			sb.draw(ImageMaster.POPUP_ARROW,
					this.nextHb.cX - 128.0F,
					this.nextHb.cY - 128.0F,
					128.0F, 128.0F,
					256.0F, 256.0F,
					Settings.scale, Settings.scale,
					0.0F,
					0, 0, 256, 256,
					true, false);

			if (this.nextHb.hovered) {
				sb.setBlendFunction(770, 1);
				sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.5F));
				sb.draw(ImageMaster.POPUP_ARROW, this.nextHb.cX - 128.0F, this.nextHb.cY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, true, false);
				sb.setColor(Color.WHITE);
				sb.setBlendFunction(770, 771);
			}
		}

		this.prevHb.render(sb);
		this.nextHb.render(sb);
	}

	private void handle(SpriteBatch sb, AbstractCard c) {
		c.render(sb);

		sb.setColor(Color.WHITE);
		sb.draw(ImageMaster.TP_HP,
				c.current_x + GOLD_IMG_OFFSET_X,
				c.current_y + GOLD_IMG_OFFSET_Y - (c.drawScale - 0.75F) * 200.0F * Settings.scale,
				GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);

		Color color = Color.WHITE.cpy();
		if (c.price >= AbstractDungeon.player.maxHealth)
			color = Color.SALMON.cpy();
		else {
			OnSaleTag saleTag = ReflectionHacks.getPrivate(this, ShopScreen.class, "saleTag");
			if (c.equals(saleTag.card))
				color = Color.SKY.cpy();
		}

		FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont,
				Integer.toString(c.price),
				c.current_x + PRICE_TEXT_OFFSET_X,
				c.current_y + PRICE_TEXT_OFFSET_Y - (c.drawScale - 0.75F) * 200.0F * Settings.scale,
				color);
	}

	@SpireOverride
	protected void renderCardsAndPrices(SpriteBatch sb) {
		this.coloredCards.forEach(c -> handle(sb, c));
		this.colorlessCards.forEach(c -> handle(sb, c));

		OnSaleTag saleTag = ReflectionHacks.getPrivate(this, ShopScreen.class, "saleTag");
		if (this.coloredCards.contains(saleTag.card) ||
				this.colorlessCards.contains(saleTag.card))
			saleTag.render(sb);

		this.coloredCards.forEach(c -> c.renderCardTip(sb));
		this.colorlessCards.forEach(c -> c.renderCardTip(sb));
	}

	@SpireOverride
	protected void renderPurge(SpriteBatch sb) {
		sb.setColor(Settings.QUARTER_TRANSPARENT_BLACK_COLOR);

		float purgeCardX = ReflectionHacks.getPrivate(this, ShopScreen.class, "purgeCardX");
		float purgeCardY = ReflectionHacks.getPrivate(this, ShopScreen.class, "purgeCardY");
		float purgeCardScale = ReflectionHacks.getPrivate(this, ShopScreen.class, "purgeCardScale");

		sb.draw(ImageMaster.TP_HP,
				purgeCardX - 256.0F + 18.0F * Settings.scale,
				purgeCardY - 256.0F - 14.0F * Settings.scale,
				256.0F, 256.0F,
				512.0F, 512.0F,
				purgeCardScale, purgeCardScale,
				0.0F,
				0, 0,
				512, 512,
				false, false);

		TextureAtlas.AtlasRegion tmpImg = ImageMaster.CARD_SKILL_BG_SILHOUETTE;
		sb.draw(tmpImg,
				purgeCardX + 18.0F * Settings.scale + tmpImg.offsetX - (float)tmpImg.originalWidth / 2.0F,
				purgeCardY - 14.0F * Settings.scale + tmpImg.offsetY - (float)tmpImg.originalHeight / 2.0F,
				(float)tmpImg.originalWidth / 2.0F - tmpImg.offsetX,
				(float)tmpImg.originalHeight / 2.0F - tmpImg.offsetY,
				(float)tmpImg.packedWidth, (float)tmpImg.packedHeight,
				purgeCardScale, purgeCardScale, 0.0F);
		sb.setColor(Color.WHITE);
		if (this.purgeAvailable) {

			sb.draw(ReflectionHacks.getPrivateStatic(ShopScreen.class, "removeServiceImg"),
					purgeCardX - 256.0F, purgeCardY - 256.0F,
					256.0F, 256.0F,
					512.0F, 512.0F,
					purgeCardScale, purgeCardScale,
					0.0F,
					0, 0,
					512, 512,
					false, false);

			sb.draw(ImageMaster.TP_HP,
					purgeCardX + GOLD_IMG_OFFSET_X,
					purgeCardY + GOLD_IMG_OFFSET_Y -
							(purgeCardScale / Settings.scale - 0.75F) * 200.0F * Settings.scale,
					GOLD_IMG_WIDTH, GOLD_IMG_WIDTH);

			Color color = Color.WHITE;
			if (MathUtils.round(actualPurgeCost / 12.5F) >= AbstractDungeon.player.maxHealth) {
				color = Color.SALMON;
			}

			FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont,
					Integer.toString(MathUtils.round(actualPurgeCost / 12.5F)),
					purgeCardX + PRICE_TEXT_OFFSET_X,
					purgeCardY + PRICE_TEXT_OFFSET_Y -
							(purgeCardScale / Settings.scale - 0.75F) * 200.0F * Settings.scale,
					color);

			FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont,
					"(" + (MAX_PURGE_COUNT - purgeCount) + "/" + MAX_PURGE_COUNT + ")",
					purgeCardX,
					purgeCardY + PRICE_TEXT_OFFSET_Y -
							(purgeCardScale / Settings.scale - 0.75F) * 200.0F * Settings.scale -
							48.0F * Settings.scale,
					Color.WHITE);
		} else {
			sb.draw(ReflectionHacks.getPrivateStatic(ShopScreen.class, "soldOutImg"),
					purgeCardX - 256.0F, purgeCardY - 256.0F,
					256.0F, 256.0F,
					512.0F, 512.0F,
					purgeCardScale, purgeCardScale,
					0.0F,
					0, 0,
					512, 512,
					false, false);
		}
	}

	public static class BloodShopRoom extends AbstractRoom {
		public static final float BLOOD_SHOP_CHANCE = 0.1F;
		private static final UIStrings uiStrings;
		public static final String[] TEXT;
		public int shopRarityBonus = 6;
		public Merchant merchant;

		public BloodShopRoom() {
			this.phase = RoomPhase.COMPLETE;
			this.merchant = null;
			this.mapSymbol = "$";
			this.mapImg = ImageMaster.MAP_NODE_MERCHANT;
			this.mapImgOutline = ImageMaster.MAP_NODE_MERCHANT_OUTLINE;
		}

		public void setMerchant(Merchant merchant) {
			this.merchant = merchant;
		}

		public void onPlayerEntry() {
			this.playBGM("SHOP");
			this.setMerchant(new Merchant());
		}

		public AbstractCard.CardRarity getCardRarity(int roll) {
			if (roll < 3 + this.shopRarityBonus) {
				return AbstractCard.CardRarity.RARE;
			} else {
				return roll < 40 + this.shopRarityBonus ? AbstractCard.CardRarity.UNCOMMON : AbstractCard.CardRarity.COMMON;
			}
		}

		public void update() {
			super.update();
			if (this.merchant != null) {
				this.merchant.update();
			}

		}

		public void render(SpriteBatch sb) {
			if (this.merchant != null) {
				this.merchant.render(sb);
				CardCrawlGame.psb.setShader(null);
			}

			super.render(sb);
			this.renderTips(sb);
		}

		static {
			uiStrings = CardCrawlGame.languagePack.getUIString("ShopRoom");
			TEXT = uiStrings.TEXT;
		}
	}
}
