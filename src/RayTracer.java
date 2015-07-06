/* 
 * Sam Rossin
 * This is a ray tracer.
 */
import java.awt.image.*;
import javax.imageio.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;


public class RayTracer{
	//The Scene Is defined here
	double viewerPosition[] = {15, 0, 2};
	
	//Pixels per unit distance
	private int dpi = 256;
	
	//Position of the pixel grid
	double gridX = 10;
	double gridY = -3; // (gridX, gridY, gridZ) is the upper left
	double gridZ = 4;  // corner of the pixel grid, which lies in
					  // the plane X=10
	
	//dimensions of the pixel grid
	double gridWidth = 6; // gridWidth and gridHeight are the
	double gridHeight = 4; // dimensions of the pixel grid
	
	//constants
	int maxLevel = 8;
	double minWeight = .01f;
	int phong = 30;
	
	//lighting
	double[] ambient = new double[]{.3f, .3f, .3f};
	private LightSource[] lights = {new LightSource(new double[] {1, 1, 1}, new double[] {14, -7, 4}), 
			new LightSource(new double[] {.8, .8, 1}, new double[] {15, 5 , 5})};
	
	//objects
	private Shape[] objects;
	double[] background = new double[]{10, 10, 40};;
	int picNum = 5;
	
	private String name = "refsphere";
	//end scene definition
	
	public static void main(String[] args) {
		new RayTracer();
	}
	
	private double BLACKf[] = {0, 0, 0};
	private int M = (int) gridWidth*dpi; // columns of bitmap
	private int N = (int) gridHeight*dpi; // rows of the bitmap
	
	public  RayTracer() {
		makePicture();
	}
	
	void putColor( BufferedImage image, int x, int y, int[] source) {
			int rgb = (255 << 24) + (source[0] << 16) + (source[1] << 8) + source[2];
			image.setRGB(x, y, rgb);
	}

	public Ray makeRay(int i, int j) {
		// returns a ray from the viewer through pixel (i, j)
		double direction[] = new double[3];
		double pixel[] = new double[3];
		pixel[0] = gridX;
		pixel[1] = gridY+(gridWidth*j)/M;
		pixel[2] = gridZ - (gridHeight*i)/N;
		for (int k = 0; k < 3; k++) {
			direction[k] = pixel[k]-viewerPosition[k];
		}
		return new Ray(viewerPosition, direction);
	}
	
	Intersection intersect(Ray r) {
		Intersection nearest = null;
		for(Shape s : objects){
			Intersection temp = s.intersect(r);
			if(temp != null){
				if (nearest == null) nearest = temp;
				else{
					if (nearest.t > temp.t) nearest = temp;
				}
			}
		}return nearest;
	}
	
	
	//matrix opperators
	private double dot(double[] a, double[] b){
		double retval = 0;
		for(int i=0; i<a.length; i++) retval += a[i]*b[i];
		return retval;
	}
	
	private double[] scale(double[] a, double b){
		double[] retval = new double[a.length];
		for(int i = 0; i<a.length; i++){
			retval[i] = a[i]*b;
		}return retval;
	}
	
	private double[] plus(double[] a, double[] b){
		double[] retval = new double[a.length];
		for(int i = 0; i<a.length; i++){
			retval[i] = a[i] + b[i];
		}return retval;
	}
	
	//calculate the diffuse color and hightlights
	double[] diffuseCalc(Shape o, Intersection I){

		//light vectors
		Ray[] lightRays = new Ray[lights.length];
		for(int i=0; i<lights.length; i++){
			double[] lDirection = new double[] {I.point[0] -lights[i].position[0], 
					I.point[1] - lights[i].position[1],
					I.point[2] - lights[i].position[2]};

			lightRays[i] = new Ray(lights[i].position, lDirection); 
		}
		
		double[][] diff = new double[lights.length][];
		double[][] ref = new double[lights.length][];
		
		for(int i=0; i< lights.length; i++){
			diff[i] = plus(I.point, scale(lights[i].position, -1));
			ref[i] = scale(plus(diff[i], scale(I.normal, -2*dot(diff[i], I.normal))),  
					(1/Math.sqrt(dot(diff[i], diff[i]))));
		}
		
		double[] retval = new double[]{0, 0, 0};
		double[] d = new double[lights.length];
		double[] h = new double[lights.length];

		double[] v = {viewerPosition[0] - I.point[0],
				viewerPosition[1] - I.point[1], viewerPosition[2] - I.point[2]};
		v = scale(v, (1/Math.sqrt(dot(v, v))));
		
		for(int i=0; i< lights.length; i++){
			Intersection lightIntersect = intersect(lightRays[i]);
			if (lightIntersect == null || lightIntersect.t > .99){
				//diffuse
				double[] dir = scale(lightRays[i].direction,  
						-(1/Math.sqrt(dot(lightRays[i].direction, lightRays[i].direction))));
				d[i] = o.diffuse*(dot(dir, I.normal));
				if (d[i] < 0) d[i] = 0;
				
				//specular
				double x = Math.max(dot(v, ref[i]), 0);
				h[i] = (o.specular*Math.pow(x, phong));
				
			}
		}
		
		double[] mult = {0,0,0};
		for(int i=0; i < lights.length; i++){
			for(int j=0; j<3; j++){
				mult[j] += (d[i]+h[i])*lights[i].color[j];
			}
		}
		
		for(int i=0; i<3; i++){
			retval[i] = o.color(I)[i]*mult[i];
		}
		return retval;
	}
	
