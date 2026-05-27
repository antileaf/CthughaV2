package com.cthugha.patches.cards.unique;

import com.cthugha.cards.colorless.BloodboonRitual;
import com.cthugha.object.AbstractSpirit;
import com.cthugha.patches.SpiritField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SuppressWarnings("unused")
public class BloodboonRitualPatch {
	@SpirePatch(clz = AbstractPlayer.class, method = "useCard",
			paramtypez = {AbstractCard.class, AbstractMonster.class, int.class})
	public static class UseCardPatch {
		@SpirePrefixPatch
		public static void Prefix(AbstractPlayer _inst, AbstractCard c, AbstractMonster monster, int energyOnUse) {
			AbstractSpirit spirit = SpiritField.spirit.get(c);
			if (spirit != null)
				spirit.onUse();
		}
		
		@SpireInstrumentPatch
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getClassName().equals(EnergyManager.class.getName()) &&
							m.getMethodName().equals("use"))
						m.replace("{ if (!c.cardID.equals(" +
								BloodboonRitual.class.getName() + ".ID)) { $_ = $proceed($$); } }");
				}
			};
		}
	}
}
