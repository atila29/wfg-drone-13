package dtu.grp13.drone.vector;

public class Matrix2 {

	private int m;
	private int n;
	
	private double[][] matrix;
	
	public Matrix2(int rows, int colms, double ... k) {
		m = rows;
		n = colms;
		matrix = new double[m][n];
		int i = 0;
		for(int row = 0; row < rows; row++){
			for(int colm = 0; colm < colms; colm++){
				matrix[row][colm] = k[i];
				i++;
			}
		}
	}
	
	public Matrix2(double[][] data) {
		matrix = data;
	}
	
	
	
	public Matrix2 plus(Matrix2 B) {
		Matrix2 A = this;
		if(A.matrix.length != B.matrix.length || A.matrix[0].length != B.matrix[0].length) throw new RuntimeException("Illegal matrix dimensions.");
		double[][] temp = new double[A.matrix.length][A.matrix[0].length];
		for (int i = 0; i < A.matrix.length; i++)
            for (int j = 0; j < A.matrix[0].length; j++)
                temp[i][j] = A.matrix[i][j] + B.matrix[i][j];
		return new Matrix2(temp);
	}
	
	public Matrix2 minus(Matrix2 B) {
		Matrix2 A = this;
		if(A.matrix.length != B.matrix.length || A.matrix[0].length != B.matrix[0].length) throw new RuntimeException("Illegal matrix dimensions.");
		double[][] temp = new double[A.matrix.length][A.matrix[0].length];
		for (int i = 0; i < A.matrix.length; i++)
            for (int j = 0; j < A.matrix[0].length; j++)
                temp[i][j] = A.matrix[i][j] - B.matrix[i][j];
		return new Matrix2(temp);
	}
	
	public Matrix2 dot(Matrix2 B) {
		Matrix2 A = this;
		if(A.getN() != B.getM()) throw new RuntimeException("Illegal matrix dimensions.");
		double[][] temp = new double[A.getM()][B.getN()];
		for(int i = 0; i < temp.length; i++)
			for(int j = 0; j < temp[0].length; j++)
				for(int k = 0; k < A.getN(); k++)
					temp[i][j] += (A.matrix[i][k] * B.matrix[k][j]);
		return new Matrix2(temp);
		
	}
	
	public void show() {
		for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) 
                System.out.printf("%9.4f ", matrix[i][j]);
            System.out.println();
        }
	}
	
	// burde flyttes til mere specifik klasse.
	public Vector2 mul(Vector2 v) {
		return new Vector2(this.getFromIndex(1, 1)*v.getX() + this.getFromIndex(1, 2)*v.getY(),
							this.getFromIndex(2, 1) *v.getX() + this.getFromIndex(2, 2)*v.getY());
	}
	
	public double getFromIndex(int m, int n){
		return matrix[--m][--n];
	}

	public int getM() {
		return m;
	}

	public int getN() {
		return n;
	}
}
