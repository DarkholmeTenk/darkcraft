package io.darkcraft.mod.common;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gnu.trove.map.hash.THashMap;
import io.darkcraft.darkcore.mod.datastore.Pair;
import io.darkcraft.darkcore.mod.datastore.UVStore;

public class DaedricLetters
{
	public static Map<Character,DaedricLetters.LetterData> dataMap = new THashMap();
	public static int maxWidth;
	public static int maxHeight;

	static
	{
		refreshData();
	}

	private static void setMax()
	{
		for(LetterData ld : dataMap.values())
		{
			if(ld.x > maxWidth)
				maxWidth = ld.x;
			if(ld.y > maxHeight)
				maxHeight = ld.y;
		}
	}

	public static void refreshData()
	{
		if(dataMap.isEmpty())
		{
			LetterData ld = new LetterData(new UVStore(1,6,63,73));
			ld.addLetters(0,0, 5,0, 5,1, 4,1, 4,2, 3,2, 3,4, 2,4, 2,2, 3,2, 3,1, 1,1, 1,8, 3,8, 3,10, 2,10, 2,9, 1,9, 1,8, 0,8);
			dataMap.put('a', ld);
			ld = new LetterData(new UVStore(14,20,63,73));
			ld.addLetters(0,0, 6,0, 6,3, 5,3, 5,4, 4,4, 4,3, 5,3, 5,1, 2,1, 2,3, 3,3, 3,4, 4,4, 4,7, 3,7, 3,8, 2,8, 2,9, 1,9, 1,10, 0,10, 0,4, 1,4, 1,1, 0,1);
			ld.addLetters(1,5, 2,5, 2,6, 3,6, 3,7, 2,7, 2,8, 1,8);
			dataMap.put('b', ld);
			ld = new LetterData(new UVStore(22,28,63,74));
			ld.addLetters(0,0, 6,0, 6,11, 5,11, 5,3, 4,3, 4,2, 5,2, 5,1, 1,1, 1,2, 2,2, 2,3, 1,3, 1,11, 0,11);
			dataMap.put('c', ld);
			ld = new LetterData(new UVStore(29,35, 63,73));
			ld.addLetters(1,0, 6,0, 6,10, 5,10, 5,2, 4,2, 4,1, 2,1, 2,3, 3,3, 3,4, 2,4, 2,5, 1,5, 1,6, 2,6, 2,8, 3,8, 3,9, 4,9, 4,10, 3,10, 3,9, 2,9, 2,8, 1,8, 1,6, 0,6, 0,3, 1,3);
			dataMap.put('d', ld);
			ld = new LetterData(new UVStore(37,42, 63,73));
			ld.addLetters(0,0, 5,0, 5,7, 4,7, 4,5, 1,5, 1,9, 5,9, 5,10, 0,10, 0,2, 1,2, 1,4, 4,4, 4,1, 0,1);
			dataMap.put('e', ld);
			ld = new LetterData(new UVStore(44,51, 63,74));
			ld.addLetters(0,0, 7,0, 7,1, 5,1, 5,2, 4,2, 4,3, 7,3, 7,7, 6,7, 6,5, 2,5, 2,7, 1,7, 1,11, 0,11, 0,7, 1,7, 1,5, 2,5, 2,3, 3,3, 3,2, 4,2, 4,1, 1,1, 1,3, 0,3);
			dataMap.put('f', ld);
			ld = new LetterData(new UVStore(53,58, 63,73));
			ld.addLetters(0,0, 5,0, 5,5, 4,5, 4,7, 3,7, 3,9, 2,9, 2,10, 1,10, 1,9, 2,9, 2,7, 3,7, 3,5, 4,5, 4,2, 3,2, 3,1, 1,1, 1,2, 0,2);
			dataMap.put('g', ld);
			ld = new LetterData(new UVStore(59,65, 62,73));
			ld.addLetters(0,1, 5,1, 5,0, 6,0, 6,3, 5,3, 5,2, 3,2, 3,4, 4,4, 4,5, 5,5, 5,6, 4,6, 4,8, 3,8, 3,10, 2,10, 2,11, 0,11, 0,10, 2,10, 2,8, 3,8, 3,4, 2,4, 2,2, 0,2);
			dataMap.put('h', ld);
			ld = new LetterData(new UVStore(66,70, 63,74));
			ld.addLetters(0,0, 4,0, 4,2, 3,2, 3,6, 2,6, 2,11, 1,11, 1,6, 2,6, 2,2, 1,2, 1,3, 0,3);
			dataMap.put('i', ld);
			ld = new LetterData(new UVStore(72,78, 63,73));
			ld.addLetters(0,0, 6,0, 6,1, 5,1, 5,3, 6,3, 6,10, 3,10, 3,9, 5,9, 5,3, 4,3, 4,2, 1,2, 1,6, 0,6);
			dataMap.put('j', ld);
			ld = new LetterData(new UVStore(79,85, 63,73));
			ld.addLetters(1,0, 6,0, 6,1, 5,1, 5,3, 6,3, 6,7, 5,7, 5,8, 4,8, 4,10, 2,10, 2,9, 1,9, 1,8, 0,8, 0,2, 1,2);
			ld.addLetters(2,1, 4,1, 4,3, 5,3, 5,7, 3,7, 3,9, 2,9, 2,8, 1,8, 1,2, 2,2);
			dataMap.put('k', ld);
			ld = new LetterData(new UVStore(87,94, 63,73));
			ld.addLetters(0,0, 7,0, 7,1, 6,1, 6,3, 5,3, 5,1, 3,1, 3,2, 4,2, 4,9, 5,9, 5,10, 3,10, 3,3, 2,3, 2,1, 0,1);
			dataMap.put('l', ld);
			ld = new LetterData(new UVStore(102,108, 64,74));
			ld.addLetters(0,0, 3,0, 3,1, 2,1, 2,2, 3,2, 3,3, 4,3, 4,0, 6,0, 6,9, 5,9, 5,10, 4,10, 4,9, 5,9, 5,3, 4,3, 4,4, 3,4, 3,3, 2,3, 2,2, 1,2, 1,5, 0,5);
			dataMap.put('m', ld);
			ld = new LetterData(new UVStore(110,115, 63,73));
			ld.addLetters(0,0, 3,0, 3,2, 4,2, 4,0, 5,0, 5,4, 4,4, 4,3, 3,3, 3,2, 2,2, 2,4, 3,4, 3,7, 2,7, 2,9, 1,9, 1,10, 0,10, 0,9, 1,9, 1,7, 2,7, 2,4, 1,4, 1,1, 0,1);
			dataMap.put('n', ld);
			ld = new LetterData(new UVStore(116,122, 63,73));
			ld.addLetters(0,0, 6,0, 6,1, 5,1, 5,2, 6,2, 6,9, 5,9, 5,10, 4,10, 4,9, 5,9, 5,2, 4,2, 4,1, 3,1, 3,2, 2,2, 2,9, 3,9, 3,10, 2,10, 2,9, 1,9, 1,1, 0,1);
			ld.addLetters(3,3, 4,3, 4,5, 3,5);
			dataMap.put('o', ld);
			ld = new LetterData(new UVStore(0,7, 82,92));
			ld.addLetters(0,0, 7,0, 7,1, 6,1, 6,2, 5,2, 5,5, 4,5, 4,8, 5,8, 5,10, 4,10, 4,8, 3,8, 3,5, 1,5, 1,1, 0,1);
			ld.addLetters(2,1, 5,1, 5,2, 4,2, 4,4, 2,4);
			dataMap.put('p', ld);
			ld = new LetterData(new UVStore(8,14, 82,92));
			ld.addLetters(0,0, 6,0, 6,1, 5,1, 5,2, 4,2, 4,3, 3,3, 3,6, 5,6, 5,7, 3,7, 3,9, 4,9, 4,10, 2,10, 2,7, 0,7, 0,6, 2,6, 2,3, 1,3, 1,1, 0,1);
			ld.addLetters(2,1, 4,1, 4,2, 3,2, 3,3, 2,3);
			dataMap.put('q', ld);
			ld = new LetterData(new UVStore(15,21, 82,93));
			ld.addLetters(0,1, 2,1, 2,0, 4,0, 4,1, 5,1, 5,2, 6,2, 6,3, 3,3, 3,4, 2,4, 2,11, 1,11, 1,3, 3,3, 3,2, 4,2, 4,1, 2,1, 2,2, 0,2);
			dataMap.put('r', ld);
			ld = new LetterData(new UVStore(22,28, 82,93));
			ld.addLetters(1,0, 5,0, 5,1, 6,1, 6,2, 5,2, 5,1, 3,1, 3,2, 4,2, 4,4, 3,4, 3,5, 4,5, 4,7, 5,7, 5,9, 4,9, 4,10, 3,10, 3,11, 2,11, 2,10, 3,10, 3,9, 4,9, 4,7, 3,7, 3,5, 2,5, 2,4, 1,4, 1,3, 0,3, 0,2, 1,2, 1,3, 3,3, 3,2, 2,2, 2,1, 1,1);
			dataMap.put('s', ld);
			ld = new LetterData(new UVStore(29,34, 80,92));
			ld.addLetters(1,0, 4,0, 4,1, 5,1, 5,3, 4,3, 4,4, 3,4, 3,3, 1,3, 1,4, 2,4, 2,5, 3,5, 3,9, 2,9, 2,11, 3,11, 3,10, 4,10, 4,9, 5,9, 5,10, 4,10, 4,11, 3,11, 3,12, 2,12, 2,11, 1,11, 1,10, 0,10, 0,9, 2,9, 2,5, 1,5, 1,4, 0,4, 0,2, 3,2, 3,3, 4,3, 4,1, 1,1);
			dataMap.put('t', ld);
			ld = new LetterData(new UVStore(36,42, 82,92));
			ld.addLetters(0,0, 6,0, 6,1, 5,1, 5,3, 6,3, 6,7, 5,7, 5,9, 4,9, 4,10, 1,10, 1,9, 0,9, 0,4, 1,4, 1,8, 2,8, 2,9, 4,9, 4,7, 5,7, 5,4, 4,4, 4,3, 3,3, 3,2, 2,2, 2,1, 0,1);
			ld.addLetters(2,5, 3,5, 3,7, 2,7);
			dataMap.put('u', ld);
			ld = new LetterData(new UVStore(44,49, 82,92));
			ld.addLetters(1,0, 2,0, 2,2, 1,2, 1,9, 3,9, 3,8, 4,8, 4,3, 3,3, 3,0, 4,0, 4,3, 5,3, 5,8, 4,8, 4,10, 1,10, 1,9, 0,9, 0,1, 1,1);
			dataMap.put('v', ld);
			ld = new LetterData(new UVStore(51,57, 82,92));
			ld.addLetters(0,0, 6,0, 6,2, 5,2, 5,1, 4,1, 4,3, 5,3, 5,6, 4,6, 4,9, 3,9, 3,10, 0,10, 0,9, 2,9, 2,7, 1,7, 1,6, 0,6, 0,2, 1,2, 1,1, 0,1);
			ld.addLetters(2,1, 3,1, 3,3, 4,3, 4,6, 3,6, 3,7, 2,7, 2,6, 1,6, 1,2, 2,2);
			dataMap.put('w', ld);
			ld = new LetterData(new UVStore(58,65, 80,92));
			ld.addLetters(0,3, 1,3, 1,2, 2,2, 2,1, 3,1, 3,0, 4,0, 4,1, 6,1, 6,3, 7,3, 7,9, 6,9, 6,11, 5,11, 5,12, 0,12, 0,10, 1,10, 1,9, 0,9);
			ld.addLetters(3,1, 4,1, 4,2, 5,2, 5,3, 6,3, 6,8, 5,8, 5,10, 2,10, 2,9, 1,9, 1,3, 2,3, 2,2, 3,2);
			dataMap.put('x', ld);
			ld = new LetterData(new UVStore(66,72, 82,92));
			ld.addLetters(1,0, 2,0, 2,1, 3,1, 3,2, 4,2, 4,0, 6,0, 6,1, 5,1, 5,2, 4,2, 4,5, 3,5, 3,9, 5,9, 5,10, 1,10, 1,9, 0,9, 0,6, 1,6, 1,8, 2,8, 2,5, 3,5, 3,2, 1,2);
			dataMap.put('y', ld);
			ld = new LetterData(new UVStore(73,77, 82,92));
			ld.addLetters(0,0, 4,0, 4,1, 3,1, 3,2, 2,2, 2,3, 3,3, 3,5, 2,5, 2,10, 1,10, 1,5, 2,5, 2,4, 0,4, 0,3, 1,3, 1,2, 2,2, 2,1, 0,1);
			dataMap.put('z', ld);

			//UPPER
			ld = new LetterData(new UVStore(0,7, 3,16));
			ld.addLettersNew(0,0, 7,1, -1,1, -1,3, -1,-3, 1,-1, -2,1, -1,7, 1,2, 1,-2, 1,2, -1,2, -1,-2, -1,-2, -1,-2, -1,-3, 1,-3, -1);
			dataMap.put('A', ld);
			ld = new LetterData(new UVStore(8,16, 3,16));
			ld.addLettersNew(0,0, 8,4, -1,1, -1,-1, 1,-3, -4,2, 1,2, 1,5, -1,1, -1,1, -2,1, -1,-8, 1,5, 2,-4, -1,-4, -1,-1, -1);
			dataMap.put('B', ld);
			ld = new LetterData(new UVStore(18,26, 3,16));
			ld.addLettersNew(0,0, 8,13, -1,-10, -1,-1, 1,-1, -6,1, 1,1, -1,10, -1);
			dataMap.put('C', ld);
			ld = new LetterData(new UVStore(28,35, 3,16));
			ld.addLettersNew(1,0, 6,13, -1,-10, -1,-1, -1,-1, -2,2, -1,1, 1,-1, 1,1, -1,2, -1,2, 1,1, 1,3, 1,1, -1,-1, -1,-1, -1,-2, -1,-7, 1);
			dataMap.put('D', ld);
			ld = new LetterData(new UVStore(37,44, 3,16));
			ld.addLettersNew(0,0, 7,11, -1,-3, -5,3, 1,1, 5,1, -7,-9, 1,2, 5,-4, -1,-1, -5);
			dataMap.put('E', ld);
			ld = new LetterData(new UVStore(46,55, 3,16));
			ld.addLettersNew(0,0, 9,1, -2,1, -1,1, -1,1, 4,5, -1,-2, -1,-1, -4,2, -1,2, -1,3, -1,-3, 1,-2, 1,-3, 1,-2, 1,-1, 1,-1, -4,3, -1);
			dataMap.put('F', ld);
			ld = new LetterData(new UVStore(57,64, 3,16));
			ld.addLettersNew(0,0, 7,1, -1,2, 1,4, -1,2, -1,2, -1,2, -1,1, -1,1, -2,-1, 2,-1, 1,-2, 1,-2, 1,-7, -4,2, -1);
			dataMap.put('G', ld);
			ld = new LetterData(new UVStore(65,73, 3,16));
			ld.addLettersNew(0,0, 8,2, -1,1, -1,-1, 1,-1, -2,3, 1,1, 1,1, -1,2, -1,2, -1,2, -1,1, -3,-1, 3,-2, 1,-3, 1,-2, -1,-3, -1,-1, -3);
			dataMap.put('H', ld);
			ld = new LetterData(new UVStore(74,80, 3,16));
			ld.addLettersNew(0,0, 6,1, -1,1, -1,5, -1, 6, -1,-6, 1,-5, -2,2, -1);
			dataMap.put('I', ld);
			ld = new LetterData(new UVStore(80,88, 3,16));
			ld.addLettersNew(1,0, 7,1, -1,2, 1,10, -3,-1, 2,-8, -1,-2, -4,1, -1,4, -1,-6, 1);
			dataMap.put('J', ld);
			ld = new LetterData(new UVStore(90,98, 3,16));
			ld.addLettersNew(1,0, 7,1, -1,2, 1,6, -1,1, -1,1, -1,-1, 1,-1, 1,-6, -1,-1, -1,-1, -3,1, 1,1, -2,7, 1,1, 1,-2, 1,4, -2,-1, -1,-2, -1,-8, 1);
			dataMap.put('K', ld);
			ld = new LetterData(new UVStore(100,108, 3,16));
			ld.addLettersNew(0,0, 8,2, -1,2, -1,-3, -3,1, 1,1, 1,9, 2,1, -3,-9, -1,-2, -1,-1, -2);
			dataMap.put('L', ld);
			ld = new LetterData(new UVStore(0,9, 22,35));
			ld.addLettersNew(0,0, 4,1, -1,3, 2,-1, 1,-2, -1,-1, 4,9, -1,3, -1,1, -2,-1, 2,-9, -1,1, -1,1, -2,-1, -1,-1, -1,5, -1);
			dataMap.put('M', ld);
			ld = new LetterData(new UVStore(11,18, 22,35));
			ld.addLettersNew(0,0, 5,1, -2,2, 1,2, 1,-1, 1,-2, 1,2, -1,2, -1,1, -1,-2, -1,5, -1,2, -1,1, -1,-1, 1,-2, 1,-6, -1,-3, -1);
			dataMap.put('N', ld);
			ld = new LetterData(new UVStore(18,27, 22,35));
			ld.addLettersNew(0,0, 9,1, -1,2, 1,8, -1,1, -1,1, -1,-1, 1,-1, 1,-7, -1,-2, -1,-1, -2,1, -1,2, -1,6, 1,2, 1,1, -1,-1, -1,-2, -1,-8, 1,-1, -2);
			ld.addLettersNew(4,4, 1,1, 1,1, -1,1, -1,-1, -1,-1, 1);
			dataMap.put('O', ld);
			ld = new LetterData(new UVStore(29,38, 22,35));
			ld.addLettersNew(0,0, 9,1, -1,2, -1,3, -1,7, -1,-7, -1,1, -1,-2, -1,-3, -1,-1, -1);
			dataMap.put('P', ld);
			ld = new LetterData(new UVStore(46,54, 22,35));
			ld.addLettersNew(0,0, 8,1, -1,1, -1,2, -1,3, 3,3, -1,-1, -2,3, 1,1, -4,-1, 1,-3, -2,1, -1,-3, 3,-3, -1,-2, -1,-1, -1);
			dataMap.put('Q', ld);
			ld = new LetterData(new UVStore(54,63, 22,35));
			ld.addLettersNew(3,0, 4,1, 1,1, 1,1, -1,1, -3,1, -1,8, 1,1, -2,-5, -1,-4, 1,-1, 2,-1, 1,-1, -4,1, -2,-1, 2,-1, 1);
			dataMap.put('R', ld);
			ld = new LetterData(new UVStore(64,71, 21,36));
			ld.addLettersNew(2,0, 5,3, -1,-2, -2,2, 1,2, 1,2, -1,-1, -2,-1, -1,1, 1,1, 1,1, 1,2, 1,4, -1,1, -1,-1, 1,-4, -1,-2, -1,-1, -1,-1, -1,-1, -1,-1, 3,1, 1,-2, -1,-2, -1);
			dataMap.put('S', ld);
			ld = new LetterData(new UVStore(72,80, 19,36));
			ld.addLettersNew(1,0, 5,1, 1,1, 1,1, -1,1, -1,1, -2,9, 2,-2, 2,1, -1,1, -1,1, -1,1, -2,-1, -1,-1, -1,-1, -1,-2, 1,2, 1,1, 1,-6, -1,-2, -1,-2, -1,-1, 6,-1, -1,-1, -4);
			dataMap.put('T', ld);
			ld = new LetterData(new UVStore(81,88, 21,35));
			ld.addLettersNew(0,0, 7,1, -2,1, 1,2, 1,7, -1,2, -1,1, -3,-1, -1,-1, -1,-7, 1,1, 1,1, -1,4, 1,1, 1,1, 2,-2, 1,-6, -1,-1, -1,-1, -1,-1, -1,-1, -2);
			ld.addLettersNew(3,8, 2,1, -2);
			dataMap.put('U', ld);
			ld = new LetterData(new UVStore(91,98, 22,35));
			ld.addLettersNew(0,0, 6,1, -1,2, 1,2, 1,4, -1,2, -1,1, -1,1, -3,-2, -1,-8, 1,-2, -1);
			dataMap.put('V', ld);
			ld = new LetterData(new UVStore(100,109, 21,35));
			ld.addLettersNew(0,1, 8,-1, 1,4, -1,-2, -2,4, 1,4, -1,2, -1,2, -1,1, -2,-1, 1,-1, 1,-1, -1,-1, -1,-1, -1,-1, -1,-3, 1,-4, -1);
			dataMap.put('W', ld);
			ld = new LetterData(new UVStore(109,118, 20,36));
			ld.addLettersNew(5,0, 1,1, 1,1, 1,2, 1,7, -1,2, -1,2, -6,1, -1,-2, 1,-1, 1,-2, -1,-7, 1,-2, 1,-1, 1,1, -1,2, -1,7, 1,2, 3,-1, 1,-1, 1,-7, -1,-2, -1,-1, -1);
			dataMap.put('X', ld);
			ld = new LetterData(new UVStore(0,7, 41,54));
			ld.addLettersNew(1,0, 3,2, 1,-1, 1,-1, 1,2, -1,2, -1,4, -1,4, 3,1, -5,-1, -2,-3, 1,-1, 1,1, -1,1, 1,1, 1,-8, -1,-2, -1);
			dataMap.put('Y', ld);
			ld = new LetterData(new UVStore(8,13, 41,54));
			ld.addLettersNew(0,0, 4,1, -1,1, 2,1, -1,2, -1,8, -1,-7, -1,-3, -1,-1, 2,-1, -2);
			dataMap.put('Z', ld);

			//NUMBERS
			ld = new LetterData(new UVStore(16,20, 46,50));
			ld.addLettersNew(1,0, 2,1, 1,2, -1,1, -2,-1, -1,-2, 1);
			ld.addLettersNew(1,1, 2,2, -2);
			dataMap.put('0', ld);
			ld = new LetterData(new UVStore(23,25, 47,49));
			ld.addLettersNew(0,0, 2,2, -2);
			dataMap.put('1', ld);
			ld = new LetterData(new UVStore(27,33, 47,50));
			ld.addLettersNew(0,0, 2,3, -2);
			ld.addLettersNew(4,0, 2,3, -2);
			dataMap.put('2', ld);
			ld = new LetterData(new UVStore(35,41, 47,53));
			ld.addLettersNew(2,0, 2,2, -2);
			ld.addLettersNew(0,3, 2,3, -2);
			ld.addLettersNew(4,3, 2,3, -2);
			dataMap.put('3', ld);
			ld = new LetterData(new UVStore(43,50, 45,54));
			ld.addLettersNew(2,0, 3,2,-3);
			ld.addLettersNew(0,3, 2,3,-2);
			ld.addLettersNew(5,3, 2,3,-2);
			ld.addLettersNew(2,7, 3,2,-3);
			dataMap.put('4', ld);
			ld = new LetterData(new UVStore(51,58, 45, 54));
			ld.addLettersNew(0,3, 2,3,-2);
			ld.addLettersNew(5,3, 2,3,-2);
			ld.addLettersNew(2,0, 3,2, -1,5, 1,2, -3,-2, 1,-5, -1);
			dataMap.put('5', ld);
			ld = new LetterData(new UVStore(60,67, 45,54));
			ld.addLettersNew(2,0, 3,2, -1,2, 1,-1, 2,3, -2,-1, -1,2, 1,2, -3,-2, 1,-2, -1,1, -2,-3, 2,1, 1,-2, -1);
			dataMap.put('6', ld);
			ld = new LetterData(new UVStore(69,76, 45,54));
			ld.addLettersNew(0,0, 5,2, -3,-1, -1,2, 1,1, 3,-1, 2,6, -5,-2, 3,1, 1,-2, -1,-1, -3,1, -2);
			dataMap.put('7', ld);
			ld = new LetterData(new UVStore(78,85, 45,54));
			ld.addLettersNew(0,0, 5,2, -1,5, 1,1, 1,-2, -1,-3, 2,6, -5,-2, 1,-5, -1,-1, -1,2, 1,3, -2);
			dataMap.put('8', ld);
			ld = new LetterData(new UVStore(87,94, 45,54));
			ld.addLettersNew(0,0, 5,2, -1,2, 1,-1, 2,6, -5,-2, 1,-2, -1,1, -2);
			ld.addLettersNew(1,1, 1,1, 1,2, -1,-1, -1);
			ld.addLettersNew(4,5, 1,1, 1,2, -1,-1, -1);
			dataMap.put('9', ld);

			//SYMBOLS
			ld = new LetterData(new UVStore(124,127, 127,127));
			dataMap.put(' ', ld);
			ld = new LetterData(new UVStore(80,83, 86,89));
			ld.addLetters(1,0, 2,0, 2,1, 3,1, 3,2, 2,2, 2,3, 1,3, 1,2, 0,2, 0,1, 1,1);
			dataMap.put('.', ld);
			ld = new LetterData(new UVStore(86,87, 84,91));
			ld.addLetters(0,0, 1,0, 1,7, 0,7);
			dataMap.put('|', ld);
			ld = new LetterData(new UVStore(89,92, 83,91));
			ld.addLettersNew(1,0, 1,1, 1,1, -1,1, -1,-1, -1,-1, 1);
			ld.addLettersNew(1,5, 1,1, 1,1, -1,1, -1,-1, -1,-1, 1);
			dataMap.put(':', ld);
			ld = new LetterData(new UVStore(94,97, 81,85));
			ld.addLettersNew(1,0, 1,1, 1,1, -1,1, -1,1, -1,-1, 1,-1, -1);
			dataMap.put(',', ld);
			ld = new LetterData(new UVStore(94,97, 81,92));
			ld.addLettersNew(1,0, 1,1, 1,1, -1,1, -1,1, -1,-1, 1,-1, -1);
			dataMap.put('\'', ld);
			ld = new LetterData(new UVStore(96,99,49,50));
			ld.addLettersNew(0,0, 3,1,-3);
			dataMap.put('-', ld);

			setMax();
		}
	}

