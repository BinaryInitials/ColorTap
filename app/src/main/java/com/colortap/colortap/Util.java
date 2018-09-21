package com.colortap.colortap;

public class Util {
	public static final int FRAME_RATE = 100;
	public static final int ALPHA_VISIBLE = 155;
	public static final double PI = 3.1415926535;
	public static final int ALPHA_PARTIALLY_VISIBLE = 200;
	public static final double RATE_GROWTH = 0.0025;
	public static final double MAX_COLOR_TIME = 12;
	public static final int MAX_LIFE= 100;
	public static final int MAX_SPEED = 5;
	public static final String[] PROPS1 = {
		"NICE!",
		"PERFECT!",
		"EXCELLENT!",
		"OUTSTANDING!",
		"TERRIFIC!",
		"WONDERFUL!",
		"DAZZLING!",
		"MARVELOUS!",
		"GREAT!",
		"REMARKABLE!",
		"EXCEPTIONAL!",
		"INCREDIBLE!",
		"GOOD!",
		"IMPRESSIVE!",
		"WOW-ZA!",
		"BEAUTIFUL!",
		"SUPERB!",
		"IMPECCABLE!"
	};
	public static final String[] PROPS2 = {
		"ALMOST ",
		"SO CLOSE!!",
		"NEAR PERFECTION!",
	};
	public static final String[] PROPS3 = {
		"Acceptably",
		"Decent",
		"Tolerable",
		"Okay",
		"Adequate",
		"Needs improvement",
		"Do better next time"
		
	};
	public static final String[] PROPS4 = {
		"SIMPLY AWEFULL!",
		"TERRIBLE!",
		"SURPRSINGLY BAD!",
		"EMBARASSING!!",
		"HORRIBLE!",
		"ATROCIOUS!",
		"DREADFUL!",
		"UPSETTING!",
		"HORRENDOUS!!!",
		"APPALLING!!",
		"DISGUSTING!",
		"UNBEARABLE!!!",
		"HORRID!"
	};
	public static final int[] BALLZ = {
		2,
		3,
		5,
		7,
		11,
		13,
		17,
		19,
		23,
		29,
		31,
		37,
		41,
		43,
		47,
		53,
		59,
		61,
		67,
		71,
		73,
		79,
		83,
		89,
		97,
		101,
		103,
		107,
		109,
		113,
		127,
		131,
		137,
		139,
		149,
		151,
		157,
		163,
		167,
		173,
		179,
		181,
		191,
		193,
		197,
		199,
	};
	protected static final String DIFFICULTY_KEY = "colortap_difficulty_key";

	public static double[] getCoefColors(double colorTime, double redShift, double greenShift, double blueShift){
		double[] coefColors = new double[3];
		coefColors[0] = 128*(1.0+Math.cos((colorTime+redShift)/MAX_COLOR_TIME * 2*PI));
		coefColors[1] = 128*(1.0+Math.cos((colorTime+greenShift)/MAX_COLOR_TIME * 2*PI));
		coefColors[2] = 128*(1.0+Math.cos((colorTime+blueShift)/MAX_COLOR_TIME * 2*PI));
		return coefColors;
	}
}
