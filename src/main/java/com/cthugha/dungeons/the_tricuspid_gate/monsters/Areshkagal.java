package com.cthugha.dungeons.the_tricuspid_gate.monsters;

import basemod.abstracts.CustomMonster;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.cthugha.Cthugha_Core;
import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.cardmodifier.ColorificStampModifier;
import com.cthugha.cards.colorless.Erosion;
import com.cthugha.patches.RevivePatch;
import com.cthugha.power.ChaosIncarnatePower;
import com.cthugha.power.ShengMingFanHuanPower;
import com.cthugha.utils.CthughaHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import com.megacrit.cardcrawl.vfx.combat.HeartBuffEffect;

public class Areshkagal extends CustomMonster {
	public static final String SIMPLE_NAME = Areshkagal.class.getSimpleName();
	public static final String ID = CthughaHelper.makeID(SIMPLE_NAME);
	public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);

	private static final byte YOU_ARE_MINE = 1;
	private static final byte BASH = 2;
	private static final byte SOUL_STRIKE = 3;
	private static final byte STRENGTHEN = 4;
	public static final byte FOOLISH = 5;
	private static final byte UNKNOWN = 6;
	private static final byte REVIVE = 7;

	private boolean firstMove = false;
	private int moveCount = 0;
	private int strengthenCount = 0;
	private boolean foolishUsed = false;

	public Areshkagal() {
		super(
				monsterStrings.NAME,
				ID,
				1100,
				0.0F,
				-15.0F,
				350.0F,
				240.0F,
				"cthughaResources/img/monster/Areshkagal.png",
				-300.0F,
				370.0F
		);

		this.drawX = Settings.WIDTH / 2.0F;

		this.type = EnemyType.BOSS;

		this.setHp(AbstractDungeon.ascensionLevel >= 9 ? 1200 : 1100);

		this.damage.add(new DamageInfo(this, AbstractDungeon.ascensionLevel >= 4 ? 32 : 26));
		this.damage.add(new DamageInfo(this, 4));
	}

	@Override
	public void usePreBattleAction() {
		CardCrawlGame.music.unsilenceBGM();
		AbstractDungeon.scene.fadeOutAmbiance();
		CardCrawlGame.music.silenceTempBgmInstantly();
		AbstractDungeon.getCurrRoom().playBgmInstantly("Facing_the_Faceless_Sphinx.mp3");

		AbstractDungeon.getCurrRoom().cannotLose = true;
		this.addToBot(new ApplyPowerAction(this, this, new RegrowPower(this)));

		this.addToBot(new ApplyPowerAction(this, this, new InvinciblePower(this,
				AbstractDungeon.ascensionLevel >= 19 ? 300 : 400)));

//		this.addToBot(new ApplyPowerAction(this, this, new FourColorTheoremPower(this)));

		this.addToBot(new ApplyPowerAction(this, this, new CuriosityPower(this,
				AbstractDungeon.ascensionLevel >= 19 ? 2 : 1)));

		this.addToBot(new ApplyPowerAction(this, this, new TimeWarpPower(this) {
			@Override
			public void updateDescription() {
				this.description = String.format(CardCrawlGame.languagePack
						.getPowerStrings("Cthugha:TimeWarp")
						.DESCRIPTIONS[0], Areshkagal.this.name);
			}
		}));

		this.addToBot(new ApplyPowerAction(this, this, new ArtifactPower(this,
				AbstractDungeon.ascensionLevel >= 19 ? 99 : 3)));

		this.addToBot(new ApplyPowerAction(this, this, new ChaosIncarnatePower(this)));
	}

	@Override
	public void takeTurn() {
		if (this.nextMove == YOU_ARE_MINE) {
			this.addToBot(new TalkAction(this, monsterStrings.DIALOG[0]));
			this.addToBot(new SFXAction("MONSTER_COLLECTOR_DEBUFF"));
			this.addToBot(new VFXAction(new CollectorCurseEffect(
					AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 2.0F));

			this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this,
					new VulnerablePower(AbstractDungeon.player, 2, true)));
			this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this,
					new WeakPower(AbstractDungeon.player, 2, true)));
			this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this,
					new FrailPower(AbstractDungeon.player, 2, true)));

			boolean[] stats = new boolean[4];
			for (int i = 0; i < 3; i++) {
				Erosion erosion = Erosion.create(stats);
				if (CardModifierManager.hasModifier(erosion, ColorificStampModifier.ID))
					CardModifierManager.getModifiers(erosion, ColorificStampModifier.ID)
							.forEach(mod -> stats[((ColorificStampModifier) mod).color.ordinal()] = true);

				this.addToBot(new MakeTempCardInDiscardAction(erosion, 1));
			}
		}
		else if (this.nextMove == BASH) {
			this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0),
					AbstractGameAction.AttackEffect.BLUNT_HEAVY));

			if (AbstractDungeon.ascensionLevel >= 19)
				this.addToBot(new MakeTempCardInDiscardAction(Erosion.create(new boolean[4]), 1));
		}
		else if (this.nextMove == SOUL_STRIKE) {
			for (int i = 0; i < 7; i++)
				this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1),
						AbstractGameAction.AttackEffect.BLUNT_LIGHT));

			this.addToBot(new ApplyPowerAction(this, this, new BufferPower(this, 1)));
		}
		else if (this.nextMove == STRENGTHEN) {
			int strength = 2;
			if (this.hasPower(StrengthPower.POWER_ID) && this.getPower(StrengthPower.POWER_ID).amount < 0)
				strength -= this.getPower(StrengthPower.POWER_ID).amount;

			AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(
					new Color(0.8F, 0.5F, 1.0F, 1.0F))));
			AbstractDungeon.actionManager.addToBottom(new VFXAction(new HeartBuffEffect(this.hb.cX, this.hb.cY)));
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this,
					new StrengthPower(this, strength)));

			for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
				if (!m.isDeadOrEscaped())
					this.addToBot(new GainBlockAction(m, 30));

			this.strengthenCount++;

			if (this.strengthenCount == 1)
				this.addToBot(new ApplyPowerAction(this, this,
						new ArtifactPower(this, 2)));
			else if (this.strengthenCount == 2)
				this.addToBot(new ApplyPowerAction(this, this,
						new BeatOfDeathPower(this, 1)));
			else if (this.strengthenCount == 3)
				this.addToBot(new ApplyPowerAction(this, this,
						new PainfulStabsPower(this)));
			else if (this.strengthenCount == 4)
				this.addToBot(new ApplyPowerAction(this, this,
						new StrengthPower(this, 10)));
			else
				this.addToBot(new ApplyPowerAction(this, this,
						new StrengthPower(this, 50)));
		}
		else if (this.nextMove == FOOLISH) {
			this.addToBot(new AnonymousAction(() -> {
				CardCrawlGame.music.unsilenceBGM();
				AbstractDungeon.scene.fadeOutAmbiance();
				CardCrawlGame.music.silenceTempBgmInstantly();
				AbstractDungeon.getCurrRoom().playBgmInstantly("Laughing_till_the_End.mp3");
			}));
			this.addToBot(new ShoutAction(this, monsterStrings.DIALOG[1], 0.5F, 2.0F));
			this.addToBot(new RemoveDebuffsAction(this));
			this.addToBot(new RemoveSpecificPowerAction(this, this, GainStrengthPower.POWER_ID));
			this.addToBot(new HealAction(this, this, this.maxHealth / 2 - this.currentHealth));

			if (AbstractDungeon.ascensionLevel >= 19) {
				this.addToBot(new GainBlockAction(this, 32));
				this.addToBot(new ApplyPowerAction(this, this, new BarricadePower(this)));
				this.addToBot(new ApplyPowerAction(this, this, new MalleablePower(this, 3)));
			}

			BurialWatchdogSword sword = (BurialWatchdogSword)
					AbstractDungeon.getMonsters().getMonster(BurialWatchdogSword.ID);
			if (sword != null) {
				if (!sword.halfDead) {
					int targetMaxHp = sword.initialMaxHealth * 2;

					if (sword.maxHealth < targetMaxHp)
						this.addToBot(new AnonymousAction(() -> {
							sword.increaseMaxHp(targetMaxHp - sword.maxHealth, true);
						}));
				}
				else
					sword.reviveByAreshkagal();
			}
			BurialWatchdogScepter scepter = (BurialWatchdogScepter)
					AbstractDungeon.getMonsters().getMonster(BurialWatchdogScepter.ID);
			if (scepter != null) {
				if (!scepter.halfDead) {
					int targetMaxHp = scepter.initialMaxHealth * 2;

					if (scepter.maxHealth < targetMaxHp)
						this.addToBot(new AnonymousAction(() -> {
							scepter.increaseMaxHp(targetMaxHp - scepter.maxHealth, true);
						}));
				}
				else
					scepter.reviveByAreshkagal();
			}
		}
		else if (this.nextMove == UNKNOWN)
			this.addToBot(new TextAboveCreatureAction(this, monsterStrings.DIALOG[2]));
		else if (this.nextMove == REVIVE) {
			if (this.maxHealth <= 1)
				Cthugha_Core.logger.info("Areshkagal: maxHealth <= 1");
			else {
				this.addToBot(new SFXAction("DARKLING_REGROW_" + MathUtils.random(1, 2),
						MathUtils.random(-0.1F, 0.1F)));

				this.addToBot(new HealAction(this, this, this.maxHealth / 2));
				this.addToBot(new AnonymousAction(() -> this.halfDead = false));

				this.addToBot(new ApplyPowerAction(this, this, new RegrowPower(this)));
				this.addToBot(new ApplyPowerAction(this, this, new InvinciblePower(this,
						AbstractDungeon.ascensionLevel >= 19 ? 300 : 400)));
//			this.addToBot(new ApplyPowerAction(this, this, new FourColorTheoremPower(this)));

				for (AbstractRelic relic : AbstractDungeon.player.relics)
					relic.onSpawnMonster(this);

				RevivePatch.Fields.tempHpReturnAmount.set(this, 0);
			}
		}

		this.addToBot(new RollMoveAction(this));
	}

