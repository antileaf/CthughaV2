package com.cthugha.cards.cthugha;

import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.cthugha.actions.common.BaoYanAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AnHongHeiYan extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(AnHongHeiYan.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/暗红黑炎.png";

    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public AnHongHeiYan() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 2;

        this.canBaoYan = true;

        this.cardsToPreview = new BaoLie();
        CardModifierManager.addModifier(this.cardsToPreview, new ExhaustMod());
    }

    private AbstractGameAction getAction() {
        AbstractCard card = new BaoLie();
        CardModifierManager.addModifier(card, new ExhaustMod());
        return new MakeTempCardInHandAction(card);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new MakeTempCardInHandAction(new Burn(), this.magicNumber));

        this.addToBot(this.getAction());
        this.addToBot(new BaoYanAction(this, this.getAction()));

//        {
//            this.addToBot(new AbstractGameAction() {
//                public void update() {
//                    AbstractCard tmp = AnHongHeiYan.this.cardsToPreview.makeStatEquivalentCopy();
//                    tmp.exhaust = true;
//                    tmp.rawDescription += " NL " + "消耗" + " 。";
//                    tmp.initializeDescription();
//                    p.hand.addToTop(tmp);
//                    p.hand.refreshHandLayout();
//                    this.isDone = true;
//                }
//            });
//        }
//
//        {
//            this.addToBot(new YanBaoAction(this, new AbstractGameAction() {
//                public void update() {
//                    AbstractCard tmp = AnHongHeiYan.this.cardsToPreview.makeStatEquivalentCopy();
//                    tmp.exhaust = true;
//                    tmp.rawDescription += " NL " + "消耗" + " 。";
//                    tmp.initializeDescription();
//                    p.hand.addToTop(tmp);
//                    p.hand.refreshHandLayout();
//                    this.isDone = true;
//                }
//            }));
//        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.initializeDescription();
        }
    }
}
