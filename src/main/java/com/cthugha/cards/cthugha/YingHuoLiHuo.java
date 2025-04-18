package com.cthugha.cards.cthugha;

import com.cthugha.actions.common.BaoYanAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.orbs.FireVampire;
import com.evacipated.cardcrawl.mod.stslib.actions.common.AllEnemyApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import com.megacrit.cardcrawl.powers.WeakPower;

public class YingHuoLiHuo extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(YingHuoLiHuo.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
//    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/yinghuolihuo.png";
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public YingHuoLiHuo() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 1;

        this.canBaoYan = true;
    }

    @Override
    public int getExtraYanZhiJing() {
        return 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ChannelAction(new FireVampire()));

//        this.addToBot(new EachYanZhiJingWeakAction(p, m, 1, false));

//        this.addToBot(new YanBaoAction(this, new DamageAction(m, new DamageInfo(p, 20, this.damageTypeForTurn),
//                AbstractGameAction.AttackEffect.BLUNT_HEAVY)));

        this.addToBot(new AllEnemyApplyPowerAction(p, this.magicNumber,
                mon -> new WeakPower(mon, this.magicNumber, false)));


        this.addToBot(new BaoYanAction(this, () -> {
            int count = 0;
            for (CardGroup group : new CardGroup[] { p.drawPile, p.hand, p.discardPile }) {
                for (AbstractCard c : group.group)
                    if (CthughaHelper.isBurn(c)) {
                        CthughaHelper.addToBuffer(new ExhaustSpecificCardAction(c, group, true));
                        count++;
                    }
            }

            CthughaHelper.addToBuffer(new DrawCardAction(p, count));
            CthughaHelper.commitBuffer();
        }));
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
