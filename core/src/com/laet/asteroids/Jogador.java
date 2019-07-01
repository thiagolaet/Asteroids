package com.laet.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Jogador extends GameObjects{

    private float[] flamex;
    private float[] flamey;

    private ArrayList<Tiro> listaTiros;
    private float contadorTiro;

    private float veloMax;
    private float acelera;
    private float desacelera;
    private float contadorAcelera;

    private boolean hit;
    private boolean dead;
    private float hitTempo;
    private float hitMaxTempo;
    private Line2D.Float[] hitLines;
    private Point2D.Float[] hitLinesVector;

    private long score;
    private int vidas;
    private long pontosVidasExtra;

    public Jogador(ArrayList<Tiro> listaTiros){

        this.listaTiros = listaTiros;

        x = Main.WIDTH/2;
        y = Main.HEIGHT/2;

        veloMax = 300;
        acelera = 200;
        desacelera = 10;

        shapex = new float[4];
        shapey = new float[4];
        flamex = new float[3];
        flamey = new float[3];

        radians = 3.1415f/2;
        rotationSpeed = 3;

        contadorTiro = 0;

        hit = false;
        hitTempo = 0;
        hitMaxTempo = 2;

        score = 0;
        vidas = 3;
        pontosVidasExtra = 5000;

    }

    public long getScore(){return score;}

    public long getVidas(){
        return vidas;
    }

    //Função responsável por criar o formato da nave através de 4 pontos da tela
    private void setShape(){
        shapex[0] = x + MathUtils.cos(radians) * 8;
        shapey[0] = y + MathUtils.sin(radians) * 8;

        shapex[1] = x + MathUtils.cos(radians - 4 * 3.1415f / 5) * 8;
        shapey[1] = y + MathUtils.sin(radians - 4 * 3.1415f / 5) * 8;

        shapex[2] = x + MathUtils.cos(radians + 3.1415f) * 5;
        shapey[2] = y + MathUtils.sin(radians + 3.1415f) * 5;

        shapex[3] = x + MathUtils.cos(radians + 4 * 3.1415f/5) * 8;
        shapey[3] = y + MathUtils.sin(radians + 4 * 3.1415f/5) * 8;
    }

    //Função responsável por criar a animação do fogo que aparece atrás da nave toda vez que a mesma é acelerada
    private void setFlame(){
        flamex[0] = x + MathUtils.cos(radians - 5 * 3.1415f / 6) * 5;
        flamey[0] = y + MathUtils.sin(radians - 5 * 3.1415f / 6) * 5;

        flamex[1] = x + MathUtils.cos(radians - 3.1415f) * (6 + contadorAcelera * 50);
        flamey[1] = y + MathUtils.sin(radians - 3.1415f) * (6 + contadorAcelera * 50);

        flamex[2] = x + MathUtils.cos(radians + 5 * 3.1415f / 6) * 5;
        flamey[2] = y + MathUtils.sin(radians + 5 * 3.1415f / 6) * 5;
    }

    public void setPosition(float x, float y){
        super.setPosition(x, y);
        setShape();
    }

    public void perdeuVida(){vidas--;}

    public void somaScore(long l){ score += l;}

    //Função responsável pela animação de "morte" da nave
    public void hit(){
        if(hit) return;
        hit = true;
        xSpeed = ySpeed = 0;
        hitLines = new Line2D.Float[4];
        for(int i = 0, j = hitLines.length - 1; i < hitLines.length; j = i++){
            hitLines[i] = new Line2D.Float(
                    shapex[i], shapey[i], shapex[j], shapey[j]
            );
        }
        hitLinesVector = new Point2D.Float[4];
        hitLinesVector[0] = new Point2D.Float(
                MathUtils.cos(radians + 1.5f), MathUtils.sin(radians + 1.5f)
        );
        hitLinesVector[1] = new Point2D.Float(
                MathUtils.cos(radians - 1.5f), MathUtils.sin(radians - 1.5f)
        );
        hitLinesVector[2] = new Point2D.Float(
                MathUtils.cos(radians - 2.8f), MathUtils.sin(radians - 2.8f)
        );
        hitLinesVector[3] = new Point2D.Float(
                MathUtils.cos(radians + 2.8f), MathUtils.sin(radians + 2.8f)
        );
    }


    public boolean isHit() {return hit;}
    public boolean isDead() {return dead;}

    //Recoloca a nave no jogo após o jogador morrer
    public void respawn(){
        x = Main.WIDTH/2;
        y = Main.HEIGHT/2;
        setShape();
        hit = dead = false;
    }

    public void update(float dt) {

        //Função responsável pela animação da morte
        if(hit){
            hitTempo += dt;
            if(hitTempo > hitMaxTempo){
                dead = true;
                hitTempo = 0;
            }
            for(int i = 0; i < hitLines.length; i++){
                hitLines[i].setLine(
                        hitLines[i].x1 + hitLinesVector[i].x * 10 * dt,
                        hitLines[i].y1 + hitLinesVector[i].y * 10 * dt,
                        hitLines[i].x2 + hitLinesVector[i].x * 10 * dt,
                        hitLines[i].y2 + hitLinesVector[i].x * 10 * dt
                );
            }
            return;
        }

        // Checando pontos para vidas extras
        if(score >= pontosVidasExtra){
            vidas++;
            pontosVidasExtra += pontosVidasExtra;
        }

        //Controla a rotação da nave
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            radians += rotationSpeed * dt;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            radians -= rotationSpeed * dt;
        }

        //Controla a aceleração da nave e quando a animação do fogo deve ocorrer
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            xSpeed += MathUtils.cos(radians) * acelera * dt;
            ySpeed += MathUtils.sin(radians) * acelera * dt;
            contadorAcelera += dt;
            if(contadorAcelera > 0.1f) {
                contadorAcelera = 0;
            }
        }
        else {
            contadorAcelera = 0;
        }

        //Atira
        contadorTiro += dt;
        if(contadorTiro > 0.3){
            if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                listaTiros.add(new Tiro(x, y, radians));
                contadorTiro = 0;
            }
        }

        //Impõe uma força contrária a da aceleração na nave fazendo com que a mesma perca velocidade e pare caso o jogador deixar de acelerar por um tempo
        float vec = (float) Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed);
        if(vec > 0){
            xSpeed -= (xSpeed /vec) * desacelera * dt;
            ySpeed -= (ySpeed /vec) * desacelera * dt;
        }

        //Impede que a velocidade ultrapasse a velocidade máxima pré-estabelecida
        if(vec > veloMax){
            xSpeed = (xSpeed /vec) * veloMax;
            ySpeed = (ySpeed /vec) * veloMax;
        }

        //Atualiza a posição da nave
        x += xSpeed * dt;
        y += ySpeed * dt;

        setFlame();
        setShape();

        wrap();
    }

    public void draw(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(1,1, 1, 1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        //Desenhando a nave explodida caso o jogador tenha perdido vida
        if(hit){
            for(int i = 0; i < hitLines.length; i++){
                shapeRenderer.line(
                        hitLines[i].x1,
                        hitLines[i].y1,
                        hitLines[i].x2,
                        hitLines[i].y2
                );
            }
            shapeRenderer.end();
            return;
        }

        //Desenha a nave na tela
        for(int i = 0, j = shapex.length - 1; i < shapex.length; j = i++){
            shapeRenderer.line(shapex[i], shapey[i], shapex[j], shapey[j]);
        }

        //Desenha o fogo na tela
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            for (int i = 0, j = flamex.length - 1; i < flamex.length; j = i++) {
                shapeRenderer.line(flamex[i], flamey[i], flamex[j], flamey[j]);
            }
        }
        shapeRenderer.end();
    }

}
