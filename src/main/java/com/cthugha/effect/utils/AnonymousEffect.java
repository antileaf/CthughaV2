package com.cthugha.effect.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class AnonymousEffect extends AbstractGameEffect {
	public AnonymousEffect(float duration, Runnable... runnables) {
		this.duration = duration;
		for (Runnable runnable : runnables)
			runnable.run();
	}

	public AnonymousEffect(Runnable... runnables) {
		this(0.0F, runnables);
	}

	public void update() {
		this.duration -= Gdx.graphics.getDeltaTime();

		if (this.duration < 0.0F)
			this.isDone = true;
	}

	@Override
	public void render(SpriteBatch sb) {
		// Do nothing
	}

	@Override
	public void dispose() {
		// Do nothing
	}
}
