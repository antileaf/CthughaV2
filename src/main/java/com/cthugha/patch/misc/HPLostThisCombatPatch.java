package com.cthugha.patch.misc;

import com.cthugha.Cthugha_Core;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

@SuppressWarnings("unused")
public class HPLostThisCombatPatch {
	// Reset in Cthugha_Core
	public static int HPLostThisCombat = 0;

	@SpirePatch(clz = AbstractPlayer.class, method = "damage", paramtypez = {DamageInfo.class})
	public static class DamagePatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractPlayer _inst, DamageInfo info) {
			Cthugha_Core.logger.info("DamagePatch.Postfix: {}", _inst.lastDamageTaken);

			HPLostThisCombat += _inst.lastDamageTaken;
		}
	}
}
