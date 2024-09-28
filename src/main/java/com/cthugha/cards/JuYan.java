package com.cthugha.cards;

import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.BaoYanHelper;
import com.cthugha.helpers.ModHelper;
import com.cthugha.helpers.StaticHelper;
import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

public class JuYan extends AbstractCthughaCard {

    public static final String ID = ModHelper.MakePath(JuYan.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/133.png";

    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = AbstractCard.CardTarget.NONE;

    public JuYan() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DecreaseMaxOrbAction(1));
//        StaticHelper.canBaoYanNums--;
//        if (StaticHelper.canBaoYanNums < 0) {
//            StaticHelper.canBaoYanNums = 0;
//        }
        this.addToBot(new AnonymousAction(() -> {
            BaoYanHelper.threshold = Math.max(0, BaoYanHelper.threshold - 1);
        }));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(0);
            this.initializeDescription();
        }
    }
}
