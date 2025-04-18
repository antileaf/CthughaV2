package com.cthugha.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class StatsScreenEffect extends AbstractGameEffect {
	private static final Texture img = new Texture("cthughaResources/img/UI/stats.png");

	public StatsScreenEffect() {
		this.duration = 2.0F;
		this.color = Color.WHITE.cpy();
	}

	@Override
	public void update() {
		this.duration -= Gdx.graphics.getDeltaTime();

		if (this.duration < 0.0F)
			this.isDone = true;
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setColor(this.color);
		sb.draw(img, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
		sb.setBlendFunction(770, 771);
	}

	@Override
	public void dispose() {

	}
}
