package com.cthugha.cards.cthugha;

import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CastleOfTheSun extends AbstractCthughaCard {

	public static final String ID = CthughaHelper.makeID(CastleOfTheSun.class.getSimpleName());
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final String NAME = cardStrings.NAME;
	private static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	private static final String IMG_PATH = "cthughaResources/img/card/日轮之城.png";
	private static final int COST = 1;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private boolean flashAgain = false;

	public CastleOfTheSun(boolean isPreviewCard) {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.block = this.baseBlock = 8;
		this.damage = this.baseDamage = 8;
		this.magicNumber = this.baseMagicNumber = 2;

		this.shunRan = this.baseShunRan = 0;
		this.secondaryShunRan = this.baseSecondaryShunRan = 1;

		if (!isPreviewCard) {
			this.cardsToPreview = new CastleOfTheSun(true);
			((CastleOfTheSun) this.cardsToPreview).changeType(CardType.ATTACK);
		}
	}

	public CastleOfTheSun() {
		this(false);
	}

	public void changeType(CardType type) {
		if (this.cardsToPreview != null)
			((CastleOfTheSun) this.cardsToPreview).changeType(this.type);

		if (type == CardType.SKILL) {
			this.type = CardType.SKILL;
			this.target = CardTarget.SELF;
			this.name = cardStrings.NAME + (this.upgraded ? "+" : "");
			this.rawDescription = cardStrings.DESCRIPTION;
		}
		else {
			this.type = CardType.ATTACK;
			this.target = CardTarget.ENEMY;
			this.name = cardStrings.EXTENDED_DESCRIPTION[1] + (this.upgraded ? "+" : "");
			this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
		}

		this.initializeDescription();
		this.initializeTitle();
		this.loadCardImage("cthughaResources/img/card/" +
				(this.type == CardType.SKILL ? "日轮之城" : "新月之衰") +
				".png");
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (this.type == CardType.SKILL) {
			this.addToBot(new GainBlockAction(p, p, this.block));
		}
		else {
			this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
					AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		}
	}

	@Override
	public void onShunRan(int level) {
		if (level >= this.shunRan) {
			if (level < this.secondaryShunRan)
				this.changeType(this.type == CardType.SKILL ? CardType.ATTACK : CardType.SKILL);

			this.flash();
			this.flashAgain = true;
			this.baseBlock += this.magicNumber;
			this.baseDamage += this.magicNumber;
			this.applyPowers();

			this.cardsToPreview.baseBlock += this.magicNumber;
			this.cardsToPreview.block += this.magicNumber;
			this.cardsToPreview.baseDamage += this.magicNumber;
			this.cardsToPreview.damage += this.magicNumber;
		}
	}

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		AbstractCard card = super.makeStatEquivalentCopy();
		if (this.type == CardType.ATTACK)
			((CastleOfTheSun) card).changeType(this.type);
		if (this.cardsToPreview != null) {
			card.cardsToPreview.block = this.cardsToPreview.block;
			card.cardsToPreview.baseBlock = this.cardsToPreview.baseBlock;
			card.cardsToPreview.damage = this.cardsToPreview.damage;
			card.cardsToPreview.baseDamage = this.cardsToPreview.baseDamage;
		}
		return card;
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			if (this.cardsToPreview != null)
				this.cardsToPreview.upgrade();

			this.upgradeName();
			this.upgradeBlock(3);
			this.upgradeDamage(3);
			this.upgradeMagicNumber(1);
			this.initializeDescription();
		}
	}

	@SpireOverride
	public void updateFlashVfx() {
		SpireSuper.call();

		if (this.flashVfx == null && this.flashAgain) {
			this.flashAgain = false;
			this.flash();
		}
	}
}
