package com.cthugha.dungeons.the_tricuspid_gate.confirm;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cthugha.dungeons.the_tricuspid_gate.TheTricuspidGate;
import com.cthugha.utils.ConfigHelper;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

public class ConfirmCheckbox {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(
			CthughaHelper.makeID(ConfirmCheckbox.class.getSimpleName()));

	public static ConfirmCheckbox inst;

	private Hitbox hb;
//	private Texture box, tick;

	public ConfirmCheckbox() {
		hb = new Hitbox(320.0F * Settings.scale, 80.0F * Settings.scale);

//		box = ImageMaster.loadImage("images/ui/checkbox.png");
//		tick = ImageMaster.loadImage("images/ui/tick.png");
	}

	private boolean check() {
		return CthughaHelper.isInBattle() && CardCrawlGame.dungeon instanceof TheTricuspidGate;
	}

	public void update() {
		if (!this.check())
			return;

		if (!AbstractDungeon.isScreenUp) {
			hb.move(160.0F * Settings.scale, 0.3F * Settings.HEIGHT);

			hb.update();

			if (hb.justHovered)
				CardCrawlGame.sound.play("UI_HOVER");

			if (hb.hovered && InputHelper.justClickedLeft) {
				CardCrawlGame.sound.play("UI_CLICK_1");
				hb.clickStarted = true;
			}

			if (hb.clicked) {
				ConfigHelper.setSecondConfirm(!ConfigHelper.secondConfirm());
				hb.clicked = false;
			}
		}
	}

	public void render(SpriteBatch sb) {
		if (!this.check())
			return;

		sb.setColor(Color.WHITE);

		sb.draw(ImageMaster.CHECKBOX,
				hb.cX - 120.0F * Settings.scale - 32.0F,
				hb.cY - 32.0F,
				32.0F, 32.0F, 64.0F, 64.0F,
				Settings.scale, Settings.scale,
				0.0F, 0, 0, 64, 64,
				false, false);

		if (ConfigHelper.secondConfirm())
			sb.draw(ImageMaster.TICK,
					hb.cX - 120.0F * Settings.scale - 32.0F,
					hb.cY - 32.0F,
					32.0F, 32.0F, 64.0F, 64.0F,
					Settings.scale, Settings.scale,
					0.0F, 0, 0, 64, 64,
					false, false);

		FontHelper.renderFont(sb, FontHelper.panelEndTurnFont, uiStrings.TEXT[0],
				hb.cX - 85.0F * Settings.scale,
				hb.cY + 10.0F * Settings.scale,
				this.hb.hovered ? Settings.BLUE_TEXT_COLOR : Settings.GOLD_COLOR);

		hb.render(sb);
	}
}
