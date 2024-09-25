package com.cthugha.characters;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import basemod.abstracts.CustomPlayer;

import com.cthugha.enums.*;
import com.cthugha.object.AbstractSpirit;
import com.cthugha.patch.SpiritField;
import com.cthugha.power.HeiYanPower;
import com.cthugha.power.XingYunPower;
import com.cthugha.relics.HuoTiHuoYan;
import com.cthugha.ui.SkinSelectScreen;

public class Cthugha extends CustomPlayer {

    // 战斗界面左下角能量图标的每个图层
    private static final String[] ORB_TEXTURES = new String[] {
            "cthughaResources/img/UI/orb/layer5.png",
            "cthughaResources/img/UI/orb/layer4.png",
            "cthughaResources/img/UI/orb/layer3.png",
            "cthughaResources/img/UI/orb/layer2.png",
            "cthughaResources/img/UI/orb/layer1.png",
            "cthughaResources/img/UI/orb/layer6.png",
            "cthughaResources/img/UI/orb/layer5d.png",
            "cthughaResources/img/UI/orb/layer4d.png",
            "cthughaResources/img/UI/orb/layer3d.png",
            "cthughaResources/img/UI/orb/layer2d.png",
            "cthughaResources/img/UI/orb/layer1d.png"
    };

    // 火堆的人物立绘（行动前）
    private static final String MY_CHARACTER_SHOULDER_1 = "cthughaResources/img/char/huodui.png";
    // 火堆的人物立绘（行动后）
    private static final String MY_CHARACTER_SHOULDER_2 = "cthughaResources/img/char/huodui.png";
    // 人物死亡图像
    private static final String CORPSE_IMAGE = "cthughaResources/img/renwu2.png";

    // 每个图层的旋转速度
    private static final float[] LAYER_SPEED = new float[] { -40.0F, -32.0F, 20.0F, -20.0F, 0.0F, -10.0F, -8.0F, 5.0F,
            -5.0F, 0.0F };

    public Cthugha(String name) {
        super(name, MyPlayerClassEnum.MY_PLAYER_CLASS, null, null,
                LAYER_SPEED, null, null);

        // 初始化你的人物，如果你的人物只有一张图，那么第一个参数填写你人物图片的路径。
        this.initializeClass(
                "cthughaResources/img/renwu2.png", // 人物图片
                MY_CHARACTER_SHOULDER_2, MY_CHARACTER_SHOULDER_1,
                CORPSE_IMAGE, // 人物死亡图像
                this.getLoadout(),
                0.0F, 0.0F,
                200.0F, 220.0F, // 人物碰撞箱大小，越大的人物模型这个越大
                new EnergyManager(3) // 初始每回合的能量
        );

        this.refreshSkin();

        // 修复卡牌命定之死在sl情况下会保留翻倍效果的bug。
        HeiYanPower.factor = HeiYanPower.baseFactor;
    }

    public void refreshSkin() {
        SkinSelectScreen.Skin skin = SkinSelectScreen.getSkin();
        this.img = ImageMaster.loadImage(skin.charPath);
        this.corpseImg = ImageMaster.loadImage(skin.charPath);
    }

    // 人物选择界面点击你的人物按钮时触发的方法，这里为屏幕轻微震动
    @Override
    public void doCharSelectScreenSelectEffect() {
        // CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED,
        // ScreenShake.ShakeDur.MED, false);
    }

    // 高进阶带来的生命值损失
    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    // 你的卡牌颜色（这个枚举在最下方创建）
    @Override
    public CardColor getCardColor() {
        // return CardColor.RED;
        return AbstractCardEnum.MOD_NAME_COLOR;
    }

    // 卡牌选择界面选择该牌的颜色
    @Override
    public Color getCardRenderColor() {
        return Color.BLUE;
    }

    // 卡牌轨迹颜色
    @Override
    public Color getCardTrailColor() {
        return Color.BLUE;
    }

