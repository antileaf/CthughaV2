package com.cthugha.cards;

import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

public class PoMieZhiZhi extends CustomCard {

    public static final String ID = ModHelper.MakePath(PoMieZhiZhi.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/破灭之枝.png";
    private static final int COST = 2;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public PoMieZhiZhi() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.isInnate = true;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = 0;
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (card.type == CardType.STATUS || card.type == CardType.CURSE) {
                this.addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.discardPile));
                count++;
            }
        }
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (card.type == CardType.STATUS || card.type == CardType.CURSE) {
                this.addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.drawPile));
                count++;
            }
        }
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.type == CardType.STATUS || card.type == CardType.CURSE) {
                this.addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
                count++;
            }
        }

        for (int i = 0; i < count; i++) {
            this.addToBot(new MakeTempCardInHandAction(new Burn()));
        }

    }

}
