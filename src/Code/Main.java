package Code;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import processing.core.*;

public class Main extends PApplet {

    Toolkit tk = Toolkit.getDefaultToolkit();
    Dimension d = tk.getScreenSize();
    int sizeMenu = d.width / 7;
    
    PGraphics canvas;

    ArrayList<Planet> planets = new ArrayList<Planet>();
    boolean takePlanet = false;

    public static void main(String[] args) {
        PApplet.main(new String[]{Code.Main.class.getName()});
    }

    @Override
    public void settings() {
        size(d.width, d.height);
    }

    @Override
    public void setup() {
        
        canvas = createGraphics(width,height);
        canvas.beginDraw();
        canvas.background(0);
        canvas.endDraw();
        
    }
    
    @Override
    public void draw() {
        if(keyPressed){
            image(canvas,0,0);
        }
        else{
            background(0);
        }
        
        
        //Situamos el transalte en el centro del mapa donde se pueden mover
        translate((d.width - sizeMenu) / 2, d.height / 2);
        for (int i = 0; i < planets.size(); i++) {
            drawPlanet(planets.get(i));
            
            canvas.beginDraw();
            canvas.translate(((d.width - sizeMenu) / 2), (d.height / 2));
            canvas.stroke(200,0,0);
            canvas.line(planets.get(i).previusPosition.x, -planets.get(i).previusPosition.y, planets.get(i).position.x, -planets.get(i).position.y);
            canvas.endDraw();
            
        }

        translate(-(d.width - sizeMenu) / 2, -(d.height / 2));
        drawGraficInterface();
        choosePlanet();

        calculateForce();
        updatePosition();
    }

    void drawGraficInterface() {
        fill(100);
        rect(d.width - d.width / 7, 0, d.width / 7, d.height);

        //Dibujamos el contorno blanco del menu
        fill(255);
        rect(d.width - d.width / 7 - 5, 0, 10, d.height);

        //Dibujamos el rectangulo donde se encuentra para elegir el planeta.
        fill(170);
        noStroke();
        rect(d.width + sizeMenu / 7 - sizeMenu, d.height / 15, (int) (sizeMenu - sizeMenu / 3.5), (int) (sizeMenu * 1.15));

        fill(190, 50, 50);
        stroke(0);
        strokeWeight(3);
        int ellipseDiameter = (int) (sizeMenu - sizeMenu / 3.5 - 50);
        ellipse(d.width - sizeMenu / 2, (int) (d.height / 15 + sizeMenu / 2.5), ellipseDiameter, ellipseDiameter);

        fill(0);
        textSize(40);
        textAlign(CENTER);
        text("PLANET", d.width - sizeMenu / 2, (int) (d.height / 15 + sizeMenu / 2.5) + ellipseDiameter + 20);

    }

    void choosePlanet() {
        if (mousePressed) {
            fill(200, 0, 0);
            ellipse(10, 10, 10, 10);
            //Comprueba si esta el mouse dentro del selector
            if (mouseX >= d.width + sizeMenu / 7 - sizeMenu && mouseX <= d.width + sizeMenu / 7 - sizeMenu + sizeMenu - sizeMenu / 3.5 && mouseY >= d.height / 15 && mouseY <= d.height / 15 + sizeMenu * 1.15) {
                takePlanet = true;
            }
        } else {
            //Este condicionador entra una vez se suelta el planeta que habias cogido
            if (takePlanet == true) {
                if (mouseX < d.width - sizeMenu) {

                    Planet planet = new Planet(new PVector(mouseX, mouseY), d);
                    PlanetCustom p = new PlanetCustom(null, true);

                    p.set(planet);

                    p.setVisible(true);

                    planets.add(planet);

                }
            }

            takePlanet = false;
        }

        if (takePlanet) {
            fill(190, 50, 50, 200);
            ellipse(mouseX, mouseY, (int) (sizeMenu - sizeMenu / 3.5 - 50), (int) (sizeMenu - sizeMenu / 3.5 - 50));
        }
    }

    void drawPlanet(Planet planet) {
        fill(planet.r, planet.g, planet.b);
        ellipse(planet.position.x, -planet.position.y, planet.radius * 2, planet.radius * 2);
    }

    void calculateForce() {
        for (int i = 0; i < planets.size(); i++) {
            for (int j = i + 1; j < planets.size(); j++) {
                float atractionForce = calculateAtractionForce(planets.get(i), planets.get(j));

                //Sacamos las dos direcciones de las fuerzas
                PVector direction1 = new PVector();
                direction1.set(planets.get(j).position);
                direction1.sub(planets.get(i).position);
                direction1.normalize();

                PVector direction2 = new PVector();
                direction2.set(direction1);
                direction2.mult(-1);

                planets.get(i).updateVelocity(atractionForce, direction1);
                planets.get(j).updateVelocity(atractionForce, direction2);

            }
        }
    }

    float calculateAtractionForce(Planet p1, Planet p2) {
        float force;
        float k = (float) 0.1;

        PVector distance = new PVector();
        distance.set(p1.position);
        distance.sub(p2.position);

        force = (float) (k * (p1.mass * p2.mass) / distance.mag());

        return force;
    }
    
    void updatePosition(){
        for(int i = 0; i < planets.size();i++){
            planets.get(i).updatePosition();
        }
    }
    
}

