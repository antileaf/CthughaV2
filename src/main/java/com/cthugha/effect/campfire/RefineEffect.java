package com.cthugha.effect.campfire;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.cthugha.Cthugha_Core;
import com.cthugha.effect.utils.AnonymousEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.*;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;

public class RefineEffect extends AbstractGameEffect {
	private boolean first = false;
	private ArrayList<Phase> queue;

	private int heal;
	private boolean waiting = false;

	public RefineEffect(ArrayList<AbstractCampfireOption> buttons) {
		queue = new ArrayList<>();

		for (AbstractCampfireOption option : buttons) {
			if (!option.usable)
				continue;

			if (option instanceof RestOption)
				queue.add(Phase.SLEEP);
			else if (option instanceof SmithOption)
				queue.add(Phase.SMITH);
			else if (option instanceof RecallOption)
				queue.add(Phase.RECALL);
			else if (option instanceof TokeOption)
				queue.add(Phase.TOKE);
			else if (option instanceof DigOption)
				queue.add(Phase.DIG);
			else if (option instanceof LiftOption)
				queue.add(Phase.LIFT);
		}

		queue.sort(Phase::compareTo);

		Cthugha_Core.logger.info("RefineEffect available options: {}", queue);

		this.duration = 0.0F;
	}

//	private void start(Phase cur) {
//		if (cur == Phase.SLEEP) {
//			this.duration = 1.5F;
//
//			CardCrawlGame.sound.play("SLEEP_BLANKET");
//
//			if (ModHelper.isModEnabled("Night Terrors")) {
//				this.heal = AbstractDungeon.player.maxHealth;
//				AbstractDungeon.player.decreaseMaxHealth(5);
//			}
//			else
//				this.heal = (int)(AbstractDungeon.player.maxHealth * 0.3F);
//
//			if (AbstractDungeon.player.hasRelic("Regal Pillow"))
//				this.heal += 15;
//		}
//		else if (cur == Phase.SMITH) {
//
//		}
//	}

//	private void end(Phase cur) {
//		if (cur == Phase.SLEEP) {
////			this.playSleepJingle();
//
//			if (AbstractDungeon.player.hasRelic("Regal Pillow"))
//				AbstractDungeon.player.getRelic("Regal Pillow").flash();
//
//			AbstractDungeon.player.heal(this.heal, false);
//
//			for (AbstractRelic r : AbstractDungeon.player.relics)
//				r.onRest();
//
//			if (AbstractDungeon.player.hasRelic("Dream Catcher")) {
//				AbstractDungeon.player.getRelic("Dream Catcher").flash();
//				ArrayList<AbstractCard> reward = AbstractDungeon.getRewardCards();
//
//				if (reward != null && !reward.isEmpty())
//					AbstractDungeon.cardRewardScreen.open(reward, null,
//							CardCrawlGame.languagePack.getUIString("CampfireSleepEffect").TEXT[0]);
//			}
//		}
//	}

	private void complete() {
		this.isDone = true;
		((RestRoom)AbstractDungeon.getCurrRoom()).fadeIn();
		AbstractRoom.waitTimer = 0.0F;
		AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
	}

	private boolean shouldContinue() {
		if (this.queue.isEmpty())
			return true;

		if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.CARD_REWARD)
			return false;
		else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID)
			return false;
		else if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD)
			return false;
		else if (AbstractDungeon.isScreenUp)
			return false;

		return true;
	}

	public void update() {
//		if (!this.first) {
//			this.first = true;
//
//			if (this.queue.isEmpty()) {
//				this.complete();
//				return;
//			}
//
//			this.start(this.queue.get(0));
//		}
//
//		if (this.waiting && this.shouldContinue()) {
//			this.waiting = false;
//			this.start(this.queue.get(0));
//		}
//
//		this.duration -= Gdx.graphics.getDeltaTime();
//		if (this.duration < 0.0F) {
//			this.end(this.queue.remove(0));
//
//			if (!this.queue.isEmpty()) {
//				if (this.shouldContinue())
//					this.start(this.queue.get(0));
//				else {
//					this.waiting = true;
//					return;
//				}
//			}
//			else
//				this.complete();
//		}

		if (!this.shouldContinue())
			return;

		this.duration -= Gdx.graphics.getDeltaTime();

		if (this.duration < 0.0F) {
			if (this.queue.isEmpty())
				this.complete();

			else {
				Phase cur = this.queue.remove(0);

				AbstractGameEffect effect;

				if (cur == Phase.SLEEP) {
					AbstractDungeon.effectsQueue.add(new AnonymousEffect(() -> CardCrawlGame.sound.play("SLEEP_BLANKET")));
					effect = new SleepEffect();
				}
				else if (cur == Phase.SMITH) {
					effect = new SmithEffect();
				}
				else if (cur == Phase.RECALL) {
					effect = new RecallEffect();
				}
				else if (cur == Phase.TOKE) {
					effect = new TokeEffect();
				}
				else if (cur == Phase.LIFT) {
					effect = new LiftEffect();
				}
				else if (cur == Phase.DIG) {
					effect = new DigEffect();
				}
				else {
					Cthugha_Core.logger.error("Unknown phase: {}", cur);
					return;
				}

				AbstractDungeon.effectsQueue.add(effect);
				this.duration = effect.duration;
			}
		}
	}

//	private void playSleepJingle() {
//		int roll = MathUtils.random(0, 2);
//		switch (AbstractDungeon.id) {
//			case "Exordium":
//				if (roll == 0) {
//					CardCrawlGame.sound.play("SLEEP_1-1");
//				} else if (roll == 1) {
//					CardCrawlGame.sound.play("SLEEP_1-2");
//				} else {
//					CardCrawlGame.sound.play("SLEEP_1-3");
//				}
//				break;
//			case "TheCity":
//				if (roll == 0) {
//					CardCrawlGame.sound.play("SLEEP_2-1");
//				} else if (roll == 1) {
//					CardCrawlGame.sound.play("SLEEP_2-2");
//				} else {
//					CardCrawlGame.sound.play("SLEEP_2-3");
//				}
//				break;
//			case "TheBeyond":
//				if (roll == 0) {
//					CardCrawlGame.sound.play("SLEEP_3-1");
//				} else if (roll == 1) {
//					CardCrawlGame.sound.play("SLEEP_3-2");
//				} else {
//					CardCrawlGame.sound.play("SLEEP_3-3");
//				}
//		}
//
//	}

	@Override
	public void render(SpriteBatch sb) {
//		sb.setColor(this.screenColor);
//		sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F,
//				(float) Settings.WIDTH, (float)Settings.HEIGHT);
		if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID)
			AbstractDungeon.gridSelectScreen.render(sb);
	}

	public void dispose() {
	}

	private enum Phase implements Comparable<Phase> {
		SLEEP,
		SMITH,
		RECALL,
		TOKE, // 1!5!
		LIFT,
		DIG
	}
}
