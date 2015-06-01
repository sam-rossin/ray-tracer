import java.text.DecimalFormat;

public class Matrix {
	private int width;
	private int height;
	private double[][] contents;
	
	public int height(){
		return height;
	}
	
	public int width(){
		return width;
	}
	
	public double dot(Matrix a){
		if (height > 1 || a.height() > 1){
			throw new IndexOutOfBoundsException("To dot, must be a vector.");
		}if(width != a.width()){
			throw new IndexOutOfBoundsException("Vectors must be same length.");
		}
		double retval = 0;
		for(int i=0; i<width; i++){
			retval += contents[0][i]*a.contents[0][i];
		}return retval;
	}
	
	public void set(int x, int y, double a){
		if (x >= width || y>=height){
			throw new IndexOutOfBoundsException("Tried to set illegal index.");
		}
		contents[y][x] = a;
	}
	
	public double get(int x, int y){
		if (x >= width || y>=height){
			throw new IndexOutOfBoundsException("Tried to get illegal index.");
		}
		return contents[y][x];
	}
	
	public Matrix(int w, int h){
		width = w;
		height = h;
		contents = new double[height][width];
	}
	
	public Matrix(double[][] contents){
		width = contents[0].length;
		height = contents.length;
		this.contents = contents;
	}
	
	public Matrix plus(Matrix b){
		if(width != b.width || height !=b.height){
			throw new IndexOutOfBoundsException("Can't add matrices that are different sizes.");
		}
		Matrix retval = new Matrix(width, height);
		for (int i=0; i<width; i++){
			for (int j=0; j<height; j++){
				retval.set(i, j, contents[j][i]+b.get(i, j));
			}
		}return retval;
	}
	
	/**
	 * turns an array into a row vector matrix
	 * @param contents
	 * @return
	 */
	public static Matrix rowVector(double[] contents){
		Matrix retval = new Matrix(contents.length, 1);
		for(int i=0; i<contents.length; i++){
			retval.set(i, 0, contents[i]);
		}
		return retval;
	}
	
	
	/**
	 * returns the product of the matrix with a matrix b
	 * @param b
	 * @return
	 */
	public Matrix times(Matrix b){
		if (width != b.height()){
			throw new IndexOutOfBoundsException("Times: matrix sizes don't match.");
		}
		Matrix retval = new Matrix(b.width(), height);
		for (int i=0; i<height; i++){
			for (int j=0; j<b.width(); j++){
				double total = 0;
				for (int k=0; k<width; k++){
					total += get(k, i)*b.get(j, k);
				}retval.set(j, i, total);
			}
		}
		return retval;
	}
	
	
	public String toString(){
		DecimalFormat df = new DecimalFormat("####0.00");
		String retval = "";
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				retval = retval + df.format(contents[i][j]) + " ";
			}retval = retval + '\n';
		}return retval;
	}
	
	/**
	 * scales the matrix by a
	 * @param a
	 */
	public void scale(double a){
		for (int i=0; i < width; i++){
			for (int j=0; j< height; j++){
				contents[j][i] = contents[j][i]*a;
			}
		}
	}
	
	/**
	 * scales the matrix by a
	 * @param a
	 */
	public void scale(int a){
		for (int i=0; i < width; i++){
			for (int j=0; j< height; j++){
				contents[j][i] = contents[j][i]*a;
			}
		}
	}
	
	/**
	 * multiplies by a scalar 
	 * @param args
	 */
	public Matrix scalarMult(double a){
		Matrix retval = new Matrix(width, height);
		for (int i=0; i < width; i++){
			for (int j=0; j< height; j++){
				retval.set(i, j, contents[j][i]*a);
			}
		}return retval;
	}
	
	
	
	public static void main(String[] args){
		double[][] thing = new double[3][4];
		for (int i=1; i<4; i++){
			thing[i-1][i-1] = i;
		}
		Matrix a = rowVector(thing[0]);
		Matrix b = rowVector(thing[1]);
		Matrix c = new Matrix(new double[][] {{1, 0, 0, 0},{5, 5, 5, 5}, {0, 0, 1, 0}, {0, 0, 0, 1}});
		System.out.println(a.plus(b).scalarMult(10).toString());
		System.out.println(a.plus(b).times(c).toString());
	}
}
