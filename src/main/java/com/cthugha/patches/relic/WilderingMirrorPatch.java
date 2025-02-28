package com.cthugha.patches.relic;

import com.cthugha.Cthugha_Core;
import com.cthugha.relics.cthugha.WilderingMirror;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.CtBehavior;

@SuppressWarnings("unused")
public class WilderingMirrorPatch {
	@SpirePatch(clz = AbstractMonster.class, method = "damage", paramtypez = {DamageInfo.class})
	public static class DamagePatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.FieldAccessMatcher(AbstractMonster.class, "lastDamageTaken"));
			}
		}

		@SpireInsertPatch(locator = Locator.class, localvars = {"damageAmount"})
		public static void Insert(AbstractMonster _inst, DamageInfo info, @ByRef int[] damageAmount) {
			if (damageAmount[0] > 0 && AbstractDungeon.player != null &&
					AbstractDungeon.player.hasRelic(WilderingMirror.ID)) {
				Cthugha_Core.logger.info("WilderingMirrorPatch: Dealing {} extra damage",
						AbstractDungeon.player.getRelic(WilderingMirror.ID).counter);

				damageAmount[0] += AbstractDungeon.player.getRelic(WilderingMirror.ID).counter;
			}
		}
	}
}
