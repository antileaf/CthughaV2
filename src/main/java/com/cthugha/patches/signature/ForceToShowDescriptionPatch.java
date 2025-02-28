package com.cthugha.patches.signature;

import com.badlogic.gdx.Gdx;
import com.cthugha.utils.ConfigHelper;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Deprecated
@SuppressWarnings("unused")
public class ForceToShowDescriptionPatch {
//	private static final Logger logger = LogManager.getLogger(ForceToShowDescriptionPatch.class.getName());
//
//	public static class GridSelectionScreenPatches {
//		@SpirePatch(clz = GridCardSelectScreen.class, method = "update", paramtypez = {})
//		public static class UpdatePatch {
//			@SpirePostfixPatch
//			public static void Postfix(GridCardSelectScreen _inst, AbstractCard ___hoveredCard) {
//				if (ConfigHelper.useSignature() && _inst.confirmScreenUp) {
//					if (___hoveredCard instanceof Burn) {
//						SignaturePatch.forceToShowDescription(___hoveredCard);
//					}
//
//					if (_inst.upgradePreviewCard instanceof Burn) {
//						SignaturePatch.forceToShowDescription(_inst.upgradePreviewCard);
//					}
//				}
//			}
//		}
//	}
//
//	public static class HandSelectScreenPatches {
//		@SpirePatch(clz = HandCardSelectScreen.class, method = "update", paramtypez = {})
//		public static class SelectHoveredCardPatch {
//			@SpirePostfixPatch
//			public static void Postfix(HandCardSelectScreen _inst) {
//				if (ConfigHelper.useSignature()) {
//					_inst.selectedCards.group.forEach(c -> {
//						if (c instanceof Burn)
//							SignaturePatch.forceToShowDescription(c);
//					});
//
//					if (_inst.upgradePreviewCard instanceof Burn)
//						SignaturePatch.forceToShowDescription(_inst.upgradePreviewCard);
//				}
//			}
//		}
//	}
//
//	@SpirePatch(clz = ShowCardBrieflyEffect.class, method = SpirePatch.CONSTRUCTOR,
//			paramtypez = {AbstractCard.class})
//	public static class ShowCardBrieflyEffectPatch1 {
//		@SpirePostfixPatch
//		public static void Postfix(ShowCardBrieflyEffect _inst, AbstractCard card) {
//			if (card instanceof Burn && ConfigHelper.useSignature())
//				SignaturePatch.forceToShowDescription(card);
//		}
//	}
//
//	@SpirePatch(clz = ShowCardBrieflyEffect.class, method = SpirePatch.CONSTRUCTOR,
//			paramtypez = {AbstractCard.class, float.class, float.class})
//	public static class ShowCardBrieflyEffectPatch2 {
//		@SpirePostfixPatch
//		public static void Postfix(ShowCardBrieflyEffect _inst, AbstractCard card, float x, float y) {
//			if (card instanceof Burn && ConfigHelper.useSignature())
//				SignaturePatch.forceToShowDescription(card);
//		}
//	}
//
//	@SpirePatch(clz = ShowCardBrieflyEffect.class, method = "update", paramtypez = {})
//	public static class ShowCardBrieflyEffectPatch3 {
//		@SpirePrefixPatch
//		public static void Postfix(ShowCardBrieflyEffect _inst, AbstractCard ___card) {
//			if (_inst.duration - Gdx.graphics.getDeltaTime() < 0.0F &&
//					___card instanceof Burn && ConfigHelper.useSignature())
//				SignaturePatch.forceToShowDescription(___card);
//		}
//	}
//
//	@SpirePatch(clz = ShowCardAndObtainEffect.class, method = SpirePatch.CONSTRUCTOR,
//			paramtypez = {AbstractCard.class, float.class, float.class, boolean.class})
//	public static class ShowCardAndObtainEffectPatch1 {
//		@SpirePostfixPatch
//		public static void Postfix(ShowCardAndObtainEffect _inst, AbstractCard card,
//								   float x, float y, boolean convergeCards) {
//			if (card instanceof Burn && ConfigHelper.useSignature()) {
//				SignaturePatch.forceToShowDescription(card);
//			}
//		}
//	}
//
//	@SpirePatch(clz = ShowCardAndObtainEffect.class, method = "update", paramtypez = {})
//	public static class ShowCardAndObtainEffectPatch2 {
//		@SpirePrefixPatch
//		public static void Prefix(ShowCardAndObtainEffect _inst, AbstractCard ___card) {
//			if (_inst.duration - Gdx.graphics.getDeltaTime() < 0.0F &&
//					___card instanceof Burn && ConfigHelper.useSignature()) {
//				SignaturePatch.forceToShowDescription(___card);
//			}
//		}
//	}
}
