package com.cthugha.cards.cthugha;

import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.orbs.FireVampire;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import com.megacrit.cardcrawl.relics.GoldPlatedCables;

import java.util.ArrayList;

public class YanWu extends AbstractCthughaCard {

	public static final String ID = CthughaHelper.makeID(YanWu.class.getSimpleName());
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	private static final String NAME = cardStrings.NAME;
	private static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	private static final String IMG_PATH = "cthughaResources/img/card/炎舞.png";
	private static final int COST = 2;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.NONE;

	public YanWu() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.exhaust = true;
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();

			this.exhaust = false;
		}
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new ChannelAction(new FireVampire()));
		this.addToBot(new AnonymousAction(() -> {
			ArrayList<AbstractCard> burns = new ArrayList<>();

			burns.addAll(AbstractDungeon.player.drawPile.group
					.stream()
					.filter(CthughaHelper::isBurn)
					.collect(ArrayList::new, ArrayList::add, ArrayList::addAll));

			burns.addAll(AbstractDungeon.player.discardPile.group
					.stream()
					.filter(CthughaHelper::isBurn)
					.collect(ArrayList::new, ArrayList::add, ArrayList::addAll));

			burns.addAll(AbstractDungeon.player.hand.group
					.stream()
					.filter(CthughaHelper::isBurn)
					.collect(ArrayList::new, ArrayList::add, ArrayList::addAll));

			if (burns.isEmpty())
				return;

			for (AbstractOrb orb : p.orbs)
				if (orb instanceof FireVampire) {
					int count = 1;
					if (p.hasRelic(GoldPlatedCables.ID) && orb == p.orbs.get(0))
						count++;

					for (int i = 0; i < count; i++) {
						AbstractCard burn = burns.get(AbstractDungeon.cardRandomRng.random(burns.size() - 1));
						burns.remove(burn);
						((FireVampire) orb).triggerPassive(burn);

						if (burns.isEmpty())
							break;
					}

					if (burns.isEmpty())
						break;
				}
		}));
	}

}
