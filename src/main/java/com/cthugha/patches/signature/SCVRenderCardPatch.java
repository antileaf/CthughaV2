package com.cthugha.patches.signature;

import basemod.ReflectionHacks;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreviewRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.cthugha.utils.ConfigHelper;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CannotCompileException;
import javassist.CtBehavior;

@Deprecated
@SuppressWarnings("unused")
public class SCVRenderCardPatch {
//	private static void renderHelper(SingleCardViewPopup scv, SpriteBatch sb,
//									 float x, float y, TextureAtlas.AtlasRegion img) {
//		ReflectionHacks.privateMethod(SingleCardViewPopup.class, "renderHelper",
//				SpriteBatch.class, float.class, float.class, TextureAtlas.AtlasRegion.class)
//				.invoke(scv, sb, x, y, img);
//	}
//
////	@SpirePatch(clz = SingleCardViewPopup.class, method = "loadPortraitImg", paramtypez = {})
////	public static class LoadPortraitImgPatch {
////		@SpirePostfixPatch
////		public static void Postfix(SingleCardViewPopup _inst, AbstractCard ___card) {
////			if (___card instanceof Burn) {
////				String sig = ((AbstractAliceCard) ___card).getSignaturePortraitImgPath();
////				Fields.signature.set(_inst, SignatureHelper.load(sig));
////			}
////		}
////	}
////
////	@SpirePatch(clz = SingleCardViewPopup.class, method = "close", paramtypez = {})
////	public static class ClosePatch {
////		@SpirePostfixPatch
////		public static void Postfix(SingleCardViewPopup _inst) {
////			if (Fields.signature.get(_inst) != null) {
////				Fields.signature.get(_inst).getTexture().dispose();
////				Fields.signature.set(_inst, null);
////			}
////		}
////	}
//
//	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderPortrait", paramtypez = {SpriteBatch.class})
//	public static class RenderPortraitPatch {
//		@SpirePrefixPatch
//		public static SpireReturn<Void> Prefix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
//			if (___card.isLocked)
//				return SpireReturn.Continue();
//
//			if (___card instanceof Burn && ConfigHelper.useSignature()) {
//				renderHelper(_inst, sb, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F,
//						___card.type == AbstractCard.CardType.STATUS ?
//								SignaturePatch.burnPortrait1024 : SignaturePatch.burnAttackPortrait1024);
//
//				if (!SCVPanelPatch.Fields.hideDesc.get(_inst))
//					renderHelper(_inst, sb, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F,
//									SignaturePatch.descriptionShadow1024);
//
//				return SpireReturn.Return(null);
//			}
//
//			return SpireReturn.Continue();
//		}
//	}
//
//	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderFrame", paramtypez = {SpriteBatch.class})
//	public static class RenderFramePatch {
//		@SpirePrefixPatch
//		public static SpireReturn<Void> Prefix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
//			if (___card instanceof Burn && ConfigHelper.useSignature()) {
//				TextureAtlas.AtlasRegion frame = ___card.type == AbstractCard.CardType.STATUS ?
//						SignaturePatch.statusFrame1024 : SignaturePatch.attackFrame1024;
//
//				renderHelper(_inst, sb, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, frame);
//
//				return SpireReturn.Return();
//			}
//
//			return SpireReturn.Continue();
//		}
//	}
//
//	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderCardBanner", paramtypez = {SpriteBatch.class})
//	public static class RenderCardBannerPatch {
//		@SpirePrefixPatch
//		public static SpireReturn<Void> Prefix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
//			if (___card instanceof Burn && ConfigHelper.useSignature())
//				return SpireReturn.Return();
//
//			return SpireReturn.Continue();
//		}
//	}
//
//	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderCardTypeText", paramtypez = {SpriteBatch.class})
//	public static class RenderCardTypeTextPatch {
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				return LineFinder.findInOrder(ctBehavior,
//						new Matcher.MethodCallMatcher(FontHelper.class, "renderFontCentered"));
//			}
//		}
//
//		@SpireInsertPatch(locator = Locator.class, localvars = {"label"})
//		public static SpireReturn<Void> Insert(SingleCardViewPopup _inst, SpriteBatch sb,
//											   AbstractCard ___card, String label) {
//			if (___card instanceof Burn && ConfigHelper.useSignature()) {
//				Color cardTypeColor = ReflectionHacks.getPrivateStatic(SingleCardViewPopup.class,
//						"CARD_TYPE_COLOR");
//
//				FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, label,
//						(float)Settings.WIDTH / 2.0F,
//						(float)Settings.HEIGHT / 2.0F - 392.0F * Settings.scale,
//						cardTypeColor);
//
//				return SpireReturn.Return();
//			}
//
//			return SpireReturn.Continue();
//		}
//	}
//
//	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescription", paramtypez = {SpriteBatch.class})
//	public static class RenderDescriptionPatch {
//		@SpirePrefixPatch
//		public static SpireReturn<Void> Prefix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
//			if (___card instanceof Burn &&
//					ConfigHelper.useSignature() &&
//					SCVPanelPatch.Fields.hideDesc.get(_inst))
//				return SpireReturn.Return();
//
//			return SpireReturn.Continue();
//		}
//	}
//
//	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderDescriptionCN", paramtypez = {SpriteBatch.class})
//	public static class RenderDescriptionCNPatch {
//		@SpirePrefixPatch
//		public static SpireReturn<Void> Prefix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
//			if (___card instanceof Burn &&
//					ConfigHelper.useSignature() &&
//					SCVPanelPatch.Fields.hideDesc.get(_inst))
//				return SpireReturn.Return();
//
//			return SpireReturn.Continue();
//		}
//	}
//
//	@SpirePatch(clz = SingleCardViewPopup.class, method = "renderTips", paramtypez = {SpriteBatch.class})
//	public static class RenderTipsPatch {
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				return LineFinder.findInOrder(ctBehavior,
//						new Matcher.MethodCallMatcher(AbstractCard.class,
//								"renderCardPreviewInSingleView"));
//			}
//		}
//
//		@SpireInsertPatch(locator = Locator.class)
//		public static void Insert(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
//			if (___card.cardsToPreview instanceof Burn && ConfigHelper.useSignature())
//				SignaturePatch.setPreviewTransparency(___card.cardsToPreview,
//						SCVPanelPatch.Fields.hideDesc.get(_inst) ? 0.0F : 1.0F);
//		}
//	}
//
//	@SpirePatch(clz = MultiCardPreviewRenderer.RenderMultiCardPreviewInSingleViewPatch.class,
//			method = "Postfix", paramtypez = {SingleCardViewPopup.class, SpriteBatch.class})
//	public static class BaseModMultiCardPreviewSCVPatch {
//		@SpirePrefixPatch
//		public static void Prefix(SingleCardViewPopup _inst, SpriteBatch sb) {
//			AbstractCard card = ReflectionHacks.getPrivate(_inst, SingleCardViewPopup.class, "card");
//
//			if (MultiCardPreview.multiCardPreview.get(card) != null)
//				for (AbstractCard preview : MultiCardPreview.multiCardPreview.get(card))
//					if (preview instanceof Burn && ConfigHelper.useSignature())
//						SignaturePatch.setPreviewTransparency(preview,
//								SCVPanelPatch.Fields.hideDesc.get(_inst) ? 0.0F : 1.0F);
//		}
//	}
}
