package com.cthugha.cards.cthugha;

import com.cthugha.actions.ZhiLiaoAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.power.HuoZhuoLianZiPower;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class HuoZhuoLianZi extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(HuoZhuoLianZi.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/118.png";
    private static final int COST = 2;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public HuoZhuoLianZi() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.magicNumber = this.baseMagicNumber = 15;
        this.shunRan = this.baseShunRan = 2;

        this.canZhiLiao = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ZhiLiaoAction(this, new AddTemporaryHPAction(p, p, this.magicNumber)));
    }

    @Override
    public void onShunRan(int level) {
        if (level >= this.shunRan) {
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                    new HuoZhuoLianZiPower(AbstractDungeon.player, 1), 1));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(5);
            this.initializeDescription();
        }
    }
}
