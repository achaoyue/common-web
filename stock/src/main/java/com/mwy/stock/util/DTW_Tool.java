package com.mwy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

public class DTW_Tool {
	private double[][] distance;
	private double[] X;
	private double[] Y;
	private double[][] dtw;
	public DTW_Tool(double[] X,double[] Y){
		this.X = new double[X.length];
		for(int i=0;i<X.length;i++){
			this.X[i]=X[i];
		}
		this.Y = new double[Y.length];
		for(int i=0;i<Y.length;i++){
			this.Y[i]=Y[i];
		}
		
	}

	private double computeDistance(double x,double y){
		return Math.sqrt((x-y)*(x-y));
		
	}
	private void initDistance(){//先初始化点与点之间的距离
		distance = new double[X.length][Y.length];
		for(int i=0;i<X.length;i++){
			for(int j =0;j<Y.length;j++){
				distance[i][j] = computeDistance(X[i],Y[j]);
				
			}
		}
			
		
	}
	private void computeDtw(){//初始化dtw数组
		dtw = new double[X.length][Y.length];
		initDistance();
		//根据distance数组来初始化dtw数组
		dtw[0][0]=0;
		for(int i=0;i<X.length;i++){
			for(int j=0;j<Y.length;j++){
				//这里要对i,j进行判定，其实就是加入边界值的考虑
				if(i>00&&j>0){
					dtw[i][j]=minDist(dtw[i][j-1]+distance[i][j],dtw[i-1][j]+distance[i][j],dtw[i-1][j-1]+2*distance[i][j]);
				}
				else if(i==0&&j>0){
					dtw[i][j] = dtw[i][j-1]+distance[i][j];
				}
				else if(i>0&&j==0){
					dtw[i][j]= dtw[i-1][j]+distance[i][j];
				}else{
					dtw[i][j]=0;
				}
				
			}
		}
		
	}
	public double getDtwDist(){
		computeDtw();
		return dtw[X.length-1][Y.length-1];
		
	}
	private double minDist(double dist1,double dist2,double dist3){
		return (dist1<dist2?(dist2<dist3?dist3:(dist1>dist3?dist3:dist1)):(dist2>dist3?dist3:dist2));
		
	}
	
}