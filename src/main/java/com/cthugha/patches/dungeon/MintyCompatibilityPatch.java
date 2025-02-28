package com.cthugha.patches.dungeon;

import com.cthugha.dungeons.the_tricuspid_gate.monsters.Areshkagal;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.MonsterHelper;

@SuppressWarnings("unused")
public class MintyCompatibilityPatch {
	@SpirePatch(clz = MonsterHelper.class, method = "getEncounterName", paramtypez = {String.class})
	public static class GetEncounterNamePatch {
		public static String Postfix(String ret, String key) {
			if (key.equals(Areshkagal.ID))
				return Areshkagal.monsterStrings.NAME;

			return ret;
		}
	}
}