//	public void setMove() {
//		this.setMove(this.nextMove, this.intent);
//	}

	@Override
	protected void getMove(int num) {
		if (this.halfDead) {
			if (this.maxHealth <= 1)
				this.setMove((byte) 0, Intent.NONE);
			else
				this.setMove(REVIVE, Intent.BUFF);
		}
		else {
			if (!this.firstMove) { // First move
				this.setMove(YOU_ARE_MINE, Intent.STRONG_DEBUFF);
				this.firstMove = true;
			}
			else if (this.currentHealth <= this.maxHealth / 2 && !this.foolishUsed) {
				this.setMove(FOOLISH, Intent.MAGIC);
				this.foolishUsed = true;
			}
			else {
				if (this.moveCount % 3 == 0) {
					if (AbstractDungeon.aiRng.randomBoolean())
						this.setMove(BASH, AbstractDungeon.ascensionLevel >= 19 ?
								Intent.ATTACK_DEBUFF : Intent.ATTACK, this.damage.get(0).base);
					else
						this.setMove(SOUL_STRIKE, Intent.ATTACK_BUFF,
								this.damage.get(1).base, 7, true);
				}
				else if (this.moveCount % 3 == 1) {
					if (this.lastMove(SOUL_STRIKE))
						this.setMove(BASH, AbstractDungeon.ascensionLevel >= 19 ?
								Intent.ATTACK_DEBUFF : Intent.ATTACK, this.damage.get(0).base);
					else
						this.setMove(SOUL_STRIKE, Intent.ATTACK_BUFF,
								this.damage.get(1).base, 7, true);
				}
				else
					this.setMove(STRENGTHEN, Intent.DEFEND_BUFF);

				this.moveCount++;
			}
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
					.filter(m -> m.hasPower(RegrowPower.POWER_ID))
					.allMatch(m -> m.halfDead);

			if (!allDead) {
				if (this.nextMove != UNKNOWN) {
					this.setMove(UNKNOWN, Intent.UNKNOWN);
					this.createIntent();
					this.addToBot(new SetMoveAction(this, UNKNOWN, Intent.UNKNOWN));

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

//		BurialWatchdogScepter other = AbstractDungeon.getMonsters().monsters.stream()
//				.filter(m -> m instanceof BurialWatchdogScepter)
//				.map(m -> (BurialWatchdogScepter) m)
//				.findFirst()
//				.orElse(null);
//
//		if (other != null) {
//			if (AbstractDungeon.player.hasPower(SurroundedPower.POWER_ID))
//				AbstractDungeon.player.flipHorizontal = other.drawX	< AbstractDungeon.player.drawX;
//
//			if (other.hasPower(BackAttackPower.POWER_ID))
//				this.addToBot(new RemoveSpecificPowerAction(other, other, BackAttackPower.POWER_ID));
//		}
	}

	@Override
	public void die() {
		if (!AbstractDungeon.getCurrRoom().cannotLose) {
			super.die();

			this.onBossVictoryLogic();
			this.onFinalBossVictoryLogic();
			CardCrawlGame.stopClock = true;
		}
	}

	@SpireOverride
	private boolean applyBackAttack() {
		return false;
	}
}
