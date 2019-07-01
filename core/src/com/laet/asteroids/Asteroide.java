package com.laet.asteroids;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class Asteroide extends GameObjects{

    private int tipo;
    public static final int PEQUENO = 3;
    public static final int MEDIO = 2;
    public static final int GRANDE = 1;
    private int score;

    //Número de vértices do asteroide e um array com a distância entre esses pontos e o centro do asteroide
    private int numPoints;
    private float[] dists;

    private boolean remove;

    public Asteroide(float x, float y, int tipo){
        this.x = x;
        this.y = y;
        this.tipo = tipo;

        //Dando atributos diferentes para cada tipo de asteroide
        if(tipo == PEQUENO){
            numPoints = 8;
            width = height = 18;
            speed = MathUtils.random(70, 100);
            score = 25;
        }
        else if(tipo == MEDIO){
            numPoints = 10;
            width = height = 36;
            speed = MathUtils.random(50,60);
            score = 50;
        }
        else if(tipo == GRANDE){
            numPoints = 12;
            width = height = 72;
            speed = MathUtils.random(20, 30);
            score = 100;
        }
        rotationSpeed = MathUtils.random(-1, 1);
        radians = MathUtils.random(2*3.1415f);
        xSpeed = MathUtils.cos(radians) * speed;
        ySpeed = MathUtils.sin(radians) * speed;
        shapex = new float[numPoints];
        shapey = new float[numPoints];
        dists = new float[numPoints];
        int radius = width/2;
        for(int i = 0; i < numPoints; i++){
            dists[i] = MathUtils.random(radius/2, radius);
        }
        setShape();
    }

    //Criando a forma do asteroide usando pontos random dentro da área aceitável
    private void setShape(){
        float angle = 0;
        for(int i = 0; i < numPoints; i++){
            shapex[i] = x + MathUtils.cos(angle + radians) * dists[i];
            shapey[i] = y + MathUtils.sin(angle + radians) * dists[i];
            angle += 2 * 3.1415f / numPoints;
        }
    }

    public int getTipo(){return tipo;}
    public int getScore(){return score;}
    public boolean shouldRemove() { return remove; }

    public void update(float dt){

        //Atualizando a rotação e a posição do asteroide
        x += xSpeed * dt;
        y += ySpeed * dt;
        radians += rotationSpeed * dt;

        setShape();
        wrap();
    }

    public void draw(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        //Desenhando asteroide
        for(int i = 0, j = shapex.length - 1; i < shapex.length; j = i++){
            shapeRenderer.line(shapex[i], shapey[i], shapex[j], shapey[j]);
        }
        shapeRenderer.end();
    }
}
