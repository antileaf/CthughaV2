package com.cthugha.patch.debugging;

import com.cthugha.Cthugha_Core;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.UnceasingTop;
import javassist.CtBehavior;

@SuppressWarnings("unused")
public class UnceasingTopPatch {
//	@SpirePatch(clz = UnceasingTop.class, method = "onRefreshHand", paramtypez = {})
//	public static class OnRefreshHandPatch {
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws Exception {
//				return LineFinder.findInOrder(ctBehavior,
//						new Matcher.MethodCallMatcher(UnceasingTop.class, "flash"));
//			}
//		}
//
//		@SpireInsertPatch(locator = Locator.class)
//		public static void Insert(UnceasingTop __instance) {
//			Cthugha_Core.logger.info("UnceasingTop onRefreshHand, hand = {}",
//					AbstractDungeon.player.hand.group.stream()
//							.map(c -> c.cardID)
//							.reduce((a, b) -> a + ", " + b)
//							.orElse("empty")
//			);
//		}
//	}
}
