package com.laet.asteroids;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class Tiro extends GameObjects{

    private float tempo;
    private float maxTempo;

    private boolean remove;

    public Tiro(float x, float y, float radians){
        this.x = x;
        this.y = y;
        this.radians = radians;

        float speed = 350;
        xSpeed = MathUtils.cos(radians) * speed;
        ySpeed = MathUtils.sin(radians) * speed;

        width = height = 2;

        maxTempo = 0;
        tempo = 1;
    }

    //Função que retorna quando o tiro deve ser removido da lista de tiros
    public boolean shouldRemove(){
        return remove;
    }

    public void update(float dt){

        //Atualiza a posição do tiro
        x += xSpeed * dt;
        y += ySpeed * dt;

        wrap();

        //Checa se o tiro deve ser removido do jogo
        maxTempo += dt;
        if(maxTempo > tempo){
            remove = true;
        }
    }

    public void draw(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(1, 1, 1, 1);

        //Desenhando o tiro
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(x - width/2,y - height/2,1);

        shapeRenderer.end();
    }
}
