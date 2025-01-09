package com.cthugha.actions.common;

import java.util.function.Predicate;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.NoDrawPower;

// 抽指定的卡
public class DrawSpecificCardAction extends AbstractGameAction {

    Predicate<AbstractCard> filter;
    int amount;

    public DrawSpecificCardAction(Predicate<AbstractCard> filter, int amount) {
        this.filter = filter;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.hasPower(NoDrawPower.POWER_ID)) {
            AbstractDungeon.player.getPower(NoDrawPower.POWER_ID).flash();
            this.isDone = true;
            return;
        }

        for (int i = 0; i < this.amount; i++) {
            if (AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
                AbstractDungeon.player.createHandIsFullDialog();
                break;
            }

            AbstractCard specifiedCard = AbstractDungeon.player.drawPile.group.stream()
                    .filter(this.filter)
                    .findFirst()
                    .orElse(null);

            if (specifiedCard != null) {
                AbstractDungeon.player.drawPile.removeCard(specifiedCard); // TODO：不确定是不是应该先移除，待观察
                AbstractDungeon.player.drawPile.addToTop(specifiedCard);
                // this.addToTop(new DrawCardAction(1));
                AbstractDungeon.player.draw();
                AbstractDungeon.player.hand.refreshHandLayout();
            }
        }

        this.isDone = true;
    }
}
