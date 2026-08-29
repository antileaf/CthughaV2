package com.cthugha.patches.cards.unique;

import com.cthugha.power.RiShiPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import javassist.CannotCompileException;
import javassist.CtBehavior;

public class RiShiPatch {
	@SpirePatch(clz = DamageInfo.class, method = SpirePatch.CLASS)
	public static class Fields {
		public static SpireField<Boolean> fromBurnCard = new SpireField<>(() -> false);
	}

	public static class BurnCardDamageInfo extends DamageInfo {
		public BurnCardDamageInfo(AbstractCreature damageSource, int base, DamageInfo.DamageType damageType) {
			super(damageSource, base, damageType);

			Fields.fromBurnCard.set(this, true);
		}

		public BurnCardDamageInfo(AbstractCreature owner, int base) {
			super(owner, base);

			Fields.fromBurnCard.set(this, true);
		}
	}

	@SpirePatch(clz = DamageAction.class, method = "update", paramtypez = {})
	public static class DamageActionPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				return LineFinder.findInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(MonsterGroup.class, "areMonstersBasicallyDead"));
			}
		}

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(DamageAction _inst, DamageInfo ___info) {
			if (Fields.fromBurnCard.get(___info) != null &&
					___info.owner != null &&
					___info.owner.isPlayer &&
					___info.owner.hasPower(RiShiPower.POWER_ID))
				((RiShiPower) ___info.owner.getPower(RiShiPower.POWER_ID)).trigger();
		}
	}

	// TODO: 我是懒狗，目前还没有造成 aoe 伤害的灼伤牌
}
