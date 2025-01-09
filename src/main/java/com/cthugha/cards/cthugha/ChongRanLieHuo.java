package com.cthugha.cards.cthugha;

import com.cthugha.actions.ResetShunRanAction;
import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.cards.AbstractCthughaCard;
import com.cthugha.enums.AbstractCardEnum;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ChongRanLieHuo extends AbstractCthughaCard {

    public static final String ID = CthughaHelper.makeID(ChongRanLieHuo.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "cthughaResources/img/card/126.png";

    private static final int COST = -2;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = AbstractCardEnum.CTHUGHA_CARD_COLOR;;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    public ChongRanLieHuo() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        this.shunRan = this.baseShunRan = 0;
        this.damage = this.baseDamage = 8;
        this.secondaryShunRan = this.baseSecondaryShunRan = 3;
        this.magicNumber = this.baseMagicNumber = 4;
    }

    // public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    //     return false;
    // }

    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void onShunRan(int level) {
        if (level >= this.shunRan) {
            this.addToBot(new AnonymousAction(() -> {
                this.applyPowers();
                this.addToTop(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player, this.damage),
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }));
        }
        if (level >= this.secondaryShunRan) {
            this.addToBot(new ResetShunRanAction());
            this.addToBot(new ModifyDamageAction(this.uuid, this.magicNumber));
            this.addToBot(new AnonymousAction(this::applyPowers));
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
            this.upgradeMagicNumber(1);
            this.initializeDescription();
        }
    }
}
