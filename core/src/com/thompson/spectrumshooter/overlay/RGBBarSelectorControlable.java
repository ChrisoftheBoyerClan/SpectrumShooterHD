package com.thompson.spectrumshooter.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * RGBBarSelectorControlable.java
 * 
 * A version of the RGB Bar Color Selector that does not use the Auto 
 * balancing allowing you to increase and decrease all of the color values
 * @author Zachary Thompson
 */
public class RGBBarSelectorControlable extends RGBBarSelector
{
	/**
	 * Constructor calls the RGBBarSelector Constructor as these two share the same table
	 */
	public RGBBarSelectorControlable() 
	{
		super();
	}
	
	/**
	 * Updates the color selector based on input
	 */
	@Override
	public void updateColor() 
	{
		boolean aPressed = Gdx.input.isKeyPressed(Input.Keys.A);
		boolean zPressed = Gdx.input.isKeyPressed(Input.Keys.Z);
		
		if ((aPressed || zPressed) && !(aPressed && zPressed))
		{
			if(aPressed)
			{
				redBar.setValue(redBar.getValue() + .01f);
			}
			else
			{
				redBar.setValue(redBar.getValue() - .01f);
			}
		}
		
		boolean sPressed = Gdx.input.isKeyPressed(Input.Keys.S);
		boolean xPressed = Gdx.input.isKeyPressed(Input.Keys.X);
		
		if ((sPressed || xPressed) && !(sPressed && xPressed))
		{
			if(sPressed)
			{
				greenBar.setValue(greenBar.getValue() + .01f);
			}
			else
			{
				greenBar.setValue(greenBar.getValue() - .01f);
			}
		}
		
		boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.D);
		boolean cPressed = Gdx.input.isKeyPressed(Input.Keys.C);
		
		if ((dPressed || cPressed) && !(dPressed && cPressed))
		{
			if(dPressed)
			{
				blueBar.setValue(blueBar.getValue() + .01f);
			}
			else
			{
				blueBar.setValue(blueBar.getValue() - .01f);
			}
		}
	}
}
