package com.cthugha.cards;

import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.cthugha.Cthugha_Core;
import com.cthugha.actions.common.BetterSelectCardsInHandAction;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.cthugha.cards.interfaces.RightClickableCard;
import com.cthugha.helpers.LanguageHelper;
import com.cthugha.helpers.ModHelper;
import com.cthugha.relics.LieSiTaShuJian;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import java.util.ArrayList;

public abstract class AbstractCthughaCard extends CustomCard implements RightClickableCard {
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
	public boolean upgradedSecondaryDamage = false;
	public boolean isSecondaryDamageModified = false;

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

		FlavorText.AbstractCardFlavorFields.boxColor.set(this, Color.SCARLET);
	}

	@Override
	public void applyPowers() {
		super.applyPowers();

		if (this.secondaryDamage != -1) {
			int originalDamage = this.damage;
			int originalBaseDamage = this.baseDamage;
			boolean originalDamageModified = this.isDamageModified;
			int[] originalMultiDamage = (this.multiDamage != null ? this.multiDamage.clone() : null);

			this.baseDamage = this.baseSecondaryDamage;
			super.applyPowers();
			this.isSecondaryDamageModified = this.secondaryDamage != this.baseSecondaryDamage;
			this.secondaryDamage = this.damage;

			this.damage = originalDamage;
			this.baseDamage = originalBaseDamage;
			this.isDamageModified = originalDamageModified;
			this.multiDamage = originalMultiDamage;
		}
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		super.calculateCardDamage(mo);

		if (this.baseSecondaryDamage != -1) {
			int originalDamage = this.damage;
			int originalBaseDamage = this.baseDamage;
			boolean originalDamageModified = this.isDamageModified;
			int[] originalMultiDamage = (this.multiDamage != null ? this.multiDamage.clone() : null);

			this.baseDamage = this.baseSecondaryDamage;
			super.calculateCardDamage(mo);
			this.isSecondaryDamageModified = this.secondaryDamage != this.baseSecondaryDamage;
			this.secondaryDamage = this.damage;

			this.damage = originalDamage;
			this.baseDamage = originalBaseDamage;
			this.isDamageModified = originalDamageModified;
			this.multiDamage = originalMultiDamage;
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

	public ArrayList<Integer> availableShunRanLevels() {
		ArrayList<Integer> levels = new ArrayList<>();
		if (this.shunRan != -1) {
			levels.add(this.shunRan);

			if (this.secondaryShunRan != -1)
				levels.add(this.secondaryShunRan);
		}
		return levels;
	}

	@Override
	public void atTurnStart() {
		this.triggeredShunRanThisTurn = false;
	}

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		AbstractCthughaCard card = (AbstractCthughaCard) super.makeStatEquivalentCopy();

		card.shunRan = this.shunRan;
		card.baseShunRan = this.baseShunRan;

		card.secondaryShunRan = this.secondaryShunRan;
		card.baseSecondaryShunRan = this.baseSecondaryShunRan;

		card.triggeredShunRanThisTurn = this.triggeredShunRanThisTurn;

		card.secondaryMagicNumber = this.secondaryMagicNumber;
		card.baseSecondaryMagicNumber = this.baseSecondaryMagicNumber;

		card.secondaryDamage = this.secondaryDamage;
		card.baseSecondaryDamage = this.baseSecondaryDamage;

		return card;
	}

	@Override
	public void onRightClick() { // 瞬燃
		if (this.shunRan == -1)
			return;

		if (this.triggeredShunRanThisTurn) {
			AbstractDungeon.effectList.add(new ThoughtBubble(
					AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY,
					3.0F, "这张牌在本回合内已经使用过瞬燃了。", true));
			return;
		}

		if (AbstractDungeon.player.hand.group.stream().noneMatch(ModHelper::IsBurnCard)) {
			this.calculateCardDamage(null);
			this.onShunRan(0);
			return;
		}

		this.addToBot(new BetterSelectCardsInHandAction(
				BaseMod.MAX_HAND_SIZE,
				LanguageHelper.getShunRanString(this.availableShunRanLevels()),
				true,
				true,
				ModHelper::IsBurnCard,
				chosen -> {
					int level = chosen.size();

					int fz = chosen.stream()
							.filter(c -> c instanceof FuZhuoShangHuan)
							.mapToInt(c -> c.magicNumber)
							.max()
							.orElse(-1);

					if (fz != -1)
						level = fz;

					for (AbstractCard c : chosen)
						if (c instanceof ZhaoZhuoShangTianQue) {
							c.costForTurn = 0;
							AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(c, false));
							this.addToBot(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
							this.triggeredShunRanThisTurn = true;
							return;
						}

					if (AbstractDungeon.player.hasRelic(LieSiTaShuJian.ID))
						level = Math.min(level, 7);

					for (AbstractCard c : chosen)
						this.addToBot(new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand));

					Cthugha_Core.logger.info("ShunRan level: {}", level);
					this.onShunRan(level);
				},
				false
		));

		this.triggeredShunRanThisTurn = true;
	}

	public void onShunRan(int level) {}
}
