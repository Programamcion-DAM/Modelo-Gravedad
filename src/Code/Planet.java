package Code;

import java.awt.Dimension;
import processing.core.PApplet;
import processing.core.PVector;

public class Planet extends PApplet {
    
    public boolean staticPlanet;
    public long mass;
    public long radius;
    public PVector position = new PVector();
    
    public PVector previusPosition = new PVector();
    
    public PVector velocity = new PVector();
    public int r,g,b;
    
    private Dimension d;
    
    public Planet(PVector posicion, Dimension d) {
        posicion.x -= ((d.width - d.width / 7) / 2);
        posicion.y = (d.height / 2) - posicion.y;
        
        this.d = d;
        
        this.position.set(posicion);
        this.previusPosition.set(posicion);
    }

    public void setMass(long mass) {
        this.mass = mass;
    }

    public void setRadius(long radius) {
        this.radius = radius;
    }
    
    public void setColor(int r,int g,int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public void setStatic(boolean staticP){
        staticPlanet = staticP;
    }
    
    public void setVelocity(PVector velocity) {
        this.velocity = velocity;
    }

    public void updatePosition() {
        this.previusPosition.set(position);
        if(staticPlanet == false){
            position.add(velocity);
        } 
    }

    public void updateVelocity(float force, PVector direction) {
        //Calculamos el angulo de la fuerza respecto de la horizontal
        float arcAngle = direction.y / direction.x;
        float angle = atan(arcAngle);

        //Segundo cuadrante
        if (direction.y > 0 && direction.x < 0) {
            angle += PI;
        }
        //Tercer cuadrante
        if (direction.y < 0 && direction.x < 0) {
            angle += PI;
        }

        float forceX = cos(angle) * force;
        float forceY = sin(angle) * force;

        velocity.x += forceX / mass;
        velocity.y += forceY / mass;

    }
    
    public boolean touchingPlanet(PVector mousePosition){
        mousePosition.x -= ((d.width - d.width / 7) / 2);
        mousePosition.y = (d.height / 2) - mousePosition.y;
        
        PVector distance = new PVector();
        distance.set(mousePosition);
        distance.sub(position);
        
        if(distance.mag() < radius){
            return true;
        }
        return false;
    }
    
    

}
