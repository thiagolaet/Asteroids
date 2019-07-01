package com.laet.asteroids;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class Particulas extends GameObjects {

    private float tempoAtual;
    private float tempoMax;
    private boolean remove;

    public Particulas(float x, float y){
        this.x = x;
        this.y = y;
        width = height = 2;

        speed = 50;
        radians = MathUtils.random(2 * 3.1415f);
        xSpeed = MathUtils.cos(radians) * speed;
        ySpeed = MathUtils.sin(radians) * speed;

        tempoAtual = 0;
        tempoMax = 1;

    }

    public boolean shouldRemove() {return remove;}

    public void update(float dt){
        x += xSpeed * dt;
        y += ySpeed * dt;

        tempoAtual += dt;
        if(tempoAtual > tempoMax){
            remove = true;
        }
    }

    public void draw(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(1, 1, 1, 1);

        //Desenhando a particula
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(x - width/2,y - height/2,width/2);
        shapeRenderer.end();

    }

}
