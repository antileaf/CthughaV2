package com.cthugha.patch;

import com.cthugha.characters.Cthugha;
import com.cthugha.helpers.ModHelper;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.cthugha.actions.DecreaseMonsterMaxHealthAction;
import com.cthugha.power.FenJiPower;
import com.cthugha.power.DaoHuoShiYanPower;
import com.cthugha.relics.HuoTiHuoYan;
import com.cthugha.relics.ShengLingLieYan;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;

@Deprecated
public class BurnPatch {

//    @SpirePatch(clz = Burn.class, method = SpirePatch.CONSTRUCTOR)
//    public static class MeteorStrikePatch {
//        @SpirePostfixPatch
//        public static void Postfix(Burn _inst) {
//            if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(DaoHuoShiYanPower.POWER_ID)) {
//                _inst.upgrade(); // TODO: 这可能不太好，考虑是否 Patch 生成临时牌的方法而不是这里
//            }
//        }
//    }


//    @SpirePatch(clz = Burn.class, method = SpirePatch.CLASS)
//    public static class BurnField {
//        public static SpireField<String> originalDescription = new SpireField<>(() -> "");
////        public static SpireField<Integer> originalMagicNumber = new SpireField<>(() -> 2);
//    }

//    @SpirePatch(clz = AbstractCard.class, method = "initializeDescription", paramtypez = {})
//    public static class InitializeDescriptionPatch {
//        @SpirePrefixPatch
//        public static void Prefix(AbstractCard _inst) {
//            if (_inst instanceof Burn && _inst.type == CardType.STATUS && ModHelper.isInBattle()) {
//                BurnField.originalDescription.set(_inst, _inst.rawDescription);
//
//                CardStrings strings = CardCrawlGame.languagePack.getCardStrings(ModHelper.makeID(Burn.class.getSimpleName()));
//
//                if (ModHelper.isInBattle() && AbstractDungeon.player.hasPower(FenJiPower.POWER_ID)) {
//                    AbstractPower power = AbstractDungeon.player.getPower(FenJiPower.POWER_ID);
//                    _inst.magicNumber += power.amount;
//                }
//
//                if (AbstractDungeon.player.hasRelic(HuoTiHuoYan.ID)) {
//                    _inst.rawDescription = strings.DESCRIPTION;
//                    _inst.magicNumber = _inst.baseMagicNumber + HuoTiHuoYan.BONUS;
//                }
//                else if (AbstractDungeon.player.hasRelic(ShengLingLieYan.ID)) {
//                    _inst.rawDescription = strings.UPGRADE_DESCRIPTION;
//                    _inst.magicNumber = _inst.baseMagicNumber + ShengLingLieYan.BONUS;
//                }
//
//                _inst.isMagicNumberModified = _inst.magicNumber != _inst.baseMagicNumber;
//            }
//        }
//
//        @SpirePostfixPatch
//        public static void Postfix(AbstractCard _inst) {
//            if (_inst instanceof Burn && _inst.type == CardType.STATUS && ModHelper.isInBattle()) {
//                _inst.rawDescription = BurnField.originalDescription.get(_inst);
////                _inst.magicNumber = _inst.baseMagicNumber;
//            }
//        }
//    }

//    @SpirePatch(clz = AbstractCard.class, method = "getDynamicValue", paramtypez = {char.class})
//    public static class GetDynamicValuePatch {
//        @SpirePrefixPatch
//        public static void Prefix(AbstractCard _inst, char key) {
//            if (_inst instanceof Burn && _inst.type == CardType.STATUS && ModHelper.isInBattle()) {
//                if (key == 'M') {
//                    if (AbstractDungeon.player.hasRelic(HuoTiHuoYan.ID)) {
//                        BurnField.originalMagicNumber.set(_inst, _inst.baseMagicNumber);
//                        _inst.magicNumber = _inst.baseMagicNumber = _inst.baseMagicNumber + HuoTiHuoYan.BONUS;
//                    }
//                    else if (AbstractDungeon.player.hasRelic(ShengLingLieYan.ID)) {
//                        BurnField.originalMagicNumber.set(_inst, _inst.baseMagicNumber);
//                        _inst.magicNumber = _inst.baseMagicNumber = _inst.baseMagicNumber + ShengLingLieYan.BONUS;
//                    }
//
//                    _inst.isMagicNumberModified = false;
//                }
//            }
//        }
//
//        @SpirePostfixPatch
//        public static void Postfix(AbstractCard _inst, char key) {
//            if (_inst instanceof Burn && _inst.type == CardType.STATUS && ModHelper.isInBattle()) {
//                if (key == 'M') {
//                    _inst.magicNumber = _inst.baseMagicNumber = BurnField.originalMagicNumber.get(_inst);
//                    _inst.isMagicNumberModified = false;
//                }
//            }
//        }
//    }

//    @SpirePatch(clz = Burn.class, method = "triggerOnEndOfTurnForPlayingCard")
//    public static class Renpi_player11 {
//        @SpirePrefixPatch
//        public static SpireReturn<Void> Prefix(AbstractCard _inst) {
//            if (_inst.type != CardType.STATUS) {
//                return SpireReturn.Return();
//            }
//            return SpireReturn.Continue();
//        }
//    }

//    @SpirePatch(clz = AbstractCard.class, method = "applyPowers", paramtypez = {})
//    public static class ApplyPowersPatch {
//        @SpirePrefixPatch
//        public static SpireReturn<Void> Prefix(AbstractCard _inst) {
//            if (_inst instanceof Burn && ModHelper.isInBattle() && AbstractDungeon.player instanceof Cthugha) {
//                if (_inst.type == CardType.STATUS) {
//                    _inst.magicNumber = _inst.baseMagicNumber;
//
//                    if (ModHelper.isInBattle() && AbstractDungeon.player.hasPower(FenJiPower.POWER_ID)) {
//                        AbstractPower power = AbstractDungeon.player.getPower(FenJiPower.POWER_ID);
//                        _inst.magicNumber += power.amount;
//                    }
//
//                    if (AbstractDungeon.player.hasRelic(HuoTiHuoYan.ID))
//                        _inst.magicNumber = _inst.baseMagicNumber + HuoTiHuoYan.BONUS;
//                    else if (AbstractDungeon.player.hasRelic(ShengLingLieYan.ID))
//                        _inst.magicNumber = _inst.baseMagicNumber + ShengLingLieYan.BONUS;
//
//
//                    _inst.isMagicNumberModified = _inst.magicNumber != _inst.baseMagicNumber;
//
//                    return SpireReturn.Return();
//                }
//            }
//            return SpireReturn.Continue();
//        }
//    }

//    @SpirePatch(clz = Burn.class, method = "upgrade")
//    public static class Renpi_player12 {
//        public static SpireReturn<Void> Postfix(AbstractCard _inst) {
//            if (ModHelper.isInBattle() && AbstractDungeon.player instanceof Cthugha) {
//                if ( _inst.type == CardType.STATUS) {
//                    _inst.magicNumber = _inst.baseMagicNumber;
//
//                    CardStrings strings = CardCrawlGame.languagePack.getCardStrings(ModHelper.makeID(Burn.class.getSimpleName()));
//
//                    if (ModHelper.isInBattle() && AbstractDungeon.player.hasPower(FenJiPower.POWER_ID)) {
//                        AbstractPower power = AbstractDungeon.player.getPower(FenJiPower.POWER_ID);
//                        _inst.magicNumber += power.amount;
//                    }
//
//                    if (AbstractDungeon.player.hasRelic(HuoTiHuoYan.ID)) {
//                        _inst.rawDescription = strings.DESCRIPTION;
//                        _inst.magicNumber = _inst.baseMagicNumber + HuoTiHuoYan.BONUS;
//                    }
//                    else if (AbstractDungeon.player.hasRelic(ShengLingLieYan.ID)) {
//                        _inst.rawDescription = strings.UPGRADE_DESCRIPTION;
//                        _inst.magicNumber = _inst.baseMagicNumber + ShengLingLieYan.BONUS;
//                    }
//
//                    _inst.isMagicNumberModified = _inst.magicNumber != _inst.baseMagicNumber;
//
//                    return SpireReturn.Continue();
//                }
//                else {
//                    if (Settings.language == Settings.GameLanguage.ZHS || Settings.language == Settings.GameLanguage.ZHT) {
//                        _inst.rawDescription = "造成 !D! 点伤害。";
//                    } else {
//                        _inst.rawDescription = "Deal !D! damage.";
//                    }
//
//                    _inst.initializeDescription();
//                    _inst.baseDamage = _inst.baseMagicNumber;
//                    return SpireReturn.Return();
//                }
//            }
//            return SpireReturn.Continue();
//        }
//    }

//    @SpirePatch(clz = Burn.class, method = "use", paramtypez = { AbstractPlayer.class, AbstractMonster.class })
//    public static class Renpi_player {
//        @SpirePrefixPatch
//        public static SpireReturn<Void> Prefix(AbstractCard _inst, AbstractPlayer p, AbstractMonster m) {
//            if (!(p instanceof Cthugha))
//                return SpireReturn.Continue();
//
//            _inst.magicNumber = _inst.baseMagicNumber;
//
//            if (AbstractDungeon.player.hasPower(FenJiPower.POWER_ID)) {
//                AbstractPower power = AbstractDungeon.player.getPower(FenJiPower.POWER_ID);
//                _inst.magicNumber += power.amount;
//                _inst.damage += power.amount;
//            }
//            if (AbstractDungeon.player.hasRelic(HuoTiHuoYan.ID)) {
//                _inst.magicNumber += HuoTiHuoYan.BONUS;
//                _inst.damage += HuoTiHuoYan.BONUS;
//            }
//            if (AbstractDungeon.player.hasRelic(ShengLingLieYan.ID)) {
//                _inst.magicNumber += ShengLingLieYan.BONUS;
//                _inst.damage += ShengLingLieYan.BONUS;
//            }
//
//            if (_inst.type == CardType.ATTACK) { // 攻击牌
//                AbstractDungeon.actionManager.addToBottom(
//                        new DamageAction(m,
//                                new DamageInfo(AbstractDungeon.player, _inst.damage,
//                                        DamageInfo.DamageType.NORMAL),
//                                AbstractGameAction.AttackEffect.NONE));
//            } else { // 自动打出
//                if (_inst.dontTriggerOnUseCard) {
//                    if (AbstractDungeon.player.hasRelic(HuoTiHuoYan.ID)) {
//                        AbstractDungeon.actionManager.addToBottom(
//                                new DamageRandomEnemyAction(
//                                        new DamageInfo(AbstractDungeon.player, _inst.magicNumber,
//                                                DamageInfo.DamageType.THORNS),
//                                        AbstractGameAction.AttackEffect.FIRE));
//                    } else if (AbstractDungeon.player.hasRelic(ShengLingLieYan.ID)) {
//                        AbstractMonster monster = AbstractDungeon.getMonsters().getRandomMonster(null, true,
//                                AbstractDungeon.cardRandomRng);
//                        if (monster != null) {
//                            AbstractDungeon.actionManager.addToBottom(
//                                    new DamageAction(monster,
//                                            new DamageInfo(AbstractDungeon.player, _inst.magicNumber,
//                                                    DamageInfo.DamageType.THORNS),
//                                            AbstractGameAction.AttackEffect.FIRE));
//                            AbstractDungeon.actionManager
//                                    .addToBottom(new DecreaseMonsterMaxHealthAction(monster, _inst.magicNumber));
//                        }
//                    } else {
//                        AbstractDungeon.actionManager
//                                .addToBottom(new DamageAction(AbstractDungeon.player,
//                                        new DamageInfo(AbstractDungeon.player, _inst.magicNumber,
//                                                DamageInfo.DamageType.THORNS),
//                                        AbstractGameAction.AttackEffect.FIRE));
//                    }
//                }
//            }
//
//            return SpireReturn.Return();
//        }
//    }

}
