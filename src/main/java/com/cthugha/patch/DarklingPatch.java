package com.cthugha.patch;

import com.cthugha.Cthugha_Core;
import com.cthugha.power.ShengMingFanHuanPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class DarklingPatch {
	@SpirePatch(clz = Darkling.class, method = SpirePatch.CLASS)
	public static class Fields {
		public static SpireField<Boolean> cannotRevive = new SpireField<>(() -> false);
		public static SpireField<Integer> tempHpReturnAmount = new SpireField<>(() -> 0);
	}
	// 喜欢特判小黑的矢野你好啊，我是雷军

	@SpirePatch(clz = Darkling.class, method = "damage", paramtypez = {DamageInfo.class})
	public static class BeforeClearPowersPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws Exception {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(ArrayList.class, "clear"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(Darkling _inst, DamageInfo info) {
			if (_inst.hasPower(ShengMingFanHuanPower.POWER_ID)) {
				Cthugha_Core.logger.info("Has ShengMingFanHuanPower, setting SpireField");
				Fields.tempHpReturnAmount.set(_inst, _inst.getPower(ShengMingFanHuanPower.POWER_ID).amount);
			}
		}
	}

	@SpirePatch(clz = Darkling.class, method = "takeTurn")
	public static class TakeTurnPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(Darkling _inst) {
			if (_inst.maxHealth <= 1 && Fields.cannotRevive.get(_inst))
				return SpireReturn.Return();

			if (_inst.nextMove == 5) {
				if (Fields.tempHpReturnAmount.get(_inst) != 0)
					Cthugha_Core.logger.info("Resetting hasTempHPReturnPower");

				Fields.cannotRevive.set(_inst, false); // 不用 loadout 应该触发不了这个
				Fields.tempHpReturnAmount.set(_inst, 0);
			}

			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = Darkling.class, method = "getMove", paramtypez = {int.class})
	public static class GetMovePatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(Darkling _inst, int num) {
			if (_inst.maxHealth <= 1 && Fields.cannotRevive.get(_inst)) {
				_inst.setMove((byte) 0, AbstractMonster.Intent.NONE);
				return SpireReturn.Return();
			}
			return SpireReturn.Continue();
		}
	}

	@SpirePatch(clz = MonsterGroup.class, method = "queueMonsters", paramtypez = {})
	public static class QueueMonstersPatch {
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("add"))
//						m.replace("if (!(m instanceof " + Darkling.class.getName() + ") || m.maxHealth > 1) { $_ = $proceed($$); }");
						m.replace("if (m instanceof " + Darkling.class.getName() +
								" && m.maxHealth <= 1 && ((Boolean) " + DarklingPatch.class.getName() +
								".Fields.cannotRevive.get(m)).booleanValue()) { } else { $_ = $proceed($$); }");
				}
			};
		}
	}
}
