package com.cthugha.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.cthugha.object.AbstractSpirit;
import com.cthugha.object.GiveFire;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class AbstractCardPatch {

    @SpirePatch(clz = AbstractCard.class, method = "triggerOnExhaust")
    public static class MeteorStrikePatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractCard _inst) {
            AbstractSpirit spirit = SpiritField.spirit.get(_inst);
            if (spirit != null) {
                if (spirit instanceof GiveFire) {
                    spirit.onUse();
                }
            }
        }
    }
}
