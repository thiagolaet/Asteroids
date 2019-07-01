package com.laet.asteroids;

public class GameObjects {

    protected float x;
    protected float y;

    protected float xSpeed;
    protected float ySpeed;

    protected float radians;
    protected float speed;
    protected float rotationSpeed;

    protected int width;
    protected int height;

    protected float[] shapex;
    protected float[] shapey;

    //Função usada para fazer com que os GameObjects apareçam do lado oposto ao que saíram da tela
    protected  void wrap(){
        if(x<0) x = Main.WIDTH;
        if(x> Main.WIDTH) x = 0;
        if(y < 0) y = Main.HEIGHT;
        if(y > Main.HEIGHT) y = 0;
    }

    public float[] getShapex() { return shapex; }
    public float[] getShapey() { return shapey; }

    //Funções responsáveis por checar se houve colisão
    public boolean contains(float x, float y){
        boolean b = false;
        for(int i = 0, j = shapex.length - 1; i < shapex.length; j=i++){
            if((shapey[i] > y) != (shapey[j] > y) &&
                    (x < (shapex[j] - shapex[i]) * (y - shapey[i]) / (shapey[j] - shapey[i]) + shapex[i])){
                b = !b;
            }
        }
        return b;
    }
    public boolean intersects(GameObjects other){
        float[] shapex = other.getShapex();
        float[] shapey = other.getShapey();

        for(int i = 0; i < shapex.length; i++) {
            if (contains(shapex[i], shapey[i])) {
                return true;
            }
        }
        return false;
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getX(){ return x; }
    public float getY(){return y; }
}
