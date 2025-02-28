package com.cthugha.patches;

import com.cthugha.utils.EnergyHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.EnergyManager;

@SuppressWarnings("unused")
@SpirePatch(clz = EnergyManager.class, method = "use")
public class UsedEnergyPatch {
	public static void Prefix(EnergyManager _inst, int e) {
//		StaticHelper.usedEnergy += Math.min(EnergyPanel.totalCount, e);
		EnergyHelper.energyUsedThisTurn += e;
	}
}
