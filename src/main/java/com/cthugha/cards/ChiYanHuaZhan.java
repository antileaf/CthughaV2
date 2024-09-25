package com.cthugha.cards;

import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import basemod.abstracts.CustomCard;

public class ChiYanHuaZhan extends CustomCard {

    public static final String ID = ModHelper.MakePath(ChiYanHuaZhan.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/炽焰华斩.png";
    private static final int COST = 2;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = AbstractCardEnum.MOD_NAME_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public ChiYanHuaZhan() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.damage = this.baseDamage = 7;
        this.magicNumber = this.baseMagicNumber = 2;

        AbstractCard card = HuoYanHuXi.getAttckBurn();
        card.upgrade();
        this.cardsToPreview = card;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.upgradeDamage(3);
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage)));

        AbstractPower power = p.getPower(StrengthPower.POWER_ID);
        if (power != null) {
            if (power.amount >= this.magicNumber) {
            } else {
                power.amount = this.magicNumber;
            }
        } else if (power == null) {
            this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber)));
        }

        this.addToBot(new AbstractGameAction() {
            public void update() {
                for (int i = 0; i < 2; i++) {
                    // // 超出手牌上限
                    // if (p.hand.size() >= 10) {
                    //     break;
                    // }

                    AbstractCard tmp = HuoYanHuXi.getAttckBurn();
                    tmp.upgrade();
                    p.hand.addToTop(tmp);
                    p.hand.refreshHandLayout();
                }
                this.isDone = true;
            }
        });
    }

}
