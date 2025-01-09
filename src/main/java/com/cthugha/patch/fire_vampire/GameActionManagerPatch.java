package com.cthugha.patch.fire_vampire;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class GameActionManagerPatch {
	@SpirePatch(clz = GameActionManager.class, method = SpirePatch.CLASS)
	public static class Fields {
		public static SpireField<ArrayList<AbstractOrb>> orbsRemovedThisCombat =
				new SpireField<>(ArrayList::new);
	}
	// 更新的逻辑在 Cthugha 角色里的 removeNextOrb()

	@SpirePatch(clz = GameActionManager.class, method = "clear")
	public static class ClearPatch {
		public static void Postfix(GameActionManager _inst) {
			Fields.orbsRemovedThisCombat.get(_inst).clear();
		}
	}
}
