package com.cthugha.patches.cards.unique;

import com.cthugha.cards.cthugha.Chixiao;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SuppressWarnings("unused")
public class ChixiaoPatch {
	@SpirePatch(clz = AbstractPlayer.class, method = "useCard",
			paramtypez = {AbstractCard.class, AbstractMonster.class, int.class})
	public static class UseCardPatch {
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getClassName().equals(EnergyManager.class.getName()) && m.getMethodName().equals("use"))
						m.replace("{ if (!(c instanceof " + Chixiao.class.getName() +
								")) { $_ = $proceed($$); } }");
				}
			};
		}
	}

//	@SpirePatch(clz = AbstractPlayer.class, method = "channelOrb", paramtypez = {AbstractOrb.class})
//	public static class ChannelOrbPatch {
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				return LineFinder.findInOrder(ctBehavior,
//						new Matcher.MethodCallMatcher(AbstractOrb.class, "applyFocus"));
//			}
//
//			@SpireInsertPatch(locator = Locator.class)
//			public static void Insert(AbstractPlayer _inst, AbstractOrb orb) {
//				Cthugha_Core.logger.info("Channeling orb: {}", orb.getClass().getSimpleName());
//
//				if (orb instanceof YanZhiJing)
//					Chixiao.updateAll();
//			}
//		}
//	}

}
