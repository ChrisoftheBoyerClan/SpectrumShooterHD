package com.thompson.spectrumshooter.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * HealthBar.java 
 * 
 * Represents the current health of the player's character.
 * 
 * @author Zachary Thompson
 *
 */
public class HealthBar extends OverlayingScreen 
{
	protected ProgressBar healthBar;
	protected Sprite fillSprite;
	
	public HealthBar(int MaxHealthBar)
	{
		super(400, 40);
		
		Texture backgroundTexture = new Texture(Gdx.files.local("colorBar.png"));
		
		Sprite backgroundSprite = new Sprite(backgroundTexture);
		backgroundSprite.setSize(350, 20);
		SpriteDrawable background = new SpriteDrawable(backgroundSprite);
		
		fillSprite = new Sprite(backgroundTexture);
		fillSprite.setColor(Color.YELLOW);
		fillSprite.setSize(16, 16);
		SpriteDrawable fill = new SpriteDrawable(fillSprite);
		
		ProgressBarStyle style = new ProgressBarStyle(background, fill);
		style.knobBefore = fill;
				
		healthBar = new ProgressBar(0,MaxHealthBar,1,false,style);
		
		healthBar.setValue(100);
		inerdTable.add(healthBar).minWidth(375);
	}

	/**
	 * Sets the current health percentage of the hero.
	 * @param value	the current health percentage of the hero
	 */
	public void setValue(int value)
	{
		healthBar.setValue(value);
	}
	
	/**
	 * TODO: why is this here?
	 */
	@Override
	public void changeColor(Color c)
	{
		for(int i = 0; i < sprites.size(); i++)
		{
			sprites.get(i).setColor(new Color(.5f + c.r, .5f + c.g, .5f + c.b, 1));
		}
		
	}
}
