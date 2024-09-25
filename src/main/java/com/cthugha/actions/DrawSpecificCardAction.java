package com.cthugha.actions;

import java.util.function.Predicate;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

// 抽指定的卡
public class DrawSpecificCardAction extends AbstractGameAction {

    Predicate<AbstractCard> cardFilter;
    int amount;

    public DrawSpecificCardAction(Predicate<AbstractCard> cardFilter, int amount) {
        this.cardFilter = cardFilter;
        this.amount = amount;
    }

    @Override
    public void update() {
        for (int i = 0; i < this.amount; i++) {
            AbstractCard specifiedCard = null;
            for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                if (this.cardFilter.test(c)) {
                    specifiedCard = c;
                    break;
                }
            }
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
