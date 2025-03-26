package com.cthugha.dungeons.the_tricuspid_gate;

import actlikeit.dungeons.CustomDungeon;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cthugha.dungeons.the_tricuspid_gate.confirm.ConfirmUseCardPopup;
import com.cthugha.dungeons.the_tricuspid_gate.monsters.Areshkagal;
import com.cthugha.dungeons.the_tricuspid_gate.monsters.BurialWatchdogScepter;
import com.cthugha.dungeons.the_tricuspid_gate.monsters.BurialWatchdogSword;
import com.cthugha.dungeons.the_tricuspid_gate.shop.BloodShopScreen;
import com.cthugha.relics.shared.ChaliceMurmurous;
import com.cthugha.relics.shared.Frangiclave;
import com.cthugha.relics.shared.KingskinBodhran;
import com.cthugha.utils.CthughaHelper;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapGenerator;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.beyond.TimeEater;
import com.megacrit.cardcrawl.rewards.chests.BossChest;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import com.megacrit.cardcrawl.scenes.TheEndingScene;

import java.util.ArrayList;
import java.util.stream.Stream;

public class TheTricuspidGate extends CustomDungeon {
	public static final String SIMPLE_NAME = TheTricuspidGate.class.getSimpleName();
	public static final String ID = CthughaHelper.makeID(SIMPLE_NAME);
	private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
	public static final String NAME = eventStrings.NAME;

	private static Texture bg = null;

	public static void initialize() {
		CustomDungeon.addAct(5, new TheTricuspidGate());

		BaseMod.addMonster(Areshkagal.ID, () -> new MonsterGroup(new AbstractMonster[] {
				new BurialWatchdogSword(), new BurialWatchdogScepter(), new Areshkagal()
		}));
		BaseMod.addBoss(ID, Areshkagal.ID,
				"cthughaResources/img/dungeon/Areshkagal.png",
				"cthughaResources/img/dungeon/AreshkagalOutline.png");

		BaseMod.addMonster("cthugha:NiGanMa", () -> new MonsterGroup(new TimeEater()));

		bg = ImageMaster.loadImage("cthughaResources/img/dungeon/TheTricuspidGate.png");
	}

	public TheTricuspidGate() {
		super(NAME, ID, "cthughaResources/img/dungeon/TheTricuspidGate.png",
				false, 0, 0, 1);
	}

	public TheTricuspidGate(CustomDungeon cd, AbstractPlayer p, ArrayList<String> list) {
		super(cd, p, list);
	}

	public TheTricuspidGate(CustomDungeon cd, AbstractPlayer p, SaveFile saveFile) {
		super(cd, p, saveFile);
	}

	@Override
	public void Ending() {
		CardCrawlGame.music.fadeOutBGM();

		AbstractDungeon.nextRoom = map.get(5).get(3);
		AbstractDungeon.closeCurrentScreen();
		AbstractDungeon.nextRoomTransitionStart();
		AbstractDungeon.overlayMenu.proceedButton.hide();
	}

	@Override
	public AbstractScene DungeonScene() {
		return new TheEndingScene();
	}

