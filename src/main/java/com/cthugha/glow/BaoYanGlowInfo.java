package com.cthugha.glow;

import basemod.helpers.CardBorderGlowManager;
import com.badlogic.gdx.graphics.Color;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.helpers.BaoYanHelper;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class BaoYanGlowInfo extends CardBorderGlowManager.GlowInfo {
	@Override
	public boolean test(AbstractCard card) {
		return card instanceof AbstractCthughaCard &&
				((AbstractCthughaCard) card).canBaoYan &&
				BaoYanHelper.canTriggerBaoYanGlowCheck(card);
	}

	@Override
	public Color getColor(AbstractCard card) {
		return Color.SCARLET.cpy();
	}

	@Override
	public String glowID() {
		return ModHelper.MakePath("BaoYanGlow");
	}
}
