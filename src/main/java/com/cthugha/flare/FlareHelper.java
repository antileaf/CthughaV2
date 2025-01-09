package com.cthugha.flare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Iterator;

public abstract class FlareHelper {
	public static boolean isCardQueued(AbstractCard card) {
		return AbstractDungeon.actionManager.cardQueue.stream()
				.anyMatch((item) -> item.card == card);
	}
}