	@Override
	protected void makeMap() {
		AbstractDungeon.name = eventStrings.NAME;
		AbstractDungeon.levelNum = eventStrings.DESCRIPTIONS[0];

		MapRoomNode reward = new MapRoomNode(3, 0);
		reward.room = new TreasureRoomBoss();
		reward.room.setMapSymbol("T");

		MapRoomNode chest = new MapRoomNode(3, 1);
		chest.room = new TreasureRoomBoss() {
			@Override
			public void onPlayerEntry() {
				this.chest = new BossChest();
				((BossChest) this.chest).relics.clear();
				((BossChest) this.chest).relics.add(new KingskinBodhran());
				((BossChest) this.chest).relics.add(new ChaliceMurmurous());
				((BossChest) this.chest).relics.add(new Frangiclave());
			}
		};
		chest.room.setMapImg(ImageMaster.MAP_NODE_TREASURE, ImageMaster.MAP_NODE_TREASURE_OUTLINE);
		chest.room.setMapSymbol("ST");

		MapRoomNode shop = new MapRoomNode(3, 2);
		shop.room = new BloodShopScreen.BloodShopRoom();

		MapRoomNode rest = new MapRoomNode(3, 3);
		rest.room = new RestRoom();

		MapRoomNode boss = new MapRoomNode(3, 4);
		boss.room = new MonsterRoomBoss();
		boss.room.setMapImg(ImageMaster.loadImage("cthughaResources/img/dungeon/Areshkagal.png"),
				ImageMaster.loadImage("cthughaResources/img/dungeon/AreshkagalOutline.png"));

		MapRoomNode victory = new MapRoomNode(3, 5);
		victory.room = new TrueVictoryRoom();

		map = new ArrayList<>();
		map.add(Stream.of(new MapRoomNode(0, 0), new MapRoomNode(1, 0),
						new MapRoomNode(2, 0), reward, new MapRoomNode(4, 0),
						new MapRoomNode(5, 0), new MapRoomNode(6, 0))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		map.add(Stream.of(new MapRoomNode(0, 1), new MapRoomNode(1, 1),
						new MapRoomNode(2, 1), chest, new MapRoomNode(4, 1),
						new MapRoomNode(5, 1), new MapRoomNode(6, 1))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		map.add(Stream.of(new MapRoomNode(0, 2), new MapRoomNode(1, 2),
						new MapRoomNode(2, 2), shop, new MapRoomNode(4, 2),
						new MapRoomNode(5, 2), new MapRoomNode(6, 2))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		map.add(Stream.of(new MapRoomNode(0, 3), new MapRoomNode(1, 3),
						new MapRoomNode(2, 3), rest, new MapRoomNode(4, 3),
						new MapRoomNode(5, 3), new MapRoomNode(6, 3))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		map.add(Stream.of(new MapRoomNode(0, 4), new MapRoomNode(1, 4),
						new MapRoomNode(2, 4), boss, new MapRoomNode(4, 4),
					new MapRoomNode(5, 4), new MapRoomNode(6, 4))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		map.add(Stream.of(new MapRoomNode(0, 5), new MapRoomNode(1, 5),
						new MapRoomNode(2, 5), victory, new MapRoomNode(4, 5),
						new MapRoomNode(5, 5), new MapRoomNode(6, 5))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll));

		reward.addEdge(new MapEdge(reward.x, reward.y, reward.offsetX, reward.offsetY,
				chest.x, chest.y, chest.offsetX, chest.offsetY, false));
		chest.addEdge(new MapEdge(chest.x, chest.y, chest.offsetX, chest.offsetY,
				shop.x, shop.y, shop.offsetX, shop.offsetY, false));
		shop.addEdge(new MapEdge(shop.x, shop.y, shop.offsetX, shop.offsetY,
				rest.x, rest.y, rest.offsetX, rest.offsetY, false));
		rest.addEdge(new MapEdge(rest.x, rest.y, rest.offsetX, rest.offsetY,
				boss.x, boss.y, boss.offsetX, boss.offsetY, false));

		logger.info("Generated the following dungeon map:");
		logger.info(MapGenerator.toString(map, Boolean.TRUE));
		logger.info("Game Seed: {}", Settings.seed);

		firstRoomChosen = false;
		fadeIn();
	}

	@Override
	protected void initializeBoss() {
		bossList.add(Areshkagal.ID);
	}

//	@Override
//	public void nextRoomTransition(SaveFile saveFile) {
//		super.nextRoomTransition(saveFile);
//
//		if (getCurrRoom() instanceof MonsterRoomBoss && lastCombatMetricKey.equals(Areshkagal.ID))
//			player.movePosition((float) Settings.WIDTH / 2.0F, floorY - 40.0F * Settings.yScale);
//	}

//	@Override
//	public void render(SpriteBatch sb) {
//		super.render(sb);
//		renderBackground(sb);
//	}

	public void renderBackground(SpriteBatch sb) {
		sb.setColor(Color.WHITE);
		sb.draw(bg, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
	}
}
