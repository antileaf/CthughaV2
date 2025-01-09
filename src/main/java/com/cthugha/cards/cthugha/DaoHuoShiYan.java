package com.cthugha.cards.cthugha;

import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.power.DaoHuoShiYanPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DaoHuoShiYan extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(DaoHuoShiYan.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/蹈火誓言.png";

    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public DaoHuoShiYan() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 3;
        this.exhaust = true;
    }

    private void upgradeBurnCardsInGroup(CardGroup cardGroup) {
        for (AbstractCard c : cardGroup.group) {
            if (CthughaHelper.isBurn(c)) {
                if (cardGroup.type == CardGroup.CardGroupType.HAND)
                    c.superFlash();
                c.upgrade();
                c.applyPowers();
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        upgradeBurnCardsInGroup(p.hand);
        upgradeBurnCardsInGroup(p.drawPile);
        upgradeBurnCardsInGroup(p.discardPile);
        upgradeBurnCardsInGroup(p.exhaustPile);

        this.addToBot(new ApplyPowerAction(p, p, new DaoHuoShiYanPower(p)));
        this.addToBot(new DrawCardAction(3));
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
