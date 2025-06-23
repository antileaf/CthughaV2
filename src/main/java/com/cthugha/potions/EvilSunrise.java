package com.cthugha.potions;

import basemod.abstracts.CustomPotion;
import com.cthugha.Cthugha_Core;
import com.cthugha.orbs.FireVampire;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class EvilSunrise extends CustomPotion {
	public static final String SIMPLE_NAME = EvilSunrise.class.getSimpleName();
	public static final String ID = CthughaHelper.makeID(SIMPLE_NAME);
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);

	private static final int POTENCY = 6;

	public EvilSunrise() {
		super(
				potionStrings.NAME,
				ID,
				PotionRarity.RARE,
				PotionSize.MOON,
				PotionColor.FIRE
		);

		this.labOutlineColor = Cthugha_Core.BLOOD_COLOR.cpy();
	}

	@Override
	public void initializeData() {
		this.potency = this.getPotency();
		this.description = String.format(potionStrings.DESCRIPTIONS[0], this.potency);

		this.tips.clear();
		this.tips.add(new PowerTip(this.name, this.description));
	}

	@Override
	public void use(AbstractCreature target) {
		if (CthughaHelper.isInBattle()) {
			for (int i = 0; i < this.potency; i++)
				this.addToBot(new ChannelAction(new FireVampire()));
		}
	}

	@Override
	public int getPotency(int ascensionLevel) {
		return POTENCY;
	}

//	@Override
//	public int getPotency() {
//		return POTENCY;
//	}

	@Override
	public AbstractPotion makeCopy() {
		return new EvilSunrise();
	}
}
