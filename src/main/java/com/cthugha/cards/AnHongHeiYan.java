package com.cthugha.cards;

import com.cthugha.actions.YanBaoAction;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

public class AnHongHeiYan extends CustomCard {

    public static final String ID = ModHelper.MakePath(AnHongHeiYan.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/暗红黑炎.png";
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public AnHongHeiYan() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 2;
        this.cardsToPreview = (AbstractCard) new BaoLie();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new MakeTempCardInHandAction(new Burn(), this.magicNumber));

        {
            this.addToBot(new AbstractGameAction() {
                public void update() {
                    AbstractCard tmp = AnHongHeiYan.this.cardsToPreview.makeStatEquivalentCopy();
                    tmp.exhaust = true;
                    tmp.rawDescription += " NL " + "消耗" + " 。";
                    tmp.initializeDescription();
                    p.hand.addToTop(tmp);
                    p.hand.refreshHandLayout();
                    this.isDone = true;
                }
            });
        }

        {
            this.addToBot(new YanBaoAction(this, new AbstractGameAction() {
                public void update() {
                    AbstractCard tmp = AnHongHeiYan.this.cardsToPreview.makeStatEquivalentCopy();
                    tmp.exhaust = true;
                    tmp.rawDescription += " NL " + "消耗" + " 。";
                    tmp.initializeDescription();
                    p.hand.addToTop(tmp);
                    p.hand.refreshHandLayout();
                    this.isDone = true;
                }
            }));
        }
    }

}
