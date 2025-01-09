package com.cthugha.utils;

import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.cards.cthugha.FenJi;
import com.cthugha.orbs.FireVampire;
import com.cthugha.power.FenJiPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public abstract class BaoYanHelper {
	public static int threshold = 6;

	public static void initPreBattle() {
		threshold = 6;
	}

	public static void clearPostBattle() {
		threshold = 6;
	}

	private static boolean checkBaoYan(AbstractCard c, boolean countExtraYanZhiJing) {
		if (!(c instanceof AbstractCthughaCard))
			return false;

		AbstractCthughaCard card = (AbstractCthughaCard) c;

		if (!card.canBaoYan)
			return false;

		int count = (int) AbstractDungeon.player.orbs.stream()
				.filter(o -> o instanceof FireVampire)
				.count();

		if (countExtraYanZhiJing)
			count += card.getExtraYanZhiJing();

		if (count < threshold)
			return false;

		if (AbstractDungeon.player.hasPower(FenJiPower.POWER_ID) && !(card instanceof FenJi))
			return false;

		return true;
	}

	public static boolean canTriggerBaoYanGlowCheck(AbstractCard c) {
		return checkBaoYan(c, true);
	}

	public static boolean canTriggerBaoYanOnUse(AbstractCard c) { // 在判断的同时顺便设置 triggeredBaoYanLastTime
		boolean result = checkBaoYan(c, false);

		if (c instanceof AbstractCthughaCard) {
			AbstractCthughaCard card = (AbstractCthughaCard) c;
			card.triggeredBaoYanLastTime = result;

			if (card.canBaoYan && AbstractDungeon.player.orbs.stream()
					.filter(o -> o instanceof FireVampire)
					.count() >= threshold &&
					AbstractDungeon.player.hasPower(FenJiPower.POWER_ID) &&
					!(card instanceof FenJi)) {
				AbstractDungeon.player.getPower(FenJiPower.POWER_ID).flash();
			}
		}

		return result;
	}
}
