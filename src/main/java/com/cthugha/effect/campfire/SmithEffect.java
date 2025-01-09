package com.cthugha.effect.campfire;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

public class SmithEffect extends AbstractGameEffect {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("CampfireSmithEffect");
	private static final float DUR = 1.5F;
	private boolean openedScreen = false;
	private Color screenColor;

	public SmithEffect() {
		this.screenColor = AbstractDungeon.fadeColor.cpy();
		this.duration = 1.5F;
		this.screenColor.a = 0.0F;
		AbstractDungeon.overlayMenu.proceedButton.hide();
	}

	public void update() {
		if (!AbstractDungeon.isScreenUp) {
			this.duration -= Gdx.graphics.getDeltaTime();
			this.updateBlackScreenColor();
		}

		if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() &&
				AbstractDungeon.gridSelectScreen.forUpgrade) {
			for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
				AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
				c.upgrade();
				AbstractDungeon.player.bottledCardUpgradeCheck(c);
				AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));

				++CardCrawlGame.metricData.campfire_upgraded;
			}

			AbstractDungeon.gridSelectScreen.selectedCards.clear();
			((RestRoom) AbstractDungeon.getCurrRoom()).fadeIn();
		}

		if (this.duration < 1.0F && !this.openedScreen) {
			this.openedScreen = true;
			AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getUpgradableCards(), 1,
					uiStrings.TEXT[0], true, false, false, false);

			for (AbstractRelic r : AbstractDungeon.player.relics)
				r.onSmith();
		}

		if (this.duration < 0.0F)
			this.isDone = true;
	}

	private void updateBlackScreenColor() {
		if (this.duration > 1.0F) {
			this.screenColor.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - 1.0F) * 2.0F);
		} else {
			this.screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / 1.5F);
		}

	}

	public void render(SpriteBatch sb) {
		sb.setColor(this.screenColor);
		sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F,
				(float) Settings.WIDTH, (float)Settings.HEIGHT);
		if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID)
			AbstractDungeon.gridSelectScreen.render(sb);
	}

	public void dispose() {
	}
}
