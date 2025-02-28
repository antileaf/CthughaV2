package com.cthugha.actions.common;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;

public class CthughaAddTempHPAction extends AbstractGameAction {
	public CthughaAddTempHPAction(AbstractCreature target, AbstractCreature source, int amount) {
		this.setValues(target, source, amount);

		this.duration = this.startDuration = 0.15F;
		this.actionType = ActionType.HEAL;
	}

	public void update() {
		if (this.duration == this.startDuration) {
			if (this.amount > 0) {
				TempHPField.tempHp.set(this.target, TempHPField.tempHp.get(this.target) + this.amount);
				AbstractDungeon.effectsQueue.add(new HealEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY, this.amount));
				this.target.healthBarUpdatedEvent();
			}
		}

		this.tickDuration();
	}
}
