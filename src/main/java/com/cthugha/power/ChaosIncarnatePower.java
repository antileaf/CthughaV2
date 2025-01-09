package com.cthugha.power;

import com.badlogic.gdx.graphics.Texture;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.CuriosityPower;
import com.megacrit.cardcrawl.powers.TimeWarpPower;

public class ChaosIncarnatePower extends AbstractPower {
	public static final String POWER_ID = CthughaHelper.makeID(ChaosIncarnatePower.class.getSimpleName());
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public ChaosIncarnatePower(AbstractCreature owner) {
		this.ID = POWER_ID;
		this.name = powerStrings.NAME;
		this.owner = owner;
		this.amount = -1;
		this.updateDescription();
		this.loadRegion("unawakened");
	}

	@Override
	public void updateDescription() {
		this.description = powerStrings.DESCRIPTIONS[0];
	}

	@Override
	public void atStartOfTurn() {
		this.flash();

		this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, CuriosityPower.POWER_ID));
		this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, TimeWarpPower.POWER_ID));
		this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, ArtifactPower.POWER_ID));
		this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, ChaosIncarnatePower.POWER_ID));
	}
}
