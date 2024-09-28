package com.cthugha.helpers;

public abstract class EnergyHelper {
	public static int energyUsedThisTurn = 0;

	public static void resetValuesPostEnergyRecharge() {
		energyUsedThisTurn = 0;
	}
}
