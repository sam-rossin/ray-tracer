
class Cube extends Shape{
	double[][] points;
	Polygon[] polygons;
	public Cube(int num, double spec, double diff, double ref,double[] point, double side){
		this.color = new double[]{255, 0, 0};
		this.number = num;
		this.specular = spec;
		this.diffuse = diff;
		this.reflection = ref;
		
		points=new double[][] {point, {point[0] + side, point[1], point[2]}, 
				{point[0]+ side, point[1]+ side, point[2]},
				{point[0], point[1]+ side, point[2]},
				{point[0], point[1], point[2] + side},
				{point[0] + side, point[1], point[2] + side}, 
				{point[0]+ side, point[1]+ side, point[2] + side},
				{point[0], point[1]+ side, point[2] + side}};
		polygons = new Polygon[]{new Polygon(0,0,0,0, new double[][]{points[3], points[2], points[1], points[0]}),
				new Polygon(0,0,0,0, new double[][]{points[0], points[1], points[5], points[4]}),
				new Polygon(0,0,0,0, new double[][]{points[0], points[3], points[7], points[4]}),
				new Polygon(0,0,0,0, new double[][]{points[1], points[2], points[6], points[5]}),
				new Polygon(0,0,0,0, new double[][]{points[2], points[3], points[7], points[6]}),
				new Polygon(0,0,0,0, new double[][]{points[4], points[5], points[6], points[7]}),
				};
	}public Intersection intersect(Ray r){
		Intersection retval = null;
		for(Polygon x: polygons) {
			Intersection i = x.intersect(r);
			if (i != null && (retval == null || i.t < retval.t)) {
				retval = i;
			}
		}if (retval != null) retval.objectNumber = number;
		return retval;
	}
	
	public double[] color(Intersection I) {return color;};
}
