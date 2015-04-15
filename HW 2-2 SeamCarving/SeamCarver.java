import java.awt.Color;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * 04/14/2015
 * @author Fiona Lu
 *
 */

// TODO By convention, the indices x and y are integers between 0 and W − 1 and between 0 and H − 1 respectively, 
// where W is the width of the current image and H is the height. Throw a java.lang.IndexOutOfBoundsException if 
// either x or y is outside its prescribed range.



public class SeamCarver {

	private Picture picture;
	private ArrayList<ArrayList<Color>> colors;
	private ArrayList<ArrayList<Double>> energys;

	/**
	 * create a seam carver object based on the given picture
	 * @param picture
	 */
	public SeamCarver(Picture picture) {
		this.picture = picture;

		colors = new ArrayList<ArrayList<Color>>();		
		for (int i = 0; i < picture.width(); i++) {
			colors.add(new ArrayList<Color>());
			for (int j = 0; j < picture.height(); j++) {
				colors.get(i).add(picture.get(i, j));
			}
		}

		energys = new ArrayList<ArrayList<Double>>();
		for (int i = 0; i < picture.width(); i++) {
			energys.add(new ArrayList<Double>());
			for (int j = 0; j < picture.height(); j++) {
				energys.get(i).add(computeEnergy(i, j));
			}
		}
	}

	/**
	 * For unit test
	 */
	public Color get(int i, int j) {
		return colors.get(i).get(j);
	}

	/**
	 * current picture
	 */
	public Picture picture() {
		if (picture.width() != this.width() || picture.height() != this.height())
			updatePicture();
		return picture;
	}

	private void updatePicture() {
		this.picture = new Picture(this.width(), this.height());
		for (int i = 0; i < picture.width(); i++) {
			for (int j = 0; j < picture.height(); j++) {
				picture.set(i, j, colors.get(i).get(j));
			}
		}
	}

	/**
	 * width of current picture
	 */
	public int width() {
		return colors.size();
	}

	/**
	 * height of current picture
	 */
	public int height() {
		if (colors.size() == 0)
			return 0;
		else return colors.get(0).size();
	}

	/**
	 * energy of pixel at column x and row y
	 */
	public double energy(int x, int y) {	
		return energys.get(x).get(y);
	}

	private double computeEnergy(int x, int y) {
		if (x == this.width() - 1 || x == 0
				|| y == this.height() - 1 || y == 0) {
			return 195075;
		}

		Color xMinusOne = colors.get(x - 1).get(y);
		Color xPlusOne = colors.get(x + 1).get(y);
		Color yMinusOne = colors.get(x).get(y - 1);
		Color yPlusOne = colors.get(x).get(y + 1);

		return Math.pow(xMinusOne.getRed() - xPlusOne.getRed(), 2) 
				+ Math.pow(xMinusOne.getGreen() - xPlusOne.getGreen(), 2)
				+ Math.pow(xMinusOne.getBlue() - xPlusOne.getBlue(), 2)
				+ Math.pow(yMinusOne.getRed() - yPlusOne.getRed(), 2) 
				+ Math.pow(yMinusOne.getGreen() - yPlusOne.getGreen(), 2)
				+ Math.pow(yMinusOne.getBlue() - yPlusOne.getBlue(), 2);
	}

	/**
	 * sequence of indices for horizontal seam
	 */
	public int[] findHorizontalSeam() {
		if (this.width() <= 1 || this.height() <= 1) throw new IllegalArgumentException();

		double[][] energysTransposed = new double[height()][width()];
		for (int i = 0; i < width(); i++) {
			for (int j = 0; j < height(); j++) {
				energysTransposed[j][i] = energys.get(i).get(j);
			}
		}
//
//		System.out.println("Energy transposed array");
//		for (int i = 0; i < height(); i++) {
//			for (int j = 0; j < width(); j++) {
//				System.out.printf("%c%6.0f%c ", ' ', energysTransposed[i][j], ' ');
//			}
//			System.out.println();
//		}
		
		return findVerticalSeam(energysTransposed, height(), width());
	}

	/**
	 * sequence of indices for vertical seam
	 */
	public int[] findVerticalSeam() {
		if (width() <= 1 || height() <= 1) throw new IllegalArgumentException();

		double[][] energyArray = new double[width()][height()];
		for (int i = 0; i < width(); i++) {
			for (int j = 0; j < height(); j++) {
				energyArray[i][j] = energys.get(i).get(j);
			}
		}

		return findVerticalSeam(energyArray, width(), height());
	}

