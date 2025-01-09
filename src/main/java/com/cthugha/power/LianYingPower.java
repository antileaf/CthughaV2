package com.cthugha.power;

import com.cthugha.cards.cthugha.LianYing;
import com.cthugha.utils.CthughaHelper;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class LianYingPower extends AbstractPower implements InvisiblePower {
	public static final String POWER_ID = CthughaHelper.makeID(LianYingPower.class.getSimpleName());
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public LianYingPower(int amount) {
		this.name = powerStrings.NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.amount = amount;
		
		this.type = AbstractPower.PowerType.BUFF;
		this.updateDescription();
	}
	
	@Override
	public void updateDescription() {
		this.description = powerStrings.DESCRIPTIONS[0];
	}
	
	@Override
	public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
		if (card instanceof LianYing)
			return damage + this.amount;
		else
			return damage;
	}

	@Override
	public void atEndOfRound() {
		this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
	}
}