    // 自定义模式选择你的人物时播放的音效
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_HEAVY";
    }

    // 卡牌的能量字体，没必要修改
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontBlue;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(
                "克图格亚", // 人物名字
                "由炎之精侍奉的被称为火焰之主的旧日支配者。 NL 永燃之焰可焚尽一切，境随心转亦御烛蚀死亡。", // 人物介绍
                66, // 当前血量
                66, // 最大血量
                6, // 初始充能球栏位
                99, // 初始携带金币
                5, // 每回合抽牌数量
                this, // 别动
                this.getStartingRelics(), // 初始遗物
                this.getStartingDeck(), // 初始卡组
                false // 别动
        );
    }

    // 游戏中左上角显示在你的名字之后的人物名称
    @Override
    public String getLocalizedCharacterName() {
        return "克图格亚";
    }

    // 人物名字（出现在游戏左上角）
    @Override
    public String getTitle(PlayerClass arg0) {
        return "克图格亚";
    }

    // 打心脏的颜色，不是很明显
    @Override
    public Color getSlashAttackColor() {
        return Color.BLUE;
    }

    // 第三章面对心脏造成伤害时的特效
    @Override
    public AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[] { AbstractGameAction.AttackEffect.SLASH_HEAVY,
                AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL };
    }

    // 第三章面对心脏说的话（例如战士是“你握紧了你的长刀……”之类的）
    @Override
    public String getSpireHeartText() {
        return "你将那焰引起随之任之燃到炽烈已极……";
    }

    // 翻牌事件出现的你的职业牌（一般设为打击）
    @Override
    public AbstractCard getStartCardForEvent() {
        return new Burn();
    }

    // 初始卡组的ID，可直接写或引用变量
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> arr = new ArrayList<String>();
        arr.add("Burn");
        arr.add("Burn");
        arr.add("Burn");
        arr.add("Burn");
        arr.add("Burn");
        arr.add("Burn");
        arr.add("Cthugha:Defend");
        arr.add("Cthugha:Defend");
        arr.add("Cthugha:Defend");
        arr.add("Cthugha:Defend");
        arr.add("Cthugha:Defend");
        arr.add("Cthugha:Defend");
        arr.add("Cthugha:HuoYanHuaSheng");

        // a.add("Perfected Strike");
        // a.add("Perfected Strike");
        // a.add("Perfected Strike");
        // a.add("Perfected Strike");
        // a.add("Perfected Strike");
        // a.add("Perfected Strike");
        // a.add("Perfected Strike");
        return arr;
    }

    // 初始遗物的ID，可以先写个原版遗物凑数
    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> arr = new ArrayList<String>();
        arr.add(HuoTiHuoYan.ID);
        return arr;
    }

    // 吸血鬼事件文本，主要是他（索引为0）和她（索引为1）的区别（机器人另外）
    @Override
    public String getVampireText() {
        return Vampires.DESCRIPTIONS[0];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Cthugha("123");
    }

    public void onVictory() {
        super.onVictory();
        HeiYanPower.factor = HeiYanPower.baseFactor;
    }

    public void useCard(AbstractCard c, AbstractMonster monster, int energyOnUse) {
        AbstractSpirit spirit = SpiritField.spirit.get(c);
        if (spirit != null) {
            spirit.onUse();
        }
        super.useCard(c, monster, energyOnUse);
    }

    public void removeNextOrb() {
        super.removeNextOrb();

        if (AbstractDungeon.player.hasPower(XingYunPower.POWER_ID)) {
            AbstractPower power = AbstractDungeon.player.getPower(XingYunPower.POWER_ID);
            AbstractDungeon.actionManager.addToBottom(new DamageRandomEnemyAction(
                    new DamageInfo(AbstractDungeon.player, power.amount, DamageType.THORNS), AttackEffect.NONE));
        }

    }

    // 碎心图片
    @Override
    public ArrayList<CutscenePanel> getCutscenePanels() {
        ArrayList<CutscenePanel> panels = new ArrayList<>();
        // 有两个参数的，第二个参数表示出现图片时播放的音效
        panels.add(new CutscenePanel("cthughaResources/img/char/cg1.png", "ATTACK_MAGIC_FAST_1"));
        panels.add(new CutscenePanel("cthughaResources/img/char/cg2.png"));
        panels.add(new CutscenePanel("cthughaResources/img/char/cg3.png"));
        return panels;
    }
}
