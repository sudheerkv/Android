package com.java.minesweeper;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	/*Size of the grid*/
	public static final int SIZEOFGRID = 8;
	
	/*Number of Mines, Depends on the level of game*/
	public static final int SIZEOFMINES = 10;

	/*Integer Matrix to randomly distribute mines and distribute number of mines that are covering*/
	public int[][] grid = new int[SIZEOFGRID][SIZEOFGRID];
	
	/*To check whether all tiles but not mines are pressed by the user*/
	public boolean[][] validate = new boolean[SIZEOFGRID][SIZEOFGRID];

	final Context c = this;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		distributeMines();
		distributeNonMines();

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		/*Adding buttons to the grid(8x8 in this case) and setting onclick listeners*/
		
		for (int i = 0; i < SIZEOFGRID; i++) {
			final LinearLayout row = new LinearLayout(this);
			row.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));

			for (int j = 0; j < SIZEOFGRID; j++) {
				final Button mine = new Button(this);
				mine.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				
				mine.setId(j + (i * SIZEOFGRID));
				
				mine.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int rowid = mine.getId() / SIZEOFGRID;
						int colid = mine.getId() % SIZEOFGRID;

						/*-1 is for mine. If user presses mine then game is over*/
						if (grid[rowid][colid] == -1) {
							for (int i = 0; i < SIZEOFGRID; i++) {
								for (int j = 0; j < SIZEOFGRID; j++)
									if (grid[i][j] == -1) {
										Button m = (Button) findViewById(j + i
												* SIZEOFGRID);
										m.setText(Integer
												.toString(grid[rowid][colid]));
									}
							}
							BuildAlert("Game Over");

						} 
						/*
						 * If user presses other than mine and zero, that number would be simply displayed
						 */ else if (grid[rowid][colid] > 0) {
						 
							mine.setText(Integer.toString(grid[rowid][colid]));
							validate[rowid][colid] = true;
						}/*
						*If user presses zero all the cells around it are displayed
						*/ 
						 else if (grid[rowid][colid] == 0) {
							int east = rowid, west = rowid;
							int north = colid, south = colid;
							validate[rowid][colid] = true;
							while (east < SIZEOFGRID && grid[east][colid] == 0) {
								validate[east][colid] = true;
								uncovermines(east, colid);
								east++;
							}
							while (west > -1 && grid[west][colid] == 0) {
								validate[west][colid] = true;
								uncovermines(west, colid);
								west--;
							}
							while (north > -1 && grid[rowid][north] == 0) {
								validate[rowid][north] = true;
								uncovermines(rowid, north);
								north--;
							}
							while (south < SIZEOFGRID
									&& grid[rowid][south] == 0) {
								validate[rowid][south] = true;
								uncovermines(rowid, south);
								south++;
							}
						}
					}
				});
				row.addView(mine);
			}

			layout.addView(row);
		}

		/*New Game Button is added and on clicking new game button whole activity is restarted*/
		final Button NewGame = new Button(this);
		NewGame.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		NewGame.setText("NEW GAME");
		NewGame.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				NewGame();
			}
		});
		layout.addView(NewGame);

		/*Validate Button is added and on clicking validate it checks if user won or lost the game*/
		final Button Validate = new Button(this);
		Validate.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		Validate.setText("Validate");
		Validate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean lost = false;
				for (int i = 0; i < SIZEOFGRID; i++) {
					for (int j = 0; j < SIZEOFGRID; j++) {
						if (grid[i][j] > -1 && validate[i][j] == false
								|| (grid[i][j] == -1 && validate[i][j] == true)) {
							BuildAlert("You Lost");
							lost = true;
							break;
						}
					}
				}
				if (!lost) {
					BuildAlert("You Won");
				}
			}
		});
		layout.addView(Validate);

		/*Cheat Open button shows all the mines without spoiling game*/
		final Button CheatOpen = new Button(this);
		CheatOpen.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		CheatOpen.setText("Cheat Open");
		CheatOpen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i = 0; i < SIZEOFGRID; i++) {
					for (int j = 0; j < SIZEOFGRID; j++)
						if (grid[i][j] == -1) {
							Button m = (Button) findViewById(j + i * SIZEOFGRID);
							m.setText(Integer.toString(grid[i][j]));
						}
				}
			}
		});
		layout.addView(CheatOpen);

		/*cheat close button hides all the mines without spoiling game*/
		final Button CheatClose = new Button(this);
		CheatClose.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		CheatClose.setText("Cheat Close");
		layout.addView(CheatClose);
		CheatClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i = 0; i < SIZEOFGRID; i++) {
					for (int j = 0; j < SIZEOFGRID; j++)
						if (grid[i][j] == -1) {
							Button m = (Button) findViewById(j + i * SIZEOFGRID);
							m.setText("");
						}
				}
			}
		});

		setContentView(layout);
		// setContentView(R.layout.activity_main);

	}

	/*Mines are distributed randomly*/
	public void distributeMines() {
		int max = 64;
		int index;

		Random r = new Random();

		for (int i = 0; i < SIZEOFMINES; i++) {
			index = r.nextInt(max);
			int row = index / SIZEOFGRID;
			int col = index % SIZEOFGRID;
			if (grid[row][col] == -1)
				i = i - 1;
			grid[row][col] = -1;
		}
	}

	/*Calculating number of mines around each tile*/
	public void distributeNonMines() {
		for (int i = 0; i < SIZEOFGRID; i++) {
			for (int j = 0; j < SIZEOFGRID; j++) {
				if (grid[i][j] != -1) {
					for (int k = Math.max(0, i - 1); k < Math.min(SIZEOFGRID,
							i + 2); k++) {
						for (int l = Math.max(0, j - 1); l < Math.min(
								SIZEOFGRID, j + 2); l++) {
							if (grid[k][l] == -1)
								grid[i][j] = grid[i][j] + 1;
						}
					}
				}
			}
		}
	}

	/*If zero is pressed all the mines around it are uncovered*/
	public void uncovermines(int rowid, int colid) {
		int hor = rowid;
		int ver = colid;
		int north, east, west, south;

		for (east = hor; ver < SIZEOFGRID && east < SIZEOFGRID
				&& grid[east][ver] == 0; ver++) {
			// System.out.println(east + " " + ver);
			Button m = (Button) findViewById(ver + east * SIZEOFGRID);
			m.setText(Integer.toString(grid[east][ver]));
			validate[east][ver] = true;
		}
		if (ver < SIZEOFGRID && grid[east][ver] > 0) {
			Button m = (Button) findViewById(ver + east * SIZEOFGRID);
			m.setText(Integer.toString(grid[east][ver]));
			validate[east][ver] = true;
		}
		hor = rowid;
		ver = colid;

		for (west = hor; ver > -1 && grid[west][ver] == 0; ver--) {
			// System.out.println(west + " " + ver);
			Button m = (Button) findViewById(ver + west * SIZEOFGRID);
			m.setText(Integer.toString(grid[west][ver]));
			validate[west][ver] = true;
		}
		if (ver > -1 && grid[west][ver] > 0) {
			Button m = (Button) findViewById(ver + west * SIZEOFGRID);
			m.setText(Integer.toString(grid[west][ver]));
			validate[west][ver] = true;
		}

		hor = rowid;
		ver = colid;

		for (north = ver; hor > -1 && grid[hor][north] == 0; hor--) {
			// System.out.println(hor + " " + north);
			Button m = (Button) findViewById(north + hor * SIZEOFGRID);
			m.setText(Integer.toString(grid[hor][north]));
			validate[hor][north] = true;
		}
		if (hor > -1 && grid[hor][north] > 0) {
			Button m = (Button) findViewById(north + hor * SIZEOFGRID);
			m.setText(Integer.toString(grid[hor][north]));
			validate[hor][north] = true;
		}

		hor = rowid;
		ver = colid;

		for (south = ver; hor < SIZEOFGRID && grid[hor][south] == 0; hor++) {
			// System.out.println(hor + " " + south);
			Button m = (Button) findViewById(south + hor * SIZEOFGRID);
			m.setText(Integer.toString(grid[hor][south]));
			validate[hor][south] = true;
		}
		if (hor < SIZEOFGRID && grid[hor][north] > 0) {
			Button m = (Button) findViewById(south + hor * SIZEOFGRID);
			m.setText(Integer.toString(grid[hor][south]));
			validate[hor][south] = true;
		}
	}

	/*To display the alert*/
	public void BuildAlert(String s) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setMessage(s)
				.setTitle("Alert")
				.setPositiveButton("OK",
						new android.content.DialogInterface.OnClickListener() {
							public void onClick(
									android.content.DialogInterface dialog,
									int which) {
								NewGame();
							}
						});
		builder.create().show();
	}

	/*To restart the game*/
	public void NewGame(){
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();

		overridePendingTransition(0, 0);
		startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
