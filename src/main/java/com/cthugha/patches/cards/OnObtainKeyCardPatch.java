package com.cthugha.patches.cards;

import basemod.ReflectionHacks;
import com.cthugha.cards.interfaces.OnObtainKeyCard;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;

@SuppressWarnings("unused")
public class OnObtainKeyCardPatch {
	@SpirePatch(clz = ObtainKeyEffect.class, method = "update", paramtypez = {})
	public static class UpdatePatch {
		@SpirePostfixPatch
		public static void Postfix(ObtainKeyEffect _inst) {
			if (_inst.isDone)
				for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
					if (c instanceof OnObtainKeyCard)
						((OnObtainKeyCard) c).onObtainKey(ReflectionHacks.getPrivate(
								_inst, ObtainKeyEffect.class, "keyColor"));
		}
	}
}
