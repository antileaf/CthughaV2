package com.cthugha.patches.cards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class HpCostCardPatch {
	@SpirePatch(clz = AbstractCard.class, method = "renderEnergy", paramtypez = {SpriteBatch.class})
	public static class RenderEnergyPatch {

	}
}
