package com.cthugha.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cthugha.Cthugha_Core;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.ArrayList;

public class YiChiRuXiEffect extends AbstractGameEffect {
	private final int count;

	public YiChiRuXiEffect(int count) {
		this.count = count;
		this.startingDuration = this.duration = 0.5F;
	}

	@Override
	public void update() {
		this.duration -= Gdx.graphics.getDeltaTime();
		if (this.duration < 0.0F) {
			this.isDone = true;

			ArrayList<AbstractCard> burns = AbstractDungeon.player.masterDeck.group.stream()
					.filter(c -> c instanceof Burn)
					.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

			Cthugha_Core.logger.info("YiChiRuXi effect: count = {}", this.count);

			ArrayList<AbstractCard> cardsToRemove = new ArrayList<>();
			for (int i = 0; i < this.count; i++) {
				if (burns.isEmpty())
					break;

				int index = AbstractDungeon.cardRng.random(burns.size() - 1);
				burns.get(index).stopGlowing();
				cardsToRemove.add(burns.get(index));
				burns.remove(index);
			}

			float displayCount = 0.0F;
			for (AbstractCard card : cardsToRemove) {
				card.untip();
				card.unhover();
//				AbstractDungeon.topLevelEffects
//						.add(new PurgeCardEffect(card, Settings.WIDTH / 3.0F + displayCount, Settings.HEIGHT / 2.0F));
				displayCount += Settings.WIDTH / 6.0F;
				AbstractDungeon.player.masterDeck.removeCard(card);
			}

			Cthugha_Core.logger.info("YiChiRuXi effect: removed {} cards", cardsToRemove.size());
		}
	}

	@Override
	public void render(SpriteBatch sb) {

	}

	@Override
	public void dispose() {

	}
}
