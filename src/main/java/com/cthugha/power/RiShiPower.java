package com.cthugha.power;

import com.badlogic.gdx.graphics.Texture;
import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RiShiPower extends AbstractPower {
	public static final String POWER_ID = CthughaHelper.makeID(RiShiPower.class.getSimpleName());
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

	public RiShiPower(AbstractCreature owner, int amount) {
		this.ID = POWER_ID;
		this.name = powerStrings.NAME;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		CthughaHelper.loadPowerRegion(this, "日蚀");
//		this.img = new Texture("cthughaResources/img/power/201_32.png");
	}

	@Override
	public void updateDescription() {
		this.description = String.format(powerStrings.DESCRIPTIONS[0], this.amount);
	}

	@Override
	public void atEndOfRound() {
		this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
	}

	// Called in patch.
	public AbstractGameAction getAction(AbstractMonster target) {
		return new AnonymousAction(() -> {
			if (target != null && !target.isDeadOrEscaped()) {
				this.flash();
				this.addToTop(new GainBlockAction(this.owner, this.amount));
			}
		});
	}
}
