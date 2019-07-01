package com.laet.asteroids;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Main extends ApplicationAdapter {
	public static int WIDTH;
	public static int HEIGHT;

	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private Texture pressStart;
	private Texture gameOver;
	private Texture logo;

	private Jogo jogo;

	private int GAME_STATE; //1 - Menu 2 - Jogando 3 - Game Over

	@Override
	public void create () {
		HEIGHT = Gdx.graphics.getHeight();
		WIDTH = Gdx.graphics.getWidth();
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont(Gdx.files.internal("asteroids.fnt"), false);
		pressStart = new Texture("press_start.png");
		gameOver = new Texture("game_over.png");
		logo = new Texture("logo.png");

		jogo = new Jogo(WIDTH, HEIGHT, shapeRenderer, batch, font);

		GAME_STATE = 1;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Switch responsável por gerenciar em que estado o jogo está / 1 - Menu / 2 - Jogo / 3 - Game Over
		switch(GAME_STATE){
			case(1):
				if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
					GAME_STATE = 2;
				}
				batch.begin();
				batch.draw(logo,WIDTH/2 - logo.getWidth()/2, HEIGHT/2 - logo.getHeight()/2 + 150);
				batch.draw(pressStart, WIDTH/2 - pressStart.getWidth()/2, HEIGHT/2 - pressStart.getHeight()/2 - 150);
				batch.end();
				break;

			case(2):
				if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
					jogo = new Jogo(WIDTH, HEIGHT, shapeRenderer, batch, font);
					GAME_STATE = 1;
				}
				if(jogo.gameOver()){
					GAME_STATE = 3;
				}
				jogo.update();
				break;

			case(3):
				if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
					jogo = new Jogo(WIDTH, HEIGHT, shapeRenderer, batch, font);
					GAME_STATE = 1;
				}
				if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
					jogo = new Jogo(WIDTH, HEIGHT, shapeRenderer, batch, font);
					GAME_STATE = 2;
				}
				batch.begin();
				batch.draw(gameOver, WIDTH/2 - gameOver.getWidth()/2, HEIGHT/2 - gameOver.getHeight()/2);
				batch.draw(pressStart, WIDTH/2 - pressStart.getWidth()/2, HEIGHT/2 - pressStart.getHeight()/2 - 150);
				String pontos = "" + jogo.getPoints();
				font.draw(batch, pontos, WIDTH/2 - 30, HEIGHT/2 + 100);
				batch.end();
				break;
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
