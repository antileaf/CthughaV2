package com.cthugha.patch.blight;

import com.cthugha.blight.TheBurningOne;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.helpers.BlightHelper;

@SuppressWarnings("unused")
public class BlightHelperPatch {
	@SpirePatch(clz = BlightHelper.class, method = "getBlight", paramtypez = {String.class})
	public static class GetBlightPatch {
		@SpirePrefixPatch
		public static SpireReturn<AbstractBlight> Prefix(String id) {
			if (id.equals(TheBurningOne.ID))
				return SpireReturn.Return(new TheBurningOne());

			return SpireReturn.Continue();
		}
	}
}
