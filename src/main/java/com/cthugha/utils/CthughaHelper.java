package com.cthugha.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.cthugha.enums.CustomTags;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;

public abstract class CthughaHelper {
	public static String makeID(String id) {
		return "Cthugha:" + id;
	}

	// 灼伤
	public static boolean isBurn(AbstractCard card) {
//        return card.cardID.equals("Burn");
		return card instanceof Burn;
	}

	// 灼伤牌
	public static boolean isBurnCard(AbstractCard card) {
		return isBurn(card) || card.hasTag(CustomTags.Burn_Card);
	}

	// 打击牌
	@Deprecated
	public static boolean isStrikeCard(AbstractCard card) {
		return card.hasTag(AbstractCard.CardTags.STRIKE);
	}

	// 防御牌
	public static boolean isDefendCard(AbstractCard card) {
		return card.hasTag(AbstractCard.CardTags.STARTER_DEFEND);
	}

	public static boolean isInBattle() {
		return CardCrawlGame.dungeon != null &&
				AbstractDungeon.isPlayerInDungeon() &&
				AbstractDungeon.currMapNode != null &&
				AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
	}

	public static final ArrayList<AbstractGameAction> buffer = new ArrayList<>();

	public static void addToBuffer(AbstractGameAction action) {
		buffer.add(action);
	}

	public static void commitBuffer() {
		for (int i = buffer.size() - 1; i >= 0; i--)
			AbstractDungeon.actionManager.addToTop(buffer.get(i));
		buffer.clear();
	}

	public static final Texture BurnAttack = ImageMaster.loadImage("cthughaResources/img/card/灼伤_p.png");
	public static final TextureAtlas.AtlasRegion BurnAttackRegion = new TextureAtlas.AtlasRegion(BurnAttack, 0, 0,
			BurnAttack.getWidth(), BurnAttack.getHeight());

	public static final TextureAtlas cardAtlas = new TextureAtlas(Gdx.files.internal("cards/cards.atlas"));
	public static final TextureAtlas.AtlasRegion BurnRegion = cardAtlas.findRegion("status/burn");

	public static void changeBurn(AbstractCard c) {
		if (c.type == AbstractCard.CardType.STATUS) {
			c.type = AbstractCard.CardType.ATTACK;
			c.target = AbstractCard.CardTarget.ENEMY;
			c.jokePortrait = c.portrait = BurnAttackRegion;
			c.baseDamage = c.baseMagicNumber;

//			c.dontTriggerOnUseCard = false;

			c.initializeDescription();
			if (isInBattle())
				c.applyPowers();
			else
				c.damage = c.baseDamage = 2;
		}
		else if (c.type == AbstractCard.CardType.ATTACK) {
			c.type = AbstractCard.CardType.STATUS;
			c.target = AbstractCard.CardTarget.NONE;
			c.jokePortrait = c.portrait = BurnRegion;
			c.damage = c.baseDamage = -1;

			c.initializeDescription();
			if (isInBattle())
				c.applyPowers();
		}
	}

	public static AbstractCard getAttackBurn() {
		AbstractCard tmp = new Burn();
		CthughaHelper.changeBurn(tmp);
		return tmp;
	}

	public static void loadPowerRegion(AbstractPower power, String id) {
		power.region128 = new TextureAtlas.AtlasRegion(new Texture(
				"cthughaResources/img/power/" + id + "_84.png"),
				0, 0, 84, 84);

		power.region48 = new TextureAtlas.AtlasRegion(new Texture(
				"cthughaResources/img/power/" + id + "_32.png"),
				0, 0, 32, 32);
	}

	@Deprecated
	public static boolean isSignatureLibAvailable() {
		return Loader.isModLoaded("SignatureLib");
	}
}
