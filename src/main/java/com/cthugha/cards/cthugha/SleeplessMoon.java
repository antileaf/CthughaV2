package com.cthugha.cards.cthugha;

import basemod.BaseMod;
import com.cthugha.Cthugha_Core;
import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.cards.interfaces.RightClickableCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.LanguageHelper;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

public class SleeplessMoon extends AbstractCthughaCard {
	public static final String ID = CthughaHelper.makeID(SleeplessMoon.class.getSimpleName());
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final String NAME = cardStrings.NAME;
	private static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final String IMG_PATH = "cthughaResources/img/card/Sleepless_moon.png";

	private static final int COST = 1;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	public SleeplessMoon() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.damage = this.baseDamage = 16;
		this.shunRan = this.baseShunRan = 2;

		this.exhaust = true;
	}

	@Override
	public RightClickableCard.Position[] clickablePositions() {
		return new RightClickableCard.Position[] { Position.DRAW_PILE, Position.DISCARD_PILE, Position.EXHAUST_PILE };
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
	}

	@Override
	public void onRightClick() {
		if (this.triggeredShunRanThisTurn) {
			AbstractDungeon.effectList.add(new ThoughtBubble(
					AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY,
					3.0F, LanguageHelper.shunRanUIStrings.TEXT[1], true));
			return;
		}

//        if (AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
//            AbstractDungeon.player.createHandIsFullDialog();
//            return;
//        }

		Cthugha_Core.logger.info("SleeplessMoon.onRightClick: Triggered.");

		super.onRightClick();
	}

	@Override
	public void onFlare(int level) {
		if (level >= this.shunRan) {
			this.addToBot(new AnonymousAction(() -> {
				boolean handIsFull = AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE;

				if (handIsFull)
					AbstractDungeon.player.createHandIsFullDialog();

				if (AbstractDungeon.player.drawPile.contains(this)) {
					if (!handIsFull)
						AbstractDungeon.player.drawPile.moveToHand(this);
					else
						AbstractDungeon.player.drawPile.moveToDiscardPile(this);
				} else if (AbstractDungeon.player.discardPile.contains(this)) {
					AbstractDungeon.player.discardPile.moveToHand(this);
				} else if (AbstractDungeon.player.exhaustPile.contains(this)) {
					this.unfadeOut();

					if (!handIsFull)
						AbstractDungeon.player.exhaustPile.moveToHand(this);
					else
						AbstractDungeon.player.exhaustPile.moveToDiscardPile(this);
				}

				this.triggeredShunRanThisTurn = false;
				this.updateBgImg();

				this.flash();
			}));
		}
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(8);
			this.initializeDescription();
		}
	}
}