	double [] trace(Ray r, int level, double weight) {
		if (level > maxLevel) return BLACKf;
		if (weight < minWeight) return BLACKf;
		
		Intersection I = intersect(r);

		if (I == null){
			if(level == 0) return background;
			else return BLACKf;
		}
		
		Shape o = objects[I.objectNumber];
		double[] oColor = o.color(I);
		//ambient
		double[] aColor = new double[]{o.amb*oColor[0]*ambient[0], 
				o.amb*oColor[1]*ambient[1], o.amb*oColor[2]*ambient[2]};

		//diffuse
		double[] dColor = diffuseCalc(o, I);
		
		//mirror reflections
		double[] D = r.direction;
		double[] R = scale(plus(D, scale(I.normal, -2*dot(D, I.normal))),(1/Math.sqrt(dot(D, D))));
		Ray reflection = new Ray(I.point, R);
		reflection = new Ray(reflection.rayPoint(.05), R);
		double[] mColor = scale(trace(reflection, level+1, weight*o.reflection), o.reflection);

		double[] total = new double[3];
		for(int i=0; i<3; i++){
			total[i] = aColor[i]+dColor[i] + mColor[i];
			if (total[i] > 255) total[i]=255;
		}
		return total;
	}
	
	private void setupPic1(){
		objects = new Shape[]{new Sphere(0, .8f, .4f, .2f, 2.2f, new double[]{-2, 3, 1}),
				new Sphere(1, 1f, .5f, 1f, 1.5, new double[]{-4, -3, .3}),
				new Sphere(2, .05f, .6f, .1f, 1.2f, new double[]{1, .5f, 0}),
				 new Plane(3, .5, .3, .3, new double[]{0,0,1}, -1.2, true)};
		background = new double[]{10, 10, 40};
		objects[1].color = new double[]{200, 200, 200};
		objects[0].color = new double[]{40, 40, 140};
	}
	
	private void setupPic2(){
		objects = new Shape[]{new Sphere(0, .8f, .4f, .2f, 1.5f, new double[]{-2, 2, .5}),
				new Cube(1, .5, .5, .9, new double[]{-6,-2, -7}, 6),
				 new Cube(2, .5, .5, .1, new double[]{-5,-4, -.4}, 3)};
		background = new double[]{100, 100, 255};
		objects[0].color = new double[]{40, 80, 140};
		objects[2].color = new double[]{20, 120, 60};
	}
	
	private void setupPic3(){
		objects = new Shape[]{
				 new Plane(0, .3, .3, .5, new double[]{0,0,1}, -1.2, true),
				 new Plane(1, .3, .1, .95, new double[]{1, 0, 0}, -10.1, false),
				 new Plane(2, .3, .1, .95, new double[]{-.1,1,0}, -7.405, false),
				 new Plane(3, .3, .1, .95, new double[]{.1, 1, 0}, 4, false),
				 new Sphere(4, .8f, .5f, .2f, 1.2f, new double[]{-4, -1, 1}),
				 new Plane(3, .3, .1, .95, new double[]{1, 0, 0}, 16, false)
		};
		objects[4].color = new double[]{30, 30, 60};
		
	}
	
	private void setupPic4(){
		Random r = new Random();
		objects = new Shape[21];
		objects[20] = new Plane(20, .5, .3, .3, new double[]{0,0,1}, -1.2, true);
		for(int i=0; i<20; i++){
			double zed = r.nextDouble()*1.5;
			objects[i] = new Ellipsoid(i, r.nextDouble(), r.nextDouble(),
					r.nextDouble(), new double[]{r.nextDouble()*30 - 20, 
					r.nextDouble()*30 - 15, r.nextDouble()*3 -1.2+zed},
					r.nextDouble()*1.5, r.nextDouble()*1.5, zed);
			objects[i].color = new double[]{r.nextInt(255), r.nextInt(255), r.nextInt(255)};
		}background = new double[]{10, 10, 40};
	}
	
	private void setupPic5(){
		objects = new Shape[]{new Sphere(0, .8f, .2f, .8f, 3f, new double[]{8, 0, 2}),
				new Sphere(1, .6f, .5f, .5f, 2f, new double[]{20, 1, 2}),
				new Sphere(2, .6f, .5f, .5f, 4f, new double[]{21, -6, -5}),
				new Sphere(3, .6f, .5f, .5f, 3f, new double[]{22, 8, 7}),
				new Sphere(4, .6f, .5f, .5f, 2f, new double[]{19, -5, 3}),
				new Cube(1, .5, .5, .9, new double[]{24, 7, -7}, 5) };
		background = new double[]{10, 10, 40};
		objects[0].color = new double[]{150, 150, 150};
		objects[1].color = new double[]{220, 0, 50};
		objects[2].color = new double[]{50, 0, 200};
		objects[3].color = new double[]{0, 100, 120};
		objects[4].color = new double[]{50, 100, 50};
	}
	
	public void makePicture() {
		//SET UP THE OBJECTS
		switch(picNum){
		case 1:
			setupPic1();
			break;
		case 2:
			setupPic2();
			break;
		case 3:
			setupPic3();
			break;
		case 4:
			setupPic4();
			break;
		case 5:
			setupPic5();
			break;
		}
		
		BufferedImage picture = new BufferedImage(M, N, BufferedImage.TYPE_INT_ARGB);
		
		// The buffer has N rows and M columns
		int percent = M*N/20;
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if((i*M+j)%percent==0){
					System.out.println((i*M+j)/percent*5 + " percent done!");
				}
				
				Ray r = makeRay(i, j);
				double[] fColor = trace(r, 0, 1);
				int[] color = new int[3];
				for (int k=0; k<3; k++){
					color[k] =  (int) Math.round(fColor[k]);
				}
				putColor(picture, j, i, color);
			}
		}File output = new File(name + ".png");
		try{
			ImageIO.write(picture, "png", output);
		}catch (IOException e){
			System.out.println("FAIL");
			System.exit(1);
		}
	}
	
}
