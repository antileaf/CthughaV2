package com.cthugha.helpers;

public abstract class EnergyHelper {
	public static int energyUsedThisTurn = 0;

	public static void initPreBattle() {
		energyUsedThisTurn = 0;
	}

	public static void clearPostBattle() {
		energyUsedThisTurn = 0;
	}

	public static void resetValuesPostEnergyRecharge() {
		energyUsedThisTurn = 0;
	}
}
