package com.cthugha.cards.cthugha;


import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.effect.YiChiRuXiEffect;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.OnObtainCard;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class YiChiRuXi extends AbstractCthughaCard implements OnObtainCard {

    public static final String ID = CthughaHelper.makeID(YiChiRuXi.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/一赤如烍.png";
    private static final int COST = -2;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = AbstractCard.CardTarget.NONE;

    public YiChiRuXi() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void onObtainCard() {
        AbstractDungeon.topLevelEffectsQueue.add(new YiChiRuXiEffect(this.magicNumber));
    }

    @Override
    public void triggerWhenDrawn() {
        addToTop(new MakeTempCardInHandAction(new Burn(), this.magicNumber));
        addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(2);
            this.initializeDescription();
        }
    }
}
