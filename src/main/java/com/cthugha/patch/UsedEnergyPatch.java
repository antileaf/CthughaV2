package com.cthugha.patch;

import com.cthugha.helpers.EnergyHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.cthugha.helpers.StaticHelper;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

@SuppressWarnings("unused")
@SpirePatch(clz = EnergyManager.class, method = "use")
public class UsedEnergyPatch {
	public static void Prefix(EnergyManager _inst, int e) {
//		StaticHelper.usedEnergy += Math.min(EnergyPanel.totalCount, e);
		EnergyHelper.energyUsedThisTurn += e;
	}
}
