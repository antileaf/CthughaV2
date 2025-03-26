package com.cthugha.dungeons.the_tricuspid_gate.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.cthugha.Cthugha_Core;
import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.patches.monster.RevivePatch;
import com.cthugha.power.ShengMingFanHuanPower;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;

public class BurialWatchdogScepter extends CustomMonster {
	public static final String SIMPLE_NAME = BurialWatchdogScepter.class.getSimpleName();
	public static final String ID = CthughaHelper.makeID(SIMPLE_NAME);
	public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);

	private static final byte BASH = 1;
	private static final byte FORTIFY = 2;
	private static final byte SMASH = 3;
	private static final byte UNKNOWN = 4;
	private static final byte REVIVE = 5;
	private static final int BASH_DEBUFF = -1;
	private static final int FORTIFY_BLOCK = 30;

	public final int initialMaxHealth;
	private int moveCount = 0;

	private boolean foolish = false;

	public BurialWatchdogScepter() {
		super(
				monsterStrings.NAME,
				ID,
				40,
				0.0F,
				-15.0F,
				280.0F,
				290.0F,
				"cthughaResources/img/monster/Scepter.png",
				-1000.0F,
				10.0F
		);

		this.type = EnemyType.ELITE;

		this.setHp(AbstractDungeon.ascensionLevel >= 8 ? 40 : 50);
		this.initialMaxHealth = this.maxHealth;

		this.damage.add(new DamageInfo(this, AbstractDungeon.ascensionLevel >= 3 ? 5 : 4));
		this.damage.add(new DamageInfo(this, AbstractDungeon.ascensionLevel >= 3 ? 13 : 12));
	}

	@Override
	public void usePreBattleAction() {
		AbstractDungeon.getCurrRoom().cannotLose = true;
		this.addToBot(new ApplyPowerAction(this, this, new RegrowPower(this)));

		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player,
				this, new SurroundedPower(AbstractDungeon.player)));
//		this.addToBot(new ApplyPowerAction(this, this, new ArtifactPower(this,
//				AbstractDungeon.ascensionLevel >= 18 ? 2 : 1)));
	}

