
class Polygon extends Shape{
	double[][] points;
	double[] norm;
	Plane plane;

	public Polygon(int num, double spec, double diff, double ref, double[][] points){
		color = new double[]{255, 0, 0};
		this.points = points;
		this.number = num;
		this.specular = spec;
		this.diffuse = diff;
		this.reflection = ref;
		
		norm = cross(minus(points[1], points[0]), minus(points[2], points[1]));
		double normLen = Math.sqrt(norm[0]*norm[0]+norm[1]*norm[1]+norm[2]*norm[2]);
		
		for(int i=0; i<3; i++) norm[i] = norm[i]/normLen;
		
		double d = dot(norm, points[0]);
		
		plane = new Plane(num, spec, diff, ref, norm, d, false);
	}
	
	public Intersection intersect(Ray r){
		Intersection temp = plane.intersect(r);
		if(temp == null) return null;
		temp.objectNumber = number;
		for(int i=0; i<points.length; i++){
			double[] e = minus(points[i], points[(i+1)%points.length]);
			double[] n = cross(e, norm);
			if(dot(n, temp.point) < dot(n, points[i])) return null;
		}
		return temp;
	}
	
	public double[] color(Intersection I){
		return color;
	}
}
