package com.cthugha.cards;

import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

public class DaiShiYueLuo extends CustomCard {

    public static final String ID = ModHelper.MakePath(DaiShiYueLuo.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/137.png";
    private static final int COST = 3;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public DaiShiYueLuo() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 15;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.upgradeMagicNumber(-2);
        }
    }

    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        int totalCost = 0;
        for (int i = 0; i < AbstractDungeon.player.hand.group.size(); i++) {
            AbstractCard card = AbstractDungeon.player.hand.group.get(i);

            if (card.costForTurn >= 0) {
                totalCost += card.costForTurn;
            }
        }

        if (totalCost >= this.magicNumber) {
            return true;
        }

        return false;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < AbstractDungeon.player.hand.group.size(); i++) {
            AbstractCard card = AbstractDungeon.player.hand.group.get(i);

            if (card.costForTurn >= 0) {
                card.costForTurn = 0;
            }
        }
    }

}
