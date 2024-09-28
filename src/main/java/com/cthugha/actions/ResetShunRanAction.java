package com.cthugha.actions;

import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.cards.AbstractShunRanCard;
import com.cthugha.enums.CustomTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ResetShunRanAction extends AbstractGameAction {

    public ResetShunRanAction() {
    }

    private void reset(AbstractCard c) {
//        if (c instanceof AbstractShunRanCard) {
//            c.tags.remove(CustomTags.Shun_Ran_Triggered);
//        }
//        else
        if (c instanceof AbstractCthughaCard) {
            ((AbstractCthughaCard) c).triggeredShunRanThisTurn = false;
            ((AbstractCthughaCard) c).updateBgImg();
        }
    }

    @Override
    public void update() {
        for (CardGroup group : new CardGroup[]{
                AbstractDungeon.player.hand,
                AbstractDungeon.player.drawPile,
                AbstractDungeon.player.discardPile})
            for (AbstractCard c : group.group)
                this.reset(c);

        this.isDone = true;
    }
}
