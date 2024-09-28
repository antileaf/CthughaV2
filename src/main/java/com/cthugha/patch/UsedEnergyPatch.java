package com.cthugha.patch;

import com.cthugha.helpers.EnergyHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.cthugha.helpers.StaticHelper;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

@SpirePatch(clz = EnergyPanel.class, method = "useEnergy")
public class UsedEnergyPatch {
	public static void Prefix(int e) {
//		StaticHelper.usedEnergy += Math.min(EnergyPanel.totalCount, e);
		EnergyHelper.energyUsedThisTurn += e;
	}
}
