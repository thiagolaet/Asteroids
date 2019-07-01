package com.laet.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

public class Jogo {

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private int nivel;

    private ArrayList<Tiro> listaTiros;
    private ArrayList<Asteroide> listaAsteroides;
    private ArrayList<Particulas> listaParticulas;

    private Jogador vidas;
    private Jogador jogador;

    private float WIDTH;
    private float HEIGHT;


    public Jogo(int WIDTH, int HEIGHT, ShapeRenderer shapeRenderer, SpriteBatch batch, BitmapFont font){
        listaTiros = new ArrayList<Tiro>();
        listaAsteroides = new ArrayList<Asteroide>();
        listaParticulas = new ArrayList<Particulas>();
        jogador = new Jogador(listaTiros);
        vidas = new Jogador(null);

        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.shapeRenderer = shapeRenderer;
        this.font = font;
        this.batch = batch;
    }

    public long getPoints(){
        return jogador.getScore();
    }

    public boolean gameOver(){
        if (jogador.getVidas() == 0) return true;
        return false;
    }

    //Cria as particulas liberadas quando um asteroide é atingido por um tiro
    private void createParticles(float x, float y){
        for(int i = 0; i < 6; i ++){
            listaParticulas.add(new Particulas(x, y));
        }
    }

    //Função responsável por dividir o asteroide em 2 após este ser atingido por um tiro
    private void splitAsteroids(Asteroide a){
        createParticles(a.getX(), a.getY());
        if(a.getTipo() == Asteroide.GRANDE){
            listaAsteroides.add(new Asteroide(a.getX(), a.getY(), Asteroide.MEDIO));
            listaAsteroides.add(new Asteroide(a.getX(), a.getY(), Asteroide.MEDIO));
        }
        if(a.getTipo() == Asteroide.MEDIO){
            listaAsteroides.add(new Asteroide(a.getX(), a.getY(), Asteroide.PEQUENO));
            listaAsteroides.add(new Asteroide(a.getX(), a.getY(), Asteroide.PEQUENO));
        }
    }

    //Função responsável por criar os asteroides e os adicionar na lista
    private void spawnAsteroides(){
        listaAsteroides.clear();
        int numToSpawn = 4 + nivel -1;

        for(int i = 0; i < numToSpawn; i++){
            float x, y, xSpeed, ySpeed, dist;

            do{ x = MathUtils.random(Main.WIDTH);
                y = MathUtils.random(Main.HEIGHT);
                xSpeed = x - jogador.getX();
                ySpeed = y - jogador.getY();
                dist = (float) Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed);
            } while (dist < 100);
            listaAsteroides.add(new Asteroide(x, y, Asteroide.GRANDE));
        }
    }

    //Função usada para checar a colisão entre o tiro e os asteroides e entre o jogador e os asteroides
    private void checkCollisions(){
        //Tiro e Asteroide
        if(!jogador.isHit()){
            for(int i = 0; i < listaTiros.size(); i++){
                Tiro tiro = listaTiros.get(i);
                for(int j = 0; j < listaAsteroides.size(); j++){
                    Asteroide a = listaAsteroides.get(j);
                    if(a.contains(tiro.getX(), tiro.getY())){
                        listaTiros.remove(i);
                        i--;
                        listaAsteroides.remove(j);
                        j--;
                        splitAsteroids(a);
                        jogador.somaScore(a.getScore());
                        break;
                    }
                }
            }
        }

        //Asteroide e Jogador
        for(int i = 0; i < listaAsteroides.size(); i++){
            Asteroide a = listaAsteroides.get(i);
            if(a.intersects(jogador)){
                jogador.hit();
                listaAsteroides.remove(i);
                i--;
                splitAsteroids(a);
                break;
            }
        }
    }

    public void update(){

        //Passando de nível
        if(listaAsteroides.size() == 0){
            nivel++;
            spawnAsteroides();
        }

        //Gerenciando respawn e perda de vida após o jogador morrer
        if(jogador.isDead()){
            jogador.respawn();
            jogador.perdeuVida();
            return;
        }

        //Atualizando os asteroides
        for(int i = 0; i < listaAsteroides.size(); i++){
            listaAsteroides.get(i).update(Gdx.graphics.getDeltaTime());
            if(listaAsteroides.get(i).shouldRemove()){
                listaAsteroides.remove(i);
            }
        }

        //Atualizando os tiros
        for(int i = 0; i < listaTiros.size(); i++){
            listaTiros.get(i).update(Gdx.graphics.getDeltaTime());
            if(listaTiros.get(i).shouldRemove()){
                listaTiros.remove(i);
                i--;
            }
        }

        //Atualizando as particulas
        for(int i = 0; i < listaParticulas.size(); i++){
            listaParticulas.get(i).update(Gdx.graphics.getDeltaTime());
            if(listaParticulas.get(i).shouldRemove()){
                listaParticulas.remove(i);
                i--;
            }
        }

        jogador.update(Gdx.graphics.getDeltaTime());

        checkCollisions();

        //Desenhando os tiros
        for(int i = 0; i< listaTiros.size(); i++){
            listaTiros.get(i).draw(shapeRenderer);
        }
        jogador.draw(shapeRenderer);

        //Desenhando os asteroides
        for(int i = 0; i < listaAsteroides.size(); i++){
            listaAsteroides.get(i).draw(shapeRenderer);
        }

        //Desenhando as particulas
        for(int i = 0; i < listaParticulas.size(); i++){
            listaParticulas.get(i).draw(shapeRenderer);
            if(listaParticulas.get(i).shouldRemove()){
                listaParticulas.remove(i);
                i--;
            }
        }

        //Desenhando a pontuação
        batch.begin();
        String pontos = "" + jogador.getScore();
        font.draw(batch, pontos, 15, HEIGHT - 20);
        batch.end();

        //Desenhando nivel
        batch.begin();
        String nivelStr = "" + this.nivel;
        font.draw(batch, nivelStr, WIDTH - 20, HEIGHT - 20);
        batch.end();

        //Desenhando as vidas
        for(int i = 0; i < jogador.getVidas(); i++){
            vidas.setPosition(20 + i * 12, HEIGHT - 50);
            vidas.draw(shapeRenderer);
        }
    }
}
