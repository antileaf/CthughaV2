package com.cthugha.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.cthugha.object.AbstractSpirit;

@SpirePatch(cls = "com.megacrit.cardcrawl.cards.AbstractCard", method = "<class>")
public class SpiritField {
  public static SpireField<AbstractSpirit> spirit = new SpireField<>(() -> null);
}