	private int[] findVerticalSeam(double[][] energys, int wid, int hei) {
		int[] seam = new int[hei];
		int[][] path = new int[wid][hei];
		double[][] pathWei = new double[wid][hei];

		for (int i = 0; i < wid; i++) {
			path[i][0] = i;
			pathWei[i][0] = energys[i][0];
		}

		for (int j = 1; j < hei; j++) {
			for (int i = 0; i < wid; i++) {
				if (i == 0) {
					if (pathWei[i][j-1] <= pathWei[i+1][j-1])
						path[i][j] = i;
					else
						path[i][j] = i+1;
				} else if (i == wid - 1) {
					if (pathWei[i-1][j-1] <= pathWei[i][j-1])
						path[i][j] = i-1;
					else
						path[i][j] = i;
				} else {
					if (pathWei[i-1][j-1] <= pathWei[i][j-1]
							&& pathWei[i-1][j-1] <= pathWei[i+1][j-1])
						path[i][j] = i-1;
					else if (pathWei[i][j-1] < pathWei[i-1][j-1]
							&& pathWei[i][j-1] <= pathWei[i+1][j-1])
						path[i][j] = i;
					else if (pathWei[i+1][j-1] < pathWei[i-1][j-1]
							&& pathWei[i+1][j-1] < pathWei[i][j-1])
						path[i][j] = i+1;
				}

				pathWei[i][j] = energys[i][j] + pathWei[path[i][j]][j-1];
			}
		}

		// Find the index of minimum weighted path at the bottom
		int minIdx = -1;
		double minWei = Double.MAX_VALUE;
		for (int i = 0; i < wid; i++) {
			if (pathWei[i][hei-1] < minWei) {
				minIdx = i;
				minWei = pathWei[i][hei-1];
			}
		}

		int k = hei - 1;
		seam[k] = minIdx;
		while (k > 0) {
			seam[k-1] = path[seam[k]][k];
			k--;
		}

//		System.out.println("Local energy array");
//		for (int j = 0; j < hei; j++) {
//			for (int i = 0; i < wid; i++) {
//				System.out.printf("%c%6.0f%c ", ' ', energys[i][j], ' ');
//			}
//			System.out.println();
//		}
//
//		System.out.println("Path Weight");
//		for (int j = 0; j < hei; j++) {
//			for (int i = 0; i < wid; i++) {
//				System.out.printf("%c%6.0f%c ", ' ', pathWei[i][j], ' ');
//			}
//			System.out.println();
//		}
//
//		System.out.println("PathIdx");
//		for (int j = 0; j < hei; j++) {
//			for (int i = 0; i < wid; i++) {
//				System.out.print(path[i][j] + " ");
//			}
//			System.out.println();
//		}
//
//		System.out.println("Seam");
//		for (int i:seam) {
//			System.out.print(i + " ");
//		}
//		System.out.println();

		return seam;
	}

	//	TODO Throw a java.lang.IllegalArgumentException if removeVerticalSeam() or 
	//	removeHorizontalSeam() is called with an array of the wrong length or if the array 
	//	is not a valid seam (i.e., either an entry is outside its prescribed range or two 
	//	adjacent entries differ by more than 1).

	/**
	 * remove horizontal seam from current picture
	 */
	public void removeHorizontalSeam(int[] seam) {
		if (seam == null) throw new NullPointerException();
		if (this.width() <= 1 || this.height() <= 1) throw new IllegalArgumentException();
		isValidSeam(seam, width());

		for (int i = 0; i < width(); i++) {
			colors.get(i).remove(seam[i]);
			energys.get(i).remove(seam[i]);
		}

		for (int i = 0; i < width(); i++) {
			if (seam[i] == 0) {
				energys.get(i).set(0, computeEnergy(i, 0));
			} else if (seam[i] == height()) {
				// New height = old height -1 already
				energys.get(i).set(height() - 1, computeEnergy(i, height() - 1));
			} else {
				energys.get(i).set(seam[i] - 1, computeEnergy(i, seam[i] - 1));
				energys.get(i).set(seam[i], computeEnergy(i, seam[i]));
			}
		}
	}

	/**
	 * remove vertical seam from current picture
	 */
	public void removeVerticalSeam(int[] seam) {
		if (seam == null) throw new NullPointerException();
		if (this.width() <= 1 || this.height() <= 1) throw new IllegalArgumentException();
		isValidSeam(seam, height());

		for (int j = 0; j < height(); j++) {
			int i = seam[j];
			while (i < width()-1) {
				colors.get(i).set(j, colors.get(i+1).get(j));
				energys.get(i).set(j, energys.get(i+1).get(j));
				i++;
			}
		}
		colors.remove(width()-1);
		energys.remove(width()-1);

		for (int j = 0; j < height(); j++) {
			if (seam[j] == 0) {
				energys.get(0).set(j, computeEnergy(0, j));
			} else if (seam[j] == width()) {
				energys.get(width() - 1).set(j, computeEnergy(width() - 1, j));
			} else {
				energys.get(seam[j] - 1).set(j, computeEnergy(seam[j] - 1, j));
				energys.get(seam[j]).set(j, computeEnergy(seam[j], j));
			}


		}
	}

	private void isValidSeam(int[] seam, int length) {
		if (seam.length != length) throw new IllegalArgumentException();
		for (int i = 1; i < seam.length; i++) {
			if (Math.abs(seam[i] - seam[i-1]) > 2) {
				//				System.out.println("seam[" + i + "]=" + seam[i]);
				//				System.out.println("seam[" + (i-1) + "]=" + seam[i-1]);
				throw new IllegalArgumentException();
			}
		}
	}

	public static void main(String[] arg) {
		ArrayList<String> ar = new ArrayList<String>();
		ar.add("A");
		ar.add("B");
		ar.add("C");
		System.out.println(ar.get(1) + "," + ar.size());
		ar.remove(1);
		System.out.println(ar.get(1) + "," + ar.size());


	}
}