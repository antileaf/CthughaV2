package com.cthugha.patches.signature;

import basemod.ReflectionHacks;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.RenderFixSwitches;
import basemod.patches.com.megacrit.cardcrawl.screens.SingleCardViewPopup.BackgroundFix;
import basemod.patches.com.megacrit.cardcrawl.screens.SingleCardViewPopup.CustomRendering;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cthugha.utils.ConfigHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

@Deprecated
@SuppressWarnings("unused")
public class BaseModPatch {
//	@SpirePatch(clz = RenderFixSwitches.RenderBgSwitch.class, method = "Prefix")
//	public static class BaseModRenderBgSwitchPatch {
//		@SpirePrefixPatch
//		public static SpireReturn<SpireReturn<?>> Prefix(AbstractCard card, SpriteBatch sb, float x, float y, Color ___renderColor) {
//			if (card instanceof Burn && ConfigHelper.useSignature())
//				return SpireReturn.Return(SpireReturn.Continue());
//			return SpireReturn.Continue();
//		}
//	}
//
//	@SpirePatch(clz = RenderFixSwitches.RenderBannerSwitch.class, method = "Prefix")
//	public static class BaseModRenderBannerSwitchPatch {
//		@SpirePrefixPatch
//		public static SpireReturn<SpireReturn<?>> Prefix(AbstractCard card, SpriteBatch sb, float x, float y, Color ___renderColor) {
//			if (card instanceof Burn && ConfigHelper.useSignature())
//				return SpireReturn.Return(SpireReturn.Continue());
//			return SpireReturn.Continue();
//		}
//	}
//
//	@SpirePatch(clz = BackgroundFix.BackgroundTexture.class, method = "Prefix")
//	public static class BaseModBackgroundTexturePatch {
//		@SpirePrefixPatch
//		public static SpireReturn<SpireReturn<?>> Prefix(Object _obj, Object sbObj) {
//			AbstractCard card = ReflectionHacks.getPrivate(_obj, SingleCardViewPopup.class, "card");
//
//			if (card instanceof Burn && ConfigHelper.useSignature())
//				return SpireReturn.Return(SpireReturn.Return());
//			return SpireReturn.Continue();
//		}
//	}
//
//	@SpirePatch(clz = CustomRendering.RenderBannerSwitch.class, method = "Prefix")
//	public static class BaseModCustomRenderingPatch {
//		@SpirePrefixPatch
//		public static SpireReturn<SpireReturn<?>> Prefix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card, float ___drawScale) {
//			if (___card instanceof Burn && ConfigHelper.useSignature())
//				return SpireReturn.Return(SpireReturn.Continue());
//			return SpireReturn.Continue();
//		}
//	}
}
