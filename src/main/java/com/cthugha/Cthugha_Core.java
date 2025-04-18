package com.cthugha;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import basemod.helpers.CardBorderGlowManager;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cthugha.blight.TheBurningOne;
import com.cthugha.cardmodifier.ColorificStampModifier;
import com.cthugha.cards.cthugha.ShiftingStar;
import com.cthugha.cards.cthugha.StarSpear;
import com.cthugha.dungeons.the_tricuspid_gate.TheTricuspidGate;
import com.cthugha.dungeons.the_tricuspid_gate.confirm.ConfirmCheckbox;
import com.cthugha.dungeons.the_tricuspid_gate.confirm.ConfirmUseCardScreen;
import com.cthugha.dungeons.the_tricuspid_gate.monsters.Areshkagal;
import com.cthugha.dungeons.the_tricuspid_gate.proceed.ConfirmProceedScreen;
import com.cthugha.events.ReplacedVampires;
import com.cthugha.glow.BaoYanGlowInfo;
import com.cthugha.glow.ZhiLiaoGlowInfo;
import com.cthugha.patches.misc.HPLostThisCombatPatch;
import com.cthugha.potions.EvilSunrise;
import com.cthugha.potions.RedLotusWater;
import com.cthugha.power.StampPower;
import com.cthugha.strings.CthughaCardModifierStrings;
import com.cthugha.ui.SkinSelectScreen;
import com.cthugha.utils.*;
import com.cthugha.potions.BottledHell;
import com.cthugha.power.HeiYanPower;
import com.cthugha.variable.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import me.antileaf.signature.card.AbstractSignatureCard;
import me.antileaf.signature.utils.SignatureHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.cthugha.characters.Cthugha;
import com.cthugha.enums.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import basemod.AutoAdd;
import basemod.BaseMod;

