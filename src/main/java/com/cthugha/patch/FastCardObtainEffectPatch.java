package com.cthugha.patch;

import java.lang.reflect.Field;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.cthugha.cards.YiChiRuXi;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.FastCardObtainEffect;

public class FastCardObtainEffectPatch {
    @SpirePatch(clz = FastCardObtainEffect.class, method = "update")
    public static class FastCardObtainEffectaa {
        public static void Postfix(FastCardObtainEffect _inst) throws Exception {
            Field cardField = _inst.getClass().getDeclaredField("card");
            cardField.setAccessible(true);
            AbstractCard card = (AbstractCard) cardField.get(_inst);
            if (card != null) {
                if (card.cardID == YiChiRuXi.ID) {
                    for (int i = 0; i < card.magicNumber; i++) {
                        AbstractDungeon.player.masterDeck.removeCard("Burn");
                    }
                }
            } else {
            }
        }
    }
}
