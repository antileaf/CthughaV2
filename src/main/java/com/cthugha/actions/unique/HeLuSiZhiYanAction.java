package com.cthugha.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class HeLuSiZhiYanAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("ReprogramAction");

    public static final String[] TEXT = uiStrings.TEXT;

    private float startingDuration;

    public HeLuSiZhiYanAction(int numCards) {
        this.amount = numCards;
        if (AbstractDungeon.player.hasRelic("GoldenEye")) {
            AbstractDungeon.player.getRelic("GoldenEye").flash();
            this.amount += 2;
        }
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
    }

    public void update() {
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.isDone = true;
            return;
        }
        if (this.duration == this.startingDuration) {
            for (AbstractPower p : AbstractDungeon.player.powers)
                p.onScry();
            if (AbstractDungeon.player.drawPile.isEmpty()) {
                this.isDone = true;
                return;
            }
            CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            if (this.amount != -1) {
                for (int i = 0; i < Math.min(this.amount, AbstractDungeon.player.drawPile.size()); i++)
                    tmpGroup.addToTop(AbstractDungeon.player.drawPile.group
                            .get(AbstractDungeon.player.drawPile.size() - i - 1));
            } else {
                for (AbstractCard c : AbstractDungeon.player.drawPile.group)
                    tmpGroup.addToBottom(c);
            }
            AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, true, TEXT[0]);
        } else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                int index = AbstractDungeon.player.drawPile.group.indexOf(c);
                if (index != -1) {
                    AbstractDungeon.player.drawPile.group.add(index, new Burn());
                    AbstractDungeon.player.drawPile.moveToDiscardPile(c);
                    if (AbstractDungeon.player.hand.size() < 10) {
                        AbstractDungeon.player.discardPile.moveToHand(c);
                    }
                }
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group)
            c.triggerOnScry();
        tickDuration();

    }
}
