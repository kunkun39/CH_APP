package com.changhong.gdappstore.view;

import com.changhong.gdappstore.util.L;

public class ListViewPosition {
	int countNum;
	int fullShowNum;
	int startNumPosition;
	int endNumPosition;
	int layoutHeight;
	int itemHeight;
	int currentPosition;
	int remainderHeight;	//高度的余数
	int lastOffset;
	public ListViewPosition(int countNum,int layoutHeight,int itemHeight) {
		// TODO Auto-generated constructor stub
		this.countNum = countNum;
		this.layoutHeight = layoutHeight;
		this.itemHeight = itemHeight;
		
		fullShowNum = layoutHeight / itemHeight;
		startNumPosition = 0;
		endNumPosition = fullShowNum - 1;
		remainderHeight = layoutHeight % itemHeight;
		
		currentPosition = 0;
		lastOffset = 0;
		
		L.i("countNum :" + this.countNum);
		L.i("layoutHeight :" + this.layoutHeight);
		L.i("itemHeight :" + this.itemHeight);
		L.i("fullShowNum :" + this.fullShowNum);
		L.i("startNumPosition :" + this.startNumPosition);
		L.i("endNumPosition :" + this.endNumPosition);
		L.i("remainderHeight :" + this.remainderHeight);
	}
	
	public int caculateOffset(int toPosition,boolean keyUpDown) {
		int offset = 0;
		
		if(toPosition < 0) {
			startNumPosition = 0;
			endNumPosition = fullShowNum - 1;
			currentPosition = 0;
			lastOffset = 0;
			return 0;
		}
		else if (toPosition > countNum - 1) {
			endNumPosition = countNum - 1;
			startNumPosition = endNumPosition - fullShowNum + 1;
			currentPosition = countNum - 1;
			lastOffset = offset = (fullShowNum - 1) * itemHeight + remainderHeight;
			return offset;
		}
		
		if(toPosition > currentPosition) {
			// 向下移动
			if(toPosition == countNum - 1) {
				//移动到最后一格
				offset = (fullShowNum - 1) * itemHeight + remainderHeight;
				endNumPosition = countNum - 1;
				startNumPosition = endNumPosition - fullShowNum + 1;
			}
			else if(toPosition > endNumPosition) {
				//需要向下切屏
				offset = (fullShowNum - 1) * itemHeight + remainderHeight / 2;
				startNumPosition++ ;
				endNumPosition++;
			}
			else {
				//不需要切屏
				if(0 == startNumPosition) {
					//上面位置不存在半屏
					offset = (toPosition - startNumPosition) * itemHeight;
				}
				else if(endNumPosition == countNum - 1) {
					//上面位置存在两个半屏
					offset = (toPosition - startNumPosition) * itemHeight + remainderHeight;
				}
				else {
					//上面位置存在一个半屏
					offset = (toPosition - startNumPosition) * itemHeight + remainderHeight / 2;
				}
			}
		}
		else if(toPosition < currentPosition) {
			//向上移动
			if (0 == toPosition) {
				//移动到第一格
				offset = 0;
				startNumPosition = 0;
				endNumPosition = fullShowNum - 1;
			}
			else if(toPosition < startNumPosition) {
				//需要向上切屏
				offset = remainderHeight / 2;
				startNumPosition-- ;
				endNumPosition--;
			}
			else {
				//不需要切屏
				if(0 == startNumPosition) {
					//上面位置不存在半屏
					offset = (toPosition - startNumPosition) * itemHeight;
				}
				else if(endNumPosition == countNum - 1) {
					//上面位置存在两个半屏
					offset = (toPosition - startNumPosition) * itemHeight + remainderHeight;
				}
				else {
					//上面位置存在一个半屏
					offset = (toPosition - startNumPosition) * itemHeight + remainderHeight / 2;;
				}
			}
		}
		else {
			//位置不变
			if(0 == toPosition) {
				offset = 0;
			}
			else {
				if(keyUpDown) {
					offset = -1;
				}
				else {
					offset = lastOffset;
				}
			}
		}
		
		if(-1 != offset) {
			lastOffset = offset;
			offset += (offset % itemHeight) > 0 ? (offset / itemHeight) * 2 : ((offset / itemHeight) + 1) * 2;
		}
		
		currentPosition = toPosition;
		L.i("Offset = " + offset + "toPosition = " + toPosition);
		return offset;
	}
	
	public int getRelativePosition() {
		return currentPosition - startNumPosition;
	}
	
	public int caculateAbsolutePosition(int relativePosition) {
		return startNumPosition + relativePosition;
	}
}
