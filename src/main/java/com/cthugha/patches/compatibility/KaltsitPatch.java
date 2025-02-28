package com.cthugha.patches.compatibility;

import KaltsitMod.patches.MonsterTakeDamagePatch;
import com.cthugha.patches.burn.BurnMechanismPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SuppressWarnings("unused")
public class KaltsitPatch {
	@SpirePatch(clz = MonsterTakeDamagePatch.BurnPatch.class, method = "Prefix",
			paramtypez = {Burn.class, AbstractPlayer.class, AbstractMonster.class},
			requiredModId = "KaltsitMod")
	public static class KaltsitBurnPatchPatch { // 你干嘛哈哈哎呦
		@SpirePrefixPatch
		public static SpireReturn<SpireReturn<Void>> Prefix(Burn _inst, AbstractPlayer p, AbstractMonster m) {
			if (_inst.dontTriggerOnUseCard && BurnMechanismPatch.hasCthughaBlight())
				return SpireReturn.Return(SpireReturn.Continue());

			return SpireReturn.Continue();
		}
	}
}
