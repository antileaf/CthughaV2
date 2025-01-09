package com.cthugha.utils;

import com.cthugha.power.HuoZhuoLianZiPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public abstract class ZhiLiaoHelper {
	public static boolean canTriggerZhiLiao(AbstractCard card) {
		return AbstractDungeon.player.hasPower(HuoZhuoLianZiPower.POWER_ID) ||
				AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
						.noneMatch(c -> c != card && c.type == AbstractCard.CardType.ATTACK);
	}

	public static boolean canTriggerZhiLiao() {
		return canTriggerZhiLiao(null);
	}
}
