package com.cthugha.dungeons.the_tricuspid_gate.proceed;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.cthugha.dungeons.the_tricuspid_gate.TheTricuspidGate;
import com.cthugha.utils.CthughaHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;

public class AnotherProceedButton extends ProceedButton {
	private static final Texture texture = new Texture("cthughaResources/img/UI/proceedButton.png");

	public AnotherProceedButton() {
		super();

		ReflectionHacks.setPrivate(this, ProceedButton.class, "current_y",
				Settings.HEIGHT * 0.5F);

		((Hitbox) ReflectionHacks.getPrivate(this, ProceedButton.class, "hb")).move(
				1670.0F * Settings.xScale, Settings.HEIGHT * 0.5F);

		this.setLabel(CardCrawlGame.languagePack.getUIString(
				CthughaHelper.makeID(AnotherProceedButton.class.getSimpleName())).TEXT[0]);
	}

	@SpireOverride
	protected void renderButton(SpriteBatch sb) {
		float current_x = ReflectionHacks.getPrivate(this, ProceedButton.class, "current_x");
		float current_y = ReflectionHacks.getPrivate(this, ProceedButton.class, "current_y");
		float wavyTimer = ReflectionHacks.getPrivate(this, ProceedButton.class, "wavyTimer");

		sb.draw(texture,
				current_x - 256.0F, current_y - 256.0F,
				256.0F, 256.0F, 512.0F, 512.0F,
				Settings.scale * 1.1F + MathUtils.cos(wavyTimer) / 50.0F,
				Settings.scale * 1.1F + MathUtils.cos(wavyTimer) / 50.0F,
				0.0F, 0, 0, 512, 512,
				false, false);
	}
}
