package com.cthugha.patches.relic;

import com.cthugha.relics.cthugha.EverBurningStone;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.SkipCardButton;
import javassist.CannotCompileException;
import javassist.CtBehavior;

@SuppressWarnings("unused")
public class EverBurningStonePatch {
//	@SpirePatch(clz = CardRewardScreen.class, method = "onClose")
	@SpirePatch(clz = SkipCardButton.class, method = "update", paramtypez = {})
	public static class CloseCurrentScreenPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(AbstractDungeon.class, "closeCurrentScreen"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(SkipCardButton _inst) {
			if (AbstractDungeon.effectList.stream()
					.anyMatch(e -> e instanceof EverBurningStone.EverBurningStoneEffect)) {
				AbstractDungeon.effectList.stream()
						.filter(e -> e instanceof EverBurningStone.EverBurningStoneEffect)
						.forEach(e -> ((EverBurningStone.EverBurningStoneEffect) e).canceled = true);
			}
		}
	}
}