@SpireInitializer
public class Cthugha_Core implements
		EditCardsSubscriber,
		EditCharactersSubscriber,
		EditRelicsSubscriber,
		EditStringsSubscriber,
		EditKeywordsSubscriber,
		OnStartBattleSubscriber,
		StartActSubscriber,
		PostInitializeSubscriber,
		PostDungeonInitializeSubscriber,
		PostBattleSubscriber,
		PostEnergyRechargeSubscriber,
		AddAudioSubscriber,
		PreRoomRenderSubscriber,
		RenderSubscriber,
		PostUpdateSubscriber
{

	public static final Logger logger = LogManager.getLogger(Cthugha_Core.class.getName());

	// 人物选择界面按钮的图片
	private static final String MY_CHARACTER_BUTTON = "cthughaResources/img/char/button_default.png";
	// 人物选择界面的立绘
	private static final String MY_CHARACTER_PORTRAIT = "cthughaResources/img/anime-fandoms-Black-Bullet-1620248_2.png";

	// 攻击牌的背景（小尺寸）
	private static final String BG_ATTACK_512 = "cthughaResources/img/512/card.png";
	// 能力牌的背景（小尺寸）
	private static final String BG_POWER_512 = "cthughaResources/img/512/card.png";
	// 技能牌的背景（小尺寸）
	private static final String BG_SKILL_512 = "cthughaResources/img/512/card.png";
	// 在卡牌和遗物描述中的能量图标
	private static final String SMALL_ORB = "cthughaResources/img/char/small_orb.png";
	// 攻击牌的背景（大尺寸）
	private static final String BG_ATTACK_1024 = "cthughaResources/img/1024/card.png";
	// 能力牌的背景（大尺寸）
	private static final String BG_POWER_1024 = "cthughaResources/img/1024/card.png";
	// 技能牌的背景（大尺寸）
	private static final String BG_SKILL_1024 = "cthughaResources/img/1024/card.png";
	// 在卡牌预览界面的能量图标
	private static final String BIG_ORB = "cthughaResources/img/char/card_orb.png";
	// 小尺寸的能量图标（战斗中，牌堆预览）
	private static final String ENEYGY_ORB = "cthughaResources/img/char/cost_orb.png";

	// 除以255得出需要的参数。你也可以直接写出计算值。
	public static final Color BLOOD_COLOR = new Color(249.0F / 255.0F, 1.0F / 255.0F, 5.0F / 255.0F, 1.0F);

	// 构造方法
	public Cthugha_Core() {
		BaseMod.subscribe(this);
		BaseMod.addColor(AbstractCardEnum.CTHUGHA_CARD_COLOR, BLOOD_COLOR, BLOOD_COLOR, BLOOD_COLOR, BLOOD_COLOR,
				BLOOD_COLOR, BLOOD_COLOR, BLOOD_COLOR, BG_ATTACK_512, BG_SKILL_512, BG_POWER_512, ENEYGY_ORB,
				BG_ATTACK_1024, BG_SKILL_1024, BG_POWER_1024, BIG_ORB, SMALL_ORB);
	}

	// 注解需要调用的方法，必须写
	public static void initialize() {
		new Cthugha_Core();
		ConfigHelper.load();
	}

	@Override
	public void receiveEditCharacters() {
		BaseMod.addCharacter(new Cthugha("qwq"), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT,
				MyPlayerClassEnum.CTHUGHA_PLAYER_CLASS);
	}

	private void loadVariables() {
		BaseMod.addDynamicVariable(new ShunRanVariable());
		BaseMod.addDynamicVariable(new SecondaryShunRanVariable());
		BaseMod.addDynamicVariable(new SecondaryMagicNumberVariable());
		BaseMod.addDynamicVariable(new SecondaryDamageVariable());
		BaseMod.addDynamicVariable(new SecondaryBlockVariable());
	}

	@Override
	public void receiveEditCards() {
		this.loadVariables();

		logger.info("Starting edit cards...");

		new AutoAdd("CthughaV2")
				.packageFilter("com.cthugha.cards.cthugha")
				.setDefaultSeen(true)
				.cards();

		new AutoAdd("CthughaV2")
				.packageFilter("com.cthugha.cards.colorless")
				.setDefaultSeen(true)
				.cards();
	}

	@Override
	public void receiveEditRelics() {
		new AutoAdd("CthughaV2")
				.packageFilter("com.cthugha.relics.cthugha")
				.any(AbstractRelic.class, (info, relic) -> {
					BaseMod.addRelicToCustomPool(relic, AbstractCardEnum.CTHUGHA_CARD_COLOR);
					UnlockTracker.markRelicAsSeen(relic.relicId);
				});

		new AutoAdd("CthughaV2")
				.packageFilter("com.cthugha.relics.shared")
				.any(AbstractRelic.class, (info, relic) -> {
					BaseMod.addRelic(relic, RelicType.SHARED);
					UnlockTracker.markRelicAsSeen(relic.relicId);
				});
	}

	@Override
	public void receiveEditStrings() {
		String lang;
		if (Settings.language == Settings.GameLanguage.ZHS ||
				Settings.language == Settings.GameLanguage.ZHT ||
				Settings.language == Settings.GameLanguage.JPN) {
			lang = "ZHS";
		} else {
			lang = "ENG";
		}

		BaseMod.loadCustomStringsFile(CharacterStrings.class, "cthughaResources/localization/" + lang + "/character.json");
		BaseMod.loadCustomStringsFile(CardStrings.class, "cthughaResources/localization/" + lang + "/cards.json");
		BaseMod.loadCustomStringsFile(RelicStrings.class, "cthughaResources/localization/" + lang + "/relics.json");
		BaseMod.loadCustomStringsFile(OrbStrings.class, "cthughaResources/localization/" + lang + "/orbs.json");
		BaseMod.loadCustomStringsFile(PowerStrings.class, "cthughaResources/localization/" + lang + "/powers.json");
		BaseMod.loadCustomStringsFile(PotionStrings.class, "cthughaResources/localization/" + lang + "/potions.json");
		BaseMod.loadCustomStringsFile(UIStrings.class, "cthughaResources/localization/" + lang + "/ui.json");
		BaseMod.loadCustomStringsFile(EventStrings.class, "cthughaResources/localization/" + lang + "/events.json");
		BaseMod.loadCustomStringsFile(BlightStrings.class, "cthughaResources/localization/" + lang + "/blights.json");
		BaseMod.loadCustomStringsFile(MonsterStrings.class, "cthughaResources/localization/" + lang + "/monsters.json");
		BaseMod.loadCustomStringsFile(ScoreBonusStrings.class, "cthughaResources/localization/" + lang + "/score.json");
		BaseMod.loadCustomStringsFile(TutorialStrings.class, "cthughaResources/localization/" + lang + "/tutorial.json");

		CthughaCardModifierStrings.init((new Gson()).fromJson(
				Gdx.files.internal("cthughaResources/localization/" + lang + "/card_modifiers.json")
						.readString(String.valueOf(StandardCharsets.UTF_8)),
				(new TypeToken<Map<String, CthughaCardModifierStrings>>() {}).getType()));
	}

	@Override
	public void receiveEditKeywords() {
		String lang;
		if (Settings.language == Settings.GameLanguage.ZHS ||
				Settings.language == Settings.GameLanguage.ZHT ||
				Settings.language == Settings.GameLanguage.JPN) {
			lang = "ZHS";
		} else {
			lang = "ENG";
		}

		String json = Gdx.files.internal("cthughaResources/localization/" + lang + "/keywords.json")
				.readString(String.valueOf(StandardCharsets.UTF_8));
		Keyword[] keywords = BaseMod.gson.fromJson(json, Keyword[].class);
		if (keywords != null) {
			for (Keyword keyword : keywords) {
				BaseMod.addKeyword("cthugha:", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
			}
		}
	}

	@Override
	public void receiveOnBattleStart(AbstractRoom room) {
		BaoYanHelper.initPreBattle();
		EnergyHelper.initPreBattle();

		HeiYanPower.percentage = HeiYanPower.BASE_PERCENTAGE;

		HPLostThisCombatPatch.HPLostThisCombat = 0;

		StampPower.clearTimer();
	}

	@Override
	public void receivePostBattle(AbstractRoom room) {
		BaoYanHelper.clearPostBattle();
		EnergyHelper.clearPostBattle();

		HeiYanPower.percentage = HeiYanPower.BASE_PERCENTAGE;

		HPLostThisCombatPatch.HPLostThisCombat = 0;
	}

	@Override
	public void receivePostInitialize() {
		BaseMod.addCustomScreen(new ConfirmUseCardScreen());
		BaseMod.addCustomScreen(new ConfirmProceedScreen());

		CardBorderGlowManager.addGlowInfo(new BaoYanGlowInfo());
		CardBorderGlowManager.addGlowInfo(new ZhiLiaoGlowInfo());

		BaseMod.addPotion(BottledHell.class, Color.SCARLET, Color.FIREBRICK, null,
				BottledHell.ID, MyPlayerClassEnum.CTHUGHA_PLAYER_CLASS);
		BaseMod.addPotion(EvilSunrise.class, Color.SCARLET, Color.FIREBRICK, null,
				EvilSunrise.ID, MyPlayerClassEnum.CTHUGHA_PLAYER_CLASS);
		BaseMod.addPotion(RedLotusWater.class, Color.SCARLET, Color.FIREBRICK, null,
				RedLotusWater.ID, MyPlayerClassEnum.CTHUGHA_PLAYER_CLASS);

		BaseMod.addEvent(
				new AddEventParams.Builder(ReplacedVampires.ID, ReplacedVampires.class)
						.playerClass(MyPlayerClassEnum.CTHUGHA_PLAYER_CLASS)
						.eventType(EventUtils.EventType.FULL_REPLACE)
						.overrideEvent(Vampires.ID)
						.create()
		);

		TheTricuspidGate.initialize();

		ColorificStampModifier.initialize();
		StampPower.initialize();

		BaseMod.registerModBadge(
				ImageMaster.loadImage("cthughaResources/img/badge.png"),
				"cthugha",
				"ee, pipige, antileaf",
				"",
				ConfigHelper.createConfigPanel()
		);

		if (true) {
			SignatureHelper.noDebuggingPrefix("Cthugha:");

			SignatureHelper.register(Burn.ID, new SignatureHelper.Info(
					(card) -> {
						if (card.type == AbstractCard.CardType.STATUS)
							return "cthughaResources/img/signature/burn.png";
						else
							return "cthughaResources/img/signature/burn_attack.png";
					},
					(card) -> {
						if (card.type == AbstractCard.CardType.STATUS)
							return "cthughaResources/img/signature/burn_p.png";
						else
							return "cthughaResources/img/signature/burn_attack_p.png";
					}
			));

			String lang;
			if (Settings.language == Settings.GameLanguage.ZHS ||
					Settings.language == Settings.GameLanguage.ZHT ||
					Settings.language == Settings.GameLanguage.JPN) {
				lang = "ZHS";
			} else {
				lang = "ENG";
			}

			SignatureHelper.addUnlockConditions("cthughaResources/localization/" +
					lang + "/signature.json");

			new AutoAdd("CthughaV2")
					.packageFilter("com.cthugha.cards.cthugha")
					.any(AbstractSignatureCard.class, (info, card) -> {
						if (card.hasSignature && !card.cardID.equals(ShiftingStar.ID))
							SignatureHelper.unlock(card.cardID, true);
					});

			if (!ConfigHelper.hasCheckedHistory()) {
				boolean res = HistoryHelper.checkHistories();

				if (res) {
					SignatureHelper.unlock(Burn.ID, true);
					SignatureHelper.unlock(ShiftingStar.ID, true);
					logger.info("Has defeated Areshkagal before, Signature of Burn and Shifting Star unlocked!");
				}
				else
					logger.info("Signature not unlocked.");

				ConfigHelper.setHasCheckedHistory(true);
			}
		}

		ConfirmCheckbox.inst = new ConfirmCheckbox();

		SkinSelectScreen.inst.refresh();
	}

	@Override
	public void receivePostDungeonInitialize() {
		if (AbstractDungeon.player instanceof Cthugha &&
				!AbstractDungeon.player.hasBlight(TheBurningOne.ID)) {
			AbstractBlight blight = new TheBurningOne();

			blight.spawn(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F);
			blight.obtain();
			blight.isObtained = true;
			blight.isAnimating = false;
			blight.isDone = false;
			blight.flash();
		}
	}

	@Override
	public void receivePostEnergyRecharge() {
		EnergyHelper.resetValuesPostEnergyRecharge();
	}

	@Override
	public void receiveStartAct() {
		for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
			if (c instanceof StarSpear)
				((StarSpear) c).onStartAct();

		if (CardCrawlGame.dungeon instanceof TheTricuspidGate)
			CardCrawlGame.stopClock = false;
	}

	@Override
	public void receiveAddAudio() {
//		BaseMod.addAudio("Cthugha:FACING_THE_FACELESS_SPHINX",
//				"cthughaResources/audio/music/Facing_the_Faceless_Sphinx.mp3");
//		BaseMod.addAudio("Cthugha:LAUGHING_TILL_THE_END",
//				"cthughaResources/audio/music/Laughing_till_the_End.mp3");

		BaseMod.addAudio("Cthugha:CHAR_SELECT",
				"cthughaResources/audio/sound/CHAR_SELECT.ogg");
	}

	@Override
	public void receivePreRoomRender(SpriteBatch sb) {
		if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss &&
				AbstractDungeon.lastCombatMetricKey.equals(Areshkagal.ID))
			((TheTricuspidGate) CardCrawlGame.dungeon).renderBackground(sb);
	}

	@Override
	public void receiveRender(SpriteBatch sb) {
//		if (CardCrawlGame.dungeon instanceof TheTricuspidGate &&
//				CthughaHelper.isInBattle())
//			ConfirmCheckbox.inst.render(sb);
	}

	@Override
	public void receivePostUpdate() {
		if (CardCrawlGame.dungeon instanceof TheTricuspidGate &&
				CthughaHelper.isInBattle()) {
			StampPower.update();
			ConfirmCheckbox.inst.update();
		}
	}
}