//	public void setIntentToRevive() {
//		this.addToBot(new SetMoveAction(this, REVIVE, Intent.BUFF));
//	}

	public void reviveByAreshkagal() {
		int targetMaxHealth = this.initialMaxHealth * 2;
		if (this.maxHealth < targetMaxHealth) {
			int amount = targetMaxHealth - this.maxHealth;
			this.maxHealth = targetMaxHealth;
			this.addToBot(new AnonymousAction(() -> {
				AbstractDungeon.effectsQueue.add(new TextAboveCreatureEffect(
						this.hb.cX - this.animX, this.hb.cY,
						AbstractCreature.TEXT[2] + amount, Settings.GREEN_TEXT_COLOR));
			}));
		}

		this.revive();

		this.addToBot(new RollMoveAction(this));
	}

	public void revive() {
		this.addToBot(new SFXAction("DARKLING_REGROW_" + MathUtils.random(1, 2),
				MathUtils.random(-0.1F, 0.1F)));

		this.addToBot(new HealAction(this, this, this.maxHealth / 2));
		this.addToBot(new AnonymousAction(() -> this.halfDead = false));
		this.addToBot(new ApplyPowerAction(this, this, new RegrowPower(this)));

		for (AbstractRelic relic : AbstractDungeon.player.relics)
			relic.onSpawnMonster(this);

		RevivePatch.Fields.tempHpReturnAmount.set(this, 0);
	}

	@Override
	public void takeTurn() {
		if (AbstractDungeon.getMonsters().monsters.stream()
				.anyMatch(m -> m instanceof Areshkagal && m.intent == Intent.MAGIC))
			return;

		if (this.nextMove == BASH) {
			this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0),
					AbstractGameAction.AttackEffect.BLUNT_HEAVY));

			this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this,
					new StrengthPower(AbstractDungeon.player, BASH_DEBUFF)));
		}
		else if (this.nextMove == FORTIFY) {
			for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
				if (!m.isDeadOrEscaped())
					this.addToBot(new GainBlockAction(m, this, FORTIFY_BLOCK));
		}
		else if (this.nextMove == SMASH) {
			this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1),
					AbstractGameAction.AttackEffect.BLUNT_HEAVY));

			this.addToBot(new GainBlockAction(this, this, AbstractDungeon.ascensionLevel >= 18 ?
					99 : this.damage.get(1).output));
		}
		else if (this.nextMove == UNKNOWN)
			this.addToBot(new TextAboveCreatureAction(this, monsterStrings.DIALOG[0]));
		else if (this.nextMove == REVIVE) {
			if (this.maxHealth <= 1)
				Cthugha_Core.logger.info("BurialWatchdogScepter: maxHealth <= 1");
			else
				this.revive();
		}

		this.addToBot(new RollMoveAction(this));
	}

	@Override
	protected void getMove(int num) {
		if (this.halfDead) {
			if (this.maxHealth <= 1)
				this.setMove((byte) 0, Intent.NONE);
			else
				this.setMove(REVIVE, Intent.BUFF);
		}
		else {
			if (this.moveCount % 3 == 0) {
				if (AbstractDungeon.aiRng.randomBoolean())
					this.setMove(FORTIFY, Intent.DEFEND);
				else
					this.setMove(BASH, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
			}
			else if (this.moveCount % 3 == 1) {
				if (!this.lastMove(BASH))
					this.setMove(BASH, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
				else
					this.setMove(FORTIFY, Intent.DEFEND);
			}
			else
				this.setMove(SMASH, Intent.ATTACK_DEFEND, this.damage.get(1).base);

			this.moveCount++;
		}
	}

	public void damage(DamageInfo info) {
		super.damage(info);

		if (this.currentHealth <= 0 && !this.halfDead) {
			this.halfDead = true;

			for (AbstractPower power : this.powers)
				power.onDeath();

			for (AbstractRelic relic : AbstractDungeon.player.relics)
				relic.onMonsterDeath(this);

			if (this.hasPower(ShengMingFanHuanPower.POWER_ID))
				RevivePatch.Fields.tempHpReturnAmount.set(this,
						this.getPower(ShengMingFanHuanPower.POWER_ID).amount);

			this.powers.clear();

			boolean allDead = AbstractDungeon.getMonsters().monsters.stream()
					.filter(m -> m != this && m.hasPower(RegrowPower.POWER_ID))
					.allMatch(m -> m.isDead || m.halfDead);

			if (!allDead) {
				if (this.nextMove != UNKNOWN && this.nextMove != REVIVE) {
					if (this.maxHealth <= 1) {
						this.setMove((byte) 0, Intent.NONE);
						this.createIntent();
						this.addToBot(new SetMoveAction(this, (byte) 0, Intent.NONE));
					}
					else {
						this.setMove(UNKNOWN, Intent.UNKNOWN);
						this.createIntent();
						this.addToBot(new SetMoveAction(this, UNKNOWN, Intent.UNKNOWN));
					}

					this.halfDie();
				}
			}
			else {
				AbstractDungeon.getCurrRoom().cannotLose = false;
				this.halfDead = false;

				for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
					m.die();
			}
		}
	}

	private void halfDie() {
		this.currentBlock = 0;

		BurialWatchdogSword other = AbstractDungeon.getMonsters().monsters.stream()
				.filter(m -> m instanceof BurialWatchdogSword)
				.map(m -> (BurialWatchdogSword) m)
				.findFirst()
				.orElse(null);

		if (other != null) {
			if (AbstractDungeon.player.hasPower(SurroundedPower.POWER_ID))
				AbstractDungeon.player.flipHorizontal = other.drawX	< AbstractDungeon.player.drawX;

			if (other.hasPower(BackAttackPower.POWER_ID))
				this.addToBot(new RemoveSpecificPowerAction(other, other, BackAttackPower.POWER_ID));
		}
	}

	@Override
	public void die() {
		if (!AbstractDungeon.getCurrRoom().cannotLose)
			super.die();
	}
}