	public static boolean validChar(char c)
	{
		if(dataMap.isEmpty())
			refreshData();
		return dataMap.keySet().contains(c);
	}

	public static class LetterData
	{
		public final UVStore uv;
		public final int x;
		public final int y;
		public Set<Pair<Integer,Integer>[]> data = new HashSet();

		public LetterData(UVStore _uv)
		{
			uv = _uv.div(128);
			x = (int) Math.round(_uv.U - _uv.u);
			y = (int) Math.round(_uv.V - _uv.v);
		}

		public void addLetters(Integer... incoming)
		{
			if((incoming.length % 2) != 0)
			{
				System.err.println("Invalid data");
				return;
			}
			int s = incoming.length / 2;
			Pair<Integer,Integer>[] pairData = new Pair[s];
			for(int i = 0; i < s; i++)
				pairData[i] = new Pair(incoming[2*i],y-incoming[(2*i)+1]);
			data.add(pairData);
		}

		public void addLettersNew(Integer... incoming)
		{
			boolean oldX = false;
			if((incoming.length) < 2)
			{
				System.err.println("Invalid data");
				return;
			}
			int s = incoming.length - 1;
			Pair<Integer,Integer>[] pairData = new Pair[s];
			pairData[0] = new Pair(incoming[0],y-incoming[1]);
			for(int i = 1; i < s; i++)
			{
				int next = incoming[i+1];
				Pair<Integer,Integer> prev = pairData[i-1];
				pairData[i] = new Pair(oldX ? prev.a : prev.a+next, oldX ? prev.b-next : prev.b);
				oldX = !oldX;
			}
			data.add(pairData);
		}
	}

}
