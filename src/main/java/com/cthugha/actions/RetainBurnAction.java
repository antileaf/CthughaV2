// Source code is decompiled from a .class file using FernFlower decompiler.
package com.cthugha.actions;

import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class RetainBurnAction extends AbstractGameAction {
   private static final UIStrings uiStrings;
   public static final String[] TEXT;

   public RetainBurnAction(AbstractCreature source, int amount) {
      this.setValues(AbstractDungeon.player, source, amount);
      this.actionType = ActionType.CARD_MANIPULATION;
   }

   public void update() {
      if (this.duration == 0.5F) {
         CardGroup g = new CardGroup(null);
         for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (CthughaHelper.isBurn(card)) {
               g.addToBottom(card);
            }
         }
         AbstractDungeon.gridSelectScreen.open(g, this.amount, true, TEXT[0]);
         this.addToBot(new WaitAction(0.25F));
         this.tickDuration();
      }
      if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
         for (AbstractCard card : AbstractDungeon.gridSelectScreen.selectedCards) {
            if (!card.isEthereal) {
               card.retain = true;
               card.dontTriggerOnUseCard = true;
               card.use(null, null);
               card.dontTriggerOnUseCard = false;
            }
         }
         AbstractDungeon.gridSelectScreen.selectedCards.clear();
         this.tickDuration();
         this.isDone = true;
      }

      // if (this.duration == 0.5F) {
      //    AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, false, true,
      //          false, false, true);
      //    this.addToBot(new WaitAction(0.25F));
      //    this.tickDuration();
      // } else {
      //    if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
      //       AbstractCard c;
      //       for (Iterator var1 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator(); var1
      //             .hasNext(); AbstractDungeon.player.hand.addToTop(c)) {
      //          c = (AbstractCard) var1.next();
      //          if (!c.isEthereal) {
      //             c.retain = true;
      //          }
      //       }

      //       AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
      //    }

      //    this.tickDuration();
      // }
   }

   static {
      uiStrings = CardCrawlGame.languagePack.getUIString("RetainCardsAction");
      TEXT = uiStrings.TEXT;
   }
}
