package com.cthugha.cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.cthugha.flare.FlareCardQueueItem;
import com.cthugha.flare.FlareHelper;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.cthugha.cards.interfaces.RightClickableCard;
import com.cthugha.utils.LanguageHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public abstract class AbstractCthughaCard extends CustomCard implements RightClickableCard {
	private static final Logger logger = LogManager.getLogger(AbstractCthughaCard.class.getName());

	private static final Color FLAVOR_TEXT_COLOR = new Color(
			255 / 255.0F, 231 / 255.0F, 150 / 255.0F, 1.0F);
	private static final Color FLAVOR_BOX_COLOR = new Color(
			38 / 255.0F, 8 / 255.0F, 4 / 255.0F, 1.0F);

	private static final float FADE_DURATION = 0.3F;
	private static final float FORCED_FADE_DURATION = 0.5F;

	public boolean canBaoYan = false;
	public boolean triggeredBaoYanLastTime = false;
	public boolean canZhiLiao = false;

	public int baseShunRan = -1;
	public int shunRan = -1;
	public boolean upgradedShunRan = false;
	public boolean isShunRanModified = false;

	public int baseSecondaryShunRan = -1;
	public int secondaryShunRan = -1;
	public boolean upgradedSecondaryShunRan = false;
	public boolean isSecondaryShunRanModified = false;

	public boolean triggeredShunRanThisTurn = false;

	public int secondaryMagicNumber = -1;
	public int baseSecondaryMagicNumber = -1;
	public boolean upgradedSecondaryMagicNumber = false;
	public boolean isSecondaryMagicNumberModified = false;

	public int secondaryDamage = -1;
	public int baseSecondaryDamage = -1;
	public int[] multiSecondaryDamage = null;
	public boolean isMultiSecondaryDamage = false;
	public boolean upgradedSecondaryDamage = false;
	public boolean isSecondaryDamageModified = false;

	public int secondaryBlock = -1;
	public int baseSecondaryBlock = -1;
	public boolean upgradedSecondaryBlock = false;
	public boolean isSecondaryBlockModified = false;

	public AbstractCthughaCard(
			String id,
			String name,
			String img,
			int cost,
			String rawDescription,
			CardType type,
			CardColor color,
			CardRarity rarity,
			CardTarget target
	) {
		super(id, name, img, cost, rawDescription, type, color, rarity, target);

		FlavorText.AbstractCardFlavorFields.textColor.set(this, FLAVOR_TEXT_COLOR);
		FlavorText.AbstractCardFlavorFields.boxColor.set(this, FLAVOR_BOX_COLOR);
	}

	public int getExtraYanZhiJing() {
		return 0;
	}

	public void updateBgImg() {
		if (this.triggeredShunRanThisTurn)
			this.textureBackgroundSmallImg = "cthughaResources/img/512/card_bw.png";
		else
			this.textureBackgroundSmallImg = null;
	}

//	@Override
//	public void triggerOnGlowCheck() {
//		super.triggerOnGlowCheck();
//	}

//	@Override
//	public Texture getBackgroundSmallTexture() {
//		if (this.shunRan != -1) {
//			if (this.triggeredShunRanThisTurn)
//				this.textureBackgroundSmallImg = "cthughaResources/img/512/card_bw.png";
//			else
//				this.textureBackgroundSmallImg = null;
//		}
//
//		return super.getBackgroundSmallTexture();
//	}

	public boolean shining() {
		return this.shunRan != -1 && !this.triggeredShunRanThisTurn;
	}

	@Override
	public void applyPowers() {
		super.applyPowers();

		if (this.secondaryDamage != -1) {
			int originalDamage = this.damage;
			int originalBaseDamage = this.baseDamage;
			boolean originalIsMultiDamage = this.isMultiDamage;
			int[] originalMultiDamage = (this.multiDamage != null ? this.multiDamage.clone() : null);
			boolean originalDamageModified = this.isDamageModified;

			this.baseDamage = this.baseSecondaryDamage;
			this.isMultiDamage = this.isMultiSecondaryDamage;
			super.applyPowers();
			this.secondaryDamage = this.damage;
			this.multiSecondaryDamage = this.multiDamage;
			this.isSecondaryDamageModified = this.isDamageModified;

			this.damage = originalDamage;
			this.baseDamage = originalBaseDamage;
			this.isMultiDamage = originalIsMultiDamage;
			this.multiDamage = originalMultiDamage;
			this.isDamageModified = originalDamageModified;
		}
	}

	@Override
	public void applyPowersToBlock() { // 还好没有同时有 secondary block 和 secondary damage 的
		super.applyPowersToBlock();

		if (this.baseSecondaryBlock != -1) {
			int originalBlock = this.block;
			int originalBaseBlock = this.baseBlock;
			boolean originalBlockModified = this.isBlockModified;

			this.baseBlock = this.baseSecondaryBlock;
			super.applyPowersToBlock();
			this.secondaryBlock = this.block;
			this.isSecondaryBlockModified = this.isBlockModified;

			this.block = originalBlock;
			this.baseBlock = originalBaseBlock;
			this.isBlockModified = originalBlockModified;
		}
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		super.calculateCardDamage(mo);

		if (this.baseSecondaryDamage != -1) {
			int originalDamage = this.damage;
			int originalBaseDamage = this.baseDamage;
			boolean originalIsMultiDamage = this.isMultiDamage;
			int[] originalMultiDamage = (this.multiDamage != null ? this.multiDamage.clone() : null);
			boolean originalDamageModified = this.isDamageModified;

			this.baseDamage = this.baseSecondaryDamage;
			this.isMultiDamage = this.isMultiSecondaryDamage;
			super.calculateCardDamage(mo);
			this.secondaryDamage = this.damage;
			this.multiSecondaryDamage = this.multiDamage;
			this.isSecondaryDamageModified = this.isDamageModified;

			this.damage = originalDamage;
			this.baseDamage = originalBaseDamage;
			this.isMultiDamage = originalIsMultiDamage;
			this.multiDamage = originalMultiDamage;
			this.isDamageModified = originalDamageModified;
		}
	}

	@Override
	public void resetAttributes() {
		super.resetAttributes();

		this.shunRan = this.baseShunRan;
		this.isShunRanModified = false;

		this.secondaryShunRan = this.baseSecondaryShunRan;
		this.isSecondaryShunRanModified = false;

		this.secondaryMagicNumber = this.baseSecondaryMagicNumber;
		this.isSecondaryMagicNumberModified = false;

		this.secondaryDamage = this.baseSecondaryDamage;
		this.isSecondaryDamageModified = false;

		this.secondaryBlock = this.baseSecondaryBlock;
		this.isSecondaryBlockModified = false;
	}

	@Override
	public void displayUpgrades() {
		super.displayUpgrades();

		if (this.upgradedShunRan) {
			this.shunRan = this.baseShunRan;
			this.isShunRanModified = true;
		}

		if (this.upgradedSecondaryShunRan) {
			this.secondaryShunRan = this.baseSecondaryShunRan;
			this.isSecondaryShunRanModified = true;
		}

		if (this.upgradedSecondaryMagicNumber) {
			this.secondaryMagicNumber = this.baseSecondaryMagicNumber;
			this.isSecondaryMagicNumberModified = true;
		}

		if (this.upgradedSecondaryDamage) {
			this.secondaryDamage = this.baseSecondaryDamage;
			this.isSecondaryDamageModified = true;
		}

		if (this.upgradedSecondaryBlock) {
			this.secondaryBlock = this.baseSecondaryBlock;
			this.isSecondaryBlockModified = true;
		}
	}

	public void upgradeShunRan(int amount) {
		this.baseShunRan += amount;
		this.shunRan = this.baseShunRan;
		this.upgradedShunRan = true;
	}

	public void upgradeSecondaryShunRan(int amount) {
		this.baseSecondaryShunRan += amount;
		this.secondaryShunRan = this.baseSecondaryShunRan;
		this.upgradedSecondaryShunRan = true;
	}

	public void upgradeSecondaryMagicNumber(int amount) {
		this.baseSecondaryMagicNumber += amount;
		this.secondaryMagicNumber = this.baseSecondaryMagicNumber;
		this.upgradedSecondaryMagicNumber = true;
	}

	public void upgradeSecondaryDamage(int amount) {
		this.baseSecondaryDamage += amount;
		this.secondaryDamage = this.baseSecondaryDamage;
		this.upgradedSecondaryDamage = true;
	}

	public void upgradeSecondaryBlock(int amount) {
		this.baseSecondaryBlock += amount;
		this.secondaryBlock = this.baseSecondaryBlock;
		this.upgradedSecondaryBlock = true;
	}

	public ArrayList<Integer> availableShunRanLevels() {
		ArrayList<Integer> levels = new ArrayList<>();
		if (this.shunRan != -1) {
			levels.add(this.shunRan);

			if (this.secondaryShunRan != -1)
				levels.add(this.secondaryShunRan);
		}
		return levels;
	}

	public void clearShunRanAtStartOfTurn() {
		this.triggeredShunRanThisTurn = false;
		this.updateBgImg();
	}

	@Override
	public void atTurnStart() {
		super.atTurnStart();
		this.clearShunRanAtStartOfTurn();
	}

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		AbstractCthughaCard card = (AbstractCthughaCard) super.makeStatEquivalentCopy();

		card.shunRan = this.shunRan;
		card.baseShunRan = this.baseShunRan;

		card.secondaryShunRan = this.secondaryShunRan;
		card.baseSecondaryShunRan = this.baseSecondaryShunRan;

		card.triggeredShunRanThisTurn = this.triggeredShunRanThisTurn;
		card.updateBgImg();

		card.secondaryMagicNumber = this.secondaryMagicNumber;
		card.baseSecondaryMagicNumber = this.baseSecondaryMagicNumber;

		card.secondaryDamage = this.secondaryDamage;
		card.baseSecondaryDamage = this.baseSecondaryDamage;

		card.secondaryBlock = this.secondaryBlock;
		card.baseSecondaryBlock = this.baseSecondaryBlock;

		return card;
	}

	@Override
	public void onRightClick() { // 瞬燃
		logger.info("Card right clicked.");

		if (this.shunRan == -1)
			return;

		if (FlareHelper.isCardQueued(this)) {
			logger.info("Card is already queued. (Please do not spam click.)");

			return;
		}

		if (this.triggeredShunRanThisTurn) {
			AbstractDungeon.effectList.add(new ThoughtBubble(
					AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY,
					3.0F, LanguageHelper.shunRanUIStrings.TEXT[1], true));
			return;
		}

		AbstractDungeon.actionManager.cardQueue.add(new FlareCardQueueItem(this));

		this.triggeredShunRanThisTurn = true;
		this.updateBgImg();
	}

	public void onFlare(int level) {}

	public int modifyFlareLevel() {
		return -1;
	}

	// Returning true will cancel the flare action
	public boolean onFlareSelectedBy(AbstractCthughaCard card) {
		return false;
	}
}
