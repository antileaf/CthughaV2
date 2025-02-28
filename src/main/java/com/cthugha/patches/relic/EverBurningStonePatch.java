package com.cthugha.patches.relic;

import com.cthugha.relics.cthugha.EverBurningStone;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

@SuppressWarnings("unused")
public class EverBurningStonePatch {
	@SpirePatch(clz = CardRewardScreen.class, method = "onClose")
	public static class CloseCurrentScreenPatch {
		@SpirePostfixPatch
		public static void Postfix(CardRewardScreen _inst) {
			if (AbstractDungeon.effectList.stream()
					.anyMatch(e -> e instanceof EverBurningStone.EverBurningStoneEffect)) {
				AbstractDungeon.effectList.stream()
						.filter(e -> e instanceof EverBurningStone.EverBurningStoneEffect)
						.forEach(e -> ((EverBurningStone.EverBurningStoneEffect) e).canceled = true);
			}
		}
	}
}
