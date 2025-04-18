package com.cthugha.dungeons.the_tricuspid_gate.cg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.VictoryScreen;

import java.util.ArrayList;

public class CG implements Disposable {
	private static final int FRAMES_PER_SECOND = 16;

	private ArrayList<Texture> textures = new ArrayList<>();

	private float darkenTimer = 0.2F;
	private float fadeTimer = 0.2F;
	private float playTimer, maxPlayTime;

	private float fadeoutTimer = 1.0F;

	private Color screenColor;
	private Color bgColor;

	private boolean isDone = false;

	public CG() {
		for (int i = 1; i <= 28; i++)
			this.textures.add(new Texture("cthughaResources/img/dungeon/CG/CG_" + i + ".png"));

		this.maxPlayTime = (float) this.textures.size() / FRAMES_PER_SECOND;
		this.playTimer = 0.0F;

		this.bgColor = Color.WHITE.cpy();
		this.screenColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
	}

	public void update() {
		this.updateFadeOut();
		this.updateFadeIn();

		if (this.isDone) {
			this.fadeoutTimer -= Gdx.graphics.getDeltaTime();
			this.screenColor.a = 1.0F - this.fadeoutTimer;

			if (this.fadeoutTimer < 0.0F) {
				this.fadeoutTimer = 0.0F;
				this.dispose();
				this.bgColor.a = 0.0F;
				this.openVictoryScreen();
			}
		}

		if (this.darkenTimer == 0.0F && this.fadeTimer == 0.0F) {
			this.playTimer += Gdx.graphics.getDeltaTime();

			if (this.playTimer > this.maxPlayTime) {
				this.playTimer = this.maxPlayTime;
				this.isDone = true;
			}
		}
	}

	private void openVictoryScreen() {
		GameCursor.hidden = false;
		AbstractDungeon.victoryScreen = new VictoryScreen((MonsterGroup)null);
	}

	private void updateFadeIn() {
		if (this.darkenTimer == 0.0F) {
			this.fadeTimer -= Gdx.graphics.getDeltaTime();
			if (this.fadeTimer < 0.0F) {
				this.fadeTimer = 0.0F;
			}

			this.screenColor.a = this.fadeTimer;
		}

	}

	private void updateFadeOut() {
		if (this.darkenTimer != 0.0F) {
			this.darkenTimer -= Gdx.graphics.getDeltaTime();
			if (this.darkenTimer < 0.0F) {
				this.darkenTimer = 0.0F;
				this.fadeTimer = 1.0F;
			}

			this.screenColor.a = 1.0F - this.darkenTimer;
		}

	}

	public void render(SpriteBatch sb) {
	}

	public void renderAbove(SpriteBatch sb) {
		if (this.fadeoutTimer <= 0.0F)
			return;

		sb.setColor(Color.BLACK);
		sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float) Settings.WIDTH, (float)Settings.HEIGHT);

		if (this.fadeTimer != 0.0F)
			return;

		int index = (int)(this.playTimer / this.maxPlayTime * (float)this.textures.size());
		if (index >= this.textures.size())
			index = this.textures.size() - 1;

		Texture img = this.textures.get(index);
		sb.setColor(this.bgColor);
		this.renderImg(sb, img);

//		this.renderPanels(sb);
		sb.setColor(this.screenColor);
		sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
	}

	private void renderImg(SpriteBatch sb, Texture img) {
		if (Settings.isSixteenByTen) {
			sb.draw(img, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
		} else {
			sb.draw(img, 0.0F, -50.0F * Settings.scale, (float)Settings.WIDTH, (float)Settings.HEIGHT + 110.0F * Settings.scale);
		}

	}

	public void dispose() {
		this.textures.forEach(Texture::dispose);
		this.textures.clear();
	}
}
