package com.cthugha.cards.cthugha;

import com.cthugha.actions.DecreaseMonsterMaxHealthAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.AutoAdd;

@Deprecated
@AutoAdd.Ignore
public class LiaoRanLuHuo extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(LiaoRanLuHuo.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/017.png";
    private static final int COST = 4;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private int rawCost = COST;

    public LiaoRanLuHuo() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();

            this.upgradeBaseCost(3);
            rawCost = this.cost;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DecreaseMonsterMaxHealthAction(m, 36));
    }

    public void applyPowers() {
        super.applyPowers();
        this.calcAndSetCost(this);
    }

    private void calcAndSetCost(LiaoRanLuHuo _inst) {
        this.addToBot(new AbstractGameAction() {
            public void update() {
                int count = 0;
                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    if (!monster.isDeadOrEscaped()) {
                        count++;
                    }
                }
                int curCost = _inst.rawCost - count;
                if (curCost < 0) {
                    curCost = 0;
                }
                _inst.upgradeBaseCost(curCost);

                this.isDone = true;
            }
        });
    }

}
