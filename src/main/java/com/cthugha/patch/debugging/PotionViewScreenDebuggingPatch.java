package com.cthugha.patch.debugging;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.cthugha.Cthugha_Core;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.screens.compendium.PotionViewScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;

@SuppressWarnings("unused")
public class PotionViewScreenDebuggingPatch {
//	@SpirePatch(clz = PotionViewScreen.class, method = "renderList")
//	public static class RenderListPatch {
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				int[] tmp = LineFinder.findAllInOrder(ctBehavior,
//						new Matcher.MethodCallMatcher(AbstractPotion.class, "labRender"));
//				return new int[]{tmp[tmp.length - 1]};
//			}
//		}
//
//		@SpireInsertPatch(locator = Locator.class, localvars = {"r"})
//		public static void Insert(PotionViewScreen _inst, AbstractPotion r) {
//			Cthugha_Core.logger.info("r.labOutlineColor: {}",
//					(Color) ReflectionHacks.getPrivate(r, AbstractPotion.class, "labOutlineColor"));
//		}
//	}
}
