package com.cthugha.cards.cthugha;

import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;

public class DianJiangChun_ZiYangHanShi extends AbstractCthughaCard {

	public static final String ID = CthughaHelper.makeID(DianJiangChun_ZiYangHanShi.class.getSimpleName());
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final String NAME = cardStrings.NAME;
	private static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	private static final String IMG_PATH = "cthughaResources/img/card/116.png";
	private static final int COST = -1;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	public DianJiangChun_ZiYangHanShi() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.magicNumber = this.baseMagicNumber = 1;

		this.exhaust = true;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new AnonymousAction(() -> {
			int amount = this.energyOnUse;

			if (p.hasRelic(ChemicalX.ID)) {
				amount += ChemicalX.BOOST;
				p.getRelic(ChemicalX.ID).flash();
			}

			if (amount > 0) {
				if (!this.freeToPlayOnce)
					p.energy.use(this.energyOnUse);

				int finalAmount = amount;
				this.addToBot(new SelectCardsInHandAction(amount,
						cardStrings.EXTENDED_DESCRIPTION[0],
						false, false,
						card -> true,
						abstractCards -> {
							int count = 0;

							for (AbstractCard card : abstractCards) {
								CthughaHelper.addToBuffer(new AnonymousAction(() -> {
									p.hand.moveToDiscardPile(card);
									card.triggerOnManualDiscard();
								}));

								if (CthughaHelper.isBurnCard(card))
									count++;
							}

							CthughaHelper.commitBuffer();

							int n = count / 2;
							count = finalAmount + this.magicNumber * n;

							this.addToBot(new GainEnergyAction(count));
							this.addToBot(new DrawCardAction(count));
						}));
			}
		}));
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();

			this.upgradeMagicNumber(1);

			this.initializeDescription();
		}
	}
}
