package com.thompson.spectrumshooter.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thompson.spectrumshooter.SpectrumShooter;
import com.thompson.spectrumshooter.assets.Assets;
import com.thompson.spectrumshooter.color.ColorWheel;
import com.thompson.spectrumshooter.overlay.ColorSelector;
import com.thompson.spectrumshooter.overlay.InstructionScreen;
import com.thompson.spectrumshooter.overlay.OverlayingScreen;
import com.thompson.spectrumshooter.overlay.RGBBarSelector;
import com.thompson.spectrumshooter.sound.AudioManager;
import com.thompson.spectrumshooter.util.Constants;

/**
 * MenuScreen.java
 *
 * The menue screen of the game.
 *
 * @author Zachary Thompson
 */
public class MenuScreen implements Screen
{
	private OrthographicCamera camera;
	private Viewport viewport;
	private SpriteBatch batch;
	private Stage stage;
	private Stage backgroundStage;
	private LabelStyle titleLabelStyle;
	private ColorWheel colorWheel;
	private int currentColorCode;
	private TextButton newGameButton;
	private TextButton instructionButton;
	private Label title;
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private ArrayList<backgroundEnemy> array = new ArrayList<backgroundEnemy>();

	private boolean isPaused = false;
	private final int CIRCLEWIDTH = 550;

	private InstructionScreen instructionScreen;
	private ColorSelector colorSelector = new RGBBarSelector();

	public MenuScreen()
	{
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

		viewport = new FitViewport(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
		batch = new SpriteBatch();

		stage = new Stage(viewport, batch);
		backgroundStage = new Stage(viewport, batch);
		Gdx.input.setInputProcessor(stage);

		colorWheel = new ColorWheel();
		currentColorCode = colorWheel.incrementColorCode(currentColorCode);

		Table backgroundTable = new Table();
		backgroundTable.setFillParent(true);
		Sprite circleBackground = new Sprite(new Texture(Gdx.files.local("whiteCircleFaded.png")));
		circleBackground.setSize(CIRCLEWIDTH, CIRCLEWIDTH);;
		backgroundTable.add(new Image(new SpriteDrawable(circleBackground)));
		backgroundStage.addActor(backgroundTable);

		for(int i = 0; i < 30; i++)
		{
			array.add(generateEnemy());
		}

		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = Constants.GAME_HEIGHT/10;
		parameter.borderWidth = 3;
		parameter.borderColor = Color.BLACK;
		parameter.shadowOffsetX = 2;
		parameter.shadowOffsetY = 2;

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.local("master_of_break.ttf"));
		titleLabelStyle = new LabelStyle(generator.generateFont(parameter), new Color(0.255f, 0.412f, 0.882f, 1f));

		Table mainTable = new Table();
		mainTable.setFillParent(true);
		title = new Label("Spectrum Shooter", titleLabelStyle);
		mainTable.add(title).row();

		TextButtonStyle style= new TextButtonStyle();
		parameter.size = Constants.GAME_HEIGHT/20;
		style.font = generator.generateFont(parameter);
		generator.dispose();
		style.fontColor = Color.WHITE;
		style.downFontColor = Color.BLACK;

		newGameButton = new TextButton("New Game",style);
		newGameButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				SpectrumShooter.getInstance().setScreen(new MainScreen());
				AudioManager.instance.play(Assets.instance.music.gameMusic);
				dispose();
			}
		});

		instructionButton = new TextButton("Instructions", style);
		instructionButton.addListener(new ChangeListener()
		{
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				disable();
				instructionScreen.makeVisibile();
			}
		});

		mainTable.add(newGameButton).size(200, 75).row();;
		mainTable.add(instructionButton);

		stage.addActor(mainTable);

		Table overTable = new Table();
		overTable.setFillParent(true);
		instructionScreen = new InstructionScreen(style);
		overTable.add(instructionScreen.getTable());

		stage.addActor(overTable);

	}

	@Override
	public void render(float delta)
	{
		updateColors();
		camera.update();
		colorSelector.updateColor();

		if(!instructionScreen.getVisbleStatus() && isPaused)
		{
			enable();
		}
		else
		{
			instructionScreen.changeColor(colorSelector.selectColor());
		}

		backgroundStage.draw();
		for(int i = 0; i < array.size(); i++)
		{
			array.get(i).updateDraw(shapeRenderer);

		}
		for(int i = 0; i < array.size(); i++)
		{
			if(array.get(i).getRespawn())
			{
				array.remove(i);
				array.add(generateEnemy());
			}
		}
		stage.draw();
	}

	@Override
	public void show() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause()
	{

	}

	@Override
	public void resume()
	{

	}

	public void disable()
	{
		isPaused = true;
		newGameButton.setDisabled(true);
		instructionButton.setDisabled(true);
		newGameButton.setVisible(false);
		instructionButton.setVisible(false);
		title.setVisible(false);
	}

	public void enable()
	{
		isPaused = false;
		newGameButton.setDisabled(false);
		instructionButton.setDisabled(false);
		newGameButton.setVisible(true);
		instructionButton.setVisible(true);
		title.setVisible(true);
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		newGameButton.setDisabled(true);
		stage.dispose();
		backgroundStage.dispose();
		shapeRenderer.dispose();
		Gdx.input.setInputProcessor(backgroundStage);
	}

	/**
	 * Increments the CurrentColor background and text color
	 */
	public void updateColors()
	{
		Color newColor =  new Color(
				colorWheel.getRedValue(currentColorCode)/255.0f,
				colorWheel.getGreenValue(currentColorCode)/255.0f,
				colorWheel.getBlueValue(currentColorCode)/255.0f,
				1);
		//newColor = selector.selectColor();
		Gdx.gl.glClearColor(newColor.r, newColor.g, newColor.b, newColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		titleLabelStyle.fontColor = new Color(1 - newColor.r,1 - newColor.g,1 - newColor.b, 1);
		currentColorCode = colorWheel.incrementColorCode(currentColorCode);

		//colorWheel2.setColor(selector.selectColor());
	}


	public backgroundEnemy generateEnemy()
	{
		int colorCode = colorWheel.random();
		Color Color = new Color(colorWheel.getRedValue(colorCode) / 255f,
				colorWheel.getGreenValue(colorCode) / 255f,
				colorWheel.getBlueValue(colorCode) / 255f,
				1f);

		return new backgroundEnemy(
				(float)Math.random() * 360f,
				CIRCLEWIDTH/2,
				(float)Math.random() * 120 + 15,
				(float)Math.random() * 20 + 15,
				Color);
	}

	private class backgroundEnemy
	{
		private float deg;
		private float distance;
		private float speed;
		private float diameter;
		private Color color;
		private boolean respawn;

		public backgroundEnemy(float deg, float distance, float speed, float diameter, Color color)
		{
			this.deg = deg;
			this.distance = distance;
			this.speed = speed;
			this.diameter = diameter;
			this.color = color;
			this.respawn = false;
		}

		public void updateDraw(ShapeRenderer shapeRenderer)
		{
			distance = distance - speed / 100;
			float width = Constants.GAME_WIDTH / 2 - MathUtils.sinDeg(deg) * distance;
			float height = Constants.GAME_HEIGHT / 2 - MathUtils.cosDeg(deg) * distance;

			if(distance < 4.5)
			{
				respawn = true;
			}

			if(!respawn)
			{
				shapeRenderer.begin(ShapeType.Filled);
				shapeRenderer.setColor(color);
				shapeRenderer.circle(width, height, diameter);
				shapeRenderer.end();
			}
		}

		public boolean getRespawn()
		{
			return respawn;
		}
	}
}