package com.cthugha.actions;

import com.cthugha.Cthugha_Core;
import com.cthugha.patch.DarklingPatch;
import com.cthugha.power.CounterOfLossMaxHpPower;
import com.cthugha.power.ShengMingFanHuanPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;

// 减少怪物最大生命值
public class DecreaseMonsterMaxHealthAction extends AbstractGameAction {

	public DecreaseMonsterMaxHealthAction(AbstractCreature m, int maxHealth) {
		this.target = m;
		this.amount = maxHealth;

		this.actionType = ActionType.DAMAGE;
		this.duration = this.startDuration = 0.1F;
	}

	@Override
	public void update() {
//		if (this.shouldCancelAction()) {
//			this.isDone = true;
//			return;
//		}

		if (this.duration == this.startDuration) {
			if (this.amount == -1)
				this.amount = this.target.maxHealth - this.target.currentHealth;

			if (this.amount > this.target.maxHealth)
				this.amount = this.target.maxHealth;
		}

		this.tickDuration();

		if (this.isDone) {
			Cthugha_Core.logger.info("DecreaseMonsterMaxHealthAction: {} maxHealth: {} amount: {}",
					this.target.name, this.target.maxHealth, this.amount);

			this.target.maxHealth -= this.amount;

			boolean kill = false;

			if (this.target.maxHealth <= 0) {
				this.target.maxHealth = 1;
				kill = true;
			}

			if (this.target.currentHealth > this.target.maxHealth)
				this.target.currentHealth = this.target.maxHealth;

			this.target.healthBarUpdatedEvent();

			if (kill) {
//                ((AbstractMonster) this.target).die();
////                this.target.hideHealthBar();
//
//                if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
//                    AbstractDungeon.actionManager.cleanCardQueue();
//                    AbstractDungeon.effectList.add(new DeckPoofEffect(64.0F * Settings.scale, 64.0F * Settings.scale, true));
//                    AbstractDungeon.effectList.add(new DeckPoofEffect((float)Settings.WIDTH - 64.0F * Settings.scale, 64.0F * Settings.scale, false));
//                    AbstractDungeon.overlayMenu.hideCombatPanels();
//                }

				this.addToTop(new InstantKillAction(this.target));

				if (this.target instanceof Darkling)
					DarklingPatch.Fields.cannotRevive.set(this.target, true);
			}
			else
				this.addToTop(new ApplyPowerAction(this.target, this.target,
						new CounterOfLossMaxHpPower(this.target, this.amount)));

			int tempHP = 0;

			if (this.target.hasPower(ShengMingFanHuanPower.POWER_ID))
				tempHP = this.target.getPower(ShengMingFanHuanPower.POWER_ID).amount;

			if (tempHP == 0 && this.target instanceof Darkling &&
					DarklingPatch.Fields.tempHpReturnAmount.get(this.target) != 0) {
				tempHP = DarklingPatch.Fields.tempHpReturnAmount.get(this.target);
				Cthugha_Core.logger.info("Special case for Darkling, tempHP: {}", tempHP);
			}

			this.addToTop(new AddTemporaryHPAction(AbstractDungeon.player,
					AbstractDungeon.player, tempHP));
		}
	}
}
